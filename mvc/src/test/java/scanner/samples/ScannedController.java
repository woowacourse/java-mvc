package scanner.samples;

import com.interface21.context.stereotype.Controller;

@Controller
public class ScannedController {

    public static int constructionCount;

    public ScannedController() {
        constructionCount++;
    }

    public String message() {
        return "scanned";
    }
}
