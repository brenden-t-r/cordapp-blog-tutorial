package com.template;

import com.google.common.collect.ImmutableList;
import net.corda.core.identity.CordaX500Name;
import net.corda.testing.core.TestIdentity;
import net.corda.testing.node.MockServices;
import org.junit.Test;

import static com.template.TemplateContract.TEMPLATE_CONTRACT_ID;
import static net.corda.testing.node.NodeTestUtils.ledger;

public class ContractTests {
    MockServices ledgerServices = new MockServices();
    static private TestIdentity sandra = new TestIdentity(
            new CordaX500Name("Sandra Bullocks", "London", "GB"));
    static private Integer BANK_ACCOUNT_NUMBER = 9999999;

    @Test
    public void ShouldHaveNoInputs() {
        ledger(ledgerServices, (ledger -> {
            ledger.transaction(tx -> {
                tx.input(TEMPLATE_CONTRACT_ID, new TemplateState(sandra.getParty(), BANK_ACCOUNT_NUMBER, 1000000));
                tx.command(ImmutableList.of(sandra.getPublicKey()), new TemplateContract.Commands.Action());
                tx.failsWith("There should be no input states");
                return null;
            });
            return null;
        }));
    }

    @Test
    public void ShouldNotHaveMoreThanOneOutput() {
        ledger(ledgerServices, (ledger -> {
            ledger.transaction(tx -> {
                tx.output(TEMPLATE_CONTRACT_ID, new TemplateState(sandra.getParty(), BANK_ACCOUNT_NUMBER, 1000000));
                tx.output(TEMPLATE_CONTRACT_ID, new TemplateState(sandra.getParty(), BANK_ACCOUNT_NUMBER, 3000000));
                tx.command(ImmutableList.of(sandra.getPublicKey()), new TemplateContract.Commands.Action());
                tx.failsWith("There should be exactly 1 output state.");
                return null;
            });
            return null;
        }));
    }

    /*@Test
    public void ShouldHaveOnlyOnePartySignature() {
        ledger(ledgerServices, (ledger -> {
            ledger.transaction(tx -> {
                tx.output(TEMPLATE_CONTRACT_ID, new TemplateState(sandra.getParty(), BANK_ACCOUNT_NUMBER, 3000000));
                TestIdentity otherParty = new TestIdentity(
                        new CordaX500Name("Edwardo Norton", "London", "GB"));
                tx.command(ImmutableList.of(sandra.getPublicKey(), otherParty.getPublicKey()), new TemplateContract.Commands.Action());
                tx.failsWith("There should be exactly 1 required signers.");
                return null;
            });
            return null;
        }));
    }

    @Test
    public void ShouldBeSignedByPartyReferencedInOutputState() {
        ledger(ledgerServices, (ledger -> {
            ledger.transaction(tx -> {
                tx.output(TEMPLATE_CONTRACT_ID, new TemplateState(sandra.getParty(), BANK_ACCOUNT_NUMBER, 3000000));
                TestIdentity otherParty = new TestIdentity(
                        new CordaX500Name("Edwardo Norton", "London", "GB"));
                tx.command(ImmutableList.of(otherParty.getPublicKey()), new TemplateContract.Commands.Action());
                tx.failsWith("Must be signed by party referenced in output state.");
                return null;
            });
            return null;
        }));
    }*/

    @Test
    public void ShouldHavePositiveNumberForAccountBalance() {
        ledger(ledgerServices, (ledger -> {
            ledger.transaction(tx -> {
                tx.output(TEMPLATE_CONTRACT_ID, new TemplateState(sandra.getParty(), BANK_ACCOUNT_NUMBER, -500));
                tx.command(ImmutableList.of(sandra.getPublicKey()), new TemplateContract.Commands.Action());
                tx.failsWith("Account Balance must be greater than zero.");
                return null;
            });
            return null;
        }));
    }

}