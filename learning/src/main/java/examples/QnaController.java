package examples;

import examples.annotations.Controller;
import examples.annotations.Inject;

@Controller

public class QnaController {

    private final MyQnaService qnaService;

    @Inject
    public QnaController(MyQnaService qnaService) {
        this.qnaService = qnaService;
    }
}
