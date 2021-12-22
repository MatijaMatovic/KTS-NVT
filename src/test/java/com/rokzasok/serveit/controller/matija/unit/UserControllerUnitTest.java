package com.rokzasok.serveit.controller.matija.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rokzasok.serveit.converters.UserToUserDTO;
import com.rokzasok.serveit.dto.UserDTO;
import com.rokzasok.serveit.model.User;
import com.rokzasok.serveit.model.UserType;
import com.rokzasok.serveit.repository.UserRepository;
import com.rokzasok.serveit.service.impl.UserService;
import org.jeasy.random.EasyRandom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class UserControllerUnitTest {
    private static final String URL_PREFIX = "/api/users/";
    private static final Integer USER_ID = 8;
    private static final Integer ADMIN_ID = 1;
    private static final Integer NON_EXISTENT_USER_ID = 420;

    private final MediaType contentType = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype()
    );

    private MockMvc mockMvc;
    private EasyRandom generator;
    private ObjectMapper mapper;

    @Autowired
    WebApplicationContext context;

    @MockBean
    UserService userService;

    @PostConstruct
    public void setup() {
        generator = new EasyRandom();
        mapper = new ObjectMapper();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @Transactional
    @Rollback(value = true)
    @WithMockUser(roles = {"ADMINISTRATOR"})
    public void testEdit() throws Exception {

        UserDTO editedDTO = generator.nextObject(UserDTO.class);
        editedDTO.setId(USER_ID);

        User fromDto = new UserToUserDTO().convert(editedDTO);

        given(userService.edit(fromDto)).willReturn(fromDto);

        String json = mapper.writeValueAsString(editedDTO);

        mockMvc.perform(
                put(URL_PREFIX + "edit")
                        .contentType(contentType)
                        .content(json)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id").value(USER_ID))
                .andExpect(jsonPath("$.firstName").value(editedDTO.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(editedDTO.getLastName()));

        verify(userService, times(1)).edit(fromDto);
    }

    @Test
    @Transactional
    @Rollback(true)
    @WithMockUser(roles = {"ADMINISTRATOR"})
    public void testEdit_NonExistingID() throws Exception {
        UserDTO editedDTO = generator.nextObject(UserDTO.class);
        editedDTO.setId(NON_EXISTENT_USER_ID);

        User fromDTO = new UserToUserDTO().convert(editedDTO);

        given(userService.edit(fromDTO)).willThrow(EntityNotFoundException.class);

        String json = mapper.writeValueAsString(editedDTO);
        mockMvc.perform(
                put(URL_PREFIX + "edit")
                        .contentType(contentType)
                        .content(json)
        )
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testDelete() throws Exception {
        given(userService.deleteOne(USER_ID)).willReturn(true);
        mockMvc.perform(
                delete(URL_PREFIX + "delete/" + USER_ID)
        )
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testDelete_NonExistingID() throws Exception {
        given(userService.deleteOne(NON_EXISTENT_USER_ID)).willThrow(EntityNotFoundException.class);

        mockMvc.perform(delete(URL_PREFIX + "delete/" + NON_EXISTENT_USER_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testDelete_DeleteAdminNotAllowed() throws Exception {
        UserDTO admin = generator.nextObject(UserDTO.class);
        admin.setType(UserType.ADMINISTRATOR);

        User fromDTO = new UserToUserDTO().convert(admin);

        given(userService.deleteOne(ADMIN_ID)).willReturn(false);

        mockMvc.perform(delete(URL_PREFIX + "delete/" + ADMIN_ID))
                .andExpect(status().isMethodNotAllowed());
    }



}
