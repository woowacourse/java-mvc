package nextstep.context.post_process_test_case;

import nextstep.web.annotation.Service;
import nextstep.web.annotation.Toransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TestTargetService {

    private static final Logger log = LoggerFactory.getLogger(TestTargetService.class);

    @Toransactional
    public void somethingDoWithTransactional() {
        log.info("TestTargetService somethingDo");
    }

    public void somethingDoWithNoTransactional() {
        log.info("TestTargetService somethingDo With No Tx");
    }

    private void somethingDoInternal() {
        log.info("TestTargetService somethingDoInternal");
    }
}
