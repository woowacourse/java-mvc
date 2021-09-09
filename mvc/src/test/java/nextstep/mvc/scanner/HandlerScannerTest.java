package nextstep.mvc.scanner;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.Test2Controller;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("HandlerScanner 스캔 테스트")
class HandlerScannerTest {

    @Test
    void getController() throws ClassNotFoundException {
        //given
        final String packageName = Test2Controller.class.getPackageName();
        final HandlerScanner handlerScanner = new HandlerScanner(packageName);
        //when
        Map<Class<?>, Object> controllers = handlerScanner.getHandler();
        //then
        assertThat(controllers).containsKey(Class.forName("samples.Test2Controller"));
    }
}