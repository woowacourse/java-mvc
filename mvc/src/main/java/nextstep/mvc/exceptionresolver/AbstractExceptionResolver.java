package nextstep.mvc.exceptionresolver;

import static nextstep.mvc.FileViewUtils.render;
import static nextstep.web.support.ContentType.HTML;
import static nextstep.web.support.ContentType.contentTypeKey;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.support.StatusCode;

public abstract class AbstractExceptionResolver implements ExceptionResolver {

    @Override
    public void resolve(Exception exception, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        renderPage(httpRequest, httpResponse);
    }

    private void renderPage(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        try {
            String pageName = pageName();
            StatusCode statusCode = statusCode();
            render(httpRequest, httpResponse)
                .path(pageName)
                .statusCode(statusCode)
                .header(contentTypeKey(), HTML.contentType())
                .flush();
        } catch (Exception e) {
            try {
                render(httpRequest, httpResponse)
                    .path("500.jsp")
                    .header(contentTypeKey(), HTML.contentType())
                    .statusCode(StatusCode.SERVER_ERROR)
                    .flush();
            } catch (Exception exc) {
                throw new IllegalStateException("500.jsp not found error");
            }
        }
    }

    protected abstract String pageName();

    protected abstract StatusCode statusCode();


}
