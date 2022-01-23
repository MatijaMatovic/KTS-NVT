package com.rokzasok.serveit.controller;

import com.rokzasok.serveit.converters.RegistrationDTOtoUser;
import com.rokzasok.serveit.converters.UserToUserDTO;
import com.rokzasok.serveit.dto.LoginDTO;
import com.rokzasok.serveit.dto.UserDTO;
import com.rokzasok.serveit.model.Role;
import com.rokzasok.serveit.model.User;
import com.rokzasok.serveit.model.UserType;
import com.rokzasok.serveit.service.IEmailService;
import com.rokzasok.serveit.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/users")
public class UserController {
    final
    IUserService userService;

    final
    IEmailService emailService;

    final
    RegistrationDTOtoUser registrationDTOtoUser;

    public UserController(IUserService userService, IEmailService emailService, RegistrationDTOtoUser registrationDTOtoUser) {
        this.userService = userService;
        this.emailService = emailService;
        this.registrationDTOtoUser = registrationDTOtoUser;
    }

    @PostMapping(value = "/director/create",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> createCompanyDirector(@RequestBody UserDTO directorDTO) {
        User director = new UserToUserDTO().convert(directorDTO);

        director.setType(UserType.DIRECTOR);
        director.setPassword(userService.generateInitialPassword());
        List<Role> roles = director.getRoles();
        roles.add(new Role("ROLE_DIRECTOR"));
        director.setRoles(roles);

        User saved = userService.save(director);
        if (saved == null)
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Save failed");

        //TODO: Dodati da se salje link za promenu lozinke
        return new ResponseEntity<>(new UserDTO(saved), HttpStatus.OK);
    }

    @PostMapping(value = "/manager/create",
        consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> createManager(@RequestBody UserDTO managerDTO) {
        User manager = new UserToUserDTO().convert(managerDTO);

        manager.setType(UserType.MANAGER);
        manager.setPassword(userService.generateInitialPassword());
        List<Role> roles = manager.getRoles();
        roles.add(new Role("ROLE_MANAGER"));
        manager.setRoles(roles);

        User saved = userService.save(manager);
        if (saved == null)
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Save failed");

        //TODO: Dodati da se salje link za promenu lozinke
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

        newUser.setPassword(userService.generateInitialPassword());

        //TODO: Dodati da se salje link za promenu lozinke
        User userSaved = userService.save(newUser);
        return new ResponseEntity<>(new UserDTO(userSaved), HttpStatus.OK);
    }

    @PutMapping(value = "/edit", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    //@PreAuthorize("hasRole('ADMINISTRATOR')")
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

    @PutMapping(value = "/renew-password/{oldHash}")
    public ResponseEntity<Boolean>
    editUserPassword(@PathVariable String oldHash, @RequestBody LoginDTO loginDTO) {
        try {
            userService.renewPassword(loginDTO.getEmail(), loginDTO.getPassword(), oldHash);
        }
        catch (EntityNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "User with such email not found");
        }
        catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED,
                    "Expired or incorrect password renewal link");
        }

        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PutMapping(value = "/reset-password")
    public ResponseEntity<Boolean>
    changeUserPassword(@RequestBody HashMap<String, String> passwordDetails) {
        String username = passwordDetails.get("username");
        String oldPassword = passwordDetails.get("oldPassword");
        String newPassword = passwordDetails.get("newPassword");

        try {
            userService.resetPassword(username, newPassword, oldPassword);
        }
        catch (EntityNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "User with such username not found");
        }
        catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Incorrect password");
        }

        return new ResponseEntity<>(true, HttpStatus.OK);
    }


    @DeleteMapping(value = "/delete/{id}")
    //@PreAuthorize("hasRole('ADMINISTRATOR')")
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
    //@PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
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
