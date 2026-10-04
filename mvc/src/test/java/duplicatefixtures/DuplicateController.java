package duplicatefixtures;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class DuplicateController {

    @RequestMapping(value = "/duplicate", method = RequestMethod.GET)
    public ModelAndView duplicate1(HttpServletRequest request, HttpServletResponse response){
        return null;
    }

    @RequestMapping(value = "/duplicate", method = RequestMethod.GET)
    public ModelAndView duplicate2(HttpServletRequest request, HttpServletResponse response){
        return null;
    }
}
