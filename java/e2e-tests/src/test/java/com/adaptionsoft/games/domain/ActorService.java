package com.adaptionsoft.games.domain;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ActorService {

    private final Map<String, Actor> actorsByName = new HashMap<>();

    public ActorService(TestProperties testProperties) {
        actorsByName.put(TestProperties.QA_FRONTEND_USER_NAME, new Actor(testProperties.getQaUserId(), TestProperties.QA_FRONTEND_USER_NAME));
        actorsByName.put(TestProperties.QA_BACKEND_LOOKUP_NAME, new Actor(testProperties.getQaUserId(), TestProperties.QA_FRONTEND_USER_NAME));
        actorsByName.put(TestProperties.TEST_USER_NAME_1, new Actor(TestProperties.TEST_USER_ID_1, TestProperties.TEST_USER_NAME_1));
        actorsByName.put(TestProperties.TEST_USER_NAME_2, new Actor(TestProperties.TEST_USER_ID_2, TestProperties.TEST_USER_NAME_2));
    }

    public Actor getActorByLookupName(String userName) {
        assertThat(actorsByName).containsKey(userName);
        return actorsByName.get(userName);
    }

    public Actor getBackendActor1() {
        return getActorByLookupName(TestProperties.TEST_USER_NAME_1);
    }

    public Actor getBackendActor2() {
        return getActorByLookupName(TestProperties.TEST_USER_NAME_2);
    }

    public Actor getQaBackendActor() {
    return getActorByLookupName(TestProperties.QA_BACKEND_LOOKUP_NAME);
    }

    public Actor getQaFrontendActor() {
    return getActorByLookupName(TestProperties.QA_FRONTEND_USER_NAME);
    }
}
