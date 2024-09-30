package com.interface21.webmvc.servlet.mvc.tobe.initializer;

import com.interface21.webmvc.servlet.mvc.tobe.adapter.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.handlermapping.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.pathfinder.RootPathStrategy;
import com.interface21.webmvc.servlet.mvc.tobe.pathfinder.TestRootPathStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

class HandlerInitializerTest {

    private final RootPathStrategy rootPathStrategy = new TestRootPathStrategy();

    @Test
    @DisplayName("Mappings를 초기화하여 처음 객체들을 가져온다.")
    void getInitMappings() {
        Set<HandlerMapping> handlerMappings = new HandlerInitializer(rootPathStrategy).getInitMappings();

        Assertions.assertNotNull(handlerMappings);
    }

    @Test
    @DisplayName("Adapters 초기화하여 처음 객체들을 가져온다.")
    void getInitAdapters() {
        Set<HandlerAdapter> handlerAdapters = new HandlerInitializer(rootPathStrategy).getInitAdapters();

        Assertions.assertNotNull(handlerAdapters);
    }
}
