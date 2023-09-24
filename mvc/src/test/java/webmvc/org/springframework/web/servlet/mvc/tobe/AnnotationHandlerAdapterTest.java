package webmvc.org.springframework.web.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;
import webmvc.org.springframework.web.servlet.view.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.handler.adapter.AnnotationHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.handler.mapping.HandlerExecution;

class AnnotationHandlerAdapterTest {

    @DisplayName("처리할 수 있는 핸들러인지 확인한다.")
    @Test
    void supports() {
        // given
        final var handlerAdapter = new AnnotationHandlerAdapter();

        // when
        // then
        assertThat(handlerAdapter.supports(new HandlerExecution(null, null))).isTrue();
    }

    @DisplayName("처리할 수 없는 핸들러는 false 를 반환한다.")
    @Test
    void supportsReturnFalse() {
        // given
        final var handlerAdapter = new AnnotationHandlerAdapter();

        // when
        // then
        assertThat(handlerAdapter.supports(new Object())).isFalse();
    }

    @DisplayName("핸들러의 동작을 수행한다.")
    @Test
    void handle() throws Exception {
        // given
        final var handlerAdapter = new AnnotationHandlerAdapter();

        // when
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("id")).thenReturn("1");
        final ModelAndView modelAndView = handlerAdapter.handle(request, null, new HandlerExecution(new TestController(),
            TestController.class.getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class)));

        // then
        assertThat(modelAndView.getModel().get("id")).isEqualTo("1");
        assertThat(modelAndView.getViewName()).isEqualTo("test");
    }
}
