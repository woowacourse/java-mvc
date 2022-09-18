package nextstep.mvc.view;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import org.junit.jupiter.api.Test;

class JspViewTest {

    @Test
    void redirect_일_경우_sendRedirect_메서드가_호출된다() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        View view = new JspView("redirect:/");
        view.render(Collections.emptyMap(), request, response);

        verify(response).sendRedirect("/");
    }

    @Test
    void forward_일_경우_forward_메서드가_호출된다() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        when(request.getRequestDispatcher("/")).thenReturn(requestDispatcher);

        View view = new JspView("/");
        view.render(Collections.emptyMap(), request, response);

        verify(requestDispatcher).forward(request, response);
    }
}
