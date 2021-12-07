package com.rokzasok.serveit.controller;

import com.rokzasok.serveit.converters.UserSalaryDTOToUserSalary;
import com.rokzasok.serveit.converters.UserSalaryToUserSalaryDTO;
import com.rokzasok.serveit.dto.UserSalaryDTO;
import com.rokzasok.serveit.model.User;
import com.rokzasok.serveit.model.UserSalary;
import com.rokzasok.serveit.service.IUserSalaryService;
import com.rokzasok.serveit.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/user-salaries")
public class UserSalaryController {
    @Autowired
    IUserSalaryService userSalaryService;

    @Autowired
    IUserService userService;

    @Autowired
    UserSalaryDTOToUserSalary userSalaryDTOToUserSalary;

    @Autowired
    UserSalaryToUserSalaryDTO userSalaryToUserSalaryDTO;

    /***
     * Creates one user salary
     * author: isidora-stanic
     * authorized: MANAGER
     * CREATE
     *
     * @return dto of created salary
     */
    @PostMapping(value = "/create", consumes = "application/json")
    public ResponseEntity<UserSalaryDTO> create(@RequestBody UserSalaryDTO userSalaryDTO) {
        User u = userService.findOne(userSalaryDTO.getUserId());
        if (u == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        UserSalary uSalary = userSalaryService.save(
                userSalaryDTOToUserSalary.convert(userSalaryDTO));
        UserSalaryDTO uSalaryDTO = userSalaryToUserSalaryDTO.convert(uSalary);
        return new ResponseEntity<>(uSalaryDTO, HttpStatus.OK);
    }

    /***
     * Gets all user salaries
     * author: isidora-stanic
     * authorized: MANAGER
     * READ (ALL)
     *
     * @return list of dtos of salaries
     */
    @GetMapping(value = "/all")
    public ResponseEntity<List<UserSalaryDTO>> all() {
        List<UserSalary> list = userSalaryService.findAll();
        return new ResponseEntity<>(userSalaryToUserSalaryDTO.convert(list), HttpStatus.OK);
    }

    /***
     * Gets one user salary
     * author: isidora-stanic
     * authorized: MANAGER
     * READ (ALL)
     *
     * @return dto of salary
     */
    @GetMapping(value = "/one/{id}")
    public ResponseEntity<UserSalaryDTO> one(@PathVariable Integer id) {
        UserSalary us = userSalaryService.findOne(id);
        if (us == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userSalaryToUserSalaryDTO.convert(us), HttpStatus.OK);
    }

    /***
     * Gets current salary for user
     * author: isidora-stanic
     * authorized: MANAGER
     *
     * @return dto of current salary
     */
    @GetMapping(value = "/current/{id}")
    public ResponseEntity<UserSalaryDTO> current(@PathVariable Integer id) {
        User u = userService.findOne(id);
        if (u == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        UserSalary currentUs = userSalaryService.current(u);
        return new ResponseEntity<>(userSalaryToUserSalaryDTO.convert(currentUs), HttpStatus.OK);
    }

    /***
     * Gets current salaries for all users who have them
     * author: isidora-stanic
     * authorized: MANAGER
     *
     * @return list of dtos of current salaries
     */
    @GetMapping(value = "/current-salaries")
    public ResponseEntity<List<UserSalaryDTO>> currentSalaries() {
        List<User> users = userService.findAll();
        List<UserSalaryDTO> currentSalaries = new ArrayList<>();
        for (User u : users) {
            UserSalary currentUs = userSalaryService.current(u);
            UserSalaryDTO currentUsDTO = userSalaryToUserSalaryDTO.convert(currentUs);
            currentSalaries.add(currentUsDTO);
        }

        return new ResponseEntity<>(currentSalaries, HttpStatus.OK);
    }

    /***
     * Creates new user salary for the same user, deletes old user salary
     * author: isidora-stanic
     * authorized: MANAGER
     * UPDATE
     *
     * @return dto of a new salary
     */
    @PutMapping(value = "/edit/{id}")
    public ResponseEntity<UserSalaryDTO> edit(@PathVariable Integer id, @RequestBody UserSalaryDTO changed) {
        UserSalary us = userSalaryService.findOne(id);
        if (us == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        UserSalary newUs = userSalaryService.edit(us);

        return new ResponseEntity<>(userSalaryToUserSalaryDTO.convert(newUs), HttpStatus.OK);
    }

    /***
     * Deletes user salary by id
     * author: isidora-stanic
     * authorized: MANAGER
     * DELETE
     *
     * @return true if successful, otherwise false
     */
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        UserSalary us = userSalaryService.findOne(id);
        if (us == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Boolean success = userSalaryService.deleteOne(id);
        return new ResponseEntity<>(success, HttpStatus.OK);
    }

    // TODO: ukloniti ukoliko se ne koristi
    @PostMapping(value = "/new-salary-for-user/{userId}")
    public ResponseEntity<UserSalaryDTO> newSalaryForUser(@PathVariable Integer userId, @RequestBody UserSalaryDTO userSalaryDTO){
        User u = userService.findOne(userId);
        if (u == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        UserSalary uSalary = userSalaryService.save(
                userSalaryDTOToUserSalary.convert(userSalaryDTO));
        UserSalaryDTO uSalaryDTO = userSalaryToUserSalaryDTO.convert(uSalary);
        return new ResponseEntity<>(uSalaryDTO, HttpStatus.OK);
    }
}
