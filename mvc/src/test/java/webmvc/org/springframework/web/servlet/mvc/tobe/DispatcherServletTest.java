package webmvc.org.springframework.web.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName("DispatcherServlet 테스트")
class DispatcherServletTest {

    @Test
    void DispatcherServlet_초기화시_등록된_adapter와_handler가_등록된다() {
        // given
        final var dispatcherServlet = new DispatcherServlet();

        // when
        dispatcherServlet.init();

        // then
        assertThat(dispatcherServlet).extracting("handlerMappings").isNotNull();
        assertThat(dispatcherServlet).extracting("handlerAdapters").isNotNull();
    }

    @Test
    void handler와_adapter를_통해_웹요청을_처리한다() {
        // given
        final DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();

        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        given(request.getRequestURI()).willReturn("/");
        given(request.getMethod()).willReturn("GET");
        given(request.getRequestDispatcher(any())).willReturn(requestDispatcher);

        // when & then
        assertThatCode(() -> dispatcherServlet.service(request, response)).doesNotThrowAnyException();
    }

    @Test
    void handler와_adapter를_통해_웹요청을_처리한다2() {
        // given
        final DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();

        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);

        given(request.getRequestURI()).willReturn("/register/view");
        given(request.getMethod()).willReturn("GET");
        given(request.getRequestDispatcher(any())).willReturn(requestDispatcher);

        // when & then
        assertThatCode(() -> dispatcherServlet.service(request, response)).doesNotThrowAnyException();
    }

    @Test
    void user조회_테스트() throws IOException {
        // given
        final DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.init();

        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);

        given(request.getRequestURI()).willReturn("/api/user");
        given(request.getParameter("account")).willReturn("gugu");
        given(request.getMethod()).willReturn("GET");
        given(response.getWriter()).willReturn(printWriter);
        given(request.getRequestDispatcher(any())).willReturn(requestDispatcher);

        final Set<String> expected = Set.of("account='gugu'", "email='hkkang@woowahan.com'");

        // when & then
        assertThatCode(() -> dispatcherServlet.service(request, response)).doesNotThrowAnyException();
        final String bufferedResponse = stringWriter.toString();
        assertThat(bufferedResponse).contains(expected);
    }
}
