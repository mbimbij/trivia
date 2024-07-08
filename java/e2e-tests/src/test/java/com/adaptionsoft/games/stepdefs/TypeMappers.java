package com.adaptionsoft.games.stepdefs;

import com.adaptionsoft.games.domain.views.DisplayedGame;
import com.adaptionsoft.games.trivia.domain.AnswerCode;
import com.adaptionsoft.games.utils.TestUtils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.DataTableType;
import io.cucumber.java.ParameterType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class TypeMappers {

    @DataTableType
    public Collection<DisplayedGame> displayedGames(DataTable dataTable) {
        return TestUtils.convertDatatableList(dataTable, DisplayedGame.class);
    }

    @ParameterType(
            value = ".+",
            name = "strings")
    public Collection<String> strings(String string) {
        return Arrays.stream(string.trim().split("\\s*,\\s*")).collect(Collectors.toCollection(ArrayList::new));
    }

    @ParameterType("(A|B|C|D)")
    public AnswerCode answerCode(String stringValue) {
        return AnswerCode.valueOf(stringValue);
    }
}
