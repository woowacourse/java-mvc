package webmvc.org.springframework.web.servlet.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class JspViewTest {

    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);

    @Test
    @DisplayName("JspView render Test")
    void renderTest() throws Exception {
        final ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(argumentCaptor.capture()))
            .thenReturn(requestDispatcher);
        final String viewName = "/register.jsp";
        final JspView jspView = new JspView(viewName);

        //when
        jspView.render(Map.of(), request, response);

        //then
        assertThat(argumentCaptor.getValue())
            .isEqualTo(viewName);
    }
}
