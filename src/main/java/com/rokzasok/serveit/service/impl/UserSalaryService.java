package com.rokzasok.serveit.service.impl;

import com.rokzasok.serveit.model.User;
import com.rokzasok.serveit.model.UserSalary;
import com.rokzasok.serveit.repository.UserSalaryRepository;
import com.rokzasok.serveit.service.IUserSalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
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
        if (userSalaries.isEmpty())
            throw new EntityNotFoundException("There is no salary for given user");

        List<UserSalary> NotFutureSalaries = userSalaries.stream()
                .filter(us -> !us.getSalaryDate().isAfter(LocalDate.now()))
                .collect(Collectors.toList());

        return NotFutureSalaries.stream()
                .sorted(Comparator.comparing(UserSalary::getSalaryDate).reversed())
                .collect(Collectors.toList()).get(0);
    }

    @Override
    public UserSalary edit(UserSalary us) {
        UserSalary newUs = new UserSalary(null, us.getSalary(), us.getSalaryDate(), false, us.getUser());
        UserSalary savedUs = save(newUs);
        deleteOne(us.getId());
        return savedUs;
    }
}
