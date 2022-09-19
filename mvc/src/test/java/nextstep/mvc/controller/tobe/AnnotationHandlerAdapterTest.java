package nextstep.mvc.controller.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class AnnotationHandlerAdapterTest {

    private AnnotationHandlerAdapter annotationHandlerAdapter;

    @BeforeEach
    void setUp() {
        annotationHandlerAdapter = new AnnotationHandlerAdapter();
    }

    @DisplayName("AnnotationHandlerAdapter는 HandlerExecution 클래스를 지원한다.")
    @Test
    void support_success() {
        // given
        HandlerExecution handlerExecution = Mockito.mock(HandlerExecution.class);

        // when
        boolean result = annotationHandlerAdapter.supports(handlerExecution);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("AnnotationHandlerAdapter는 HandlerExecution 이외의 클래스는 지원하지 않는다.")
    @Test
    void support_fail() {
        // given
        Controller controller = Mockito.mock(Controller.class);

        // when
        boolean result = annotationHandlerAdapter.supports(controller);

        // then
        assertThat(result).isFalse();
    }
}
