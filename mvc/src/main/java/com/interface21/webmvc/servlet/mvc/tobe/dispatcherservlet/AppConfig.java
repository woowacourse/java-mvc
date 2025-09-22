package com.interface21.webmvc.servlet.mvc.tobe.dispatcherservlet;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import java.util.Map;

public interface AppConfig {

    Map<String, Controller> getManualControllers();

    String getControllerBasePackage();
}
