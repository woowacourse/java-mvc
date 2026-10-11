package fixtures.mvc;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JsonView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ResponseController {

    @RequestMapping(value = "/fixture/page", method = RequestMethod.GET)
    public ModelAndView page(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(new JspView("/fixture.jsp"))
                .addObject("message", "화면 데이터");
    }

    @RequestMapping(value = "/fixture/page", method = RequestMethod.POST)
    public ModelAndView submit(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(new JspView("redirect:/fixture/page"));
    }

    @RequestMapping(value = "/fixture/json", method = RequestMethod.GET)
    public ModelAndView json(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView(new JsonView())
                .addObject("account", request.getParameter("account"));
    }
}
