package samples;

public class DataExample {
    private final Long id;
    private final String name;

    public DataExample(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
