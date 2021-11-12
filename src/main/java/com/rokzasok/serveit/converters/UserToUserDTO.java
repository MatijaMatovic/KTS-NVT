package com.rokzasok.serveit.converters;

import com.rokzasok.serveit.dto.UserDTO;
import com.rokzasok.serveit.model.User;
import org.springframework.core.convert.converter.Converter;

public class UserToUserDTO implements Converter<UserDTO, User> {
    @Override
    public User convert(UserDTO source) {
        User user = new User();
        user.setFirstName(source.getFirstName());
        user.setLastName(source.getLastName());
        user.setAddress(source.getAddress());
        user.setPhoneNumber(source.getPhoneNumber());
        user.setType(source.getType());
        user.setImagePath(source.getImagePath());
        user.setIsDeleted(false);
        return user;
    }
}
