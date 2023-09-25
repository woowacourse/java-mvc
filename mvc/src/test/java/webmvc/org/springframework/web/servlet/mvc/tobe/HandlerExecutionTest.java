package webmvc.org.springframework.web.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import java.lang.reflect.Method;

class HandlerExecutionTest {

    private static final View VIEW = (model, request, response) -> {

    };

    @Test
    void testHandle() throws Exception {
        //given
        final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        final HttpServletResponse response = Mockito.mock(HttpServletResponse.class);


        final Class<?> testClass = TestClass.class;
        final Method method = testClass.getDeclaredMethod("test", HttpServletRequest.class, HttpServletResponse.class);

        final HandlerExecution handlerExecution = new HandlerExecution(method);

        //when
        final ModelAndView result = handlerExecution.handle(request, response);

        //thenR
        assertThat(result.getViewName()).isEqualTo(null);
    }

    static class TestClass {

        public ModelAndView test(final HttpServletRequest request, final HttpServletResponse response) {
            final ModelAndView modelAndView = new ModelAndView(VIEW);
            return modelAndView.addObject("test", "test");
        }
    }
}
