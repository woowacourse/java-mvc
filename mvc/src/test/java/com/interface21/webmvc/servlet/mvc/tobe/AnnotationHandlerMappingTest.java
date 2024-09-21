package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
    }

    @Test
    @DisplayName("핸들러를 통해 GET 요청을 하고 모델에 속성 id 를 조회한다.")
    void get() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        // when
        HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        ModelAndView modelAndView = handlerExecution.handle(request, response);

        // then
        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    @DisplayName("핸들러를 통해 POST 요청을 하고 모델에 속성 id 를 조회한다.")
    void post() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("POST");

        // when
        HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        ModelAndView modelAndView = handlerExecution.handle(request, response);

        // then
        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    @DisplayName("동일한 요청을 처리하는 2개의 핸들러 등록 시 예외가 발생한다.")
    void assignHandlerDuplicated() {
        // given
        String basePackage = "com.interface21.webmvc.servlet.samples.duplicated";
        AnnotationHandlerMapping mapping = new AnnotationHandlerMapping(basePackage);

        // when & then
        assertThatCode(mapping::initialize)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("HandlerKey exists");
    }

    @Test
    @DisplayName("생성자가 가려진 컨트롤러 등록 시 예외가 발생한다.")
    void assignHandlerPrivate() {
        // given
        String basePackage = "com.interface21.webmvc.servlet.samples.hided";
        AnnotationHandlerMapping mapping = new AnnotationHandlerMapping(basePackage);

        // when & then
        assertThatCode(mapping::initialize)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Public constructor not found");
    }

    @Test
    @DisplayName("정의되지 않은 HTTP 메서드로 요청 시 핸들러를 찾을 수 없다.")
    void requestWithInvalidMethod() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("CONNECT");

        // when
        Object handlerExecution = handlerMapping.getHandler(request);

        // then
        assertThat(handlerExecution).isNull();
    }

    @Test
    @DisplayName("등록되지 않은 엔드포인트로 요청 시 핸들러를 찾을 수 없다.")
    void requestWithUnassignedEndpoint() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/unknown-endpoint");
        when(request.getMethod()).thenReturn("GET");

        // when
        Object handlerExecution = handlerMapping.getHandler(request);

        // then
        assertThat(handlerExecution).isNull();
    }

    @Test
    @DisplayName("컨트롤러의 메서드가 요구하는 파라미터를 동적으로 대응하여 의존성을 주입한다.")
    void passArgumentsWithRequiredParameters() throws Exception {
        joinPathOfControllerAndMethod();
    }

    @Test
    @DisplayName("컨트롤러와 메서드의 경로를 합쳐서 엔드포인트로 요청 경로를 결정한다.")
    void joinPathOfControllerAndMethod() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getRequestURI()).thenReturn("/api/join-paths");
        when(request.getMethod()).thenReturn("GET");

        // when
        HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        ModelAndView modelAndView = handlerExecution.handle(request, response);

        // then
        assertThat(modelAndView.getObject("message")).isEqualTo("Paths joined");
    }
}
