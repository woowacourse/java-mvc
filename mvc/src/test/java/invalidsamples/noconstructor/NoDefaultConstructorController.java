package invalidsamples.noconstructor;

import com.interface21.context.stereotype.Controller;

@Controller
public class NoDefaultConstructorController {

    private final String name;

    public NoDefaultConstructorController(final String name) {
        this.name = name;
    }
}
