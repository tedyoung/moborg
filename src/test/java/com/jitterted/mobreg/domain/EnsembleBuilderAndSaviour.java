package com.jitterted.mobreg.domain;

import com.jitterted.mobreg.application.TestMemberBuilder;

public class EnsembleBuilderAndSaviour {

    private Ensemble ensemble;

    public EnsembleBuilderAndSaviour() {
        ensemble = EnsembleFactory.withStartTimeNow();
    }

    public EnsembleBuilderAndSaviour accept(TestMemberBuilder memberBuilder) {
        ensemble.acceptedBy(memberBuilder.buildAndSave().getId());
        return this;
    }

    public EnsembleBuilderAndSaviour decline(TestMemberBuilder memberBuilder) {
        ensemble.declinedBy(memberBuilder.buildAndSave().getId());
        return this;
    }

    public Ensemble build() {
        Ensemble ensembleToReturn = ensemble;
        ensemble = EnsembleFactory.withStartTimeNow();
        return ensembleToReturn;
    }

    public EnsembleBuilderAndSaviour named(String name) {
        ensemble.changeNameTo(name);
        return this;
    }
}
