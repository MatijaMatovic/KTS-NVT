package com.rokzasok.serveit.service;

import com.rokzasok.serveit.model.Role;

import java.util.List;

public interface RoleService {
    Role findById(Long id);
    List<Role> findByName(String name);
}
