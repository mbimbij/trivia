package com.adaptionsoft.games.domain;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ActorService {

    private final Map<String, TestActor> actorsByName = new HashMap<>();

    public ActorService(FrontendActor qaFrontendActor, BackendActor qaBackendActor, BackendActor backendActor1, BackendActor backendActor2) {
        actorsByName.put(TestContext.QA_FRONTEND_USER_NAME, qaFrontendActor);
        actorsByName.put(TestContext.QA_BACKEND_USER_NAME, qaBackendActor);
        actorsByName.put(TestContext.TEST_USER_NAME_1, backendActor1);
        actorsByName.put(TestContext.TEST_USER_NAME_2, backendActor2);
    }

    public TestActor getActorByName(String userName) {
        assertThat(actorsByName).containsKey(userName);
        return actorsByName.get(userName);
    }
}
