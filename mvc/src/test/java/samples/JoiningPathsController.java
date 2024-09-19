package samples;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;

@Controller
@RequestMapping("/api")
public class JoiningPathsController {

    @RequestMapping(value = "/join-paths", method = RequestMethod.GET)
    public ModelAndView expectJoiningPaths() {
        ModelAndView modelAndView = new ModelAndView((model, request, response) -> {
        });
        modelAndView.addObject("message", "Paths joined");
        return modelAndView;
    }
}
