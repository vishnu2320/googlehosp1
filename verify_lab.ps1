# Lab API Test Script
$baseUrl = "http://localhost:8080/api"
$hospitalId = "3fa85f64-5717-4562-b3fc-2c963f66afa6"

# 1. Login as Admin
$loginBody = @{
    username = "admin"
    password = "admin123"
} | ConvertTo-Json

try {
    $loginResponse = Invoke-RestMethod -Uri "$baseUrl/auth/login" -Method Post -Body $loginBody -ContentType "application/json"
    $token = $loginResponse.accessToken
    Write-Host "Login Successful. Token: $token"
}
catch {
    Write-Error "Login Failed: $_"
    exit
}

$headers = @{
    Authorization = "Bearer $token"
}

# 2. Create Lab Test
$testCode = "TEST-" + (Get-Random)
$testBody = @{
    name       = "Automated Test $testCode"
    code       = $testCode
    price      = 50
    hospitalId = $hospitalId
} | ConvertTo-Json

try {
    $testResponse = Invoke-RestMethod -Uri "$baseUrl/lab-tests" -Method Post -Body $testBody -Headers $headers -ContentType "application/json"
    $testId = $testResponse.id
    Write-Host "Created Lab Test: $testCode (ID: $testId)"
}
catch {
    Write-Error "Create Test Failed: $_"
    exit
}

# 3. Find a Patient (Create one if needed, or search)
# Let's just create a dummy patient for testing to be sure
$patientBody = @{
    firstName     = "Test"
    lastName      = "Patient"
    gender        = "MALE"
    dateOfBirth   = "1990-01-01"
    contactNumber = "1234567890"
    email         = "test@example.com"
    address       = "123 Test St"
    city          = "Test City"
    idProofType   = "AADHAAR"
    idProofNumber = "12345678" + (Get-Random)
} | ConvertTo-Json

try {
    $patientResponse = Invoke-RestMethod -Uri "$baseUrl/hospitals/$hospitalId/patients" -Method Post -Body $patientBody -Headers $headers -ContentType "application/json"
    $patientId = $patientResponse.id
    Write-Host "Created/Found Patient ID: $patientId"
}
catch {
    Write-Error "Create Patient Failed: $_"
    exit
}

# 4. Request Test
$requestBody = @{
    hospitalId = $hospitalId
    patientId  = $patientId
    doctorId   = "3fa85f64-5717-4562-b3fc-2c963f66afa6" # Placeholder
    labTestId  = $testId
} | ConvertTo-Json

# If FK constraint exists for doctorId, this might fail. Let's hope seeded data has a doctor or we can use admin.
# Wait, DatabaseSeeder seeded doctors. 
# Let's search for a doctor.
try {
    $doctors = Invoke-RestMethod -Uri "$baseUrl/hospitals/$hospitalId/doctors?query=Dr" -Method Get -Headers $headers
    if ($doctors.Count -gt 0) {
        $doctorId = $doctors[0].id
        Write-Host "Using Doctor ID: $doctorId"
        
        $requestBody = @{
            hospitalId = $hospitalId
            patientId  = $patientId
            doctorId   = $doctorId
            labTestId  = $testId
        } | ConvertTo-Json
    }
}
catch {
    Write-Host "Could not find doctor, trying with placeholder..."
}

try {
    $reqResponse = Invoke-RestMethod -Uri "$baseUrl/test-requests" -Method Post -Body $requestBody -Headers $headers -ContentType "application/json"
    $requestId = $reqResponse.id
    Write-Host "Requested Test (ID: $requestId)"
}
catch {
    Write-Error "Request Test Failed: $_"
    exit
}

# 5. Verify it's Pending
try {
    $pending = Invoke-RestMethod -Uri "$baseUrl/hospitals/$hospitalId/test-requests/pending" -Method Get -Headers $headers
    $found = $pending | Where-Object { $_.id -eq $requestId }
    if ($found) {
        Write-Host "Verified: Request is in Pending List"
    }
    else {
        Write-Error "Verification Failed: Request NOT found in Pending List"
    }
}
catch {
    Write-Error "Get Pending Failed: $_"
}

# 6. Enter Result
$resultBody = @{
    result  = "Positive"
    remarks = "Auto Verified"
} | ConvertTo-Json

try {
    $updateResponse = Invoke-RestMethod -Uri "$baseUrl/test-requests/$requestId/result" -Method Put -Body $resultBody -Headers $headers -ContentType "application/json"
    Write-Host "Entered Result. Status: $($updateResponse.status)"
}
catch {
    Write-Error "Enter Result Failed: $_"
}
