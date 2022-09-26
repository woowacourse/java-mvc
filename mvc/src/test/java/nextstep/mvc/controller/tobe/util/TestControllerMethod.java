package nextstep.mvc.controller.tobe.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;
import nextstep.mvc.controller.tobe.ControllerScanner;
import org.reflections.Reflections;
import samples.TestController;

public class TestControllerMethod {

    public static Object HANDLER;

    public static Method FIND_USER_ID;

    public static Method SAVE;

    static {
        try {
            final Reflections reflections = new Reflections("samples");
            final Map<Class<?>, Object> handlers = new ControllerScanner(reflections).getControllers();
            HANDLER = handlers.get(TestController.class);
            FIND_USER_ID = TestController.class.getMethod("findUserId",
                    HttpServletRequest.class, HttpServletResponse.class);
            SAVE = TestController.class.getMethod("save",
                    HttpServletRequest.class, HttpServletResponse.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
