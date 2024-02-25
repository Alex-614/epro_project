package de.thbingen.epro.project.okrservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.thbingen.epro.project.okrservice.dtos.CompanyDto;
import de.thbingen.epro.project.okrservice.dtos.UserDto;
import de.thbingen.epro.project.okrservice.entities.Company;
import de.thbingen.epro.project.okrservice.entities.User;
import de.thbingen.epro.project.okrservice.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private UserController userController;
    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void UserController_Register_ReturnCreated() throws Exception {
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@test.de")
                .password("test")
                .firstname("my first name")
                .surname("sur").build();
        UserDto userDto = user.toDto();
        userDto.setPassword(user.getPassword());
        given(userService.register(ArgumentMatchers.any())).willAnswer((invocation -> invocation.getArgument(0)));

        ResultActions response = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)));

        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(userDto)));
    }

    @Test
    public void UserController_GetAllUsers_ReturnResponseDto() throws Exception {
        ArrayList<User> users = new ArrayList<User>();
        for(long i = 1; i < 11; i++) {
            User user = new User();
            user.setId(i);
            user.setUsername("testuser" + i);
            user.setPassword("test" + i);
            user.setEmail("test" + + i + "@test.de");
            user.setFirstname("my first name" + i);
            user.setSurname("sur" + i);
            users.add(user);
        }

        ArrayList<UserDto> usersDtos = users.stream()
                .map(User::toDto).collect(Collectors.toCollection(ArrayList::new));

        when(userService.findAllUsers()).thenReturn(usersDtos);

        ResultActions response = mockMvc.perform(get("/user"));

        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(usersDtos)));
    }

    @Test
    public void UserController_GetUser_ReturnUserDto() throws Exception {
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .password("test")
                .email("test@test.de")
                .firstname("my first name")
                .surname("sur").build();
        UserDto userDto = new UserDto(user);

        when(userService.findUser(user.getId())).thenReturn(user);

        ResultActions response = mockMvc.perform(get("/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)));

        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(userDto)));
    }

    @Test
    public void UserController_GetUserCompanies_ReturnUserDto() throws Exception {
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .password("test")
                .email("test@test.de")
                .firstname("my first name")
                .surname("sur").build();
        UserDto userDto = new UserDto(user);

        Company company = Company.builder()
                .name("test-company")
                .build();
        List<CompanyDto> companyDtos = new ArrayList<>();
        companyDtos.add(new CompanyDto(company));

        when(userService.findUserCompanies(user.getId())).thenReturn(companyDtos);

        ResultActions response = mockMvc.perform(get("/user/1/companies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)));

        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(companyDtos)));
    }

    @Test
    public void UserController_PatchUser_ReturnResponseDto() throws Exception {
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .password("test")
                .email("test@test.de")
                .firstname("my first name")
                .surname("sur").build();
        UserDto userDto = new UserDto(user);

        when(userService.patchUser(user.getId(), userDto)).thenReturn(userDto);

        ResultActions response = mockMvc.perform(patch("/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)));

        response.andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(userDto)));
    }

    @Test
    public void UserController_DeleteUser_ReturnString() throws Exception {
        ResultActions response = mockMvc.perform(delete("/user/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk());
    }
}
