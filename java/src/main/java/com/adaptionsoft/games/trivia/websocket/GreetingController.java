package com.adaptionsoft.games.trivia.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class GreetingController {
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1001); // simulated delay
        String formattedName = HtmlUtils.htmlEscape(message.getName());
        return new Greeting("Hello, %s!".formatted(formattedName));
    }
}
