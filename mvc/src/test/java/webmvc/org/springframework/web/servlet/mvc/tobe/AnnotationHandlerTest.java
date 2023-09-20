package webmvc.org.springframework.web.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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
import web.org.springframework.web.bind.annotation.RequestMapping;
import webmvc.org.springframework.web.servlet.ModelAndView;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AnnotationHandlerTest {

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 다룬다() throws Exception {
        // given
        Class<?> clazz = Class.forName("samples.TestAnnotationController");
        Object object = clazz.getDeclaredConstructor().newInstance();
        Method method = Arrays.stream(clazz.getDeclaredMethods())
                .filter(declaredMethod -> Objects.nonNull(declaredMethod.getAnnotation(RequestMapping.class)))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
        AnnotationHandler annotationHandler = new AnnotationHandler(object, method);

        when(request.getAttribute("id")).thenReturn("gugu");

        // when
        ModelAndView modelAndView = annotationHandler.handle(request, response);

        // then
        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }
}
