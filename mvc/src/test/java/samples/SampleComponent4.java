package samples;

import nextstep.mvc.assembler.annotation.Component;

@Component
public class SampleComponent4 {
    public SampleComponent4(SampleComponent3 sampleComponent3) {
        System.out.println("sampleComponent4");
    }
}
