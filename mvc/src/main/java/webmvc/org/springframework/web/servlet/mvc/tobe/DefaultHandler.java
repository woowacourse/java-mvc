package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultHandler implements Handler {
    private static final Logger log = LoggerFactory.getLogger(DefaultHandler.class);

    @Override
    public boolean isSupport() {
        return true;
    }

    @Override
    public Object handle(final HttpServletRequest request, final HttpServletResponse response) {
        log.info("요청하신 HTTP 메서드와 경로에 매핑되는 핸들러가 존재하지 않습니다.");
        return null;
    }
}
