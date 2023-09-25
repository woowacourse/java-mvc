package webmvc.org.springframework.web.servlet.mvc.tobe.handler;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

class NotFoundHandlerTest {

    @Test
    @DisplayName("notFound일 때 적절한 viewName을 반환한다.")
    void handle() {
        final NotFoundHandler notFoundHandler = new NotFoundHandler();
        final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        final HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        final ModelAndView modelAndView = notFoundHandler.handle(request, response);

        final JspView expectedJspView = new JspView("redirect:/404.jsp");
        assertThat(modelAndView.getView())
            .usingRecursiveComparison()
            .isEqualTo(expectedJspView);
    }

}
