package nextstep.mvc.controller.scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.Set;
import nextstep.mvc.controller.scanner.test_classes.Test1;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RequestMappingScannerTest {

    @DisplayName("RequestMapping 어노테이셔이 붙은 method를 찾는다.")
    @Test
    void getRequestMappedMethod() {
        Set<Method> requestMappedMethod = RequestMappingScanner.getRequestMappedMethod(Test1.class);
        assertThat(requestMappedMethod).hasSize(2);
    }
}
