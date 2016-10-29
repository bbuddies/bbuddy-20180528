package com.odde.bbuddy.account.domain;

public class NameDuplicatedAccountPostActions implements AccountPostActions {
    @Override
    public AccountPostActions success(Runnable afterSuccess) {
        return this;
    }

    @Override
    public AccountPostActions failed(Runnable afterFailed) {
        return this;
    }

    @Override
    public AccountPostActions nameDuplicated(Runnable afterNameDuplicated) {
        afterNameDuplicated.run();
        return this;
    }
}
