package com.rokzasok.serveit.dto;

import com.rokzasok.serveit.model.User;

public class RegistrationDTO extends UserDTO {
    private String password;

    public RegistrationDTO(User user) {
        super(user);
        this.password = user.getPassword();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
