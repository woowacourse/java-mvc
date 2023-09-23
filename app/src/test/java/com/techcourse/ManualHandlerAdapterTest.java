package com.techcourse;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;

import java.lang.reflect.Method;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ManualHandlerAdapterTest {

    private ManualHandlerAdapter manualHandlerAdapter;

    @BeforeEach
    void setup() {
        manualHandlerAdapter = new ManualHandlerAdapter();
    }

    @Test
    void 처리가_가능한_핸들러인지_판단할_수_있다() throws NoSuchMethodException {
        // given
        ClassCanHandle clazz = new ClassCanHandle();

        Method method = clazz.getClass()
                .getDeclaredMethod("execute", HttpServletRequest.class, HttpServletResponse.class);

        // expect
        assertThat(manualHandlerAdapter.supports(method)).isTrue();
    }

    @Test
    void 처리가_불가능한_핸들러인지_판단할_수_있다() throws NoSuchMethodException {
        // given
        ClassCannotHandle clazz = new ClassCannotHandle();

        Method method = clazz.getClass()
                .getDeclaredMethod("execute", HttpServletRequest.class, HttpServletResponse.class);

        // expect
        assertThat(manualHandlerAdapter.supports(method)).isFalse();
    }

    @Test
    void 핸들러를_실행시키고_결과를_반환할_수_있다() throws NoSuchMethodException {
        // given
        ClassCanHandle clazz = new ClassCanHandle();

        // when
        Method method = clazz.getClass()
                .getDeclaredMethod("execute", HttpServletRequest.class, HttpServletResponse.class);
        HandlerExecution handlerExecution = new HandlerExecution(clazz, method);

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        // then
        ModelAndView modelAndView = manualHandlerAdapter.handle(request, response, handlerExecution);

        assertThat(modelAndView).isNotNull();
    }

    static class ClassCanHandle implements Controller {

        @Override
        public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
            return "viewName";
        }
    }

    static class ClassCannotHandle {

        public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
            return null;
        }
    }
}
