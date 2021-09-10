package nextstep.mvc.exceptionresolver;

import nextstep.mvc.exception.PageNotFoundException;
import nextstep.web.support.StatusCode;

public class NotFoundExceptionResolver extends AbstractExceptionResolver{
    @Override
    public boolean supportsException(Exception exception) {
        return exception.getClass().isAssignableFrom(PageNotFoundException.class);
    }

    @Override
    protected String pageName() {
        return "404.jsp";
    }

    @Override
    protected StatusCode statusCode() {
        return StatusCode.NOT_FOUND;
    }
}
