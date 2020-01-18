package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;
import com.google.common.collect.ImmutableList;
import com.template.contracts.IOUContract;
import com.template.contracts.TradeContract;
import com.template.states.IOUState;
import com.template.states.TradeState;
import net.corda.core.contracts.Command;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.node.ServiceHub;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;

import java.security.PublicKey;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@InitiatingFlow
@StartableByRPC
public class InsertTradeStateFlow extends FlowLogic<Void> {
    private final List<TradeState> state;
    public InsertTradeStateFlow(List<TradeState> state) {
        this.state = state;
    }
    @Suspendable
    @Override
    public Void call() throws FlowException {
        // We retrieve the notary identity from the network map.
        Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);
        Party ourIdentity = getOurIdentity();
        ServiceHub serviceHub = getServiceHub();
        for(TradeState s : state){
            s.setParticipants(ImmutableList.of(ourIdentity));
            s.setUniqueIdentifier(new UniqueIdentifier(UUID.randomUUID().toString()));
            List<PublicKey> requiredSigners = Arrays.asList(getOurIdentity().getOwningKey());
            Command command = new Command<>(new TradeContract.Commands.ISSUE(), requiredSigners);

            TransactionBuilder txBuilder = new TransactionBuilder(notary)
                    .addOutputState(s, TradeContract.ID)
                    .addCommand(command);
            txBuilder.verify(serviceHub);
            SignedTransaction signedTx = getServiceHub().signInitialTransaction(txBuilder);
            subFlow(new FinalityFlow(signedTx, ImmutableList.of()));
        };
        return null;
    }
}
