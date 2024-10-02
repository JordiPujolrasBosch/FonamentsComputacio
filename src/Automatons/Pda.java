package Automatons;

import Elements.Alphabet;
import Elements.Transitions.Pdtf;
import Elements.State;
import Elements.Grammars.Gvar;
import Factory.Algorithms;
import Factory.Constructors.PdaConstructor;

import java.util.Set;
import java.util.Map;

/**
 * Pushdown automaton. A pda can check if a context free language contains a word.
 */

public class Pda {
    private final Set<State> states;
    private final Alphabet alphabet;
    private final State start;
    private final Set<State> finalStates;
    private final Pdtf transition;

    private final Map<Character, Integer> mapperChar;
    private final Map<Gvar, Integer> mapperVar;

    public Pda(Set<State> states, Alphabet alphabet, State start, Set<State> finalStates,
               Pdtf transition, Map<Character,Integer> mapperChar, Map<Gvar,Integer> mapperVar) {
        this.states = states;
        this.alphabet = alphabet;
        this.start = start;
        this.finalStates = finalStates;
        this.transition = transition;

        this.mapperChar = mapperChar;
        this.mapperVar = mapperVar;
    }

    /**
     * Checks if the automaton can accept a word.
     * @param word A word that can or cannot be part of the language represented by the automaton.
     * @return True if it accepts the word. False otherwise.
     */
    public boolean checkWord(String word){
        return Algorithms.checkWordPda(this, word);
    }

    /**
     * @return A constructor of this pda.
     */
    public PdaConstructor getConstructor() {
        return new PdaConstructor(states, alphabet, start, finalStates, transition, mapperChar, mapperVar);
    }
}
