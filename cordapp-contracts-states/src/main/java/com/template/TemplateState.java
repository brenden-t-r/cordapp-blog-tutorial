package com.template;

import com.google.common.collect.ImmutableList;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;

import java.util.Collections;
import java.util.List;

/**
 * Define your state object here.
 */
public class TemplateState implements ContractState {
    private final Party me;
    private final Integer bankAccountNumber;
    private final Integer bankAccountBalance;

    public TemplateState(Party me, Integer bankAccountNumber,  Integer bankAccountBalance) {
        this.me = me;
        this.bankAccountNumber = bankAccountNumber;
        this.bankAccountBalance = bankAccountBalance;
    }

    public Party getMe() {
        return me;
    }

    public Integer getBankAccountNumber() {
        return bankAccountNumber;
    }

    public Integer getBankAccountBalance() {
        return bankAccountBalance;
    }

    @Override
    public List<AbstractParty> getParticipants() {
        return ImmutableList.of(me);
    }
}