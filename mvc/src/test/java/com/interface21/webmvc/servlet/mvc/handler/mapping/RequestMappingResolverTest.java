package com.interface21.webmvc.servlet.mvc.handler.mapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.reflect.Method;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

class RequestMappingResolverTest {

    @DisplayName("RequestMapping에 지정된 url을 가져온다.")
    @Test
    void getUrl() throws NoSuchMethodException {
        // given
        Method fakeGet = FakeController.class.getMethod("fakeGet");
        RequestMapping requestMapping = fakeGet.getAnnotation(RequestMapping.class);
        RequestMappingResolver requestMappingResolver = new RequestMappingResolver(requestMapping);

        // when
        String url = requestMappingResolver.getUrl();

        // then
        assertThat(url).isEqualTo("/fake-get");
    }

    @DisplayName("RequestMapping에 지정된 url이 없으면 예외가 발생한다.")
    @Test
    void getUrlFail() throws NoSuchMethodException {
        // given
        Method noValue = FakeController.class.getMethod("noValue");
        RequestMapping requestMapping = noValue.getAnnotation(RequestMapping.class);
        RequestMappingResolver requestMappingResolver = new RequestMappingResolver(requestMapping);

        // when & then
        assertThatThrownBy(() -> requestMappingResolver.getUrl()).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("@RequestMapping의 value값이 지정되어 있지 않습니다.");
    }

    @DisplayName("RequestMapping에 지정된 method를 가져온다.")
    @Test
    void getRequestMethods() throws NoSuchMethodException {
        // given
        Method fakeGet = FakeController.class.getMethod("fakeGet");
        RequestMapping requestMapping = fakeGet.getAnnotation(RequestMapping.class);
        RequestMappingResolver requestMappingResolver = new RequestMappingResolver(requestMapping);

        // when
        Set<RequestMethod> requestMethods = requestMappingResolver.getRequestMethods();

        // then
        assertThat(requestMethods).containsExactly(RequestMethod.GET);
    }

    @DisplayName("RequestMapping에 지정된 method가 없으면 모든 RequestMethod를 가져온다.")
    @Test
    void getRequestMethodsAll() throws NoSuchMethodException {
        // given
        Method fakeGet = FakeController.class.getMethod("noRequestMethod");
        RequestMapping requestMapping = fakeGet.getAnnotation(RequestMapping.class);
        RequestMappingResolver requestMappingResolver = new RequestMappingResolver(requestMapping);

        // when
        Set<RequestMethod> requestMethods = requestMappingResolver.getRequestMethods();

        // then
        assertThat(requestMethods).containsExactlyInAnyOrder(RequestMethod.values());
    }

    static class FakeController {

        @RequestMapping(value = "/fake-get", method = RequestMethod.GET)
        public void fakeGet() {
        }

        @RequestMapping(method = RequestMethod.GET)
        public void noValue() {
        }

        @RequestMapping(value = "/no-request-method")
        public void noRequestMethod() {
        }
    }
}
