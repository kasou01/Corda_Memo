package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;
import com.template.states.IOUState;
import net.corda.core.contracts.ContractState;
import net.corda.core.crypto.SecureHash;
import net.corda.core.flows.*;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.utilities.UntrustworthyData;

import static net.corda.core.contracts.ContractsDSL.requireThat;

@InitiatedBy(NotaryHealthCheck.class)
public class NotaryHealthCheckResponder extends FlowLogic<Void> {
    private final FlowSession otherPartySession;

    public NotaryHealthCheckResponder(FlowSession otherPartySession) {
        this.otherPartySession = otherPartySession;
    }

    @Suspendable
    @Override
    public Void call() throws FlowException {
        UntrustworthyData<String> receive = otherPartySession.receive(String.class,true);
        String unwrap = receive.unwrap(x -> x);
        if(unwrap.equals("1")){
            otherPartySession.send(Boolean.TRUE,true);
        }
        return null;
    }
}