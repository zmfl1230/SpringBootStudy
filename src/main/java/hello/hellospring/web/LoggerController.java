package hello.hellospring.web;

import hello.hellospring.common.Logger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class LoggerController {

    private final LoggerService loggerService;
    private final ObjectProvider<Logger> loggerProvider;



    @GetMapping("log-demo")
    @ResponseBody
    public String logDemo(HttpServletRequest request) {
        Logger logger = loggerProvider.getObject();

        String requestURL = request.getRequestURL().toString();
        logger.setUrl(requestURL);

        logger.log("Controller - log demo" );
        loggerService.logic("Service - log demo");

        return "OK";
    }
}
