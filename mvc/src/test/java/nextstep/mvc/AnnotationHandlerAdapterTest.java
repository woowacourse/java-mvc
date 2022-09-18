package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestAnnotationController;

class AnnotationHandlerAdapterTest {

    @DisplayName("AnnotationHandlerAdapter가 HandlerExecution을 지원하는지 확인한다.")
    @Test
    void handlerAdapterSupportsHandlerExecute() throws NoSuchMethodException {
        AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();

        Method method = TestAnnotationController.class.getDeclaredMethod("findUserId",
                HttpServletRequest.class,
                HttpServletResponse.class);

        assertThat(annotationHandlerAdapter.supports(new HandlerExecution("/get-test", method))).isTrue();
    }

    @DisplayName("AnnotationHandlerAdapter가 HandlerExecution을 실행한다.")
    @Test
    void handlerAdapterExecutesHandler() throws Exception {
        //given
        var request = mock(HttpServletRequest.class);
        var response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        AnnotationHandlerMapping handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();

        //when & then
        var handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();
        assertThat(annotationHandlerAdapter.handle(request, response, handlerExecution))
                .isInstanceOf(ModelAndView.class);
    }
}