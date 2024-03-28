package com.adaptionsoft.games.trivia.web;

import com.adaptionsoft.games.trivia.domain.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class TriviaController {

    @Autowired
    private GameRepository gameRepository;

    @GetMapping("/game")
    public Collection<GameDto> listGames() {
        return gameRepository.list().stream().map(GameDto::from).toList();
    }
}
