package samples;

import nextstep.mvc.assembler.annotation.Component;
import nextstep.mvc.assembler.annotation.ComponentScan;
import nextstep.web.annotation.Controller;

@ComponentScan
public class SampleComponent1 {
    public SampleComponent1() {
        System.out.println("sampleComponent1");
    }
}

