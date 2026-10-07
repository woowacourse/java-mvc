package creationFailureFixture.abstractType;


import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public abstract class AbstractController {

    public AbstractController() {
    }

    @RequestMapping(value = "/abstract", method = RequestMethod.GET)
    public ModelAndView abstractController(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }
}
