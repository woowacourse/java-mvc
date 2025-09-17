package samples.scanner.fail;

import com.interface21.context.stereotype.Controller;

@Controller
public final class ControllerWithNoDefaultConstructor {

    private final String name;

    public ControllerWithNoDefaultConstructor(final String name) {
        this.name = name;
    }
}
