package webmvc.org.springframework.web.servlet.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import samples.TestController;
import webmvc.org.springframework.web.servlet.mvc.asis.ControllerHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerExecution;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecutionHandlerAdapter;

class HandlerAdapterRegistryTest {

    @Test
    void 적절한_handlerAdapter를_찾지_못하면_예외가_발생한다() throws NoSuchMethodException {
        final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
        final HandlerExecution postHandlerExecution = new AnnotationHandlerExecution(
                new TestController(),
                TestController.class.getDeclaredMethod(
                        "save",
                        HttpServletRequest.class,
                        HttpServletResponse.class
                )
        );
        handlerAdapterRegistry.addHandlerAdapter(new ControllerHandlerAdapter());

        assertThatThrownBy(() -> handlerAdapterRegistry.findHandlerAdapter(postHandlerExecution))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("요청을 처리할 수 있는 HandlerAdapter를 찾을 수 없습니다!");
    }

    @Test
    void 적절한_handlerAdapter를_반환한다() throws NoSuchMethodException {
        final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
        final HandlerExecution postHandlerExecution = new AnnotationHandlerExecution(
                new TestController(),
                TestController.class.getDeclaredMethod(
                        "save",
                        HttpServletRequest.class,
                        HttpServletResponse.class
                )
        );
        handlerAdapterRegistry.addHandlerAdapter(new HandlerExecutionHandlerAdapter());

        final HandlerAdapter handlerAdapter = handlerAdapterRegistry.findHandlerAdapter(postHandlerExecution);

        assertThat(handlerAdapter).isInstanceOf(HandlerExecutionHandlerAdapter.class);
    }
}
