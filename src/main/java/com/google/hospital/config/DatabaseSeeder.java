package com.google.hospital.config;

import com.google.hospital.admin.entity.AppUser;
import com.google.hospital.admin.entity.Department;
import com.google.hospital.admin.entity.Hospital;
import com.google.hospital.admin.enums.DepartmentType;
import com.google.hospital.admin.enums.UserRole;
import com.google.hospital.admin.repository.AppUserRepository;
import com.google.hospital.admin.repository.DepartmentRepository;
import com.google.hospital.admin.repository.HospitalRepository;
import com.google.hospital.doctor.entity.Doctor;
import com.google.hospital.doctor.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Profile("dev")
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Fixed UUID for Hospital (used in frontend)
    private static final UUID HOSPITAL_ID = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");

    @Override
    public void run(String... args) throws Exception {
        // Check if already seeded
        if (hospitalRepository.findById(HOSPITAL_ID).isPresent()) {
            System.out.println("Database already seeded. Skipping...");
            return;
        }

        System.out.println("Seeding database...");

        // Create Hospital
        Hospital hospital = new Hospital();
        hospital.setId(HOSPITAL_ID);
        hospital.setName("City General Hospital");
        hospital.setAddress("123 Healthcare Avenue, Medical District");
        hospital.setPhone("555-0100");
        hospital.setEmail("info@citygeneralhospital.com");
        hospitalRepository.save(hospital);

        // Create Departments
        Department cardiology = createDepartment("Cardiology", DepartmentType.CARDIOLOGY, HOSPITAL_ID);
        Department general = createDepartment("General Medicine", DepartmentType.GENERAL, HOSPITAL_ID);
        Department orthopedics = createDepartment("Orthopedics", DepartmentType.ORTHOPEDICS, HOSPITAL_ID);
        Department pediatrics = createDepartment("Pediatrics", DepartmentType.PEDIATRICS, HOSPITAL_ID);
        Department radiology = createDepartment("Radiology", DepartmentType.RADIOLOGY, HOSPITAL_ID);

        // Create Doctors
        createDoctor("Dr. Sarah Johnson", "Cardiology", "MD, FACC", "MED-CARD-001", cardiology.getId(), HOSPITAL_ID);
        createDoctor("Dr. Michael Chen", "General Medicine", "MD, MBBS", "MED-GEN-002", general.getId(), HOSPITAL_ID);
        createDoctor("Dr. Emily Rodriguez", "Orthopedics", "MD, MS Ortho", "MED-ORTH-003", orthopedics.getId(),
                HOSPITAL_ID);
        createDoctor("Dr. David Kim", "Pediatrics", "MD, DCH", "MED-PED-004", pediatrics.getId(), HOSPITAL_ID);
        createDoctor("Dr. Lisa Patel", "Radiology", "MD, DMRD", "MED-RAD-005", radiology.getId(), HOSPITAL_ID);

        // Create Admin User
        AppUser admin = new AppUser();
        admin.setUsername("admin");
        admin.setPasswordHash(passwordEncoder.encode("admin123"));
        admin.setFullName("System Administrator");
        admin.setRole(UserRole.ADMIN);
        admin.setHospitalId(HOSPITAL_ID);
        admin.setIsActive(true);
        userRepository.save(admin);

        // Create Test Doctor User
        AppUser doctorUser = new AppUser();
        doctorUser.setUsername("doctor");
        doctorUser.setPasswordHash(passwordEncoder.encode("doctor123"));
        doctorUser.setFullName("Dr. Test Doctor");
        doctorUser.setRole(UserRole.DOCTOR);
        doctorUser.setHospitalId(HOSPITAL_ID);
        doctorUser.setIsActive(true);
        userRepository.save(doctorUser);

        System.out.println("✅ Database seeded successfully!");
        System.out.println("Login Credentials:");
        System.out.println("  Admin: username=admin, password=admin123");
        System.out.println("  Doctor: username=doctor, password=doctor123");
    }

    private Department createDepartment(String name, DepartmentType type, UUID hospitalId) {
        Department dept = new Department();
        dept.setName(name);
        dept.setType(type);
        dept.setHospitalId(hospitalId);
        return departmentRepository.save(dept);
    }

    private void createDoctor(String name, String specialization, String qualification, String licenseNo, UUID deptId,
            UUID hospitalId) {
        Doctor doctor = new Doctor();
        doctor.setName(name);
        doctor.setSpecialization(specialization);
        doctor.setQualification(qualification);
        doctor.setLicenseNo(licenseNo);
        doctor.setDepartmentId(deptId);
        doctor.setHospitalId(hospitalId);
        doctorRepository.save(doctor);
    }
}
