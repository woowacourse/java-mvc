package com.interface21.webmvc.servlet;

public record Handler(
        Object instance,
        HandlerType type
) {
}
