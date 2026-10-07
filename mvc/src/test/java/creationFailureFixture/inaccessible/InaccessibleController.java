package creationFailureFixture.inaccessible;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class InaccessibleController {

    @RequestMapping(value = "/inaccessible", method = RequestMethod.GET)
    public ModelAndView inaccessible(HttpServletRequest request, HttpServletResponse response){
        return null;
    }

    private InaccessibleController(){
    }
}
