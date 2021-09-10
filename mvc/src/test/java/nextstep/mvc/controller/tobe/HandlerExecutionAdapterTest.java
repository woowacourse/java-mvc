package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Enumeration;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples1.TestController1;

class HandlerExecutionAdapterTest {

    private HandlerExecution execution;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        TestController1 testController = new TestController1();

        Class<TestController1> testControllerClass = TestController1.class;
        Method method = testControllerClass
            .getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        execution = new HandlerExecution(method, testController);

    }

    @DisplayName("지원 여부 테스트")
    @Test
    void supportsTest() {
        //given
        //when
        HandlerExecutionAdapter adapter = new HandlerExecutionAdapter();
        //then
        assertThat(adapter.supports(execution)).isTrue();
    }

    @DisplayName("핸들러 실행결과 테스트")
    @Test
    void handlerTest() throws Exception {
        //given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getAttributeNames()).thenReturn(new Enumeration<String>() {
            int count = 0;
            @Override
            public boolean hasMoreElements() {
                return count++ < 1;
            }

            @Override
            public String nextElement() {
                return "id";
            }
        });
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        //when
        HandlerExecutionAdapter adapter = new HandlerExecutionAdapter();
        ModelAndView modelAndView = adapter.handle(request, response, execution);

        //then
        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
        assertThat(((JspView) modelAndView.getView()).getViewName()).isEqualTo("/get-test");
    }

}