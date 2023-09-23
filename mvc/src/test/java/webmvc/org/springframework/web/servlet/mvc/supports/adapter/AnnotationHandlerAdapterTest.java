package webmvc.org.springframework.web.servlet.mvc.supports.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import samples.TestLegacyController;
import webmvc.org.springframework.web.servlet.mvc.supports.adapter.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.supports.adapter.AnnotationHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.supports.mapping.AnnotationHandlerMapping;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AnnotationHandlerAdapterTest {

    @Test
    void 생성자는_호출하면_ManualHandlerMapping을_초기화한다() {
        Assertions.assertDoesNotThrow(AnnotationHandlerAdapter::new);
    }

    @Test
    void supports_메서드는_처리할_수_있는_핸들러를_전달하면_true를_반환한다() {
        final AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("samples");
        annotationHandlerMapping.initialize();
        final HttpServletRequest request = mock(HttpServletRequest.class);
        given(request.getRequestURI()).willReturn("/get-test");
        given(request.getMethod()).willReturn("GET");
        final Object handler = annotationHandlerMapping.getHandler(request);
        final AnnotationHandlerAdapter adapter = new AnnotationHandlerAdapter();

        final boolean actual = adapter.supports(handler);

        assertThat(actual).isTrue();
    }

    @Test
    void supports_메서드는_처리할_수_없는_핸들러를_전달하면_false를_반환한다() {
        final AnnotationHandlerAdapter adapter = new AnnotationHandlerAdapter();
        final Object invalidHandler = new TestLegacyController();

        final boolean actual = adapter.supports(invalidHandler);

        assertThat(actual).isFalse();
    }

    @Test
    void execute_메서드는_요청_응답_핸들러를_전달하면_요청을_처리하고_ModelAndView를_반환한다() throws Exception {
        final AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("samples");
        annotationHandlerMapping.initialize();
        final HttpServletRequest request = mock(HttpServletRequest.class);
        given(request.getRequestURI()).willReturn("/get-test");
        given(request.getMethod()).willReturn("GET");
        final HttpServletResponse response = mock(HttpServletResponse.class);
        final Object handler = annotationHandlerMapping.getHandler(request);
        final AnnotationHandlerAdapter adapter = new AnnotationHandlerAdapter();

        final ModelAndView actual = adapter.execute(request, response, handler);

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isNotNull();
            softAssertions.assertThat(actual.getView()).isNotNull();
        });
    }
}
