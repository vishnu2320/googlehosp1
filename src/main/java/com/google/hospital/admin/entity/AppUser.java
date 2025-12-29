package com.google.hospital.admin.entity;

import com.google.hospital.admin.enums.UserRole;
import com.google.hospital.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "app_user")
@Getter
@Setter
public class AppUser extends BaseEntity {

    @Column(nullable = false)
    private UUID hospitalId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String passwordHash;

    private String fullName;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private Boolean isActive;
}
