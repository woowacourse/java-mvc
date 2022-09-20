package samples;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import nextstep.mvc.controller.asis.Controller;

public class TestManualController implements Controller {

    private final String path;

    public TestManualController(final String path) {
        this.path = Objects.requireNonNull(path);
    }

    @Override
    public String execute(final HttpServletRequest request, final HttpServletResponse response) {
        return path;
    }
}
