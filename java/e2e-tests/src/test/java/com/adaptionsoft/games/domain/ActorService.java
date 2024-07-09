package com.adaptionsoft.games.domain;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ActorService {

    private final Map<String, Actor> actorsByName = new HashMap<>();

    public ActorService(TestProperties testProperties, FrontendActor qaFrontendActor) {
        actorsByName.put(TestContext.QA_FRONTEND_USER_NAME, qaFrontendActor);
        actorsByName.put(TestContext.QA_BACKEND_LOOKUP_NAME, new Actor(testProperties.getQaUserId(), TestContext.QA_FRONTEND_USER_NAME));
        actorsByName.put(TestContext.TEST_USER_NAME_1, new Actor(TestContext.TEST_USER_ID_1, TestContext.TEST_USER_NAME_1));
        actorsByName.put(TestContext.TEST_USER_NAME_2, new Actor(TestContext.TEST_USER_ID_2, TestContext.TEST_USER_NAME_2));
    }

    public Actor getActorByLookupName(String userName) {
        assertThat(actorsByName).containsKey(userName);
        return actorsByName.get(userName);
    }

    public Actor getBackendActor1() {
        return getActorByLookupName(TestContext.TEST_USER_NAME_1);
    }

    public Actor getBackendActor2() {
        return getActorByLookupName(TestContext.TEST_USER_NAME_2);
    }

    public Actor getQaBackendActor() {
    return getActorByLookupName(TestContext.QA_BACKEND_LOOKUP_NAME);
    }
}
