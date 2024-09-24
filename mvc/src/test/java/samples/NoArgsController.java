package samples;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;

@Controller
@RequestMapping("/api")
public class NoArgsController {

    @RequestMapping(value = "/no-args", method = RequestMethod.GET)
    public ModelAndView noArgsMethod() {
        ModelAndView modelAndView = new ModelAndView((model, request, response) -> {
        });
        modelAndView.addObject("message", "no args");
        return modelAndView;
    }
}
