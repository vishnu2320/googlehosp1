package com.google.hospital.pharmacy.service;

import com.google.hospital.pharmacy.entity.Item;
import com.google.hospital.pharmacy.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PharmacyService {

    @Autowired
    private ItemRepository itemRepository;

    public Item createItem(Item item) {
        return itemRepository.save(item);
    }

    public List<Item> getStockSummary(UUID hospitalId) {
        return itemRepository.findByHospitalId(hospitalId);
    }

    @Autowired
    private com.google.hospital.pharmacy.repository.ItemBatchRepository itemBatchRepository;

    public void issueMedicines(UUID billId, com.google.hospital.pharmacy.dto.IssueRequest request) {
        for (com.google.hospital.pharmacy.dto.IssueRequest.IssueItem issueItem : request.getItems()) {
            Item item = itemRepository.findById(issueItem.getItemId())
                    .orElseThrow(() -> new RuntimeException("Item not found: " + issueItem.getItemId()));

            // Logic: Deduct from specific batch if provided, otherwise FIFO
            // (simplification)
            // For vertical slice, we'll try to use the batchId if provided.

            if (issueItem.getBatchId() != null) {
                com.google.hospital.pharmacy.entity.ItemBatch batch = itemBatchRepository
                        .findById(issueItem.getBatchId())
                        .orElseThrow(() -> new RuntimeException("Batch not found: " + issueItem.getBatchId()));

                if (batch.getCurrentQty() < issueItem.getQuantity()) {
                    throw new RuntimeException("Insufficient stock in batch for item: " + item.getItemName());
                }
                batch.setCurrentQty(batch.getCurrentQty() - issueItem.getQuantity());
                itemBatchRepository.save(batch);
            } else {
                // Fallback: If no batch provided, finding any batch with stock (Simple
                // approach)
                // In real app, we should enforce batch selection.
                // For now, let's just throw if no batch ID, as the spec example payload has
                // batchId.
                throw new IllegalArgumentException("Batch ID is required for issuing medicines.");
            }
        }
    }
}
