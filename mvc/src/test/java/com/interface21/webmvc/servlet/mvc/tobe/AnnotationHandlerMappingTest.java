package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
        when(request.getContextPath()).thenReturn("");
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
        when(request.getContextPath()).thenReturn("");
        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("POST");

        final var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final var modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    @DisplayName("동일한 URL은 HTTP 메서드에 따라 서로 다른 컨트롤러 메서드로 매핑된다")
    void sameUrlIsMappedByHttpMethod() throws Exception {
        // given
        final var getRequest = mock(HttpServletRequest.class);
        when(getRequest.getContextPath()).thenReturn("");
        when(getRequest.getRequestURI()).thenReturn("/same-test");
        when(getRequest.getMethod()).thenReturn("GET");

        final var postRequest = mock(HttpServletRequest.class);
        when(postRequest.getContextPath()).thenReturn("");
        when(postRequest.getRequestURI()).thenReturn("/same-test");
        when(postRequest.getMethod()).thenReturn("POST");

        final var response = mock(HttpServletResponse.class);

        // when
        final var getHandler = (HandlerExecution) handlerMapping.getHandler(getRequest);
        final var postHandler = (HandlerExecution) handlerMapping.getHandler(postRequest);
        final var getModelAndView = getHandler.handle(getRequest, response);
        final var postModelAndView = postHandler.handle(postRequest, response);

        // then
        assertThat(getModelAndView.getObject("method")).isEqualTo("GET");
        assertThat(postModelAndView.getObject("method")).isEqualTo("POST");
    }

    @Test
    @DisplayName("HTTP 메서드를 지정하지 않으면 모든 지원 메서드에 매핑된다")
    void omittedMethodMatchesEverySupportedHttpMethod() {
        for (final RequestMethod method : RequestMethod.values()) {
            // given
            final var request = mock(HttpServletRequest.class);
            when(request.getContextPath()).thenReturn("");
            when(request.getRequestURI()).thenReturn("/any-method");
            when(request.getMethod()).thenReturn(method.name());

            // when
            final var handler = handlerMapping.getHandler(request);

            // then
            assertThat(handler).isInstanceOf(HandlerExecution.class);
        }
    }

    @Test
    @DisplayName("동일한 URL과 HTTP 메서드가 중복되면 초기화에 실패한다")
    void duplicateMappingFailsOnInitialization() {
        // given
        final var duplicateMapping = new AnnotationHandlerMapping("duplicatehandlers");

        // when, then
        assertThatThrownBy(duplicateMapping::initialize)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("/duplicate")
                .hasMessageContaining("GET");
    }

    @Test
    @DisplayName("애플리케이션의 context path를 제외한 경로로 핸들러를 찾는다")
    void matchesRequestPathWithoutContextPath() {
        // given
        final var request = mock(HttpServletRequest.class);
        when(request.getContextPath()).thenReturn("/java-mvc");
        when(request.getRequestURI()).thenReturn("/java-mvc/get-test");
        when(request.getMethod()).thenReturn("GET");

        // when
        final var handler = handlerMapping.getHandler(request);

        // then
        assertThat(handler).isInstanceOf(HandlerExecution.class);
    }

    @Test
    void registersOverriddenMappingOnlyOnce() throws Exception {
        final var mapping = new AnnotationHandlerMapping("inheritedhandlers.child");
        mapping.initialize();
        assertThat(execute(mapping, "/inherited").getObject("handler")).isEqualTo("child");
    }

    @Test
    void inheritsMappingWhenOverrideHasNoAnnotation() throws Exception {
        final var mapping = new AnnotationHandlerMapping("inheritedhandlers.unannotated");
        mapping.initialize();
        assertThat(execute(mapping, "/inherited").getObject("handler")).isEqualTo("inherited-child");
    }

    @Test
    void childMappingReplacesParentMapping() throws Exception {
        final var mapping = new AnnotationHandlerMapping("inheritedhandlers.remapped");
        mapping.initialize();
        assertThat(execute(mapping, "/child").getObject("handler")).isEqualTo("remapped");
        assertThat(mapping.getHandler(request("/inherited"))).isNull();
    }

    @Test
    void usesInterfaceMappingOnPublicImplementation() throws Exception {
        final var mapping = new AnnotationHandlerMapping("inheritedhandlers.interfaces");
        mapping.initialize();
        assertThat(execute(mapping, "/interface").getObject("handler")).isEqualTo("interface");
    }

    @Test
    void rejectsConflictingInterfaceMappings() {
        final var mapping = new AnnotationHandlerMapping("inheritedhandlers.conflicts");
        assertThatThrownBy(mapping::initialize)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Ambiguous request mapping");
    }

    @Test
    void canInitializeAgain() throws Exception {
        handlerMapping.initialize();
        assertThat(execute(handlerMapping, "/get-test")).isNotNull();
    }

    @Test
    void failedInitializationDoesNotExposePartialMappings() {
        final var mapping = new AnnotationHandlerMapping("duplicatehandlers");
        assertThatThrownBy(mapping::initialize).isInstanceOf(IllegalStateException.class);
        assertThat(mapping.getHandler(request("/duplicate"))).isNull();
    }

    @Test
    void inheritsNearestParentMapping() throws Exception {
        final var mapping = new AnnotationHandlerMapping("inheritedhandlers.nearest");
        mapping.initialize();
        assertThat(execute(mapping, "/nearest").getObject("handler")).isEqualTo("nearest-child");
        assertThat(mapping.getHandler(request("/inherited"))).isNull();
    }

    @Test
    void classMappingTakesPrecedenceOverInterfaces() throws Exception {
        final var mapping = new AnnotationHandlerMapping("inheritedhandlers.classmapping");
        mapping.initialize();
        assertThat(execute(mapping, "/class").getObject("handler")).isEqualTo("class");
        assertThat(mapping.getHandler(request("/first"))).isNull();
        assertThat(mapping.getHandler(request("/second"))).isNull();
    }

    private ModelAndView execute(
            AnnotationHandlerMapping mapping, String path) throws Exception {
        final var request = request(path);
        final var handler = (HandlerExecution) mapping.getHandler(request);
        assertThat(handler).isNotNull();
        return handler.handle(request, mock(HttpServletResponse.class));
    }

    private HttpServletRequest request(String path) {
        final var request = mock(HttpServletRequest.class);
        when(request.getContextPath()).thenReturn("");
        when(request.getRequestURI()).thenReturn(path);
        when(request.getMethod()).thenReturn("GET");
        return request;
    }

    @Test
    @DisplayName("요청과 응답 인자가 아닌 핸들러는 초기화할 때 거부한다")
    void rejectsInvalidHandlerParameters() {
        // given
        final var invalidMapping = new AnnotationHandlerMapping("invalidhandlers.parameters");

        // when, then
        assertThatThrownBy(invalidMapping::initialize)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("InvalidParametersController.invalid");
    }

    @Test
    @DisplayName("ModelAndView를 반환하지 않는 핸들러는 초기화할 때 거부한다")
    void rejectsInvalidHandlerReturnType() {
        // given
        final var invalidMapping = new AnnotationHandlerMapping("invalidhandlers.returntype");

        // when, then
        assertThatThrownBy(invalidMapping::initialize)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("InvalidReturnTypeController.invalid");
    }

    @Test
    @DisplayName("공개되지 않은 핸들러는 초기화할 때 거부한다")
    void rejectsNonPublicHandler() {
        // given
        final var invalidMapping = new AnnotationHandlerMapping("invalidhandlers.visibility");

        // when, then
        assertThatThrownBy(invalidMapping::initialize)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("NonPublicController.invalid");
    }
}
