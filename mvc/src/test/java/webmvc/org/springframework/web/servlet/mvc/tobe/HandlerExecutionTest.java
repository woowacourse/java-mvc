package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.exception.HandlerExecutionException;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.when;
import static org.mockito.Mockito.mock;

class HandlerExecutionTest {

    @Test
    void 생성_인자에_Object가_null일_경우_예외가_발생한다() {
        // given
        final Method method = mock(Method.class);

        // expect
        assertThatThrownBy(() -> new HandlerExecution(null, method))
                .isInstanceOf(HandlerExecutionException.class);
    }

    @Test
    void 생성_인자에_Method가_null일_경우_예외가_발생한다() {
        // given
        final Object controller = mock(Object.class);

        // expect
        assertThatThrownBy(() -> new HandlerExecution(controller, null))
                .isInstanceOf(HandlerExecutionException.class);
    }

    @Test
    void 요청과_응답을_이용해서_모델뷰를_반환한다() throws Exception {
        // given
        final HandlerExecution handlerExecution = mock(HandlerExecution.class);

        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final ModelAndView expected = mock(ModelAndView.class);

        // when
        when(handlerExecution.handle(any(HttpServletRequest.class), any(HttpServletResponse.class)))
                .thenReturn(expected);

        // expect
        final ModelAndView actual = handlerExecution.handle(request, response);

        assertThat(actual).isEqualTo(expected);
    }
}
