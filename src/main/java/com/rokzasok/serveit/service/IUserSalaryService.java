package com.rokzasok.serveit.service;

import com.rokzasok.serveit.dto.UserSalaryDTO;
import com.rokzasok.serveit.model.User;
import com.rokzasok.serveit.model.UserSalary;


public interface IUserSalaryService extends IGenericService<UserSalary>{
    UserSalary current(User user) throws Exception;

    /**
     *
     * @param userId - id usera
     * @param userSalaryDTO - id unutar ovog dto-a je id stare cene
     * @return
     * @throws Exception
     */
    UserSalary edit(Integer userId, UserSalaryDTO userSalaryDTO) throws Exception;
}
