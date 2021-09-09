package com.techcourse.air;

import com.techcourse.air.core.Application;

public class JwpApplication {

    public static void main(String[] args) throws Exception {
        String[] packages = new String[]{"com.techcourse.air"};
        Application.run(args, packages);
    }
}
