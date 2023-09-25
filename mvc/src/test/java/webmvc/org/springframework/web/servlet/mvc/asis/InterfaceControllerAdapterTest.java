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


class InterfaceControllerAdapterTest {

    InterfaceControllerAdapter interfaceControllerAdapter = new InterfaceControllerAdapter();

    @Mock
    Controller controller;

    @BeforeEach
    void setting() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void invalidHandlerException() {
        // given
        Object invalidHandler = new HandlerExecution(null, null);

        // when & then
        assertThatThrownBy(() -> interfaceControllerAdapter.handle(null, null, invalidHandler))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("해당 Adapter 는 전달된 handler 를 처리할 수 없습니다.");
    }

    @Test
    void validHandle() {
        // given
        String handlerResponse = "/response.jsp";
        given(controller.execute(any(), any()))
            .willReturn(handlerResponse);

        // when
        ModelAndView result = interfaceControllerAdapter.handle(null, null, controller);

        // then
        assertThat(result)
            .extracting("view")
            .extracting("viewName")
            .isEqualTo(handlerResponse);
    }
}
