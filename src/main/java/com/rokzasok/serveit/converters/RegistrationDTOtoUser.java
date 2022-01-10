package com.rokzasok.serveit.converters;

import com.rokzasok.serveit.dto.RegistrationDTO;
import com.rokzasok.serveit.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class RegistrationDTOtoUser extends UserToUserDTO{
    final
    PasswordEncoder encoder;

    public RegistrationDTOtoUser(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public User convert(RegistrationDTO source) {
        User user = super.convert(source);
        user.setPassword(encoder.encode(source.getPassword()));
        return user;
    }
}
