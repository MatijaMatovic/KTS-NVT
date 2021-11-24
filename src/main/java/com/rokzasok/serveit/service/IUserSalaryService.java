package com.rokzasok.serveit.service;

import com.rokzasok.serveit.model.User;
import com.rokzasok.serveit.model.UserSalary;


public interface IUserSalaryService extends IGenericService<UserSalary>{
    UserSalary current(User user);
}
