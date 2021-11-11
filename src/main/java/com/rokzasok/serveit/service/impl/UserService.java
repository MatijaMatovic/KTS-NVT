package com.rokzasok.serveit.service.impl;

import com.rokzasok.serveit.model.User;
import com.rokzasok.serveit.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {
    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public User findOne(Integer id) {
        return null;
    }

    @Override
    public User save(User entity) {
        return null;
    }

    @Override
    public Boolean deleteOne(Integer id) {
        return null;
    }
}
