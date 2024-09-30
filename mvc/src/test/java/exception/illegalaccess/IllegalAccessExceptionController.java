package exception.illegalaccess;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;

@Controller
public class IllegalAccessExceptionController {

    protected IllegalAccessExceptionController() {
    }

    @RequestMapping("/illegal-access")
    public void illegalAccess() {
    }
}
