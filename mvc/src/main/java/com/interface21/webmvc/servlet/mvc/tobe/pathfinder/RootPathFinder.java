package com.interface21.webmvc.servlet.mvc.tobe.pathfinder;

public class RootPathFinder {

    private final RootPathStrategy rootPathStrategy;

    public RootPathFinder(RootPathStrategy rootPathStrategy) {
        this.rootPathStrategy = rootPathStrategy;
    }

    public String find() {
        return rootPathStrategy.search();
    }
}
