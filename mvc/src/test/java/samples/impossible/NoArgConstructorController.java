package samples.impossible;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class NoArgConstructorController {

    private String name;
    private String value;

    public NoArgConstructorController(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @RequestMapping(value = "/multi-constructor-test", method = RequestMethod.GET)
    public ModelAndView test(final HttpServletRequest request, final HttpServletResponse response) {
        final var modelAndView = new ModelAndView(new JspView(""));
        modelAndView.addObject("name", null);
        return modelAndView;
    }
}
