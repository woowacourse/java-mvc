package com.interface21.webmvc.servlet.mvc.handler.mapping;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interface21.web.bind.annotation.RequestMapping;

import samples.TestController;

class MethodScannerTest {

    @DisplayName("특정 어노테이션이 붙어있는 메서드들을 찾는다.")
    @Test
    void findAllByAnnotation() throws NoSuchMethodException {
        // given
        MethodScanner methodScanner = new MethodScanner(TestController.class);

        // when
        Set<Method> methods = methodScanner.findAllByAnnotation(RequestMapping.class);

        // then
        Method findUserId = TestController.class.getMethod("findUserId", HttpServletRequest.class, HttpServletResponse.class);
        Method save = TestController.class.getMethod("save", HttpServletRequest.class, HttpServletResponse.class);
        Method allMethod = TestController.class.getMethod("allMethod", HttpServletRequest.class, HttpServletResponse.class);

        assertThat(methods).containsExactlyInAnyOrder(findUserId, save, allMethod);
    }
}
