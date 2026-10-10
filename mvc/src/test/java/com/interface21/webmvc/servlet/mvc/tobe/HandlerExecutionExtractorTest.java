package com.interface21.webmvc.servlet.mvc.tobe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

class HandlerExecutionExtractorTest {

    private final HandlerExecutionExtractor extractor = new HandlerExecutionExtractor();

    @Test
    @DisplayName("@RequestMapping이 붙은 메서드로 HandlerKey와 HandlerExecution을 만든다")
    void extract() {
        // given
        Map<Class<?>, Object> controllers = Map.of(TestController.class, new TestController());

        // when
        List<HandlerRegistration> registrations = extractor.extract(controllers);

        // then
        assertEquals(
                Set.of(
                        new HandlerKey("/get-test", RequestMethod.GET),
                        new HandlerKey("/post-test", RequestMethod.POST)
                ),
                registrations.stream()
                        .map(HandlerRegistration::handlerKey)
                        .collect(Collectors.toSet())
        );
    }

    @Test
    @DisplayName("서로 다른 컨트롤러가 같은 HandlerKey에 매핑되어도 덮어쓰지 않고 모두 반환한다")
    void extract_withDuplicatedMapping() {
        // given
        Map<Class<?>, Object> controllers = Map.of(
                FirstController.class, new FirstController(),
                SecondController.class, new SecondController()
        );

        // when
        List<HandlerRegistration> registrations = extractor.extract(controllers);

        // then
        assertEquals(2, registrations.size());
        assertTrue(registrations.stream()
                .allMatch(registration -> registration.handlerKey()
                        .equals(new HandlerKey("/duplicated", RequestMethod.GET))));
    }

    static class FirstController {

        @RequestMapping(value = "/duplicated", method = RequestMethod.GET)
        public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) {
            return null;
        }
    }

    static class SecondController {

        @RequestMapping(value = "/duplicated", method = RequestMethod.GET)
        public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) {
            return null;
        }
    }
}
