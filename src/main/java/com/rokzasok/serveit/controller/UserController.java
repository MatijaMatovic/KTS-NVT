package com.rokzasok.serveit.controller;

import com.rokzasok.serveit.converters.UserToUserDTO;
import com.rokzasok.serveit.dto.UserDTO;
import com.rokzasok.serveit.model.Role;
import com.rokzasok.serveit.model.User;
import com.rokzasok.serveit.model.UserType;
import com.rokzasok.serveit.service.IEmailService;
import com.rokzasok.serveit.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/users")
public class UserController {
    final
    IUserService userService;

    final IEmailService emailService;

    public UserController(IUserService userService, IEmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @PostMapping(value = "/director/create",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> createCompanyDirector(@RequestBody UserDTO directorDTO) {
        User director = new UserToUserDTO().convert(directorDTO);

        director.setType(UserType.DIRECTOR);
        List<Role> roles = director.getRoles();
        roles.add(new Role("ROLE_DIRECTOR"));
        director.setRoles(roles);

        User saved = userService.save(director);
        if (saved == null)
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Save failed");
        return new ResponseEntity<>(new UserDTO(saved), HttpStatus.OK);
    }

    @PostMapping(value = "/manager/create",
        consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> createManager(@RequestBody UserDTO managerDTO) {
        User manager = new UserToUserDTO().convert(managerDTO);

        manager.setType(UserType.MANAGER);
        List<Role> roles = manager.getRoles();
        roles.add(new Role("ROLE_MANAGER"));
        manager.setRoles(roles);

        User saved = userService.save(manager);
        if (saved == null)
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Save failed");
        return new ResponseEntity<>(new UserDTO(saved), HttpStatus.OK);
    }

    @PostMapping(value = "/create",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO newUserDTO) {
        UserType[] administrativeTypes = {
                UserType.MANAGER, UserType.ADMINISTRATOR, UserType.DIRECTOR
        };

        User newUser = new UserToUserDTO().convert(newUserDTO);

        if (Arrays.asList(administrativeTypes).contains(newUser.getType()))
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED,
                    "You do not have the permission to create a user with administrative role");


        return new ResponseEntity<>(new UserDTO(userService.save(newUser)), HttpStatus.OK);
    }

    @PutMapping(value = "/edit", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMINISTRATOR')")
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
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Integer id) throws Exception {
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

    //todo: Samo za testiranje, skloniti posle
    @GetMapping(value="/send-email", produces = MediaType.APPLICATION_JSON_VALUE)
    public void sendEmail(){
        try {
            emailService.sendPasswordChangedEmail("nekiUser@serveit.com", "nekiUser");
        } catch (InterruptedException | MessagingException e) {
            e.printStackTrace();
        }
    }
}
