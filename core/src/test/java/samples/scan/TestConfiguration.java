package samples.scan;

import com.techcourse.air.core.annotation.Bean;
import com.techcourse.air.core.annotation.Configuration;

@Configuration
public class TestConfiguration {

    @Bean
    public TestBean testBean() {
        return new TestBean();
    }

    @Bean
    public TestBean2 testBean2() {
        return new TestBean2();
    }
}
