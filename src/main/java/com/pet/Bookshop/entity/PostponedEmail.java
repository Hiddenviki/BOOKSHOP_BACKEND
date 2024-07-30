package com.pet.Bookshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "postponed_emails")
public class PostponedEmail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login", nullable = false)
    private String login;  //кому надо отправить email

    @Column(name = "topic", nullable = false)
    private String topic;  // темы сообщения
}
