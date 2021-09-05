package nextstep.mvc;


import static nextstep.web.support.ContentType.contentType;
import static nextstep.web.support.ContentType.contentTypeKey;

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
            final byte[] body = Files.readAllBytes(new File(resourceUrl.toURI()).toPath());
            final String content = new String(body);
            httpResponse.setHeader(contentTypeKey(), contentType(path));
            httpResponse.getWriter().append(content);
            httpResponse.flushBuffer();
        } catch (Exception e) {
          throw new PageNotFoundException();
        }
    }
}
