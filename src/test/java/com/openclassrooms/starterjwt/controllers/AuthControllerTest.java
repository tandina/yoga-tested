package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "oc.app.jwtSecret=openclassrooms",
        "oc.app.jwtExpirationMs=3600000"
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthControllerTest {
     @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @AfterEach
    public void tearDown() {

        userRepository.deleteAll();
    }

    @Test
    public void testRegisterUserConnexionSuccess() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("Test");
        signupRequest.setLastName("User");
        signupRequest.setPassword("testPassword");
        System.out.println("inscription"+ signupRequest);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk());
    }

    @Test
    public void testRegisterUserConnexionFail() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("Test");
        signupRequest.setLastName("User");
        signupRequest.setPassword("");
        System.out.println("inscription"+ signupRequest);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testLoginUser() throws Exception {
        User userTest = new User();
        userTest.setEmail("test@test.fr");
        userTest.setFirstName("Tandina");
        userTest.setLastName("Touré");
        userTest.setPassword(passwordEncoder.encode("passwordTest"));

        userRepository.save(userTest);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.fr");
        loginRequest.setPassword("passwordTest");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());

    }

    @Test
    public void testLoginUserFail() throws Exception {
        User userTest = new User();
        userTest.setEmail("test@test.fr");
        userTest.setFirstName("Tandina");
        userTest.setLastName("Touré");
        userTest.setPassword(passwordEncoder.encode("passwordTest"));

        userRepository.save(userTest);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.fr");
        loginRequest.setPassword("password");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void testLoginUserBlankField() throws Exception {
        User userTest = new User();
        userTest.setEmail("test@test.fr");
        userTest.setFirstName("Tandina");
        userTest.setLastName("Touré");
        userTest.setPassword(passwordEncoder.encode("passwordTest"));

        userRepository.save(userTest);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.fr");
        loginRequest.setPassword("");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());

    }
}
