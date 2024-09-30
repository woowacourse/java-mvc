package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;

import com.interface21.DispatcherServlet;
import com.interface21.web.bind.annotation.RequestMethod;

public class DispatcherServletTest {

    private DispatcherServlet sut;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ArgumentCaptor<String> captor;

    @BeforeEach
    void setUp() {
        sut = new DispatcherServlet("samples");
        sut.init();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        captor = ArgumentCaptor.forClass(String.class);
    }

    @Nested
    @DisplayName("Controller 어노테이션 기반 컨트롤러에 대한 요청을 처리한다.")
    class ControllerAnnotation {

        @Test
        @DisplayName("/get-test에 대한 GET 요청을 처리한다.")
        void handleGetTest() throws ServletException, IOException {
            // given
            when(request.getRequestURI()).thenReturn("/get-test");
            when(request.getMethod()).thenReturn("GET");
            when(request.getRequestDispatcher(anyString())).thenReturn(mock());

            // when
            sut.service(request, response);

            // then
            verify(request).getRequestDispatcher(captor.capture());
            assertThat(captor.getValue()).isEqualTo("/get");
        }

        @Test
        @DisplayName("/post-test에 대한 POST 요청을 처리한다.")
        void handlePostTest() throws ServletException, IOException {
            // given
            when(request.getRequestURI()).thenReturn("/post-test");
            when(request.getMethod()).thenReturn("POST");
            when(request.getRequestDispatcher(anyString())).thenReturn(mock());

            // when
            sut.service(request, response);

            // then
            verify(request).getRequestDispatcher(captor.capture());
            assertThat(captor.getValue()).isEqualTo("/post");
        }

        @ParameterizedTest
        @DisplayName("/all-test에 대한 모든 요청을 처리한다.")
        @EnumSource(RequestMethod.class)
        void handlePostTest(RequestMethod method) throws ServletException, IOException {
            // given
            when(request.getRequestURI()).thenReturn("/all-test");
            when(request.getMethod()).thenReturn(method.name());
            when(request.getRequestDispatcher(anyString())).thenReturn(mock());

            // when
            sut.service(request, response);

            // then
            verify(request).getRequestDispatcher(captor.capture());
            assertThat(captor.getValue()).isEqualTo("/all");
        }
    }
}
