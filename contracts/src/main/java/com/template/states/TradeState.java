package com.template.states;

import com.template.contracts.TemplateContract;
import com.template.contracts.TradeContract;
import net.corda.core.contracts.Amount;
import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.LinearState;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.identity.AbstractParty;
import net.corda.core.serialization.CordaSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@BelongsToContract(TradeContract.class)
@CordaSerializable
public class TradeState implements LinearState {
    private UniqueIdentifier uniqueIdentifier;
    private List<AbstractParty> participants;

    private long tradeDate;
    private String source;
    private String destination;
    private String command;
    private Amount amount;
    private String isin;

    public TradeState(UniqueIdentifier uniqueIdentifier, List<AbstractParty> participants, long tradeDate, String source, String destination, String command, Amount amount, String isin) {
        this.uniqueIdentifier = uniqueIdentifier;
        this.participants = participants;
        this.tradeDate = tradeDate;
        this.source = source;
        this.destination = destination;
        this.command = command;
        this.amount = amount;
        this.isin = isin;
    }

    public long getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(long tradeDate) {
        this.tradeDate = tradeDate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public String getIsin() {
        return isin;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    public void setUniqueIdentifier(UniqueIdentifier uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }

    public void setParticipants(List<AbstractParty> participants) {
        this.participants = participants;
    }

    @NotNull
    @Override
    public UniqueIdentifier getLinearId() {
        return this.uniqueIdentifier;
    }

    @NotNull
    @Override
    public List<AbstractParty> getParticipants() {
        return this.participants;
    }
}
