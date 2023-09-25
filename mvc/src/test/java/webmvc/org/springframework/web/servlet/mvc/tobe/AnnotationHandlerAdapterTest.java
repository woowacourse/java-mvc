package webmvc.org.springframework.web.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static web.org.springframework.web.bind.annotation.RequestMethod.GET;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import samples.TestAnnotationController;
import samples.TestManualController;
import web.org.springframework.web.bind.annotation.RequestMapping;
import webmvc.org.springframework.web.servlet.ModelAndView;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AnnotationHandlerAdapterTest {

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    private Object object;
    private Method method;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        Class<?> clazz = TestAnnotationController.class;
        object = clazz.getDeclaredConstructor().newInstance();
        method = Arrays.stream(clazz.getDeclaredMethods())
                .filter(declaredMethod -> {
                    RequestMapping requestMapping = declaredMethod.getAnnotation(RequestMapping.class);
                    return Objects.nonNull(requestMapping) && requestMapping.method() == GET;
                })
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    @Test
    void Controller_애노테이션이_설정된_handler는_지원한다() {
        // given
        Object handler = new HandlerExecution(object, method);
        AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();

        // when
        boolean expected = annotationHandlerAdapter.supports(handler);

        // then
        assertThat(expected).isTrue();
    }

    @Test
    void Controller_애노테이션이_설정된_handler는_지원하지_않는다() {
        // given
        Object handler = new TestManualController();
        AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();

        // when
        boolean expected = annotationHandlerAdapter.supports(handler);

        // then
        assertThat(expected).isFalse();
    }

    @Test
    void handler를_다룬다() {
        // given
        Object handler = new HandlerExecution(object, method);
        AnnotationHandlerAdapter annotationHandlerAdapter = new AnnotationHandlerAdapter();

        // when
        ModelAndView modelAndView = annotationHandlerAdapter.handle(handler, request, response);

        // then
        assertThat(modelAndView.getViewName()).isEqualTo("/tests");
    }
}
