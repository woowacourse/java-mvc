package creationFailureFixture.throwing;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ThrowingController {

    @RequestMapping(value = "/throwing", method = RequestMethod.GET)
    public ModelAndView throwing(HttpServletRequest request, HttpServletResponse response){
        return null;
    }

    public ThrowingController() {
        throw new RuntimeException();
    }
}
