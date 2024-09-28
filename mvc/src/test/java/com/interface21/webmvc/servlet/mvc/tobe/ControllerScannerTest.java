package com.interface21.webmvc.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import samples.TestController;

class ControllerScannerTest {

	@DisplayName("주어진 폴더에 존재하는 컨트롤러를 찾아서 인스턴스 생성한다.")
	@Test
	void instantiate() {
		// given
		String basePackage = "samples";

		// when
		ControllerScanner controllerScanner = new ControllerScanner(basePackage);
		Map<Class<?>, Object> controllers = controllerScanner.getControllers();

		// then
		assertThat(controllers)
			.isNotEmpty()
			.hasEntrySatisfying(TestController.class, controller -> {
				assertThat(controller).isInstanceOf(TestController.class);
			});
	}
}
