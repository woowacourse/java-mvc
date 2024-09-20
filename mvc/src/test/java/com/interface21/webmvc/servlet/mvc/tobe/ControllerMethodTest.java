package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.mvc.tobe.handler.Handler;
import com.interface21.webmvc.servlet.mvc.tobe.handler.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.handler.HandlerKey;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.ExampleController;

class ControllerMethodTest {

    @DisplayName("메서드에 @RequestMapping 어노테이션이 없는 경우 예외가 발생한다.")
    @Test
    void methodWithoutRequestMapping() throws Exception {
        Method method = ExampleController.class.getMethod("method3",
                HttpServletRequest.class, HttpServletResponse.class);

        assertThatThrownBy(() -> new ControllerMethod(method))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Method must be annotated with @RequestMapping");
    }

    @DisplayName("메서드에 대한 핸들러를 올바르게 생성한다.")
    @Test
    void createHandlerKeys() throws Exception {
        Method method = getMethod("method1");
        ExampleController instance = new ExampleController();
        ControllerMethod controllerMethod = new ControllerMethod(method);

        List<Handler> handlers = controllerMethod.createHandlers(instance);

        assertThat(handlers)
                .containsExactlyInAnyOrder(
                        createHandler("/get-test", RequestMethod.DELETE, instance, "method1"),
                        createHandler("/get-test", RequestMethod.GET, instance, "method1")
                );
    }

    @DisplayName("@RequestMapping()에 method 설정이 되어 있지 않으면 모든 HTTP method에 대한 핸들러를 생성한다.")
    @Test
    void createHandlersWithAllMethod() throws Exception {
        Method method = getMethod("method4");
        ExampleController instance = new ExampleController();
        ControllerMethod controllerMethod = new ControllerMethod(method);

        List<Handler> handlers = controllerMethod.createHandlers(instance);

        assertThat(handlers)
                .containsExactlyInAnyOrder(
                        createHandler("/all", RequestMethod.GET, instance, "method4"),
                        createHandler("/all", RequestMethod.HEAD, instance, "method4"),
                        createHandler("/all", RequestMethod.POST, instance, "method4"),
                        createHandler("/all", RequestMethod.PUT, instance, "method4"),
                        createHandler("/all", RequestMethod.PATCH, instance, "method4"),
                        createHandler("/all", RequestMethod.DELETE, instance, "method4"),
                        createHandler("/all", RequestMethod.OPTIONS, instance, "method4"),
                        createHandler("/all", RequestMethod.TRACE, instance, "method4")
                );
    }

    private static Method getMethod(String name) throws Exception {
        return ExampleController.class.getMethod(name, HttpServletRequest.class, HttpServletResponse.class);
    }

    private Handler createHandler(String url, RequestMethod requestMethod, Object controller, String methodName)
            throws Exception {
        Method method = controller.getClass()
                .getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
        return new Handler(
                new HandlerKey(url, requestMethod),
                new HandlerExecution(controller, method)
        );
    }
}
