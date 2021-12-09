package com.rokzasok.serveit.converters;

import com.rokzasok.serveit.dto.UserDTO;
import com.rokzasok.serveit.model.Role;
import com.rokzasok.serveit.model.User;
import com.sun.istack.NotNull;
import org.springframework.core.convert.converter.Converter;

import java.util.ArrayList;
import java.util.List;

public class UserToUserDTO implements Converter<UserDTO, User> {
    @Override
    public User convert(UserDTO source) {
        User user = new User();
        user.setId(source.getId());
        user.setUsername(source.getUsername());
        user.setFirstName(source.getFirstName());
        user.setLastName(source.getLastName());
        user.setAddress(source.getAddress());
        user.setPhoneNumber(source.getPhoneNumber());
        user.setType(source.getType());
        user.setImagePath(source.getImagePath());
        user.setIsDeleted(false);

        List<Role> roles = new ArrayList<>();
        switch (source.getType()) {
            case CHEF:
                roles.add(new Role("ROLE_CHEF"));
                break;
            case COOK:
                roles.add(new Role("ROLE_COOK"));
                break;
            case WAITER:
                roles.add(new Role("ROLE_WAITER"));
                break;
            case BARTENDER:
                roles.add(new Role("ROLE_BARTENDER"));
                break;
            case MANAGER:
                roles.add(new Role("ROLE_MANAGER"));
                break;
            case DIRECTOR:
                roles.add(new Role("ROLE_DIRECTOR"));
                break;
            case ADMINISTRATOR:
                roles.add(new Role("ROLE_ADMINISTRATOR"));
                break;
        }
        user.setRoles(roles);

        return user;
    }
}
