package com.rokzasok.serveit.service.impl;

import com.rokzasok.serveit.dto.UserSalaryDTO;
import com.rokzasok.serveit.exceptions.UserNotFoundException;
import com.rokzasok.serveit.exceptions.UserSalaryNotFoundException;
import com.rokzasok.serveit.model.User;
import com.rokzasok.serveit.model.UserSalary;
import com.rokzasok.serveit.repository.UserRepository;
import com.rokzasok.serveit.repository.UserSalaryRepository;
import com.rokzasok.serveit.service.IUserSalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserSalaryService implements IUserSalaryService {
    @Autowired
    UserSalaryRepository userSalaryRepository;

    @Autowired
    UserRepository userRepository;

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
    public Boolean deleteOne(Integer id) throws Exception {
        UserSalary toDelete = userSalaryRepository.findById(id).orElseThrow(() -> new UserSalaryNotFoundException("Salary with provided ID does not exist"));
        userSalaryRepository.delete(toDelete);
        return true;
    }

    @Override
    public UserSalary current(User user) throws Exception {
        List<UserSalary> userSalaries = userSalaryRepository.findByUser(user);
        if (userSalaries.isEmpty())
            throw new UserSalaryNotFoundException("There is no salary for given user");

        List<UserSalary> NotFutureSalaries = userSalaries.stream()
                .filter(us -> !us.getSalaryDate().isAfter(LocalDate.now()))
                .collect(Collectors.toList());

        return NotFutureSalaries.stream()
                .sorted(Comparator.comparing(UserSalary::getSalaryDate).reversed())
                .collect(Collectors.toList()).get(0);
    }

    @Override
    public UserSalary edit(Integer userId, UserSalaryDTO us) throws Exception {
        //UserSalary toEdit = userSalaryRepository.findById(id).orElseThrow(() -> new UserSalaryNotFoundException("Salary with provided ID does not exist"));
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with provided ID does not exist"));
        UserSalary newUs = new UserSalary(null, us.getSalary(), us.getSalaryDate(), false, user);
        UserSalary savedUs = save(newUs);
        deleteOne(us.getId());
        return savedUs;
    }
}
