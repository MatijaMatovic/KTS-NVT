package com.rokzasok.serveit.service;

import com.rokzasok.serveit.dto.UserDTO;
import com.rokzasok.serveit.model.User;

public interface IUserService extends IGenericService<User>{

    User edit(User user);
}
