package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AnnotationHandlerMappingInitializationTest {

    @Test
    @DisplayName("동일한 URL과 HTTP 메서드가 중복으로 매핑되면 초기화에 실패한다")
    void 중복_매핑을_거부한다() {
        final var duplicateHandlerMapping = new AnnotationHandlerMapping(
                "com.interface21.webmvc.servlet.mvc.tobe"
        );

        assertThatThrownBy(duplicateHandlerMapping::initialize)
                .isInstanceOf(IllegalStateException.class);
    }

    @Controller
    public static class DuplicateMappingController {

        @RequestMapping(value = "/duplicate", method = RequestMethod.GET)
        public ModelAndView first(final HttpServletRequest request, final HttpServletResponse response) {
            return null;
        }

        @RequestMapping(value = "/duplicate", method = RequestMethod.GET)
        public ModelAndView second(final HttpServletRequest request, final HttpServletResponse response) {
            return null;
        }
    }
}
