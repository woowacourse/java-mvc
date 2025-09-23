package samples;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.view.JsonView;
import com.interface21.webmvc.servlet.view.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class JsonTestController {

    public record TestUser(String name, String email) {
    }

    @RequestMapping(value = "/json/single", method = RequestMethod.GET)
    public ModelAndView findSingleObject(HttpServletRequest request, HttpServletResponse response) {
        final var modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user", new TestUser("gugu", "gugu@email.com"));
        return modelAndView;
    }

    @RequestMapping(value = "/json/multiple", method = RequestMethod.GET)
    public ModelAndView findMultipleObjects(HttpServletRequest request, HttpServletResponse response) {
        final var modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("user1", new TestUser("gugu", "gugu@email.com"));
        modelAndView.addObject("user2", new TestUser("mj", "mj@email.com"));
        return modelAndView;
    }
}
