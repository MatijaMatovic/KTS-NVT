package com.rokzasok.serveit.controller;

import com.rokzasok.serveit.converters.UserToUserDTO;
import com.rokzasok.serveit.dto.UserDTO;
import com.rokzasok.serveit.model.User;
import com.rokzasok.serveit.model.UserType;
import com.rokzasok.serveit.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/users")
public class UserController {
    final
    IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/director/create",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> createCompanyDirector(@RequestBody User director) {
        director.setType(UserType.DIRECTOR);
        User saved = userService.save(director);
        if (saved == null)
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Save failed");
        return new ResponseEntity<>(new UserDTO(saved), HttpStatus.OK);
    }

    @PostMapping(value = "/manager/create",
        consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> createManager(@RequestBody User manager) {
        manager.setType(UserType.MANAGER);
        User saved = userService.save(manager);
        if (saved == null)
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Save failed");
        return new ResponseEntity<>(new UserDTO(saved), HttpStatus.OK);
    }

    @PostMapping(value = "/create",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> createUser(@RequestBody User newUser) {
        UserType[] administrativeTypes = {
                UserType.MANAGER, UserType.ADMINISTRATOR, UserType.DIRECTOR
        };
        if (Arrays.asList(administrativeTypes).contains(newUser.getType()))
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED,
                    "You do not have the permission to create a user with administrative role");


        return new ResponseEntity<>(new UserDTO(userService.save(newUser)), HttpStatus.OK);
    }

    @PutMapping(value = "/edit", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> editUser(@RequestBody UserDTO userDTO) {
        User edited, user = new UserToUserDTO().convert(userDTO);
        try {
            edited = userService.edit(user);
        }
        catch (EntityNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new UserDTO(edited), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Integer id) {
        try {
            Boolean deleted = userService.deleteOne(id);
            if (!deleted)
                throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED,
                        "You cannot delete the system administrator");
        }
        catch (EntityNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> allUsers = userService.findAll().stream()
                                            .map(UserDTO::new)
                                            .collect(Collectors.toList());
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @GetMapping(value = "/one/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getUserByID(@PathVariable Integer id) {
        User theOne = userService.findOne(id);
        if (theOne == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with given ID not found");
        return new ResponseEntity<>(new UserDTO(theOne), HttpStatus.OK);
    }
}
