package com.adaptionsoft.games.trivia.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ws-monitoring")
public class MonitoringController {
    private final SimpUserRegistry simpUserRegistry;

    @GetMapping("/users")
    public Set<SimpUser> listUsers() {
        return simpUserRegistry.getUsers();
    }
}
