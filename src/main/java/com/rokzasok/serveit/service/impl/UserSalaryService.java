package com.rokzasok.serveit.service.impl;

import com.rokzasok.serveit.model.User;
import com.rokzasok.serveit.model.UserSalary;
import com.rokzasok.serveit.model.UserType;
import com.rokzasok.serveit.repository.UserSalaryRepository;
import com.rokzasok.serveit.service.IUserSalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserSalaryService implements IUserSalaryService {
    @Autowired
    UserSalaryRepository userSalaryRepository;

    @Override
    public List<UserSalary> findAll() {
        return userSalaryRepository.findAll();
    }

    @Override
    public UserSalary findOne(Integer id) {
        return userSalaryRepository.findById(id).orElse(null);
    }

    @Override
    public UserSalary save(UserSalary entity) {
        return userSalaryRepository.save(entity);
    }

    @Override
    public Boolean deleteOne(Integer id) {
        UserSalary toDelete = findOne(id);
        if (toDelete == null)
            throw new EntityNotFoundException("User sallary with given ID not found");
        userSalaryRepository.delete(toDelete);
        return true;
    }

    @Override
    public UserSalary current(User user) {
        List<UserSalary> userSalaries = userSalaryRepository.findByUser(user);
        return userSalaries.stream()
                .sorted(Comparator.comparing(UserSalary::getSalaryDate))
                .collect(Collectors.toList()).get(0);
    }
}
