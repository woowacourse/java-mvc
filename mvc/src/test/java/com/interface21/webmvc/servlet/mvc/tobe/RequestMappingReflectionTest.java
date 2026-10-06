package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("리플렉션으로 요청 매핑 정보를 읽는다")
class RequestMappingReflectionTest {

    @Test
    @DisplayName("공개 메서드 중 @RequestMapping이 붙은 메서드만 찾는다")
    void findsRequestMappingMethods() {
        // given
        final var controllerClass = TestController.class;

        // when
        final var mappingMethods = Arrays.stream(controllerClass.getMethods())
                .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                .toList();

        // then
        assertThat(mappingMethods)
                .extracting(method -> method.getName())
                .containsExactlyInAnyOrder("findUserId", "save");
    }

    @Test
    @DisplayName("@RequestMapping에서 요청 경로를 읽는다")
    void readsRequestPath() throws NoSuchMethodException {
        // given
        final var method = TestController.class.getMethod(
                "findUserId", HttpServletRequest.class, HttpServletResponse.class);

        // when
        final var requestMapping = method.getAnnotation(RequestMapping.class);

        // then
        assertThat(requestMapping.value()).isEqualTo("/get-test");
    }

    @Test
    @DisplayName("@RequestMapping에서 지원하는 HTTP 메서드를 읽는다")
    void readsRequestMethods() throws NoSuchMethodException {
        // given
        final var method = TestController.class.getMethod(
                "findUserId", HttpServletRequest.class, HttpServletResponse.class);

        // when
        final var requestMapping = method.getAnnotation(RequestMapping.class);

        // then
        assertThat(requestMapping.method()).containsExactly(RequestMethod.GET);
    }
}
