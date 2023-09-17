package webmvc.org.springframework.web.servlet.mvc.tobe;

import static fixture.HttpServletFixture.httpServletRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import webmvc.org.springframework.web.servlet.ModelAndView;

class AnnotationHandlerMappingTest {

    private AnnotationHandlerMapping handlerMapping;

    @BeforeEach
    void setUp() {
        handlerMapping = new AnnotationHandlerMapping("samples");
        handlerMapping.initialize();
    }

    @CsvSource({"/request, GET", "/get, GET", "/post, POST", "/patch, PATCH", "/put, PUT", "/delete, DELETE"})
    @ParameterizedTest
    void handlerMapping이_정상적으로_동작한다(final String uri, final String method) throws Exception {
        // given
        final HttpServletRequest request = httpServletRequest("/test" + uri, method, Map.of("id", "gugu"));
        final HttpServletResponse response = mock(HttpServletResponse.class);

        final HandlerExecution handlerExecution = (HandlerExecution) handlerMapping.getHandler(request);

        // when
        final ModelAndView modelAndView = handlerExecution.handle(request, response);

        // then
        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }
}
