package nextstep.mvc.controller.scanner;

import static java.util.stream.Collectors.toList;
import static nextstep.web.support.RequestMethod.GET;
import static nextstep.web.support.RequestMethod.POST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import nextstep.mvc.controller.scanner.test_classes.Test1;
import nextstep.mvc.controller.tobe.HandlerKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RequestMappingScannerTest {

    @DisplayName("RequestMapping 어노테이셔이 붙은 method를 찾는다.")
    @Test
    void getRequestMappedMethod() {
        Set<Method> requestMappedMethod = RequestMappingScanner.getRequestMappedMethod(Test1.class);
        assertThat(requestMappedMethod).hasSize(2);
    }

    @DisplayName("RequestMapping을 HandlerKey로 변환한다")
    @Test
    public void extractHandlerKeys() {
        Set<Method> requestMappedMethod = RequestMappingScanner.getRequestMappedMethod(Test1.class);

        List<HandlerKey> handlerKeys = requestMappedMethod.stream()
            .map(RequestMappingScanner::extractHandlerKeys)
            .flatMap(Collection::stream)
            .collect(toList());

        assertThat(handlerKeys).containsOnly(
            new HandlerKey("/test1", GET),
            new HandlerKey("/test2", GET),
            new HandlerKey("/test2", POST)
        );
    }

    @DisplayName("RequestMapping 어노테이션이 없으면 예외")
    @Test
    public void extractHandlerKeys_exception() throws NoSuchMethodException {
        Method test3 = Test1.class.getMethod("test3");

        assertThatThrownBy(() -> RequestMappingScanner.extractHandlerKeys(test3))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("This method is not RequestMapped");
    }
}
