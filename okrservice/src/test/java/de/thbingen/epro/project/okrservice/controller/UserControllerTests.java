package de.thbingen.epro.project.okrservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.thbingen.epro.project.okrservice.dtos.UserDto;
import de.thbingen.epro.project.okrservice.entities.User;
import de.thbingen.epro.project.okrservice.jwt.JwtGenerator;
import de.thbingen.epro.project.okrservice.repositories.UserRepository;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private JwtGenerator jwtGenerator;

    Gson gson;

    private ArrayList<User> users;
    private ArrayList<UserDto> userDtos;

    @BeforeEach
    public void init() {
        gson = new GsonBuilder()
                .serializeNulls()
                .create();

        users = new ArrayList<User>();
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

        userDtos = users.stream()
                .map(UserDto::new)
                .collect(Collectors.toCollection(ArrayList::new));
    }
    @Test
    public void testPostUserEndpoint() throws Exception {
        when(userRepository.save(any(User.class))).thenAnswer(invocationOnMock -> {
            return users.getFirst();
        });

        String userJson = gson.toJson(userDtos.getFirst());
        ResultActions resultActions = mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isOk())
                .andExpect(content().string(userJson));
    }

    @Test
    @WithMockUser(username = "user", roles = "CO_OKR_ADMIN")
    public void testPatchUserEndpoint() throws Exception {
        when(userRepository.findById(1L)).thenAnswer(invocationOnMock -> {
            return Optional.of(users.getFirst());
        });

        UserDto firstUser = userDtos.getFirst();
        firstUser.setEmail("test123@test.de");

        String userJson = gson.toJson(firstUser);

        mockMvc.perform(patch("/user/{userId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"test123@test.de\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(userJson));
    }

    @Test
    @WithMockUser(username = "user", roles = "CO_OKR_ADMIN")
    public void testPatchNotExistingUserEndpoint() throws Exception {
        mockMvc.perform(patch("/user/{userId}", 12L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\": \"test123@test.de\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found!"));
    }

    @Test
    @WithMockUser(username = "user", roles = "CO_OKR_ADMIN")
    public void testGetAllUsersEndpoint() throws Exception {
        String userDtosJson = gson.toJson(userDtos);
        when(userRepository.findAll()).thenAnswer(invocationOnMock -> {
            return users;
        });

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(content().string(userDtosJson));
    }

    @Test
    @WithMockUser(username = "user", roles = "CO_OKR_ADMIN")
    public void testGetUserEndpoint() throws Exception {
        String userDtoJson = gson.toJson(userDtos.getFirst());
        when(userRepository.findById(1L)).thenAnswer(invocationOnMock -> {
            return Optional.of(users.getFirst());
        });

        mockMvc.perform(get("/user/{userId}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string(userDtoJson));
    }

    @Test
    @WithMockUser(username = "user", roles = "CO_OKR_ADMIN")
    public void testGetNotExistingUserEndpoint() throws Exception {
        mockMvc.perform(get("/user/{userId}", 12L))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found!"));
    }

    @Test
    @WithMockUser(username = "user", roles = "CO_OKR_ADMIN")
    public void testDeleteUserEndpoint() throws Exception {
        String response = userDtos.getFirst().getUsername() + " deleted!";
        when(userRepository.findById(1L)).thenAnswer(invocationOnMock -> {
            return Optional.of(users.getFirst());
        });

        mockMvc.perform(delete("/user/{userId}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string(response));
    }

    @Test
    @WithMockUser(username = "user", roles = "CO_OKR_ADMIN")
    public void testDeleteNotExistingUserEndpoint() throws Exception {
        mockMvc.perform(delete("/user/{userId}", 12L))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found!"));
    }
}
