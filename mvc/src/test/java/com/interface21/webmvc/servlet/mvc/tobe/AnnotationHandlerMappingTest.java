package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
    }

    @Test
    void get() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
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

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    void mapsSameUrlByHttpMethod() throws Exception {
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        when(request.getRequestURI()).thenReturn("/same-url");

        when(request.getMethod()).thenReturn("GET");
        final var getHandler = (HandlerExecution) handlerMapping.getHandler(request);
        final var getModelAndView = getHandler.handle(request, response);
        assertThat(getModelAndView.getObject("method")).isEqualTo("GET");

        when(request.getMethod()).thenReturn("POST");
        final var postHandler = (HandlerExecution) handlerMapping.getHandler(request);
        final var postModelAndView = postHandler.handle(request, response);
        assertThat(postModelAndView.getObject("method")).isEqualTo("POST");
    }

    @Test
    void rejectsDuplicateRequestMappingsDuringInitialization() {
        final var duplicateMapping = new AnnotationHandlerMapping("duplicate");

        assertThatThrownBy(duplicateMapping::initialize)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("/duplicate")
                .hasMessageContaining("GET");
    }

    @Test
    void rejectsInvalidParametersDuringInitialization() {
        final var invalidMapping = new AnnotationHandlerMapping("invalidparameters");

        assertThatThrownBy(invalidMapping::initialize)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Invalid @RequestMapping method")
                .hasMessageContaining("InvalidParametersController.show");
    }

    @Test
    void rejectsInvalidReturnTypeDuringInitialization() {
        final var invalidMapping = new AnnotationHandlerMapping("invalidreturntype");

        assertThatThrownBy(invalidMapping::initialize)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Invalid @RequestMapping method")
                .hasMessageContaining("InvalidReturnTypeController.show");
    }

    @Test
    void rejectsPrivateMappingMethodDuringInitialization() {
        final var invalidMapping = new AnnotationHandlerMapping("privatemapping");

        assertThatThrownBy(invalidMapping::initialize)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Invalid @RequestMapping method")
                .hasMessageContaining("PrivateMappingController.show");
    }

    @Test
    void supportsAllHttpMethodsWhenMethodIsOmitted() {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/all-test");

        for (final RequestMethod method : RequestMethod.values()) {
            when(request.getMethod()).thenReturn(method.name());
            assertThat(handlerMapping.getHandler(request)).isInstanceOf(HandlerExecution.class);
        }
    }
}
