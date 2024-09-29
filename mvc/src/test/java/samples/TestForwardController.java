package samples;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;

public class TestForwardController implements Controller {

    private final String path;

    public TestForwardController(String path) {
        this.path = Objects.requireNonNull(path);
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return path;
    }
}
