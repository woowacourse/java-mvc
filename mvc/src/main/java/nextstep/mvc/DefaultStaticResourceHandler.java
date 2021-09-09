package nextstep.mvc;

import static nextstep.web.support.ContentType.contentType;
import static nextstep.web.support.ContentType.headerKey;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import nextstep.mvc.exception.PageNotFoundException;

public class DefaultStaticResourceHandler implements StaticResourceHandler {

    @Override
    public void handleResource(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        try {
            final String path = httpRequest.getRequestURI();
            final URL resourceUrl = httpRequest.getServletContext().getResource(path);
            final String content = Files.readString(new File(resourceUrl.toURI()).toPath());
            httpResponse.setHeader(headerKey(), contentType(path));
            httpResponse.getWriter().append(content);
            httpResponse.flushBuffer();
        } catch (Exception e) {
            throw new PageNotFoundException();
        }
    }
}
