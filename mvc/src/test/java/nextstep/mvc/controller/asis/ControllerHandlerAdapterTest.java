package nextstep.mvc.controller.asis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples1.HelloController;

class ControllerHandlerAdapterTest {

    @DisplayName("지원여부 테스트")
    @Test
    void supportsTest() {
        //given
        Controller controller = new HelloController();
        //when
        HandlerAdapter adapter = new ControllerHandlerAdapter();
        //then
        assertThat(adapter.supports(controller)).isTrue();
    }

    @DisplayName("핸들러 실행결과 테스트")
    @Test
    void handleTest() throws Exception {
        //given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getAttribute("word")).thenReturn("hello");
        when(request.getAttributeNames()).thenReturn(new Enumeration<String>() {
            int count = 0;
            @Override
            public boolean hasMoreElements() {
                return count++ < 1;
            }

            @Override
            public String nextElement() {
                return "word";
            }
        });
        when(request.getMethod()).thenReturn("GET");

        //when
        Controller controller = new HelloController();
        ControllerHandlerAdapter adapter = new ControllerHandlerAdapter();
        ModelAndView handle = adapter.handle(request, response, controller);

        //then
        assertThat(((JspView)handle.getView()).getViewName()).isEqualTo("/index.jsp");
        assertThat(handle.getObject("word")).isEqualTo("hello");
    }
}