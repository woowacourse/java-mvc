package webmvc.org.springframework.web.servlet.mvc.supports.mapping;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import webmvc.org.springframework.web.servlet.mvc.supports.adapter.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.supports.mapping.HandlerExecution;
import webmvc.org.springframework.web.servlet.mvc.view.JspView;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HandlerExecutionTest {

    @Test
    void 생성자는_handler와_method를_전달하면_HandlerExecution을_초기화한다() throws NoSuchMethodException {
        final TestController testController = new TestController();
        final Method method = TestController.class.getMethod(
                "test",
                HttpServletRequest.class,
                HttpServletResponse.class
        );

        assertDoesNotThrow(() -> new HandlerExecution(testController, method));
    }

    @Test
    void handle_메서드는_Controller의_반환값을_ModelAndView로_변환해_반환한다() throws Exception {
        final TestController testController = new TestController();
        final Method method = TestController.class.getMethod(
                "test",
                HttpServletRequest.class,
                HttpServletResponse.class
        );
        final HandlerExecution handlerExecution = new HandlerExecution(testController, method);
        final HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);

        given(httpServletRequest.getSession(anyBoolean())).willReturn(null);

        final ModelAndView actual = handlerExecution.handle(httpServletRequest, null);

        // TODO : 이후 미션 단계에서 테스트 케이스 추가 예정
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isNotNull();
            softAssertions.assertThat(actual.getView()).isNotNull();
        });
    }

    static class TestController {

        public ModelAndView test(final HttpServletRequest ignoreRequest, final HttpServletResponse ignoreResponse) {
            return new ModelAndView(new JspView("/hello.jsp"));
        }
    }
}
