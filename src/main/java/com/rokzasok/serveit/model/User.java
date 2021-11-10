package com.rokzasok.serveit.model;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder

public class User {
    private Integer id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private UserType type;
    private String address;
    private String phoneNumber;
    private String imagePath;
    private Boolean isDeleted;

}
