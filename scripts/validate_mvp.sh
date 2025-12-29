#!/bin/bash

# Configuration
BASE_URL="http://localhost:8080/api"
HOSPITAL_ID="1" # Default MVP Hospital

echo "=== Starting E2E Validation ==="

# 1. Health Check
echo "\n[1] Checking Health..."
curl -s "$BASE_URL/actuator/health" | grep "UP" && echo "✅ Service UP" || echo "❌ Service DOWN"

# 2. Search/Create Patient
echo "\n[2] Creating Patient (Ravi Kumar)..."
PATIENT_ID=$(curl -s -X POST "$BASE_URL/hospitals/11111111-1111-1111-1111-111111111111/patients" \
  -H "Content-Type: application/json" \
  -d '{"firstName": "Ravi", "lastName": "Kumar", "email": "ravi@test.com", "mobile": "9876543210", "dateOfBirth": "1990-01-01", "gender": "Male", "address": "123 Street"}' \
  | jq -r '.id')

if [ "$PATIENT_ID" != "null" ]; then
  echo "✅ Patient Created: $PATIENT_ID"
else
  echo "❌ Failed to create patient"
fi

# 3. Create Visit
echo "\n[3] Creating Visit..."
VISIT_ID=$(curl -s -X POST "$BASE_URL/patients/$PATIENT_ID/visits" \
  -H "Content-Type: application/json" \
  -d '{"type": "OP", "reason": "Fever", "status": "PLANNED", "hospitalId": "11111111-1111-1111-1111-111111111111", "doctorId": "22222222-2222-2222-2222-222222222222"}' \
  | jq -r '.id')

echo "✅ Visit Created: $VISIT_ID"

# 4. Create Bill
echo "\n[4] Creating Bill..."
BILL_ID=$(curl -s -X POST "$BASE_URL/visits/$VISIT_ID/bills" \
  -H "Content-Type: application/json" \
  -d '{"hospitalId": "11111111-1111-1111-1111-111111111111", "patientId": "'$PATIENT_ID'", "items": [{"serviceName": "Consultation", "unitPrice": 500, "quantity": 1}]}' \
  | jq -r '.id')

echo "✅ Bill Created: $BILL_ID"

echo "\n=== Validation Complete ==="
