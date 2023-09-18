package webmvc.org.springframework.web.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.view.JspView;

class ManualHandlerAdapterTest {

    private static final String INDEX_JSP = "/index.jsp";
    private final HandlerAdapter handlerAdaptor = new ManualHandlerAdapter();
    private final Controller controller = mock(Controller.class);

    @DisplayName("object가 Controller 타입에 해당하면 True를 반환한다.")
    @Test
    void isSupport() {
        // when & then
        assertThat(handlerAdaptor.isSupport(controller)).isTrue();
    }

    @DisplayName("object가 Controller 타입이 아니면 False를 반환한다.")
    @Test
    void isSupport_NotHandlerExecution() {
        // given
        final HandlerExecution handlerExecution = mock(HandlerExecution.class);

        // when & then
        assertThat(handlerAdaptor.isSupport(handlerExecution)).isFalse();
    }

    @DisplayName("Controller를 통해 올바르게 handle 메서드를 실행할 수 있다.")
    @Test
    void handle() throws Exception {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final String path = INDEX_JSP;

        when(controller.execute(request, response)).thenReturn(path);

        // when
        final ModelAndView actual = handlerAdaptor.handle(controller, request, response);

        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(new ModelAndView(new JspView(path)));
    }
}
