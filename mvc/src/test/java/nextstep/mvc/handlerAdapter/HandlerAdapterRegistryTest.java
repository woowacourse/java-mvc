package nextstep.mvc.handlerAdapter;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.handlerMapping.HandlerMapping;
import nextstep.mvc.view.ModelAndView;

class HandlerAdapterRegistryTest {

    @Test
    void getHandlerAdapter() {
        HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.addHandlerAdapter(new HandlerAdapterForTest());

        HandlerAdapter handlerAdapter = handlerAdapterRegistry.getHandlerAdapter(new HandlerForTest());

        assertThat(handlerAdapter).isInstanceOf(HandlerAdapterForTest.class);
    }

    @Test
    void cantGetHandlerAdapter() {
        HandlerAdapterRegistry handlerAdapterRegistry = new HandlerAdapterRegistry();

        assertThatThrownBy(
            () -> handlerAdapterRegistry.getHandlerAdapter(new HandlerForTest()))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("해당 핸들러를 처리할 수 있는 핸들러 어댑터를 찾지 못했습니다.");
    }

    class HandlerAdapterForTest implements HandlerAdapter {
        @Override
        public boolean supports(Object handler) {
            return handler instanceof HandlerForTest;
        }

        @Override
        public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
            Exception {
            return null;
        }
    }

    class HandlerForTest implements HandlerMapping {
        @Override
        public void initialize() {

        }

        @Override
        public Object getHandler(HttpServletRequest request) {
            return null;
        }
    }

}
