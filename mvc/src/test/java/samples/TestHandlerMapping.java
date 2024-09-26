package samples;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class TestHandlerMapping implements HandlerMapping {

    private HandlerExecution handlerExecution;

    @Override
    public void initialize() {
        try {
            Class<TestController> testControllerClass = TestController.class;
            TestController testController = testControllerClass.getConstructor().newInstance();
            Method method = testControllerClass.getMethod(
                    "findUserId", HttpServletRequest.class, HttpServletResponse.class);

            handlerExecution = new HandlerExecution(testController, method);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        if (request.getRequestURI().equals("/get-test")) {
            return handlerExecution;
        } else {
            return null;
        }
    }
}
