package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


@DisplayName("TestController는")
class TestControllerTest {

    private HttpServletRequest request;
    private HttpServletResponse response;

    private TestController testController;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        testController = new TestController();
    }

    @DisplayName("/get-test GET 요청시")
    @Nested
    class RegisterGet {

        @BeforeEach
        void setUp() {
            when(request.getRequestURI()).thenReturn("/get-test");
            when(request.getMethod()).thenReturn("GET");
        }

        @DisplayName("빈 페이지와 id 정보를 null로 반환한다.")
        @Test
        void loginPage() {
            // when
            ModelAndView modelAndView = testController.findUserId(request, response);

            // then
            assertThat(modelAndView.getViewName()).isEqualTo("");
            assertThat(modelAndView.getObject("id")).isNull();
        }
    }

    @DisplayName("/post-test POST 요청시")
    @Nested
    class RegisterPost {

        @BeforeEach
        void setUp() {
            when(request.getRequestURI()).thenReturn("/post-test");
            when(request.getMethod()).thenReturn("POST");
        }

        @DisplayName("빈 페이지와 id 정보를 null로 반환한다.")
        @Test
        void registerSuccess() {
            // when
            ModelAndView modelAndView = testController.findUserId(request, response);

            // then
            assertThat(modelAndView.getViewName()).isEqualTo("");
            assertThat(modelAndView.getObject("id")).isNull();
        }
    }
}
