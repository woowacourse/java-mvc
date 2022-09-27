package nextstep.mvc.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import nextstep.mvc.controller.handlermapping.AnnotationHandlerMapping;
import nextstep.mvc.view.ModelAndView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.User;

class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
    }

    @DisplayName("GET /get-test로 요청을 받을 수 있다.")
    @Test
    void get() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        ModelAndView modelAndView = handlerExecution.handle(request, response);

        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }

    @DisplayName("POST /post-test로 요청을 받을 수 있다.")
    @Test
    void post() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        PrintWriter printWriter = new PrintWriter(new ByteArrayOutputStream());

        String expected = "gugu";
        when(request.getAttribute("id")).thenReturn(expected);
        when(request.getRequestURI()).thenReturn("/post-test");
        when(request.getMethod()).thenReturn("POST");
        when(response.getWriter()).thenReturn(printWriter);

        // when
        HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        ModelAndView modelAndView = handlerExecution.handle(request, response);
        modelAndView.render(request, response);
        String actual = extractBody(printWriter);

        // then
        assertThat(actual).isEqualTo("\"gugu\"");
    }

    @DisplayName("POST /api/user로 요청하면 user의 데이터를 응답한다.")
    @Test
    void post_api_user() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        PrintWriter printWriter = new PrintWriter(new ByteArrayOutputStream());

        User user = new User("tonic", "tonic@gmail.com");
        when(request.getAttribute("user")).thenReturn(user);
        when(request.getRequestURI()).thenReturn("/api/user");
        when(request.getMethod()).thenReturn("POST");
        when(response.getWriter()).thenReturn(printWriter);

        // when
        HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        ModelAndView modelAndView = handlerExecution.handle(request, response);
        modelAndView.render(request, response);
        String actual = extractBody(printWriter);
        // then
        assertThat(actual).isEqualTo("{\"name\":\"tonic\",\"email\":\"tonic@gmail.com\"}");
    }

    @DisplayName("POST /api/user로 요청할 때 json 값이 없다면 빈 문자열을 반환한다.")
    @Test
    void post_api_user_empty() throws Exception {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        PrintWriter printWriter = new PrintWriter(new ByteArrayOutputStream());

        when(request.getRequestURI()).thenReturn("/empty-body");
        when(request.getMethod()).thenReturn("POST");
        when(response.getWriter()).thenReturn(printWriter);

        // when
        HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);
        ModelAndView modelAndView = handlerExecution.handle(request, response);
        modelAndView.render(request, response);
        String actual = extractBody(printWriter);

        // then
        assertThat(actual).isEqualTo("");
    }

    private String extractBody(PrintWriter printWriter) throws NoSuchFieldException, IllegalAccessException {
        Field field = printWriter.getClass().getDeclaredField("out");
        field.setAccessible(true);
        Field cb = field.get(printWriter).getClass().getDeclaredField("cb");
        cb.setAccessible(true);
        String s = new String((char[]) cb.get(field.get(printWriter)));
        return s.replace("\0", "");
    }
}
