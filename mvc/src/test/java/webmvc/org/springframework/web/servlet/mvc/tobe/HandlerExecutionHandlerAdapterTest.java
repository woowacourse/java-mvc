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
import webmvc.org.springframework.web.servlet.HandlerAdapter;
import webmvc.org.springframework.web.servlet.ModelAndView;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HandlerExecutionHandlerAdapterTest {

    private final HandlerAdapter handlerExecutionHandlerAdapter = new HandlerExecutionHandlerAdapter();

    @Test
    void 핸들러를_지원하는지_확인한다() {
        // given
        final HandlerExecution handlerExecution = new HandlerExecution(null, null);

        // expect
        assertThat(handlerExecutionHandlerAdapter.isSupport(handlerExecution)).isTrue();
    }

    @Test
    void 핸들러를_실행시킨다() throws Exception {
        // given
        final HttpServletRequest request = httpServletRequest("/post-test", "POST", Map.of("id", "gugu"));
        final HttpServletResponse response = mock(HttpServletResponse.class);

        final TestController testController = new TestController();
        final Method method = testController.getClass()
                .getDeclaredMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        final HandlerExecution handlerExecution = new HandlerExecution(testController, method);

        // when
        final ModelAndView modelAndView = handlerExecutionHandlerAdapter.handle(request, response, handlerExecution);

        // then
        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }
}
