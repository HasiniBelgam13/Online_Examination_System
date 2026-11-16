package com.qveto.exam.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.qveto.exam.dao.UserDao;
import com.qveto.exam.model.Role;
import com.qveto.exam.model.User;
import com.qveto.exam.util.PasswordUtil;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserDao userDao;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userDao);
    }

    @Test
    void authenticate_Success_WhenActiveUserAndValidPassword() {
        char[] rawPassword = "CorrectHorseBatteryStaple!1".toCharArray();
        String hash = PasswordUtil.hashPassword(rawPassword.clone());

        User activeUser = new User(
            1L, "student01", hash, "Aarav Sharma", Role.STUDENT, true, LocalDateTime.now()
        );
        when(userDao.findByUserName("student01")).thenReturn(Optional.of(activeUser));

        char[] loginAttempt = "CorrectHorseBatteryStaple!1".toCharArray();
        User result = authService.authenticate("  student01  ", loginAttempt);

        assertEquals(1L, result.id());
        assertEquals(Role.STUDENT, result.role());
        // Verify password array was wiped in memory
        for (char c : loginAttempt) {
            assertEquals('\0', c);
        }
    }

    @Test
    void authenticate_ThrowsSecurityException_WhenAccountIsDeactivated() {
        char[] rawPassword = "SecretPassword123!".toCharArray();
        String hash = PasswordUtil.hashPassword(rawPassword.clone());

        User inactiveUser = new User(
            2L, "disabled_user", hash, "Inactive Student", Role.STUDENT, false, LocalDateTime.now()
        );
        when(userDao.findByUserName("disabled_user")).thenReturn(Optional.of(inactiveUser));

        SecurityException ex = assertThrows(
            SecurityException.class,
            () -> authService.authenticate("disabled_user", "SecretPassword123!".toCharArray())
        );
        assertTrue(ex.getMessage().contains("deactivated"));
    }

    @Test
    void authenticate_ThrowsSecurityException_WhenPasswordIsWrong() {
        String hash = PasswordUtil.hashPassword("ActualPassword123!".toCharArray());
        User activeUser = new User(
            1L, "admin01", hash, "System Admin", Role.ADMIN, true, LocalDateTime.now()
        );
        when(userDao.findByUserName("admin01")).thenReturn(Optional.of(activeUser));

        assertThrows(
            SecurityException.class,
            () -> authService.authenticate("admin01", "WrongPassword!".toCharArray())
        );
    }

    @Test
    void authenticate_ThrowsIllegalArgumentException_WhenInputsBlank() {
        assertThrows(
            IllegalArgumentException.class,
            () -> authService.authenticate("   ", "pass".toCharArray())
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> authService.authenticate("admin", "   ".toCharArray())
        );
    }
}