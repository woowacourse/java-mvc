package nextstep.mvc.controller.exception;

import nextstep.mvc.Pages;

public class InternalServerException extends AbstractCustomException {

    @Override
    public Pages getPages() {
        return Pages.INTERNAL_SERVER;
    }
}
