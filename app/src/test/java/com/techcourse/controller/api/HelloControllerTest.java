package com.techcourse.controller.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@DisplayName("HelloController 테스트")
class HelloControllerTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private HelloController helloController;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        helloController = new HelloController();
    }

    @DisplayName("HelloController GET /api/hello 요청 테스트")
    @Test
    void hello() {
        // given
        // when
        final ModelAndView modelAndView = helloController.hello(request, response);
        final Map<String, Object> model = modelAndView.getModel();

        // then
        assertThat(model).containsEntry("hello", "Hello, World!");
    }
}