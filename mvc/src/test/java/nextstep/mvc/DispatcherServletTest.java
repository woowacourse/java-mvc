package nextstep.mvc;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import nextstep.mvc.controller.tobe.AnnotationHandlerAdapter;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.web.support.RequestMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DispatcherServletTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private DispatcherServlet dispatcherServlet;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    @DisplayName("어노테이션 기반 컨트롤러에 해당하는 요청에 응답할 수 있다.")
    void handleAnnotatedControllerRequest() throws ServletException {
        // given
        given(request.getRequestURI()).willReturn("/get-test");
        given(request.getMethod()).willReturn("GET");
        given(request.getAttribute("id")).willReturn("gugu");
        given(request.getRequestDispatcher("")).willReturn(mock(RequestDispatcher.class));
        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        dispatcherServlet.addHandlerAdapter(new AnnotationHandlerAdapter());

        // when
        dispatcherServlet.init();
        dispatcherServlet.service(request, response);

        // then
        verify(request).getAttribute("id");
    }

    @Test
    @DisplayName("Response Body로 JSON 형식의 데이터를 담아 반환할 수 있다.")
    void handleJsonView() throws ServletException, IOException {
        // given
        given(request.getRequestURI()).willReturn("/api/sample");
        given(request.getMethod()).willReturn(RequestMethod.GET.name());
        final PrintWriter writer = mock(PrintWriter.class);
        given(response.getWriter()).willReturn(writer);
        willDoNothing().given(writer).write(any(String.class));

        dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.addHandlerMapping(new AnnotationHandlerMapping("samples"));
        dispatcherServlet.addHandlerAdapter(new AnnotationHandlerAdapter());
        dispatcherServlet.init();

        // when
        dispatcherServlet.service(request, response);

        // then
        assertAll(
                () -> verify(writer).write(argThat(this::isValidJson))
        );
    }

    private boolean isValidJson(final String argument) {
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.readTree(argument);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }
}
