package com.rokzasok.serveit.service;

import com.rokzasok.serveit.dto.UserDTO;
import com.rokzasok.serveit.model.User;

public interface IUserService extends IGenericService<User>{

    User edit(User user);

    String generateInitialPassword();

    void renewPassword(String username, String password, String oldPasswordHash);

    boolean resetPassword(String username, String newPassword, String oldPassword);
}
