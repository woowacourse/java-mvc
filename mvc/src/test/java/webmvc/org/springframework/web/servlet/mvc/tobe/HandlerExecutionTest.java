package webmvc.org.springframework.web.servlet.mvc.tobe;

import static fixture.HttpServletFixture.httpServletRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import samples.TestController;
import webmvc.org.springframework.web.servlet.ModelAndView;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HandlerExecutionTest {

    @Test
    void handle_메서드_실행() throws Exception {
        // given
        final HttpServletRequest request = httpServletRequest("/test/get", "GET", Map.of("id", "gugu"));
        final HttpServletResponse response = mock(HttpServletResponse.class);

        final Method method = TestController.class.getDeclaredMethod(
                "find",
                HttpServletRequest.class,
                HttpServletResponse.class
        );

        final HandlerExecution handlerExecution = new HandlerExecution(new TestController(), method);

        // when
        final ModelAndView modelAndView = handlerExecution.handle(request, response);

        // then
        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }
}
