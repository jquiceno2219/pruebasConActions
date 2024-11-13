package co.edu.cue.pruebasConActions;

import co.edu.cue.pruebasConActions.controller.UserController;
import co.edu.cue.pruebasConActions.domain.entities.User;
import co.edu.cue.pruebasConActions.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;  // Used to simulate HTTP requests

    @MockBean
    private UserService userService;  // Mock the service layer

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User(1L, "john_doe", "john@example.com");
    }

    // Test for creating a user
    @Test
    public void testCreateUser() throws Exception {
        // Given
        when(userService.createUser(any(User.class))).thenReturn(user);

        // When & Then
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\": \"john_doe\", \"userEmail\": \"john@example.com\"}"))
                .andExpect(status().isOk())  // Verify the status is OK
                .andExpect(jsonPath("$.userName").value("john_doe"))
                .andExpect(jsonPath("$.userEmail").value("john@example.com"));
    }

    // Test for getting a user by ID
    @Test
    public void testGetUserById() throws Exception {
        // Given
        when(userService.getUserById(1L)).thenReturn(user);

        // When & Then
        mockMvc.perform(get("/users/{userId}", 1L))
                .andExpect(status().isOk())  // Verify the status is OK
                .andExpect(jsonPath("$.userName").value("john_doe"))
                .andExpect(jsonPath("$.userEmail").value("john@example.com"));
    }

    // Test for updating a user
    @Test
    public void testUpdateUser() throws Exception {
        // Given
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(user);

        // When & Then
        mockMvc.perform(put("/users/{userId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\": \"john_doe\", \"userEmail\": \"john@example.com\"}"))
                .andExpect(status().isOk())  // Verify the status is OK
                .andExpect(jsonPath("$.userName").value("john_doe"))
                .andExpect(jsonPath("$.userEmail").value("john@example.com"));
    }

    // Test for deleting a user
    @Test
    public void testDeleteUser() throws Exception {
        // Given
        doNothing().when(userService).deleteUser(1L);

        // When & Then
        mockMvc.perform(delete("/users/{userId}", 1L))
                .andExpect(status().isNoContent());  // Verify the status is No Content
    }

    // Test for finding users by email domain
    @Test
    public void testFindUsersByEmailDomain() throws Exception {
        // Given
        when(userService.findUsersByUserEmailDomain("example.com")).thenReturn(List.of(user));

        // When & Then
        mockMvc.perform(get("/users/email-domain/{domain}", "example.com"))
                .andExpect(status().isOk())  // Verify the status is OK
                .andExpect(jsonPath("$[0].userName").value("john_doe"))
                .andExpect(jsonPath("$[0].userEmail").value("john@example.com"));
    }
}