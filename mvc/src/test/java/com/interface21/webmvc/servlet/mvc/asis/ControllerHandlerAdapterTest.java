package com.interface21.webmvc.servlet.mvc.asis;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ControllerHandlerAdapterTest {

    private ControllerHandlerAdapter handlerAdapter;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        handlerAdapter = new ControllerHandlerAdapter();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    @DisplayName("Controller 구현체를 처리할 수 있다")
    void handleable_withController() {
        assertTrue(handlerAdapter.handleable(new ForwardController("/index.jsp")));
    }

    @Test
    @DisplayName("Controller 구현체가 아니면 처리할 수 없다")
    void handleable_withNonController() {
        assertFalse(handlerAdapter.handleable(new Object()));
    }

    @Test
    @DisplayName("Controller가 반환한 뷰 이름을 JspView로 감싼 ModelAndView를 반환한다")
    void execute() throws Exception {
        // given
        Controller controller = new ForwardController("redirect:/index.jsp");

        // when
        ModelAndView modelAndView = handlerAdapter.execute(request, response, controller);
        modelAndView.getView().render(modelAndView.getModel(), request, response);

        // then
        assertInstanceOf(JspView.class, modelAndView.getView());
        verify(response).sendRedirect("/index.jsp");
    }

    @Test
    @DisplayName("Controller에서 발생한 예외를 그대로 던진다")
    void execute_withException() {
        // given
        IllegalArgumentException exception = new IllegalArgumentException();
        Controller controller = (req, res) -> {
            throw exception;
        };

        // when & then
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> handlerAdapter.execute(request, response, controller)
        );
        assertSame(exception, thrown);
    }
}
