package webmvc.org.springframework.web.servlet.mvc.asis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.view.JspView;

class AnnotationControllerAdapterTest {

    AnnotationControllerAdapter annotationControllerAdapter = new AnnotationControllerAdapter();

    @Mock
    HandlerExecution handlerExecution;

    @BeforeEach
    void setting() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void HandlerExecutionSupport() {
        // given
        HandlerExecution handler = new HandlerExecution(null, null);

        // when
        boolean actual = annotationControllerAdapter.support(handler);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void handleException() {
        // given
        Object handler = new Object();

        // when & then
        assertThatThrownBy(() -> annotationControllerAdapter.handle(null, null, handler))
            .isInstanceOfAny(IllegalArgumentException.class)
            .hasMessage("해당 Adapter 는 전달된 handler 를 처리할 수 없습니다.");
    }

    @Test
    void handle() {
        // given
        ModelAndView modelAndView = new ModelAndView(new JspView("/response.jsp"));
        given(handlerExecution.handle(any(), any()))
            .willReturn(modelAndView);

        // when
        ModelAndView actual = annotationControllerAdapter.handle(null, null, handlerExecution);

        // then
        assertThat(actual).isEqualTo(modelAndView);
    }
}
