package com.pet.Bookshop.entity;

import com.pet.Bookshop.enums.Roles;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Column(name = "password", length = 1000)
    private String password;

    @Column(name = "email", nullable = false)
    private String email;

    @CreationTimestamp //Hibernate автоматически устанавливает время создания
    @Temporal(TemporalType.TIMESTAMP) //устанавливает тип хранения времени в базе данных
    @Column(name = "created_date", nullable = false)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime createdDate;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Roles role;

    @Column(name = "active", nullable = false)
    private boolean active; //активный аккаунт

}
