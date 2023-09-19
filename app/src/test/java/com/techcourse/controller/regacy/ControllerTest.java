package com.techcourse.controller.regacy;

import static org.mockito.Mockito.mockStatic;

import com.techcourse.controller.UserSession;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

public abstract class ControllerTest {

    @Mock
    HttpServletRequest httpServletRequest;

    MockedStatic<UserSession> mockUserSession;
    MockedStatic<InMemoryUserRepository> mockUserRepository;

    @BeforeEach
    void setting() {
        MockitoAnnotations.openMocks(this);
        mockUserSession = mockStatic(UserSession.class);
        mockUserRepository = mockStatic(InMemoryUserRepository.class);
    }

    @AfterEach
    void deleteStaticMock() {
        mockUserSession.close();
        mockUserRepository.close();
    }
}
