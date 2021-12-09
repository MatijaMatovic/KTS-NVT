package com.rokzasok.serveit.service.impl;

import com.rokzasok.serveit.converters.UserToUserDTO;
import com.rokzasok.serveit.dto.UserDTO;
import com.rokzasok.serveit.model.User;
import com.rokzasok.serveit.model.UserType;
import com.rokzasok.serveit.repository.UserRepository;
import com.rokzasok.serveit.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.MethodNotAllowedException;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class UserService implements IUserService {
    final
    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findOne(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User save(User entity) {
        return userRepository.save(entity);
    }

    @Override
    public Boolean deleteOne(Integer id) throws EntityNotFoundException {
        User toDelete = findOne(id);
        if (toDelete == null)
            throw new EntityNotFoundException("User with given ID not found");
        else if (toDelete.getType() == UserType.ADMINISTRATOR)
            return false;
        userRepository.delete(toDelete);
        return true;
    }

    @Override
    public User edit(User user) throws EntityNotFoundException {
        User toEdit = findOne(user.getId());
        if (toEdit == null)
            throw new EntityNotFoundException("User with given ID not found");

        toEdit.setFirstName(user.getFirstName());
        toEdit.setLastName(user.getLastName());
        toEdit.setAddress(user.getAddress());
        toEdit.setImagePath(user.getImagePath());
        toEdit.setPhoneNumber(user.getPhoneNumber());

        return userRepository.save(toEdit);
    }
}
