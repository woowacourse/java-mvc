package nextstep.mvc;

import jakarta.servlet.http.HttpServletRequest;
import java.io.Serializable;

public interface HandlerMapping extends Serializable {

    void initialize();

    Object getHandler(HttpServletRequest request);
}
