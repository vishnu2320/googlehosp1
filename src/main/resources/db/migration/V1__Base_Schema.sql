-- Hospital Management System - Schema Design (MVP Phase 1)

-- 1. Users & Auth
CREATE TABLE users (
    id UUID PRIMARY KEY,
    hospital_id UUID, -- FK to hospital, defined later
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL, -- ADMIN, DOCTOR, RECEPTION, BILLING, PHARMACY, ANALYTICS
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_users_username UNIQUE (hospital_id, username)
);

-- 2. Core
CREATE TABLE hospital (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20)
);

-- Add circular FK for users -> hospital
ALTER TABLE users ADD CONSTRAINT fk_users_hospital FOREIGN KEY (hospital_id) REFERENCES hospital(id);

CREATE TABLE department (
    id UUID PRIMARY KEY,
    hospital_id UUID REFERENCES hospital(id),
    name VARCHAR(50) NOT NULL,
    type VARCHAR(50) NOT NULL -- CLINICAL, DIAGNOSTIC, PHARMACY
);

-- 3. Patient
CREATE TABLE patient (
    id UUID PRIMARY KEY,
    hospital_id UUID REFERENCES hospital(id),
    mrn VARCHAR(20) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50),
    dob DATE,
    gender VARCHAR(10) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    email VARCHAR(100),
    address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_patient_mrn UNIQUE (hospital_id, mrn),
    CONSTRAINT uk_patient_phone UNIQUE (hospital_id, phone)
);
-- Indexes for Patient
CREATE INDEX idx_patient_mobile ON patient(phone);
CREATE INDEX idx_patient_hospital_mrn ON patient(hospital_id, mrn);

-- 4. Clinical
CREATE TABLE doctor (
    id UUID PRIMARY KEY,
    hospital_id UUID REFERENCES hospital(id),
    user_id UUID REFERENCES users(id),
    department_id UUID REFERENCES department(id),
    specialization VARCHAR(50),
    qualification VARCHAR(50),
    license_no VARCHAR(50)
);

CREATE TABLE visit (
    id UUID PRIMARY KEY,
    hospital_id UUID REFERENCES hospital(id),
    patient_id UUID REFERENCES patient(id),
    doctor_id UUID REFERENCES doctor(id),
    department_id UUID REFERENCES department(id),
    visit_date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20), -- PLANNED, ACTIVE, COMPLETED
    type VARCHAR(10),   -- OP, IP
    reason TEXT
);
-- Indexes for Visit
CREATE INDEX idx_visit_hospital_date ON visit(hospital_id, visit_date_time);
CREATE INDEX idx_visit_doctor_date ON visit(doctor_id, visit_date_time);

CREATE TABLE appointment (
    id UUID PRIMARY KEY,
    hospital_id UUID REFERENCES hospital(id),
    patient_id UUID REFERENCES patient(id),
    doctor_id UUID REFERENCES doctor(id),
    visit_id UUID REFERENCES visit(id), -- Optional: link to actual visit
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    status VARCHAR(20) -- BOOKED, COMPLETED, CANCELLED
);
-- Indexes for Appointment
CREATE INDEX idx_appointment_doctor_start ON appointment(doctor_id, start_time);

-- 5. Billing
CREATE TABLE bill (
    id UUID PRIMARY KEY,
    hospital_id UUID REFERENCES hospital(id),
    visit_id UUID REFERENCES visit(id),
    patient_id UUID REFERENCES patient(id),
    bill_no VARCHAR(20) NOT NULL,
    bill_date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount NUMERIC(10,2) DEFAULT 0,
    paid_amount NUMERIC(10,2) DEFAULT 0,
    balance_amount NUMERIC(10,2) DEFAULT 0,
    status VARCHAR(10), -- OPEN, PAID
    CONSTRAINT uk_bill_no UNIQUE (hospital_id, bill_no)
);
-- Indexes for Bill
CREATE INDEX idx_bill_hospital_date ON bill(hospital_id, bill_date_time);

CREATE TABLE bill_item (
    id UUID PRIMARY KEY,
    bill_id UUID REFERENCES bill(id),
    doctor_id UUID REFERENCES doctor(id),
    department_id UUID REFERENCES department(id),
    service_code VARCHAR(20) NOT NULL,
    service_name VARCHAR(100),
    quantity INT DEFAULT 1,
    unit_price NUMERIC(10,2) NOT NULL,
    net_amount NUMERIC(10,2) NOT NULL
);
-- Indexes for BillItem
CREATE INDEX idx_bill_item_bill_id ON bill_item(bill_id);
CREATE INDEX idx_bill_item_service_code ON bill_item(service_code);

CREATE TABLE payment (
    id UUID PRIMARY KEY,
    bill_id UUID REFERENCES bill(id),
    payment_date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    amount NUMERIC(10,2) NOT NULL,
    payment_mode VARCHAR(20) -- CASH, CARD, UPI
);
-- Indexes for Payment
CREATE INDEX idx_payment_bill_date ON payment(bill_id, payment_date_time);

-- 6. Pharmacy
CREATE TABLE item (
    id UUID PRIMARY KEY,
    hospital_id UUID REFERENCES hospital(id),
    item_name VARCHAR(100) NOT NULL,
    item_code VARCHAR(20),
    category VARCHAR(50),
    stock_quantity INT DEFAULT 0
);
-- Indexes for Item
CREATE INDEX idx_item_hospital_name ON item(hospital_id, item_name);

CREATE TABLE item_batch (
    id UUID PRIMARY KEY,
    item_id UUID REFERENCES item(id),
    batch_no VARCHAR(20) NOT NULL,
    expiry_date DATE NOT NULL,
    purchase_price NUMERIC(10,2),
    sale_price NUMERIC(10,2),
    current_qty INT DEFAULT 0 CHECK (current_qty >= 0)
);
-- Indexes for ItemBatch
CREATE INDEX idx_item_batch_expiry ON item_batch(item_id, expiry_date);

CREATE TABLE stock_transaction (
    id UUID PRIMARY KEY,
    item_id UUID REFERENCES item(id),
    batch_id UUID REFERENCES item_batch(id),
    txn_type VARCHAR(10), -- IN, OUT
    quantity INT NOT NULL,
    txn_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    reference_id UUID -- e.g., Bill ID or PO ID
);
-- Indexes for StockTransaction
CREATE INDEX idx_stock_txn_item_date ON stock_transaction(item_id, txn_date);

-- 7. Audit Logging
CREATE TABLE audit_log (
    id UUID PRIMARY KEY,
    user_id UUID REFERENCES users(id),
    action VARCHAR(50) NOT NULL, -- CREATE_BILL, ISSUE_MEDICINE, etc.
    entity_type VARCHAR(50) NOT NULL,
    entity_id UUID NOT NULL,
    details TEXT,
    ip_address VARCHAR(45),
    action_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
-- Indexes for AuditLog
CREATE INDEX idx_audit_log_entity ON audit_log(entity_type, entity_id);
CREATE INDEX idx_audit_log_time ON audit_log(action_at);

-- Lab Module (Added recent Sprint)
CREATE TABLE lab_test (
    id UUID PRIMARY KEY,
    hospital_id UUID REFERENCES hospital(id),
    test_code VARCHAR(20) NOT NULL,
    test_name VARCHAR(100) NOT NULL,
    price NUMERIC(10,2) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    CONSTRAINT uk_lab_test_code UNIQUE (hospital_id, test_code)
);

CREATE TABLE test_request (
    id UUID PRIMARY KEY,
    hospital_id UUID REFERENCES hospital(id),
    visit_id UUID REFERENCES visit(id),
    patient_id UUID REFERENCES patient(id),
    test_id UUID REFERENCES lab_test(id),
    request_date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'PENDING',
    result_data TEXT,
    result_date_time TIMESTAMP,
    abnormal BOOLEAN DEFAULT FALSE
);

-- Update BillItem schema to include discount_amount and gross_amount if missing
-- (Assuming they were added in code but maybe not here, let's create them explicitly if strictly following V1)
-- Actually, the Entity had them added. Let's make sure the DB matches.
ALTER TABLE bill_item ADD COLUMN discount_amount NUMERIC(10,2) DEFAULT 0;
ALTER TABLE bill_item ADD COLUMN gross_amount NUMERIC(10,2) DEFAULT 0;

-- Stock Transaction fix?
-- No, seems fine.

-- Patient Photo
ALTER TABLE patient ADD COLUMN photo_path VARCHAR(255);
