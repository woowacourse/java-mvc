package com.techcourse.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class HomeControllerTest {

    private final HomeController controller = new HomeController();

    @DisplayName("/ 요청 시")
    @Nested
    class Home {

        @DisplayName("/index.jsp 가 응답된다")
        @Test
        void root_request_should_be_returned_with_index() {
            // given
            final HttpServletRequest request = mock(HttpServletRequest.class);
            final HttpServletResponse response = mock(HttpServletResponse.class);

            // when
            final String viewName = controller.execute(request, response);

            // then
            assertThat(viewName).isEqualTo("/index.jsp");
        }
    }
}
