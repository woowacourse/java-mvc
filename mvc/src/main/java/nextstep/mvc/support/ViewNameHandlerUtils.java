package nextstep.mvc.support;

public class ViewNameHandlerUtils {

    private ViewNameHandlerUtils() {
    }

    public static String getExtension(String fileName) {
        int extensionIndex = fileName.lastIndexOf(".");
        if (extensionIndex < 0 || extensionIndex >= fileName.length() - 1) {
            return null;
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
