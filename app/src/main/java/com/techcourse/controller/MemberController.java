package com.techcourse.controller;

import static com.techcourse.exception.MemberExceptionMessage.NO_ACCOUNT;

import com.techcourse.domain.User;
import com.techcourse.exception.MemberNotFoundException;
import com.techcourse.repository.InMemoryUserRepository;
import nextstep.core.annotation.Autowired;
import nextstep.mvc.annotation.Controller;
import nextstep.mvc.annotation.RequestMapping;
import nextstep.mvc.annotation.ResponseBody;
import nextstep.mvc.argument.annotation.RequestParams;
import nextstep.mvc.returntype.ResponseEntity;
import nextstep.mvc.view.JsonView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.support.RequestMethod;

@Controller
public class MemberController {

    private final InMemoryUserRepository repository;

    @Autowired
    public MemberController(InMemoryUserRepository repository) {
        this.repository = repository;
    }

    @ResponseBody
    @RequestMapping(value = "/members", method = RequestMethod.GET)
    public ResponseEntity members() {
        return ResponseEntity.ok(repository.findAll());
    }

    @ResponseBody
    @RequestMapping(value = "/single-members", method = RequestMethod.GET)
    public ResponseEntity singleMember(@RequestParams(name = "account") String account) {
        return ResponseEntity.ok(repository.findByAccount(account)
            .orElseThrow(() -> new MemberNotFoundException(NO_ACCOUNT)));
    }

    @RequestMapping(value = "/single-members/model", method = RequestMethod.GET)
    public ModelAndView singleMemberWithModel(@RequestParams(name = "account") String account) {
        final User user = repository.findByAccount(account)
            .orElseThrow(() -> new MemberNotFoundException(NO_ACCOUNT));

        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("account", user.getAccount());
        modelAndView.addObject("email", user.getEmail());
        return modelAndView;
    }
}
