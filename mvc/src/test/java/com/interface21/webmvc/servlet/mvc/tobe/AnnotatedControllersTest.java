package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

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
import samples.AnnotatedExampleController;
import samples.TestController;

class AnnotatedControllersTest {

    @DisplayName("모든 컨트롤러의 핸들러를 생성한다.")
    @Test
    void createHandlers() throws Exception {
        TestController testController = new TestController();
        AnnotatedExampleController exampleController = new AnnotatedExampleController();
        AnnotatedControllers annotatedControllers = AnnotatedControllers.from("samples");

        List<Handler> handlers = annotatedControllers.createHandlers();

        assertThat(handlers)
                .containsExactlyInAnyOrder(
                        createHandler("/get-test", RequestMethod.GET, testController, "findUserId"),
                        createHandler("/post-test", RequestMethod.POST, testController, "save"),
                        createHandler("/hello", RequestMethod.GET, exampleController, "world")
                );
    }

    private static Handler createHandler(String url, RequestMethod requestMethod, Object controller, String methodName)
            throws Exception {
        Method method = controller.getClass()
                .getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
        return new Handler(
                new HandlerKey(url, requestMethod),
                new HandlerExecution(controller, method)
        );
    }
}
