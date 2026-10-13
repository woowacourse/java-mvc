package serializationfailurehandlers;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JsonView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class FailingJsonController {

    @RequestMapping(value = "/serialization-failure", method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(new JsonView()).addObject("user", new FailingResponse());
    }

    public static class FailingResponse {
        public String getAccount() {
            throw new IllegalStateException("Cannot read account");
        }
    }
}
