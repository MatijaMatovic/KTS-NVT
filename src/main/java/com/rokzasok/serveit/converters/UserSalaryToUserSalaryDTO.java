package com.rokzasok.serveit.converters;

import com.rokzasok.serveit.dto.UserSalaryDTO;
import com.rokzasok.serveit.model.UserSalary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserSalaryToUserSalaryDTO implements Converter<UserSalary, UserSalaryDTO> {

    @Override
    public UserSalaryDTO convert(UserSalary source) {
        return UserSalaryDTO.builder()
                .id(source.getId())
                .salary(source.getSalary())
                .salaryDate(source.getSalaryDate())
                .userId(source.getUser().getId())
                .build();
    }

    public List<UserSalaryDTO> convert(List<UserSalary> list) {
        List<UserSalaryDTO> listDto = new ArrayList<>();
        for (UserSalary us : list) {
            listDto.add(convert(us));
        }
        return listDto;
    }
}
