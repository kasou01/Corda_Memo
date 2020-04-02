package com.template.contracts;

import com.template.states.IOUState;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.CommandWithParties;
import net.corda.core.contracts.Contract;
import net.corda.core.identity.Party;
import net.corda.core.transactions.LedgerTransaction;

import java.security.PublicKey;
import java.util.Arrays;
import java.util.List;

import static net.corda.core.contracts.ContractsDSL.requireSingleCommand;

// ************
// * Contract *
// ************
public class IOUContract implements Contract {
    public static final String ID = "com.template.contracts.IOUContract";

    // Our Create command.
    public static class Create implements CommandData {
    }
    // Our Redeem command.
    public static class Redeem implements CommandData {
    }
    // Our Move command.
    public static class Move implements CommandData {
    }

    @Override
    public void verify(LedgerTransaction tx) {
        List<CommandWithParties<CommandData>> commands = tx.getCommands();
        if(commands.size() != 1){
            throw new IllegalArgumentException(" There should be one command");
        }
        CommandData commandType = commands.get(0).getValue();
        if(commandType instanceof IOUContract.Create ){
            if (!tx.getInputs().isEmpty())
                throw new IllegalArgumentException("No inputs should be consumed when issuing an IOU.");
            if (!(tx.getOutputs().size() == 1))
                throw new IllegalArgumentException("TEST 3: There should be one output state of type IOUState.");

            // IOU-specific constraints.
            final IOUState output = tx.outputsOfType(IOUState.class).get(0);
            final Party lender = output.getLender();
            final Party borrower = output.getBorrower();
            int value = 110;
            if (output.getValue() == value)
                throw new IllegalArgumentException("The IOU's value must be " + value);
            if (lender.equals(borrower))
                throw new IllegalArgumentException("The lender and the borrower cannot be the same entity.");

            // Constraints on the signers.
            final List<PublicKey> requiredSigners = commands.get(0).getSigners();
            final List<PublicKey> expectedSigners = Arrays.asList(borrower.getOwningKey(), lender.getOwningKey());
            if (requiredSigners.size() != 2)
                throw new IllegalArgumentException("There must be two signers.");
            if (!(requiredSigners.containsAll(expectedSigners)))
                throw new IllegalArgumentException("The borrower and lender must be signers.");
        }else if(commandType instanceof  IOUContract.Redeem){

        }else if(commandType instanceof  IOUContract.Move) {

        }else{
            throw new IllegalArgumentException("Unknown Command");
        }
    }
}