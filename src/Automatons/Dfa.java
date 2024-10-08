package Automatons;

import Elements.Alphabet;
import Elements.Transitions.Dtf;
import Elements.State;
import Factory.Algorithms;
import Factory.Constructors.DfaConstructor;
import Factory.Printer;

import java.util.Set;

/**
 * Deterministic finite automaton. A dfa can check if a regular language contains a word.
 */

public class Dfa {
    private final Set<State> states;
    private final Set<State> finalStates;
    private final Alphabet alphabet;
    private final State start;
    private final Dtf transition;

    public Dfa(Set<State> states, Alphabet alphabet, State start, Set<State> finalStates, Dtf transition){
        this.states = states;
        this.alphabet = alphabet;
        this.start = start;
        this.finalStates = finalStates;
        this.transition = transition;
    }

    /**
     * @return A constructor of this dfa.
     */
    public DfaConstructor getConstructor() {
        return new DfaConstructor(states, alphabet, start, finalStates, transition);
    }

    /**
     * Checks if the automaton can accept a word.
     * @param word A word that can or cannot be part of the language represented by the automaton.
     * @return True if it accepts the word. False otherwise.
     */
    public boolean checkWord(String word){
        return Algorithms.checkWordDfa(this, word);
    }

    /**
     * Checks if two dfas are equivalent. Two dfas are equivalent if they can accept the same words. This and b must be minimized.
     * @param b A dfa to compare with this. Must be a minimized dfa.
     * @return True if this and b are equivalent. False otherwise.
     */
    public boolean compare(Dfa b){
        return Algorithms.equalsDfas(this, b);
    }

    /**
     * Transforms the dfa to a nfa.
     * @return A nfa that is equivalent to this.
     */
    public Nfa toNfa(){
        return Algorithms.dfaToNfa(this);
    }

    /**
     * Minimizes a dfa. Reduces the number of states at minimum without changing the language.
     * @return A dfa that is equivalent to this but minimized.
     */
    public Dfa minimize(){
        return Algorithms.minimize(this);
    }

    /**
     * Build a dfa which language is the complement of this. The result dfa accepts the words that aren't accepted by this.
     * @return A dfa that represents the complement of this.
     */
    public Dfa complement(){
        return Algorithms.complement(this);
    }

    /**
     * Build a nfa which language is the reverse of this. The result nfa accepts the words of the original language but reversed.
     * @return A nfa that represents the reverse of this.
     */
    public Nfa reverse(){
        return Algorithms.reverse(this);
    }

    /**
     * @return A string that represents this dfa in the defined format.
     */
    @Override
    public String toString(){
        return Printer.stringOfDfa(this);
    }
}
