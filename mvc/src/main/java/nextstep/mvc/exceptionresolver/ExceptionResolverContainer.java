package nextstep.mvc.exceptionresolver;

import static nextstep.web.support.ContentType.headerKey;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import nextstep.mvc.FileViewUtils;
import nextstep.web.support.ContentType;
import nextstep.web.support.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceptionResolverContainer {

    private static final Logger log = LoggerFactory.getLogger(ExceptionResolverContainer.class);

    private final List<ExceptionResolver> exceptionResolvers;

    public ExceptionResolverContainer() {
        this.exceptionResolvers = defaultExceptionResolvers();
    }

    private List<ExceptionResolver> defaultExceptionResolvers() {
        List<ExceptionResolver> exceptionResolvers = new ArrayList<>();
        exceptionResolvers.add(new NotFoundExceptionResolver());
        return exceptionResolvers;
    }

    public void resolve(Exception e, HttpServletRequest httpRequest, HttpServletResponse httpResponse)
        throws ServletException {
        try {
            for (ExceptionResolver exceptionResolver : exceptionResolvers) {
                if(exceptionResolver.supportsException(e)) {
                    exceptionResolver.resolve(e, httpRequest, httpResponse);
                    return;
                }
            }
            serverError(httpRequest, httpResponse, e);
        } catch (Exception exception) {
            throw new ServletException();
        }
    }

    private void serverError(HttpServletRequest httpRequeset, HttpServletResponse httpResponse, Exception exception) {
        try {
            FileViewUtils.render(httpRequeset, httpResponse)
                .path("500.jsp")
                .statusCode(StatusCode.SERVER_ERROR)
                .header(headerKey(), ContentType.HTML.contentType())
                .flush();
            for (StackTraceElement stackTraceElement : exception.getStackTrace()) {
                log.error(stackTraceElement.toString());
            }
        } catch (Exception e) {
            throw new IllegalStateException("unknown exception");
        }
    }
}
