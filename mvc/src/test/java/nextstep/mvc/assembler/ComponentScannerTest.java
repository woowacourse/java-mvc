package nextstep.mvc.assembler;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.SampleComponent1;
import samples.SampleComponent2;
import samples.SampleComponent3;
import samples.SampleComponent4;

class ComponentScannerTest {
    @DisplayName("ComponentScan 하위의 Component를 로드한다.")
    @Test
    void load() {
        ComponentScanner scanner = new ComponentScanner();
        Map<Class<?>, Object> samples = scanner.scan("samples");

        assertThat(scanner.contains(samples, SampleComponent1.class)).isTrue();
        assertThat(scanner.contains(samples, SampleComponent2.class)).isTrue();
        assertThat(scanner.contains(samples, SampleComponent3.class)).isTrue();
        assertThat(scanner.contains(samples, SampleComponent4.class)).isTrue();
    }
}

