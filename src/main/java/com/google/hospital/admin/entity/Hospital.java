package com.google.hospital.admin.entity;

import com.google.hospital.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "hospital")
@Getter
@Setter
public class Hospital extends BaseEntity {

    private String name;
    private String address;
    private String city;
    private String state;
    private String gst;
    private String contact;
    private String phone;
    private String email;
}
