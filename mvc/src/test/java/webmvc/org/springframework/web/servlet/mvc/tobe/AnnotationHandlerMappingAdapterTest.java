package webmvc.org.springframework.web.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import samples.TestController;
import webmvc.org.springframework.web.servlet.ModelAndView;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AnnotationHandlerMappingAdapterTest {

    @Test
    void supports_메서드는_HandlerExecution_타입이면_true_반환() throws NoSuchMethodException {
        AnnotationHandlerMappingAdapter annotationHandlerMappingAdapter = new AnnotationHandlerMappingAdapter();
        HandlerExecution handlerExecution = new HandlerExecution(new TestController(),
                TestController.class.getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class));

        boolean supports = annotationHandlerMappingAdapter.supports(handlerExecution);

        assertThat(supports).isTrue();
    }

    @Test
    void supports_메서드는_HandlerExecution_타입이_아니면_false_반환() {
        AnnotationHandlerMappingAdapter annotationHandlerMappingAdapter = new AnnotationHandlerMappingAdapter();
        Object object = new Object();

        boolean supports = annotationHandlerMappingAdapter.supports(object);

        assertThat(supports).isFalse();
    }

    @Test
    void 어노테이션_컨트롤러의_execute_메서드를_실행하고_ModelAndView_반환한다() throws Exception {
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        AnnotationHandlerMappingAdapter annotationHandlerMappingAdapter = new AnnotationHandlerMappingAdapter();
        HandlerExecution handlerExecution = new HandlerExecution(new TestController(),
                TestController.class.getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class));

        ModelAndView modelAndView = annotationHandlerMappingAdapter.handle(httpServletRequest, httpServletResponse,
                handlerExecution);

        assertThat(modelAndView).isNotNull();
    }
}
