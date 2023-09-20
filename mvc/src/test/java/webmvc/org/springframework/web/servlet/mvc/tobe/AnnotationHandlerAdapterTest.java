package webmvc.org.springframework.web.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

class AnnotationHandlerAdapterTest {

    private AnnotationHandlerAdapter handlerAdapter;

    @BeforeEach
    void setUp() {
        handlerAdapter = new AnnotationHandlerAdapter();
    }

    @Test
    void 핸들러가_HandlerExecution_타입이면_true를_반환한다() {
        // given
        final HandlerExecution handlerExecution = new HandlerExecution(null, null);

        // when
        final boolean result = handlerAdapter.supports(handlerExecution);

        // thend
        assertThat(result).isTrue();
    }

    @Test
    void 핸들러가_HandlerExecution_타입이_아니면_false를_반환한다() {
        // given
        final String handlerExecution = "HandlerExecution";

        // when
        final boolean result = handlerAdapter.supports(handlerExecution);

        // then
        assertThat(result).isFalse();
    }

    @Test
    void 인자로_들어온_핸들러의_메소드를_어댑터를_통해_실행한다() throws Exception {
        // given
        final var request = mock(HttpServletRequest.class);
        final var response = mock(HttpServletResponse.class);
        final var handlerMapping = mock(HandlerMapping.class);

        final Method handlerMethod = getClass().getDeclaredMethod("testHandler", HttpServletRequest.class,
                HttpServletResponse.class);
        final HandlerExecution handler = new HandlerExecution(getClass().getDeclaredConstructor().newInstance(),
                handlerMethod);
        when(handlerMapping.getHandler(request)).thenReturn(handler);

        // when
        final ModelAndView modelAndView = handlerAdapter.handle(handler, request, response);

        // then
        assertThat(modelAndView.getObject("id")).isEqualTo("id");
    }

    ModelAndView testHandler(final HttpServletRequest request, final HttpServletResponse response) {
        final ModelAndView modelAndView = new ModelAndView(new JspView(""));
        modelAndView.addObject("id", "id");
        return modelAndView;
    }
}
