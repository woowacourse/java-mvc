package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerAdapterRegistryTest {

    private HandlerAdapterRegistry handlerAdapterRegistry;

    @BeforeEach
    public void setUp() {
        handlerAdapterRegistry = new HandlerAdapterRegistry();
        handlerAdapterRegistry.addHandlerAdapter(new ControllerHandlerAdapter());
    }

    @DisplayName("지원하는 handlerAdapter를 찾아서 ModelAndView를 반환한다.")
    @Test
    void execute_returnModelAndView() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Controller handler = mock(Controller.class);

        // when
        ModelAndView actual = handlerAdapterRegistry.execute(request, response, handler);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual).isInstanceOf(ModelAndView.class);
    }

    @DisplayName("지원하는 handlerAdapter를 찾지 못하면, null을 반환한다.")
    @Test
    void execute_returnNull() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        HandlerExecution handler = mock(HandlerExecution.class);

        // when
        ModelAndView actual = handlerAdapterRegistry.execute(request, response, handler);

        // then
        assertThat(actual).isNull();
    }
}
