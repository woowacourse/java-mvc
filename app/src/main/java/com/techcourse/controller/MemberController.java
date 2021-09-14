package com.techcourse.controller;

import com.techcourse.repository.InMemoryUserRepository;
import nextstep.core.annotation.Autowired;
import nextstep.mvc.annotation.Controller;
import nextstep.mvc.annotation.RequestMapping;
import nextstep.mvc.annotation.ResponseBody;
import nextstep.mvc.returntype.ResponseEntity;
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
}
