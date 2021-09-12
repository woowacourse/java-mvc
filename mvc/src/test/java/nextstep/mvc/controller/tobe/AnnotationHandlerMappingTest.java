package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
    }

    @DisplayName("@Controller 만 가지고 있는 클래스 등록 확인 (value 에 /를 붙여주지 않으면 붙여줌)")
    @Test
    void getHandlerWithOnlyControllerAnnotation() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        given(request.getRequestURI()).willReturn("/single");
        given(request.getMethod()).willReturn("GET");

        // when, then
        HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        assertThat(handlerExecution).isNotNull();
    }

    @DisplayName("@Controller 만 가지고 있는 클래스 등록 확인 (value 에 /를 붙여주지 않으면 붙여줌)")
    @Test
    void getHandlerWithRequestMappingInMethod() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        given(request.getRequestURI()).willReturn("/single/mapping");
        given(request.getMethod()).willReturn("GET");

        // when, then
        HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        assertThat(handlerExecution).isNotNull();
    }

    @DisplayName("클래스 위와 메서드 위에 @RequestMapping 이 있는 경우 등록 확인")
    @Test
    void getHandlerWithRequestMappingAnnotation() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        given(request.getRequestURI()).willReturn("/default-url/detailed-url");
        given(request.getMethod()).willReturn("GET");

        // when, then
        HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        assertThat(handlerExecution).isNotNull();
    }

    @DisplayName("클래스 위의 @Controller 와 @RequestMapping 에 둘 다 value 가 있는 경우 있는 @RequestMapping 이 적용된다.")
    @Test
    void getHandlerWithBothAnnotation() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        given(request.getRequestURI()).willReturn("/both");
        given(request.getMethod()).willReturn("GET");

        // when, then
        HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        assertThat(handlerExecution).isNotNull();
    }

    @Test
    void get() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final ModelAndView modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @Test
    void post() throws Exception {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("POST");

        final HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        final ModelAndView modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }
}
