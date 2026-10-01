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
    void mapsMultipleHttpMethodsToTheSameHandler() {
        initializeScannerFixtures();

        final var getHandler = handlerMapping.getHandler(request("/shared", RequestMethod.GET));
        final var postHandler = handlerMapping.getHandler(request("/shared", RequestMethod.POST));

        assertThat(getHandler).isInstanceOf(HandlerExecution.class);
        assertThat(postHandler).isSameAs(getHandler);
        assertThat(handlerMapping.getHandler(request("/shared", RequestMethod.PUT))).isNull();
    }

    @Test
    void mapsAllHttpMethodsWhenMethodIsNotSpecified() {
        initializeScannerFixtures();

        for (RequestMethod method : RequestMethod.values()) {
            assertThat(handlerMapping.getHandler(request("/all-methods", method)))
                    .as("handler for %s", method)
                    .isInstanceOf(HandlerExecution.class);
        }
    }

    @Test
    void sharesOneControllerInstanceAcrossMappedMethods() throws Exception {
        initializeScannerFixtures();
        final var response = mock(HttpServletResponse.class);
        final var getRequest = request("/shared", RequestMethod.GET);
        final var postRequest = request("/shared", RequestMethod.POST);
        final var otherRequest = request("/all-methods", RequestMethod.GET);
        final var getHandler = (HandlerExecution) handlerMapping.getHandler(getRequest);
        final var postHandler = (HandlerExecution) handlerMapping.getHandler(postRequest);
        final var otherHandler = (HandlerExecution) handlerMapping.getHandler(otherRequest);

        assertThat(getHandler.handle(getRequest, response).getObject("invocations")).isEqualTo(1);
        assertThat(postHandler.handle(postRequest, response).getObject("invocations")).isEqualTo(2);
        assertThat(otherHandler.handle(otherRequest, response).getObject("invocations")).isEqualTo(3);
    }

    @Test
    void ignoresMappingsOnClassesWithoutControllerAnnotation() {
        initializeScannerFixtures();

        assertThat(handlerMapping.getHandler(request("/ignored", RequestMethod.GET))).isNull();
    }

    @Test
    void rejectsDuplicateMappingsDuringInitialization() {
        final var mapping = new AnnotationHandlerMapping("scannerfixtures.duplicate");

        assertThatThrownBy(mapping::initialize)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("/duplicate")
                .hasMessageContaining("GET");
    }

    private void initializeScannerFixtures() {
        handlerMapping = new AnnotationHandlerMapping("scannerfixtures.valid");
        handlerMapping.initialize();
    }

    private HttpServletRequest request(final String uri, final RequestMethod method) {
        final var request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(uri);
        when(request.getMethod()).thenReturn(method.name());
        return request;
    }
}
