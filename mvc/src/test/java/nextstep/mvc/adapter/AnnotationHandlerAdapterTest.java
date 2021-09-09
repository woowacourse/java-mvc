package nextstep.mvc.adapter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.techcourse.air.core.context.ApplicationContext;
import com.techcourse.air.core.context.ApplicationContextProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.techcourse.air.mvc.core.controller.tobe.HandlerExecution;
import com.techcourse.air.mvc.core.view.JspView;
import com.techcourse.air.mvc.core.view.ModelAndView;

import com.techcourse.air.mvc.core.adapter.AnnotationHandlerAdapter;
import com.techcourse.air.mvc.web.annotation.RequestMapping;
import org.reflections.ReflectionUtils;
import samples.TestController;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class AnnotationHandlerAdapterTest {

    private AnnotationHandlerAdapter handlerAdapter;

    @BeforeEach
    void setUp() {
        ApplicationContext context = new ApplicationContext("samples");
        ApplicationContextProvider.setApplicationContext(context);
        context.initializeContext();
        handlerAdapter = context.findBeanByType(AnnotationHandlerAdapter.class);
    }

    @Test
    @DisplayName("해당 어댑터가 처리할 수 있는지 확인")
    void supports() {
        // given
        TestController testController = new TestController();
        Set<Method> allMethods = ReflectionUtils.getAllMethods(testController.getClass(), ReflectionUtils.withAnnotation(RequestMapping.class));
        List<Method> methods = new ArrayList<>(allMethods);
        HandlerExecution handler = new HandlerExecution(testController, methods.get(0));

        // when
        boolean supports = handlerAdapter.supports(handler);

        // then
        assertThat(supports).isTrue();
    }

    @Test
    @DisplayName("해당 어댑터로 핸들러 처리하기")
    void handle() throws Exception {
        // given
        final HttpServletRequest request = mock(HttpServletRequest.class);
        final HttpServletResponse response = mock(HttpServletResponse.class);
        TestController testController = new TestController();
        Set<Method> allMethods = ReflectionUtils.getAllMethods(testController.getClass(), ReflectionUtils.withAnnotation(RequestMapping.class));
        List<Method> methods = new ArrayList<>(allMethods);
        HandlerExecution handler = new HandlerExecution(testController, methods.get(0));

        // when
        ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);

        // then
        assertThat(modelAndView.getView()).usingRecursiveComparison()
                                          .isEqualTo(new JspView(""));
    }

}
