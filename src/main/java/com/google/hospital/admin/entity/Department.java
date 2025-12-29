package com.google.hospital.admin.entity;

import com.google.hospital.admin.enums.DepartmentType;
import com.google.hospital.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "department")
@Getter
@Setter
public class Department extends BaseEntity {

    @Column(nullable = false)
    private UUID hospitalId; // Keeping as ID reference for modularity, rather than strict FK object for now

    private String name;

    @Enumerated(EnumType.STRING)
    private DepartmentType type;
}
