package samples;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.mapper.HandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class TestManualHandlerMapping implements HandlerMapping {

    private static final Map<String, Controller> controllers = new HashMap<>();

    @Override
    public void initialize() {
        controllers.put("/supported-uri", new TestManualController());
    }

    @Override
    public Object getHandler(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return controllers.get(uri);
    }

    static class TestManualController implements Controller {

        @Override
        public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
            return "redirect:/test.jsp";
        }
    }
}
