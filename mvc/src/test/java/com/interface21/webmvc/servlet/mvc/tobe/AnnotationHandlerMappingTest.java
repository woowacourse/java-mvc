package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples.valid");
        handlerMapping.initialize();
    }

    @DisplayName("같은 path와 method를 가진 handler를 두 개 이상 등록하려 하면 예외가 발생한다.")
    @Test
    void initialize_duplicatedAnnotationValue() {
        // given
        AnnotationHandlerMapping mapping = new AnnotationHandlerMapping("samples.duplicatedvalue");

        // when & then
        assertThatThrownBy(mapping::initialize).isInstanceOf(HandlingException.class)
                .hasMessage("같은 요청을 처리할 메서드가 두 개 이상 존재합니다.");
    }

    @DisplayName("기본 생성자를 사용할 수 없는 컨트롤러를 등록하려 하면 예외가 발생한다.")
    @Test
    void initialize_invalidConstructor() {
        // given
        AnnotationHandlerMapping mapping = new AnnotationHandlerMapping("samples.invalidconstructor");

        // when & then
        assertThatThrownBy(mapping::initialize).isInstanceOf(HandlingException.class)
                .hasMessage("해당 컨트롤러의 기본 생성자로 인스턴스를 생성할 수 없습니다.");
    }

    @Test
    void get() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final var handlerExecution = handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    void post() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("POST");

        final var handlerExecution = handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }
}
