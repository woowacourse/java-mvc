package nextstep.mvc.view;

import java.util.Objects;

public class ViewName {

    public static final ViewName EMPTY = new ViewName(null);

    private final String name;

    public static ViewName of(String name) {
        return new ViewName(name);
    }

    private ViewName(String name) {
        this.name = name;
    }

    public String value() {
        if (this.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return name;
    }

    public boolean isEmpty() {
        return this.equals(EMPTY);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ViewName viewName = (ViewName) o;
        return Objects.equals(name, viewName.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
