package com.rokzasok.serveit.service;

import com.rokzasok.serveit.dto.UserSalaryDTO;
import com.rokzasok.serveit.model.User;
import com.rokzasok.serveit.model.UserSalary;


public interface IUserSalaryService extends IGenericService<UserSalary>{
    UserSalary current(User user) throws Exception;
    UserSalary edit(Integer id, UserSalaryDTO userSalaryDTO) throws Exception;
}
