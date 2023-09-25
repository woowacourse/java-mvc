package webmvc.org.springframework.web.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
    }

    @Test
    void methodDuplicationException_existMappingInOtherClass_case1() {
        // given & when & then
        AnnotationHandlerMapping duplicatedHandlerMapping = new AnnotationHandlerMapping(
            "samples",
            "duplicate.case1");
        assertThatThrownBy(() -> duplicatedHandlerMapping.initialize())
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Duplicated HandlerKey");
    }

    @Test
    void methodDuplicationException_existMappingIntSameClass_case2() {
        // given & when & then
        AnnotationHandlerMapping duplicatedHandlerMapping = new AnnotationHandlerMapping(
            "duplicate.case2");
        assertThatThrownBy(() -> duplicatedHandlerMapping.initialize())
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Duplicated HandlerKey");
    }

    @Test
    void methodDuplicationException_duplicateMappingInOneMethod_case3() {
        // given & when & then
        AnnotationHandlerMapping duplicatedHandlerMapping = new AnnotationHandlerMapping(
            "duplicate.case3");
        assertThatThrownBy(() -> duplicatedHandlerMapping.initialize())
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Duplicated HandlerKey");
    }

    @Test
    void oneMethodCanCreateManyHttpRequestMappings() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/multi-method-test");
        when(request.getMethod()).thenReturn("GET");

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request).get();
        final var getModelAndView = handlerExecution.handle(request, response);

        when(request.getMethod()).thenReturn("POST");

        final var postModelAndView = handlerExecution.handle(request, response);

        assertThat(getModelAndView.getObject("id")).isEqualTo("getPooh");
        assertThat(postModelAndView.getObject("id")).isEqualTo("postPooh");
    }

    @Test
    void get() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request).get();
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

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request).get();
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }
}
