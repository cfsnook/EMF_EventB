/*******************************************************************************
 * Copyright (c) 2015 University of Southampton.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     University of Southampton - initial API and implementation
 *******************************************************************************/

package org.eventb.emf.persistence.tests;

import java.util.Iterator;

import org.eclipse.emf.common.util.EList;
import org.eventb.emf.core.context.Axiom;
import org.eventb.emf.core.context.CarrierSet;
import org.eventb.emf.core.context.Constant;
import org.eventb.emf.core.context.Context;
import org.eventb.emf.core.machine.Action;
import org.eventb.emf.core.machine.Event;
import org.eventb.emf.core.machine.Guard;
import org.eventb.emf.core.machine.Invariant;
import org.eventb.emf.core.machine.Machine;
import org.eventb.emf.core.machine.Parameter;
import org.eventb.emf.core.machine.Variable;
import org.eventb.emf.core.machine.Witness;
import org.eventb.emf.persistence.EMFRodinDB;
import org.junit.After;
import org.junit.Before;

import ch.ethz.eventb.utils.tests.AbstractEventBTests;

/**
 * <p>
 *
 * </p>
 *
 * @author htson
 * @version 0.1
 * @see
 * @since 0.1
 */
public abstract class AbstractEventBEMFTests extends AbstractEventBTests {

	public EMFRodinDB emfRodinDB;

	/**
	 * Constructor: Create a test case.
	 */
	public AbstractEventBEMFTests() {
		super();
	}

	/**
	 * Constructor: Create a test case with the given name.
	 * 
	 * @param name
	 *            the name of test
	 */
	public AbstractEventBEMFTests(String name) {
		super(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	@Override
	protected void setUp() throws Exception {
		super.setUp();

		emfRodinDB = new EMFRodinDB();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	@After
	@Override
	protected void tearDown() throws Exception {
		workspace.getRoot().delete(true, null);
		super.tearDown();
	}

	// =========================================================================
	// Utility methods for testing various Event-B EMF elements.
	// =========================================================================

	/**
	 * Utility method for testing EXTENDS clauses of a context.
	 * 
	 * @param message
	 *            a message for debugging.
	 * @param ctx
	 *            A context root whose EXTENDS clauses will be tested.
	 * @param expected
	 *            the array of expected EXTENDS clauses. Each clause is
	 *            represented by the abstract context name. The order of the
	 *            EXTENDS clause is important.
	 */
	protected void testContextExtendsClauses(String message, Context ctx,
			String... expected) {
		EList<String> extendsNames = ctx.getExtendsNames();
		assertSameStrings(message,
				extendsNames.toArray(new String[extendsNames.size()]), expected);
	}

	/**
	 * Utility method for testing the carrier sets of a context.
	 * 
	 * @param message
	 *            a message for debugging.
	 * @param ctx
	 *            a context whose carrier sets will be tested.
	 * @param expected
	 *            an array of expected carrier sets. Each carrier set is
	 *            represented by its identifier. The order of the carrier sets
	 *            is important.
	 */
	protected void testContextCarrierSets(String message, Context ctx,
			String... expected) {
		EList<CarrierSet> sets = ctx.getSets();
		assertEquals(message + ": Incorrect number of carrier sets",
				expected.length, sets.size());
		Iterator<CarrierSet> iterator = sets.iterator();
		for (int i = 0; i < expected.length; i++) {
			testCarrierSet(message, iterator.next(), expected[i]);
		}
	}

	/**
	 * Utility method for testing a carrier set.
	 * 
	 * @param message
	 *            a message for debugging.
	 * @param carrierSet
	 *            the carrier set under test.
	 * @param expected
	 *            the expected identifier of the carrier set.
	 */
	protected void testCarrierSet(String message, CarrierSet carrierSet,
			String expected) {
		assertEquals(message + ": Incorrect carrier set", expected,
				carrierSet.getName());
	}

	/**
	 * Utility method for testing the constants of a context.
	 * 
	 * @param message
	 *            a message for debugging.
	 * @param ctx
	 *            a context whose constants will be tested.
	 * @param expected
	 *            an array of expected constants. Each constant is represented
	 *            by its identifier. The order of the constants is important.
	 */
	protected void testContextConstants(String message, Context ctx,
			String... expected) {
		EList<Constant> csts = ctx.getConstants();
		assertEquals(message + ": Incorrect number of constants",
				expected.length, csts.size());
		Iterator<Constant> iterator = csts.iterator();
		for (int i = 0; i < expected.length; i++) {
			testConstant(message, iterator.next(), expected[i]);
		}
	}

	/**
	 * Utility method for testing a constant.
	 * 
	 * @param message
	 *            a message for debugging.
	 * @param set
	 *            the constant under test.
	 * @param expected
	 *            the expected identifier of the constant.
	 */
	protected void testConstant(String message, Constant constant,
			String expected) {
		assertEquals(message + ": Incorrect constant", expected,
				constant.getName());
	}

	/**
	 * Utility method for testing the axioms of a context.
	 * 
	 * @param message
	 *            a message for debugging.
	 * @param ctx
	 *            a context root whose axioms will be tested.
	 * @param expected
	 *            the expected pretty-print axioms. The axioms are
	 *            "pretty-printed" as follows:
	 *            "label:predicateString:isTheorem". The order of the axioms is
	 *            important.
	 */
	protected void testContextAxioms(String message, Context ctx,
			String... expected) {
		EList<Axiom> axms = ctx.getAxioms();
		assertEquals(message + ": Incorrect number of axioms", expected.length,
				axms.size());
		Iterator<Axiom> iterator = axms.iterator();
		for (int i = 0; i < expected.length; i++) {
			testAxiom(message, iterator.next(), expected[i]);
		}
	}

	/**
	 * Utility method for testing an axiom.
	 * 
	 * @param message
	 *            a message for debugging.
	 * @param axiom
	 *            the axiom under test.
	 * @param expected
	 *            the expected pretty print axiom. The axiom is "pretty-printed"
	 *            as follows: "label:predicateString:isTheorem".
	 */
	protected void testAxiom(String message, Axiom axiom, String expected) {
		assertEquals(message + ": Incorrect axiom", expected, axiom.getName()
				+ ":" + axiom.getPredicate() + ":" + axiom.isTheorem());
	}

	/**
	 * Utility method for testing the REFINES clauses of a machine.
	 * 
	 * @param message
	 *            a message for debugging.
	 * @param mch
	 *            a machine root whose REFINES clauses will be tested.
	 * @param expected
	 *            an array of expected REFINES clause. Each REFINES clause is
	 *            represented by its abstract machine name. The order of the
	 *            REFINES clauses is important.
	 */
	protected void testMachineRefinesClauses(String message, Machine mch,
			String... expected) {
		EList<String> refinesNames = mch.getRefinesNames();
		assertSameStrings(message,
				refinesNames.toArray(new String[refinesNames.size()]), expected);
	}

	/**
	 * Utility method for testing the SEES clauses of a machine.
	 * 
	 * @param message
	 *            a message for debugging.
	 * @param mch
	 *            a machine root whose SEES clauses will be tested.
	 * @param expected
	 *            an array of expected SEES clause. Each SEES clause is
	 *            represented by its seen context name. The order of the SEES
	 *            clauses is important.
	 */
	protected void testMachineSeesClauses(String message, Machine mch,
			String... expected) {
		EList<String> seesNames = mch.getSeesNames();
		assertSameStrings(message,
				seesNames.toArray(new String[seesNames.size()]), expected);
	}

	/**
	 * Utility method for testing the variables of a machine.
	 * 
	 * @param message
	 *            a message for debugging.
	 * @param mch
	 *            the machine root whose variables will be tested.
	 * @param expected
	 *            an array of expected variables. Each variable is represented
	 *            by its identifier. The order of the variables is important.
	 */
	protected void testMachineVariables(String message, Machine mch,
			String... expected) {
		EList<Variable> vars = mch.getVariables();
		assertEquals(message + ": Incorrect number of variables",
				expected.length, vars.size());
		Iterator<Variable> iterator = vars.iterator();
		for (int i = 0; i < expected.length; i++) {
			testVariable(message, iterator.next(), expected[i]);
		}
	}

	/**
	 * Utility method for testing the variables of a machine.
	 * 
	 * @param message
	 *            a message for debugging.
	 * @param mch
	 *            the machine root whose variables will be tested.
	 * @param expected
	 *            an array of expected variables. Each variable is represented
	 *            by its identifier. The order of the variables is NOT
	 *            important.
	 */
	protected void testMachineVariablesUnordered(String message, Machine mch,
			String... expected) {
		EList<Variable> vars = mch.getVariables();
		assertEquals(message + ": Incorrect number of variables",
				expected.length, vars.size());
		for (int i = 0; i < expected.length; i++) {
			boolean b = false;
			for (Iterator<Variable> iterator = vars.iterator(); iterator
					.hasNext();) {
				Variable var = iterator.next();
				if (var.getName().equals(expected[i])) {
					b = true;
					break;
				}
			}
			if (!b) {
				fail("Variable " + expected[i] + " cannot be found");
			}
		}
	}

	/**
	 * Utility method for testing a variable.
	 * 
	 * @param message
	 *            a message for debugging.
	 * @param variable
	 *            the variable under test.
	 * @param expected
	 *            the expected identifier of the variable.
	 */
	protected void testVariable(String message, Variable variable,
			String expected) {
		assertEquals(message + ": Incorrect variable", expected,
				variable.getName());
	}

	/**
	 * Utility method for testing the invariants of a context.
	 * 
	 * @param message
	 *            a message for debugging.
	 * @param mch
	 *            a context root whose invariants will be tested.
	 * @param expected
	 *            the expected pretty-print invariants. The invariants are
	 *            "pretty-printed" as follows:
	 *            "label:predicateString:isTheorem". The order of the invariants
	 *            is important.
	 */
	protected void testMachineInvariants(String message, Machine mch,
			String... expected) {
		EList<Invariant> invs = mch.getInvariants();
		assertEquals(message + ": Incorrect number of invariants",
				expected.length, invs.size());
		Iterator<Invariant> iterator = invs.iterator();
		for (int i = 0; i < expected.length; i++) {
			testInvariant(message, iterator.next(), expected[i]);
		}
	}

	/**
	 * Utility method for testing an invariant.
	 * 
	 * @param message
	 *            a message for debugging.
	 * @param invariant
	 *            the invariant under test.
	 * @param expected
	 *            the expected pretty-print invariant. The invariant is
	 *            "pretty-printed" as follows:
	 *            "label:predicateString:isTheorem".
	 */
	protected void testInvariant(String message, Invariant invariant,
			String expected) {
		assertEquals(message + ": Incorrect invariant", expected,
				invariant.getName() + ":" + invariant.getPredicate() + ":"
						+ invariant.isTheorem());
	}

	/**
	 * Utility method for testing the events of a machine.
	 * 
	 * @param message
	 *            a message for debugging.
	 * @param mch
	 *            a machine root whose events will be tested.
	 * @param expected
	 *            the expected pretty-print events (only the signature). The
	 *            events are "pretty-printed" as follows:
	 *            "label:convergent:isExtended". The order of the events is
	 *            important.
	 */
	protected void testMachineEvents(String message, Machine mch,
			String... expected) {
		EList<Event> evts = mch.getEvents();
		assertEquals(message + ": Incorrect number of events", expected.length,
				evts.size());
		Iterator<Event> iterator = evts.iterator();
		for (int i = 0; i < expected.length; i++) {
			testEvent(message, iterator.next(), expected[i]);
		}
	}

	/**
	 * Utility method for testing an event.
	 * 
	 * @param message
	 *            a message for debugging.
	 * @param event
	 *            the event under test.
	 * @param expected
	 *            the expected pretty-print event (only the signature). The
	 *            event is "pretty-printed" as follows:
	 *            "label:convergent:isExtended".
	 */
	protected void testEvent(String message, Event event, String expected) {
		assertNotNull(message + ": The event must not be null", event);
		assertEquals(message + ": Incorrect event", expected, event.getName()
				+ ":" + event.getConvergence() + ":" + event.isExtended());
	}

	/**
	 * Utility method for testing the REFINES clauses of an event.
	 * 
	 * @param message
	 *            a message for debugging.
	 * @param mch
	 *            an event whose REFINES clauses will be tested.
	 * @param expected
	 *            an array of expected REFINES clause. Each REFINES clause is
	 *            represented by its abstract event name. The order of the
	 *            REFINES clauses is important.
	 */
	protected void testEventRefinesClauses(String message, Event evt,
			String... expected) {
		EList<String> refinesNames = evt.getRefinesNames();
		assertSameStrings(message,
				refinesNames.toArray(new String[refinesNames.size()]), expected);
	}

	/**
	 * Utility method for testing the parameters of an event.
	 * 
	 * @param message
	 *            a message for debugging.
	 * @param evt
	 *            an event whose parameters will be tested.
	 * @param expected
	 *            the expected set of parameters. Each parameter is represented
	 *            by its identifier. The order of the parameters is important.
	 */
	protected void testEventParameters(String message, Event evt,
			String... expected) {
		EList<Parameter> pars = evt.getParameters();
		assertEquals(message + ": Incorrect number of parameters",
				expected.length, pars.size());
		Iterator<Parameter> iterator = pars.iterator();
		for (int i = 0; i < expected.length; i++) {
			testParameter(message, iterator.next(), expected[i]);
		}
	}

	/**
	 * Utility method for testing a parameter.
	 * 
	 * @param message
	 *            a message for debugging.
	 * @param parameter
	 *            the parameter under test.
	 * @param expected
	 *            the expected parameter identifier.
	 */
	protected void testParameter(String message, Parameter parameter,
			String expected) {
		assertEquals(message + ": Incorrect parameter", expected,
				parameter.getName());
	}

	/**
	 * Utility method for testing the guards of an event.
	 * 
	 * @param message
	 *            a message for debugging.
	 * @param evt
	 *            an event whose guards will be tested.
	 * @param expected
	 *            the expected pretty-print guards. The guards are
	 *            "pretty-printed" as follows:
	 *            "label:predicateString:isTheorem". The order of the guards is
	 *            important.
	 */
	protected void testEventGuards(String message, Event evt,
			String... expected) {
		EList<Guard> grds = evt.getGuards();
		assertEquals(message + ": Incorrect number of guards", expected.length,
				grds.size());
		Iterator<Guard> iterator = grds.iterator();
		for (int i = 0; i < expected.length; i++) {
			testGuard(message, iterator.next(), expected[i]);
		}
	}

	/**
	 * Utility method for testing a guard.
	 * 
	 * @param message
	 *            a message for debugging.
	 * @param guard
	 *            the guard under test.
	 * @param expected
	 *            the expected pretty-print guard. The guard is "pretty-printed"
	 *            as follows: "label:predicateString:isTheorem".
	 */
	protected void testGuard(String message, Guard guard, String expected) {
		assertEquals(message + ": Incorrect guard", expected, guard.getName()
				+ ":" + guard.getPredicate() + ":" + guard.isTheorem());
	}

	/**
	 * Utility method for testing the witnesses of an event.
	 * 
	 * @param message
	 *            a message for debugging.
	 * @param evt
	 *            an event whose witnesses will be tested.
	 * @param expected
	 *            the expected pretty-print witnesses. The witnesses are
	 *            "pretty-printed" as follows: "label:predicateString". The
	 *            order of the witnesses is important.
	 */
	protected void testEventWitnesses(String message, Event evt,
			String... expected) {
		EList<Witness> wits = evt.getWitnesses();
		assertEquals(message + ": Incorrect number of witnesses",
				expected.length, wits.size());
		Iterator<Witness> iterator = wits.iterator();
		for (int i = 0; i < expected.length; i++) {
			testWitness(message, iterator.next(), expected[i]);
		}
	}

	/**
	 * Utility method for testing an witness.
	 * 
	 * @param message
	 *            a message for debugging.
	 * @param witness
	 *            the witness under test.
	 * @param expected
	 *            the expected pretty-print witness. The witness is
	 *            "pretty-printed" as follows: "label:predicateString".
	 */
	protected void testWitness(String message, Witness witness, String expected) {
		assertEquals(message + ": Incorrect witness", expected,
				witness.getName() + ":" + witness.getPredicate());
	}

	/**
	 * Utility method for testing the actions of an event.
	 * 
	 * @param message
	 *            a message for debugging.
	 * @param evt
	 *            an event whose actions will be tested.
	 * @param expected
	 *            expected pretty-print actions. The actions are
	 *            "pretty-printed" as follows: "label:assignmentString". The
	 *            order of the actions is important.
	 */
	protected void testEventActions(String message, Event evt,
			String... expected) {
		EList<Action> acts = evt.getActions();
		assertEquals(message + ": Incorrect number of actions",
				expected.length, acts.size());
		Iterator<Action> iterator = acts.iterator();
		for (int i = 0; i < expected.length; i++) {
			testAction(message, iterator.next(), expected[i]);
		}
	}

	/**
	 * Utility method for testing an action.
	 * 
	 * @param message
	 *            a message
	 * @param action
	 *            the action under test
	 * @param expected
	 *            expected pretty-print action. The action is "pretty-printed"
	 *            as follows: "label:assignmentString".
	 */
	protected void testAction(String message, Action action, String expected) {
		assertEquals(message + ": Incorrect action", expected, action.getName()
				+ ":" + action.getAction());
	}

}