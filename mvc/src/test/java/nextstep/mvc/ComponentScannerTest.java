package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import nextstep.web.annotation.Controller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.TestController;

public class ComponentScannerTest {

    @DisplayName("특정 어노테이션이 붙은 Component를 Map 형태로 반환한다.")
    @Test
    void getComponent_ControllerClass_Success() {
        // given
        ComponentScanner componentScanner = new ComponentScanner("samples");

        // when
        Map<String, Object> components = componentScanner.getComponent(Controller.class);

        // then
        assertThat(components.containsKey("TestController")).isTrue();
        assertThat(components.get("TestController")).isInstanceOf(TestController.class);
    }
}
