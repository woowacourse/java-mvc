package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.interface21.web.bind.annotation.RequestMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples.success");
        handlerMapping.initialize();
    }

    @Test
    @DisplayName("GET 메소드에 대한 요청 핸들러를 찾아서 처리할 수 있다.")
    void getHandlerTest() throws Exception {
        final var request = new MockHttpServletRequest("GET", "/get-test");
        request.setAttribute("id", "gugu");
        final var response = new MockHttpServletResponse();

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    @DisplayName("POST 메소드에 대한 요청 핸들러를 찾아서 처리할 수 있다.")
    void postHandlerTest() throws Exception {
        final var request = new MockHttpServletRequest("POST", "/post-test");
        request.setAttribute("id", "gugu");
        final var response = new MockHttpServletResponse();

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @ParameterizedTest
    @EnumSource(value = RequestMethod.class)
    @DisplayName("RequestMapping의 method가 설정되어 있지 않은 경우 모든 HTTP method를 지원해야 한다.")
    void requestMappingWithoutMethodSettingTest(RequestMethod method) throws Exception {
        final var request = new MockHttpServletRequest(method.name(), "/all-method-test");
        request.setAttribute("id", "gugu");
        final var response = new MockHttpServletResponse();

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    @DisplayName("같은 url, method에 대한 핸들러를 중복으로 등록하는 경우 예외가 발생한다.")
    void duplicateHandlerKeyExceptionTest() {
        AnnotationHandlerMapping handlerMapping = new AnnotationHandlerMapping("samples.exception.duplicate");
        assertThatThrownBy(() -> handlerMapping.initialize())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("GET /get-test에 대한 핸들러가 중복됩니다.");
    }

    @Test
    @DisplayName("url, method를 처리할 수 있는 핸들러가 존재하지 않는 경우 예외가 발생한다.")
    void getHandlerAbsenceExceptionTest() {
        final var request = new MockHttpServletRequest("GET", "/absence-test");

        assertThatThrownBy(() -> handlerMapping.getHandler(request))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("GET /absence-test를 처리할 수 있는 핸들러가 존재하지 않습니다.");
    }
}
