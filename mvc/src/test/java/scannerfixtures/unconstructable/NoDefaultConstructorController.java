package scannerfixtures.unconstructable;

import com.interface21.context.stereotype.Controller;

@Controller
public class NoDefaultConstructorController {

    public NoDefaultConstructorController(final String name) {
    }
}
