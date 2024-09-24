package exception.instantiation;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;

@Controller
public abstract class InstantiationExceptionController {

    @RequestMapping("/instantiation")
    public void instantiation() {
    }
}
