package co.edu.cue.pruebasConActions;

import co.edu.cue.pruebasConActions.domain.entities.User;
import co.edu.cue.pruebasConActions.repositories.UserRepo;
import co.edu.cue.pruebasConActions.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

	@Mock
	private UserRepo usersRepository;

	@InjectMocks
	private UserService userService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testCreateUser() {
		User user = new User();
		user.setUserId(1L);
		user.setUserName("Test User");
		user.setUserEmail("test@example.com");

		when(usersRepository.save(any(User.class))).thenReturn(user);

		User createdUser = userService.createUser(user);
		assertEquals("Test User", createdUser.getUserName());
		assertEquals("test@example.com", createdUser.getUserEmail());
	}

	@Test
	void testGetUserById() {
		User user = new User();
		user.setUserId(1L);
		user.setUserName("Test User");

		when(usersRepository.findById(1L)).thenReturn(Optional.of(user));

		User foundUser = userService.getUserById(1L);
		assertNotNull(foundUser);
		assertEquals(1L, foundUser.getUserId());
		assertEquals("Test User", foundUser.getUserName());
	}

	@Test
	void testUpdateUser() {
		User existingUser = new User();
		existingUser.setUserId(1L);
		existingUser.setUserName("Old Name");
		existingUser.setUserEmail("old@example.com");

		User updatedUserInfo = new User();
		updatedUserInfo.setUserName("New Name");
		updatedUserInfo.setUserEmail("new@example.com");

		when(usersRepository.findById(1L)).thenReturn(Optional.of(existingUser));
		when(usersRepository.save(any(User.class))).thenReturn(existingUser);

		User updatedUser = userService.updateUser(1L, updatedUserInfo);
		assertNotNull(updatedUser);
		assertEquals("New Name", updatedUser.getUserName());
		assertEquals("new@example.com", updatedUser.getUserEmail());
	}

	@Test
	void testDeleteUser() {
		doNothing().when(usersRepository).deleteById(1L);

		userService.deleteUser(1L);

		verify(usersRepository, times(1)).deleteById(1L);
	}

	@Test
	void testFindUsersByEmailDomain() {
		User user1 = new User();
		user1.setUserEmail("user1@example.com");
		User user2 = new User();
		user2.setUserEmail("user2@example.com");

		when(usersRepository.findByUserEmailEndingWith("example.com")).thenReturn(List.of(user1, user2));

		List<User> users = userService.findUsersByUserEmailDomain("example.com");
		assertNotNull(users);
		assertEquals(2, users.size());
		assertEquals("user1@example.com", users.get(0).getUserEmail());
		assertEquals("user2@example.com", users.get(1).getUserEmail());
	}
}