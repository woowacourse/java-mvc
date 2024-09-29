package samples.valid;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.handler.adapter.HandlerAdapter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TestAdapter implements HandlerAdapter<TestController> {

    @Override
    public Class<TestController> getSupportedClass() {
        return TestController.class;
    }

    @Override
    public ModelAndView handle(Object handler, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return null;
    }
}
