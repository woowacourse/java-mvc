package webmvc.org.springframework.web.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
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
import web.org.springframework.web.bind.annotation.RequestMapping;
import webmvc.org.springframework.web.servlet.ModelAndView;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HandlerExecutionTest {

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
                .filter(declaredMethod -> {
                    RequestMapping requestMapping = declaredMethod.getAnnotation(RequestMapping.class);
                    return Objects.nonNull(requestMapping) && requestMapping.method() == GET;
                })
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
        HandlerExecution handlerExecution = new HandlerExecution(object, method);

        when(request.getAttribute("id")).thenReturn("gugu");

        // when
        ModelAndView modelAndView = handlerExecution.handle(request, response);

        // then
        assertThat(modelAndView.getObject("id")).isEqualTo("gugu");
    }
}
