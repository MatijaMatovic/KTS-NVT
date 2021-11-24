package com.rokzasok.serveit.converters;

import com.rokzasok.serveit.dto.UserSalaryDTO;
import com.rokzasok.serveit.model.UserSalary;
import com.rokzasok.serveit.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserSalaryDTOToUserSalary implements Converter<UserSalaryDTO, UserSalary> {

    @Autowired
    IUserService userService;

    @Override
    public UserSalary convert(UserSalaryDTO source) {
        return UserSalary.builder()
                .salary(source.getSalary())
                .salaryDate(source.getSalaryDate())
                .id(null)
                .isDeleted(false)
                .user(userService.findOne(source.getUserId()))
                .build();
    }

}
