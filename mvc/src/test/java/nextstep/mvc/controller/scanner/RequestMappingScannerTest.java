package nextstep.mvc.controller.scanner;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.mvc.controller.tobe.HandlerKey;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("RequestMapping Scanner는 ")
class RequestMappingScannerTest {

    @Test
    @DisplayName("BasePackage를 기준으로 @RequestMapping이 사용된 모든 메서드를 찾는다.")
    void findAllMethodsOfAnnotationRequestMapping() {
        // given & when
        Set<?> methods = RequestMappingScanner
            .getInstance()
            .getAllAnnotations(AnnotationController.class);

        // then
        assertThat(methods).hasSize(2);
    }

    @Test
    @DisplayName("URL과 HttpMethod를 가지는 HandlerKey로 변환을 할 수 있다.")
    void convertObjectToHavingURLAndHttpMethod() {
        // given
        RequestMappingScanner requestMappingScanner = RequestMappingScanner.getInstance();
        Set<Method> methods = requestMappingScanner
            .getAllAnnotations(AnnotationController.class);

        // when
        List<HandlerKey> handlerKeys = methods.stream()
            .map(requestMappingScanner::extractHandlerKeys)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());

        // then
        assertThat(handlerKeys).containsOnly(
            new HandlerKey("/", RequestMethod.GET),
            new HandlerKey("/", RequestMethod.POST));
    }

    @Controller
    private class AnnotationController {

        @RequestMapping(value = "/", method = RequestMethod.GET)
        public void requestMappingMethodOfGet() {

        }

        @RequestMapping(value = "/", method = RequestMethod.POST)
        public void requestMappingMethodOfPost() {

        }

        public void notRequestMappingMethod() {

        }
    }
}
