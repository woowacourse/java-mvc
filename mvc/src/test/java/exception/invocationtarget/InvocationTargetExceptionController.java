package exception.invocationtarget;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;

@Controller
public class InvocationTargetExceptionController {

    public InvocationTargetExceptionController() {
        throw new RuntimeException();
    }

    @RequestMapping("/invocation-target")
    public void invocationTarget() {
    }
}
