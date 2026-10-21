package invalidcontrollers.throwing;

import com.interface21.context.stereotype.Controller;

@Controller
public class ThrowingController {

    public static final IllegalStateException FAILURE = new IllegalStateException("constructor failed");

    public ThrowingController() {
        throw FAILURE;
    }
}
