package com.google.hospital.common.entity;

import com.google.hospital.admin.entity.AppUser;
import com.google.hospital.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "audit_log")
@Getter
@Setter
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Column(nullable = false)
    private String action; // CREATE_BILL, ISSUE_MEDICINE, etc.

    @Column(name = "entity_type", nullable = false)
    private String entityType;

    @Column(name = "entity_id", nullable = false)
    private UUID entityId;

    private String details;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "action_at")
    private LocalDateTime actionAt = LocalDateTime.now();

    // Explicitly no BaseEntity inheritance if we want strict schema match,
    // though BaseEntity adds createdAt/updatedAt which might dupe actionAt.
    // 'actionAt' is schema requirement.
}
