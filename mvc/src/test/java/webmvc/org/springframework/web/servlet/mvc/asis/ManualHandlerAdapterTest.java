package webmvc.org.springframework.web.servlet.mvc.asis;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class ManualHandlerAdapterTest {

    private final ManualHandlerAdapter manualHandlerAdapter = new ManualHandlerAdapter();

    @DisplayName("Controller 인터페이스를 구현한 handler는 true를 반환한다.")
    @Test
    void supports() {
        // given
        Controller handler = mock(Controller.class);

        // when
        boolean actual = manualHandlerAdapter.supports(handler);

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("Controller 인터페이스를 구현하지 않은 handler는 false를 반환한다.")
    @Test
    void supports_false() {
        // given
        Object objectHandler = mock(Object.class);

        // when
        boolean actual = manualHandlerAdapter.supports(objectHandler);

        // then
        assertThat(actual).isFalse();
    }

    @DisplayName("handler를 실행하고 ModelAndView를 반환한다.")
    @Test
    void handle() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Controller handler = mock(Controller.class);

        given(handler.execute(request, response)).willReturn("index");

        // when & then
        assertThat(manualHandlerAdapter.handle(request, response, handler)).isInstanceOf(ModelAndView.class);
    }
}
