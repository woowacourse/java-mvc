package nextstep.mvc.controller.exception;

import nextstep.mvc.Pages;

public class BadRequestException extends AbstractCustomException {

    @Override
    public Pages getPages() {
        return Pages.BAD_REQUEST;
    }
}
