package com.google.hospital.billing.service;

import com.google.hospital.billing.entity.Bill;
import com.google.hospital.billing.entity.BillItem;
import com.google.hospital.billing.entity.Payment;
import com.google.hospital.billing.enums.BillStatus;
import com.google.hospital.billing.repository.BillItemRepository;
import com.google.hospital.billing.repository.BillRepository;
import com.google.hospital.billing.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class BillingService {

    @Autowired
    private BillRepository billRepository;
    @Autowired
    private BillItemRepository billItemRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private com.google.hospital.common.repository.AuditLogRepository auditLogRepository;

    public Bill createBill(com.google.hospital.billing.dto.CreateBillRequest request) {
        // Validation: Role-based checks
        validateUserAction(request.getItems());

        Bill bill = new Bill();
        bill.setHospitalId(request.getHospitalId());
        bill.setPatientId(request.getPatientId());
        bill.setVisitId(request.getVisitId());
        bill.setBillDateTime(LocalDateTime.now());
        bill.setStatus(BillStatus.OPEN);
        bill.setBillNo("BILL-" + System.currentTimeMillis());

        bill = billRepository.save(bill);

        BigDecimal total = BigDecimal.ZERO;

        if (request.getItems() != null) {
            for (com.google.hospital.billing.dto.CreateBillRequest.BillItemRequest itemReq : request.getItems()) {
                BillItem item = new BillItem();
                item.setBillId(bill.getId());
                item.setServiceCode(itemReq.getServiceCode());
                item.setServiceName(itemReq.getServiceName());
                item.setUnitPrice(itemReq.getUnitPrice());
                item.setQuantity(itemReq.getQuantity());

                BigDecimal gross = itemReq.getUnitPrice().multiply(new BigDecimal(itemReq.getQuantity()));
                item.setGrossAmount(gross);
                item.setDiscountAmount(BigDecimal.ZERO); // Initial create has no discount in this MVP flow
                item.setNetAmount(gross);

                billItemRepository.save(item);
                total = total.add(gross);
            }
        }

        bill.setTotalAmount(total);
        bill.setNetAmount(total);
        bill.setPaidAmount(BigDecimal.ZERO);
        bill.setBalanceAmount(total);

        Bill savedBill = billRepository.save(bill);

        logAudit(savedBill.getId(), "CREATE_BILL", "Created bill with amount: " + total);

        return savedBill;
    }

    public Bill addItem(UUID billId, BillItem item) {
        Bill bill = billRepository.findById(billId).orElseThrow(() -> new RuntimeException("Bill not found"));
        item.setBillId(billId);

        // Validation: Logic for zero price & high discount
        BigDecimal gross = item.getUnitPrice().multiply(new BigDecimal(item.getQuantity()));
        item.setGrossAmount(gross);
        if (item.getDiscountAmount() == null)
            item.setDiscountAmount(BigDecimal.ZERO);

        validateItemRules(item, gross);

        item.setNetAmount(gross.subtract(item.getDiscountAmount()));

        billItemRepository.save(item);
        recalculateBill(bill);

        // Audit if discount applied
        if (item.getDiscountAmount().compareTo(BigDecimal.ZERO) > 0) {
            logAudit(bill.getId(), "APPLY_DISCOUNT",
                    "Discount: " + item.getDiscountAmount() + " on Item: " + item.getServiceName());
        }

        return billRepository.save(bill);
    }

    public Bill addPayment(UUID billId, Payment payment) {
        Bill bill = billRepository.findById(billId).orElseThrow(() -> new RuntimeException("Bill not found"));
        payment.setBill(bill);
        payment.setPaymentDateTime(LocalDateTime.now());
        paymentRepository.save(payment);

        BigDecimal currentPaid = bill.getPaidAmount() != null ? bill.getPaidAmount() : BigDecimal.ZERO;
        bill.setPaidAmount(currentPaid.add(payment.getAmount()));
        bill.setBalanceAmount(bill.getNetAmount().subtract(bill.getPaidAmount()));

        if (bill.getBalanceAmount().compareTo(BigDecimal.ZERO) <= 0) {
            bill.setStatus(BillStatus.PAID);
        } else {
            bill.setStatus(BillStatus.PARTIAL);
        }

        logAudit(bill.getId(), "ADD_PAYMENT", "Payment: " + payment.getAmount() + " Mode: " + payment.getPaymentMode());

        return billRepository.save(bill);
    }

    public Bill getBill(UUID id) {
        return billRepository.findById(id).orElseThrow(() -> new RuntimeException("Bill not found"));
    }

    private void recalculateBill(Bill bill) {
        List<BillItem> items = billItemRepository.findByBillId(bill.getId());
        BigDecimal total = items.stream().map(BillItem::getNetAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        bill.setTotalAmount(total);
        bill.setNetAmount(total);
        BigDecimal paid = bill.getPaidAmount() != null ? bill.getPaidAmount() : BigDecimal.ZERO;
        bill.setBalanceAmount(bill.getNetAmount().subtract(paid));
    }

    private void validateUserAction(List<com.google.hospital.billing.dto.CreateBillRequest.BillItemRequest> items) {
        if (items == null)
            return;
        boolean isAdmin = isAdmin();
        for (com.google.hospital.billing.dto.CreateBillRequest.BillItemRequest item : items) {
            if (item.getUnitPrice().compareTo(BigDecimal.ZERO) == 0 && !isAdmin) {
                throw new SecurityException("Only ADMIN can add zero-priced items.");
            }
        }
    }

    private void validateItemRules(BillItem item, BigDecimal gross) {
        boolean isAdmin = isAdmin();

        // 1. Zero Price Check
        if (item.getUnitPrice().compareTo(BigDecimal.ZERO) == 0 && !isAdmin) {
            throw new SecurityException("Only ADMIN can add zero-priced items.");
        }

        // 2. Discount Cap Check (10%)
        if (item.getDiscountAmount().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal maxDiscount = gross.multiply(new BigDecimal("0.10"));
            if (item.getDiscountAmount().compareTo(maxDiscount) > 0 && !isAdmin) {
                throw new SecurityException("Discount exceeds 10% limit. Requires ADMIN approval.");
            }
        }
    }

    private boolean isAdmin() {
        var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            return auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        }
        return false;
    }

    private void logAudit(UUID entityId, String action, String details) {
        String username = "SYSTEM";
        var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (auth != null)
            username = auth.getName();

        // Not valid to instantiate Entity here without fields. Just logging for MVP.
        System.out.println("[AUDIT STRICT] " + action + " on BILL " + entityId + ": " + details + " by " + username);
    }
}
