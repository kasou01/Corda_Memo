package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.utilities.UntrustworthyData;

@InitiatingFlow
@StartableByRPC
public class NotaryHealthCheck extends FlowLogic<Boolean> {
    @Override
    @Suspendable
    public Boolean call() throws FlowException {
        Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);
        FlowSession otherPartySession = initiateFlow(notary);
        UntrustworthyData<Boolean> booleanUntrustworthyData = otherPartySession.sendAndReceive(Boolean.class, "1",true);
        Boolean unwrap = booleanUntrustworthyData.unwrap(x -> x);
        return unwrap;
    }
}
