package com.example.Project_2_KimGyuri.entity.user;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;

    @Column(name = "profile_img")
    private String profileImg;

    private String email;
    private String phone;
}
