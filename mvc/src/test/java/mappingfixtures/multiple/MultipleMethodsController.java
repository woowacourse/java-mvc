package mappingfixtures.multiple;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class MultipleMethodsController {

    @RequestMapping(
            value = "/users",
            method = {RequestMethod.GET, RequestMethod.POST}
    )
    public ModelAndView users(HttpServletRequest request, HttpServletResponse response) {
        JspView view = new JspView("/users.jsp");
        ModelAndView modelAndView = new ModelAndView(view);
        modelAndView.addObject("id", "juni");
        return modelAndView;
    }
}
