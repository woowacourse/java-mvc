package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class ManualHandlerAdapterTest {

    @DisplayName("ManualHandlerAdapter가 Controller를 지원하는지 확인한다.")
    @Test
    void ManualHandlerAdapterSupportsController() {
        ManualHandlerAdapter manualHandlerAdapter = new ManualHandlerAdapter();

        assertThat(manualHandlerAdapter.supports(new TestController())).isTrue();
    }

    @DisplayName("ManualHandlerAdapter가 Controller를 실행한다.")
    @Test
    void handlerAdapterExecutesHandler() throws Exception {
        //given
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/hello");
        when(request.getMethod()).thenReturn("GET");

        ManualHandlerAdapter manualHandlerAdapter = new ManualHandlerAdapter();

        //when & then
        assertThat(manualHandlerAdapter.handle(request, response, new TestController()))
                .isInstanceOf(ModelAndView.class);
        assertThat(manualHandlerAdapter.handle(request, response, new TestController())
                .getView()).isInstanceOf(JspView.class);
    }
}
