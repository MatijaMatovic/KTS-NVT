package com.rokzasok.serveit.model;

import lombok.*;

import javax.persistence.*;
import javax.persistence.Table;

@SuppressWarnings("LombokEqualsAndHashCodeInspection")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "users") //Mora jer je "user" keyword
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @Column(nullable = false, unique = true)
    private String username;

    @Enumerated(EnumType.STRING)
    private UserType type;

    private String password;
    private String firstName;
    private String lastName;

    private String address;
    private String phoneNumber;
    private String imagePath;
    private Boolean isDeleted;

}
