package com.techcourse.controller;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JsonView;
import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Test
    void show_returnsJsonView_andModelContainsUser() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        String account = "ddiyong";
        String password = "1234";
        String email = "ddiyong@gmail.com";

        when(request.getParameter("account")).thenReturn(account);

        User expected = new User(1L, account, password, email);

        try (MockedStatic<InMemoryUserRepository> mocked = mockStatic(InMemoryUserRepository.class)) {
            mocked.when(() -> InMemoryUserRepository.findByAccount(account))
                    .thenReturn(Optional.of(expected));

            UserController controller = new UserController();

            // when
            ModelAndView mav = controller.show(request, response);

            // then
            assertNotNull(mav, "ModelAndView should not be null");
            assertTrue(mav.getView() instanceof JsonView, "View should be JsonView");
            assertEquals(expected, mav.getModel().get("user"), "Model should contain 'user'");

            verify(request).getParameter("account");
            mocked.verify(() -> InMemoryUserRepository.findByAccount(account));
            verifyNoMoreInteractions(request, response);
        }
    }
}