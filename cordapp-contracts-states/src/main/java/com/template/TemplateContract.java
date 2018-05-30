package com.template;

import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.CommandWithParties;
import net.corda.core.contracts.Contract;
import net.corda.core.transactions.LedgerTransaction;

import static net.corda.core.contracts.ContractsDSL.requireSingleCommand;
import static net.corda.core.contracts.ContractsDSL.requireThat;

/**
 * Define your contract here.
 */
public class TemplateContract implements Contract {
    // This is used to identify our contract when building a transaction.
    public static final String TEMPLATE_CONTRACT_ID = "com.template.TemplateContract";

    /**
     * A transaction is considered valid if the verify() function of the contract of each of the transaction's input
     * and output states does not throw an exception.
     */
    @Override
    public void verify(LedgerTransaction tx) {
        CommandWithParties<Commands> command = requireSingleCommand(tx.getCommands(), Commands.class);

        if (command.getValue() instanceof Commands.Action) {
            /*
             Contract verification rules
             */
            requireThat(require -> {
                require.using("There should be no input states", tx.getInputs().isEmpty());
                require.using("There should be exactly 1 output state.", tx.getOutputs().size() == 1);
                require.using("There should be exactly 1 required signers.", command.getSigners().size() == 1);
                TemplateState output = (TemplateState) tx.getOutputStates().get(0);
                require.using("Must be signed by party referenced in output state.", command.getSigners().contains(output.getMe().getOwningKey()));
                require.using("Account Balance must be greater than zero.", output.getBankAccountBalance() > 0);
                return null;
            });

        } else {
            throw new IllegalArgumentException("Unrecognized command.");
        }
    }

    public interface Commands extends CommandData {
        class Action implements Commands {}
    }
}