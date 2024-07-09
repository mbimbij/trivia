package com.adaptionsoft.games.stepdefs;

import com.adaptionsoft.games.domain.Actor;
import com.adaptionsoft.games.domain.ActorService;
import com.adaptionsoft.games.domain.views.DisplayedGame;
import com.adaptionsoft.games.trivia.domain.AnswerCode;
import com.adaptionsoft.games.utils.TestUtils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.DataTableType;
import io.cucumber.java.ParameterType;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TypeMappers {

    private final ActorService actorService;

    @DataTableType
    public Collection<DisplayedGame> displayedGames(DataTable dataTable) {
        return TestUtils.convertDatatableList(dataTable, DisplayedGame.class);
    }

    @ParameterType(value = ".+")
    public Collection<String> strings(String strings) {
        return Arrays.stream(strings.trim().split("\\s*,\\s*")).collect(Collectors.toCollection(ArrayList::new));
    }

    @ParameterType(value = ".+")
    public Actor actor(String actorName) {
        return actorService.getActorByLookupName(actorName);
    }

    @ParameterType("(A|B|C|D)")
    public AnswerCode answerCode(String stringValue) {
        return AnswerCode.valueOf(stringValue);
    }
}
