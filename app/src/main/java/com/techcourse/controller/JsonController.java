package com.techcourse.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

@Controller
public class JsonController {

    static class JsonTest {

        private final String stringField = "A";
        private final int intField = 123;

        public JsonTest() {
        }

        public String getStringField() {
            return stringField;
        }

        public int getIntField() {
            return intField;
        }
    }

    @RequestMapping(value = "/object-json", method = RequestMethod.GET)
    public JsonTest handleJson(HttpServletRequest request, HttpServletResponse response) {
        return new JsonTest();
    }
}
