package nextstep.mvc.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ControllerHandlerAdapterTest {

    ControllerHandlerAdapter sut;

    @BeforeEach
    void setUp() {
        sut = new ControllerHandlerAdapter();
    }

    @Test
    void supportControllerInstance() {
        Controller controller = (req, res) -> null;

        boolean actual = sut.supports(controller);
        assertThat(actual).isTrue();
    }

    @Test
    void notSupportNonControllerInstance() {
        Object nonController = new Object();

        boolean actual = sut.supports(nonController);

        assertThat(actual).isFalse();
    }

    @Test
    @DisplayName("JspView를 View로 갖는 ModelAndView 객체를 리턴한다")
    void returnModelAndViewInstanceHavingJspView() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Controller controller = (req, res) -> "/test.jsp";

        ModelAndView actual = sut.handle(request, response, controller);

        assertThat(actual.getView()).isInstanceOf(JspView.class);
        assertThat(((JspView)actual.getView()).getViewName()).isEqualTo("/test.jsp");
    }

    @Test
    @DisplayName("입력받은 request의 attribute를 model에 저장한다")
    void addRequestAttributeToModel() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Controller controller = (req, res) -> null;

        when(request.getAttributeNames()).thenReturn(Collections.enumeration(List.of("id")));
        when(request.getAttribute("id")).thenReturn("gugu");

        // when
        ModelAndView actual = sut.handle(request, response, controller);

        // then
        assertThat(actual.getObject("id")).isEqualTo("gugu");
    }
}
