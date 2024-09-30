package exception.nosuchmethod;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;

@Controller
public class NoSuchMethodExceptionController {

    public NoSuchMethodExceptionController(String arg) {
    }

    @RequestMapping("/no-such-method")
    public void noSuchMethod() {
    }
}
