package com.rokzasok.serveit.service.impl;

import com.rokzasok.serveit.model.User;
import com.rokzasok.serveit.model.UserType;
import com.rokzasok.serveit.repository.UserRepository;
import com.rokzasok.serveit.service.IUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Random;

@Service
public class UserService implements IUserService {
    final
    UserRepository userRepository;

    final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
        toEdit.setType(user.getType());
        toEdit.setRoles(user.getRoles());

        return userRepository.save(toEdit);
    }

    @Override
    public String generateInitialPassword() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return passwordEncoder.encode(generatedString);
    }

    @Override
    public void renewPassword(String username, String password, String oldPasswordHash) {
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null)
            throw new EntityNotFoundException("User with given username not found!");

        if (!user.getPassword().equals(oldPasswordHash))
            throw new IllegalArgumentException("Incorrect password link");

        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public boolean resetPassword(String username, String newPassword, String oldPassword) {
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null)
            throw new EntityNotFoundException("User with given username not found");

        String oldPasswordHash = passwordEncoder.encode(oldPassword);

        if (!oldPasswordHash.equals(user.getPassword()))
            throw new IllegalArgumentException("Old and new passwords don't match");

        String newPasswordHash = passwordEncoder.encode(newPassword);

        user.setPassword(newPasswordHash);
        userRepository.save(user);
        return true;
    }
}
