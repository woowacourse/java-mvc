import java.net.URL;

public class Resource {

    public URL get(String path) {
        return ClassLoader.getSystemClassLoader().getResource(path);
    }
}
