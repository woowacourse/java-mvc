package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {

    private final HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();

    @Test
    @DisplayName("기존 Controller를 ControllerHandlerAdapter로 핸들링한다")
    void handlesController() {
        Controller controller = (request, response) -> "/legacy.jsp";

        HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(controller);

        assertThat(handlerAdapter).isInstanceOf(ControllerHandlerAdapter.class);
    }

    @Test
    @DisplayName("HandlerExecution을 HandlerExecutionAdapter로 핸들링한다")
    void handlesHandlerExecution() throws NoSuchMethodException {
        HandlerExecution handlerExecution = createHandlerExecution();

        HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handlerExecution);

        assertThat(handlerAdapter).isInstanceOf(HandlerExecutionAdapter.class);
    }

    @Test
    @DisplayName("여러 어댑터가 처리할 수 있는 핸들러는 ControllerHandlerAdapter를 우선한다")
    void prioritizesControllerHandlerAdapter() throws NoSuchMethodException {
        HandlerExecution handler = new ControllerHandlerExecution();

        HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(handler);

        assertThat(handlerAdapter).isInstanceOf(ControllerHandlerAdapter.class);
    }

    @Test
    @DisplayName("아무 어댑터도 처리할 수 없는 핸들러이면 예외를 던진다")
    void throwsWhenNoAdapterCanHandle() {
        Object unsupportedHandler = new Object();

        assertThatThrownBy(() -> handlerAdapterRegistry.getHandlerAdapter(unsupportedHandler))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private HandlerExecution createHandlerExecution() throws NoSuchMethodException {
        AdapterTestController controller = new AdapterTestController();
        Method method = AdapterTestController.class.getDeclaredMethod(
                "handle",
                HttpServletRequest.class,
                HttpServletResponse.class
        );
        return new HandlerExecution(controller, method);
    }

    public static class AdapterTestController {

        public ModelAndView handle(
                final HttpServletRequest request,
                final HttpServletResponse response
        ) {
            return new ModelAndView(new JspView("/adapter-test.jsp"));
        }
    }

    private static class ControllerHandlerExecution extends HandlerExecution implements Controller {

        ControllerHandlerExecution() throws NoSuchMethodException {
            super(
                    new AdapterTestController(),
                    AdapterTestController.class.getDeclaredMethod(
                            "handle",
                            HttpServletRequest.class,
                            HttpServletResponse.class
                    )
            );
        }

        @Override
        public String execute(
                final HttpServletRequest request,
                final HttpServletResponse response
        ) {
            return "/legacy.jsp";
        }
    }
}
