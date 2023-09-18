package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.asis.ForwardController;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("DispatcherServlet 테스트")
class DispatcherServletTest {

    @Test
    void DispatcherServlet_초기화시_등록된_adapter와_handler가_등록된다() {
        // given
        final var dispatcherServlet = new DispatcherServlet();

        // when
        dispatcherServlet.init();

        // then
        assertThat(dispatcherServlet).extracting("handlerMappings").isNotNull();
        assertThat(dispatcherServlet).extracting("handlerAdapters").isNotNull();
    }

    @Test
    void handler와_adapter를_통해_웹요청을_처리한다() {
        // given
        final DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();

        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        given(request.getRequestURI()).willReturn("/");
        given(request.getMethod()).willReturn("GET");
        given(request.getRequestDispatcher(any())).willReturn(requestDispatcher);

        // when & then
        assertThatCode(() -> dispatcherServlet.service(request, response)).doesNotThrowAnyException();
    }
}
