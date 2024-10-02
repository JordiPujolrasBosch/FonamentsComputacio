package Automatons;

import Elements.Alphabet;
import Elements.Transitions.Ntf;
import Elements.State;
import Factory.Algorithms;
import Factory.Constructors.NfaConstructor;
import Factory.Printer;

import java.util.Set;

/**
 * Nondeterministic finite automaton. A nfa is used to transform a regex to a dfa and to transform a dfa to a regex.
 */

public class Nfa {
    private final Set<State> states;
    private final Alphabet alphabet;
    private final State start;
    private final Set<State> finalStates;
    private final Ntf transition;

    public Nfa(Set<State> states, Alphabet alphabet, State start, Set<State> finalStates, Ntf transition){
        this.states = states;
        this.alphabet = alphabet;
        this.start = start;
        this.finalStates = finalStates;
        this.transition = transition;
    }

    /**
     * @return A constructor of this nfa.
     */
    public NfaConstructor getConstructor() {
        return new NfaConstructor(states, alphabet, start, finalStates, transition);
    }

    /**
     * Transforms the nfa to a dfa.
     * @return A dfa that is equivalent to this.
     */
    public Dfa toDfa(){
        return Algorithms.nfaToDfa(this);
    }

    /**
     * Transforms the nfa to a gnfa.
     * @return A gnfa that is equivalent to this.
     */
    public Gnfa toGnfa(){
        return Algorithms.nfaToGnfa(this);
    }

    /**
     * @return A string that represents this nfa in the defined format.
     */
    @Override
    public String toString(){
        return Printer.stringOfNfa(this);
    }

}
