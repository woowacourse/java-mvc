package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.interface21.web.bind.annotation.RequestMethod;

class ComponentScannerTest {

	@Test
	@DisplayName("@Controller 메소드에 대한 @RequestMapping 속성을 추출한다.")
	void scan() {
		Map<HandlerKey, HandlerExecution> scan = ComponentScanner.scan();

		assertThat(scan).hasSize(2);
		assertThat(scan.get(new HandlerKey("/get-test", RequestMethod.GET))).isNotNull();
		assertThat(scan.get(new HandlerKey("/post-test", RequestMethod.POST))).isNotNull();
	}
}