package nextstep.mvc.handler.mapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.support.RequestMethod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import samples.SampleComponent1;
import samples.TestController;

import static org.assertj.core.api.Assertions.assertThat;

class HandlerExecutionTest {

    private final HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
    private final HttpServletResponse mockResponse = Mockito.mock(HttpServletResponse.class);

    @DisplayName("처리 결과가 ModelAndView인 경우, ModelAndView을 반환한다")
    @Test
    void responseWithModelAndView() throws Throwable {
        createMockRequest("/response-modelAndView", RequestMethod.GET);

        HandlerExecution handlerExecution = HandlerExecution.of(TestController.class);
        ModelAndView result = handlerExecution.handle(mockRequest, mockResponse);

        assertThat(result.getViewName().value()).isEqualTo("test.jsp");
    }

    @DisplayName("처리 결과가 문자열인 경우, ModelAndView의 viewName으로 매핑된다")
    @Test
    void responseWithString() throws Throwable {
        createMockRequest("/response-string", RequestMethod.GET);

        HandlerExecution handlerExecution = HandlerExecution.of(TestController.class);
        ModelAndView result = handlerExecution.handle(mockRequest, mockResponse);

        assertThat(result.getViewName().value()).isEqualTo("test.jsp");
    }

    @DisplayName("처리 결과가 그 외 일반 객체인 경우, ModelAndView에 <key:ObjectBeanName, value:Object>을 담아 리턴한다")
    @Test
    void responseWithObject() throws Throwable {
        createMockRequest("/response-object", RequestMethod.GET);

        HandlerExecution handlerExecution = HandlerExecution.of(TestController.class);
        ModelAndView result = handlerExecution.handle(mockRequest, mockResponse);

        assertThat(result.getViewName().isEmpty()).isTrue();
        assertThat(result.getObject("sampleComponent1").getClass()).isEqualTo(SampleComponent1.class);
    }

    private void createMockRequest(String requestUrl, RequestMethod requestMethod) {
        Mockito.when(mockRequest.getRequestURI()).thenReturn(requestUrl);
        Mockito.when(mockRequest.getMethod()).thenReturn(requestMethod.name());
    }
}
