package hello.hellospring.web;

import hello.hellospring.common.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoggerService {

    private final ObjectProvider<Logger> loggerObjectProvider;

    public void logic(String msg) {
        Logger logger = loggerObjectProvider.getObject();
        logger.log(msg);
    }
}
