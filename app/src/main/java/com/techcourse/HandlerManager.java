package com.techcourse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import com.techcourse.controller.NotFoundViewController;

public class HandlerManager {

    private final List<HandlerMapping> mappings;

    public HandlerManager(HandlerMapping... handlerMappings) {
        this.mappings = List.of(handlerMappings);
    }

    public void initialize() {
        mappings.forEach(HandlerMapping::initialize);
    }

    public void handle(final HttpServletRequest request, final HttpServletResponse response) {
        final Optional<HandlerMapping> handlerMapping = mappings.stream()
                .filter(mapping-> mapping.hasHandler(request))
                .findFirst();

        if (handlerMapping.isEmpty()) {
            renderNotFoundPage(request, response);
            return;
        }
        handlerMapping.get().handle(request, response);
    }

    private void renderNotFoundPage(final HttpServletRequest request, final HttpServletResponse response) {
        final NotFoundViewController controller = new NotFoundViewController();
        final String viewName = controller.execute(request, response);

        final var requestDispatcher = request.getRequestDispatcher(viewName);
        try {
            requestDispatcher.forward(request, response);
        } catch (final ServletException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
