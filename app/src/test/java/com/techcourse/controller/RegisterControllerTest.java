package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("레지스터 컨트롤러 테스트")
class RegisterControllerTest {

    private HttpServletRequest request;
    private RegisterController registerController = new RegisterController();

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
    }

    @DisplayName("Post 요청 시")
    @Nested
    class RegisterPost {

        @DisplayName("유저가 등록된다")
        @Test
        void userRegisterTest() {
            //given
            when(request.getParameter("account")).thenReturn("street");
            when(request.getParameter("password")).thenReturn("women");
            when(request.getParameter("email")).thenReturn("fight@whyNotSee.com");

            String expectedResult = "redirect:/index.jsp";
            //when
            String destination = registerController.register(request);
            //then
            User streetWomenFighter = InMemoryUserRepository.findByAccount("street").get();
            assertThat(destination).isEqualTo(expectedResult);
            assertThat(streetWomenFighter.account().value()).isEqualTo("street");
            assertThat(streetWomenFighter.checkPassword("women")).isTrue();
        }
    }
}