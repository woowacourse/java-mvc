package nextstep.mvc.assembler;

import nextstep.mvc.assembler.annotation.Component;
import nextstep.mvc.assembler.annotation.ComponentScan;
import nextstep.web.annotation.Controller;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import samples.SampleComponent1;
import samples.SampleComponent2;
import samples.SampleComponent3;
import samples.SampleComponent4;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class ComponentScannerTest {
    @DisplayName("ComponentScan 하위의 Component를 로드한다.")
    @Test
    void load() {

        assertThat(Arrays.asList()).usingRecursiveComparison()
        ComponentScanner scanner = new ComponentScanner();
        scanner.scanFromComponentScan("samples");
        System.out.println(this.getClass().getName());
        scanner.contains(SampleComponent1.class);
        scanner.contains(SampleComponent2.class);
        scanner.contains(SampleComponent3.class);
        scanner.contains(SampleComponent4.class);
    }
}

