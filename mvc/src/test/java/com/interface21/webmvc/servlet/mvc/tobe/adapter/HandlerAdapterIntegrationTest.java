package com.interface21.webmvc.servlet.mvc.tobe.adapter;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HandlerAdapterIntegrationTest {

    private HttpServletRequest request;
    private HttpServletResponse response;

    private ControllerHandlerAdapter controllerAdapter;
    private AnnotationHandlerAdapter annotationAdapter;
    private HandlerAdapterRegistry registry;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        
        controllerAdapter = new ControllerHandlerAdapter();
        annotationAdapter = new AnnotationHandlerAdapter();
        
        registry = new HandlerAdapterRegistry();
        registry.addHandlerAdapter(controllerAdapter);
        registry.addHandlerAdapter(annotationAdapter);
    }

    @Test
    @DisplayName("ControllerHandlerAdapter - Controller 타입 지원 확인")
    void controllerHandlerAdapter_supports_Controller() {
        // given
        Controller controller = new TestController();
        String notController = "not a controller";

        // when & then
        assertThat(controllerAdapter.supports(controller)).isTrue();
        assertThat(controllerAdapter.supports(notController)).isFalse();
    }

    @Test
    @DisplayName("ControllerHandlerAdapter - ModelAndView 정상 반환")
    void controllerHandlerAdapter_handle_returnsModelAndView() throws Exception {
        // given
        Controller controller = new TestController();

        // when
        ModelAndView modelAndView = controllerAdapter.handle(request, response, controller);

        // then
        assertThat(modelAndView).isNotNull();
        assertThat(modelAndView.getView()).isInstanceOf(JspView.class);
    }

    @Test
    @DisplayName("AnnotationHandlerAdapter - HandlerExecution 타입 지원 확인")
    void annotationHandlerAdapter_supports_HandlerExecution() throws Exception {
        // given
        TestAnnotationController controllerInstance = new TestAnnotationController();
        Method method = TestAnnotationController.class.getMethod("testMethod", HttpServletRequest.class, HttpServletResponse.class);
        HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);
        String notHandlerExecution = "not a handler execution";

        // when & then
        assertThat(annotationAdapter.supports(handlerExecution)).isTrue();
        assertThat(annotationAdapter.supports(notHandlerExecution)).isFalse();
    }

    @Test
    @DisplayName("AnnotationHandlerAdapter - ModelAndView 정상 반환")
    void annotationHandlerAdapter_handle_returnsModelAndView() throws Exception {
        // given
        TestAnnotationController controllerInstance = new TestAnnotationController();
        Method method = TestAnnotationController.class.getMethod("testMethod", HttpServletRequest.class, HttpServletResponse.class);
        HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);
        
        when(request.getAttribute("test")).thenReturn("testValue");

        // when
        ModelAndView modelAndView = annotationAdapter.handle(request, response, handlerExecution);

        // then
        assertThat(modelAndView).isNotNull();
        assertThat(modelAndView.getObject("test")).isEqualTo("testValue");
        assertThat(modelAndView.getView()).isInstanceOf(JspView.class);
    }

    @Test
    @DisplayName("HandlerAdapterRegistry - Controller에 대해 ControllerHandlerAdapter 반환")
    void handlerAdapterRegistry_getAdapter_forController() throws Exception {
        // given
        Controller controller = new TestController();

        // when
        HandlerAdapter adapter = registry.getHandlerAdapter(controller);

        // then
        assertThat(adapter).isInstanceOf(ControllerHandlerAdapter.class);
    }

    @Test
    @DisplayName("HandlerAdapterRegistry - HandlerExecution에 대해 AnnotationHandlerAdapter 반환")
    void handlerAdapterRegistry_getAdapter_forHandlerExecution() throws Exception {
        // given
        TestAnnotationController controllerInstance = new TestAnnotationController();
        Method method = TestAnnotationController.class.getMethod("testMethod", HttpServletRequest.class, HttpServletResponse.class);
        HandlerExecution handlerExecution = new HandlerExecution(controllerInstance, method);

        // when
        HandlerAdapter adapter = registry.getHandlerAdapter(handlerExecution);

        // then
        assertThat(adapter).isInstanceOf(AnnotationHandlerAdapter.class);
    }

    @Test
    @DisplayName("HandlerAdapterRegistry - 지원하지 않는 핸들러 타입일 때 예외 발생")
    void handlerAdapterRegistry_getAdapter_throwsException_forUnsupportedHandler() {
        // given
        Object unsupportedHandler = "unsupported handler";

        // when & then
        assertThatThrownBy(() -> registry.getHandlerAdapter(unsupportedHandler))
                .isInstanceOf(NoHandlerAdapterFoundException.class)
                .hasMessageContaining("핸들러")
                .hasMessageContaining("어댑터가 없습니다")
                .hasMessageContaining("java.lang.String");
    }

    @Test
    @DisplayName("HandlerAdapterRegistry - 등록된 순서대로 어댑터 검색")
    void handlerAdapterRegistry_searchesAdapters_inRegistrationOrder() throws Exception {
        // given - 새로운 레지스트리에 순서를 바꿔서 등록
        HandlerAdapterRegistry testRegistry = new HandlerAdapterRegistry();
        testRegistry.addHandlerAdapter(annotationAdapter);  // 먼저 등록
        testRegistry.addHandlerAdapter(controllerAdapter);  // 나중에 등록
        
        Controller controller = new TestController();

        // when
        HandlerAdapter adapter = testRegistry.getHandlerAdapter(controller);

        // then - ControllerHandlerAdapter가 Controller를 지원하므로 해당 어댑터가 반환되어야 함
        assertThat(adapter).isInstanceOf(ControllerHandlerAdapter.class);
    }

    @Test
    @DisplayName("빈 HandlerAdapterRegistry에서 어댑터 조회 시 예외 발생")
    void emptyHandlerAdapterRegistry_throwsException() {
        // given
        HandlerAdapterRegistry emptyRegistry = new HandlerAdapterRegistry();
        Controller handler = new TestController();

        // when & then
        assertThatThrownBy(() -> emptyRegistry.getHandlerAdapter(handler))
                .isInstanceOf(NoHandlerAdapterFoundException.class);
    }

    // 테스트용 Controller
    private static class TestController implements Controller {
        @Override
        public String execute(HttpServletRequest request, HttpServletResponse response) {
            return "/test.jsp";
        }
    }

    // 테스트용 Annotation Controller
    public static class TestAnnotationController {
        public ModelAndView testMethod(HttpServletRequest request, HttpServletResponse response) {
            ModelAndView modelAndView = new ModelAndView(new JspView("/annotation-test.jsp"));
            modelAndView.addObject("test", request.getAttribute("test"));
            return modelAndView;
        }
    }
}
