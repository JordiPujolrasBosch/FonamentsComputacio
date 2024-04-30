package Factory.Constructors;

import AutomatonElements.Alphabet;
import AutomatonElements.Dtf;
import AutomatonElements.State;

import Automatons.Dfa;

import java.util.Set;
import java.util.HashSet;

public class DfaConstructor {
    public Set<State> states;
    public Alphabet alphabet;
    public State start;
    public Set<State> finalStates;
    public Dtf transition;

    public DfaConstructor(){
        states = new HashSet<>();
        alphabet = new Alphabet();
        start = null;
        finalStates = new HashSet<>();
        transition = new Dtf();
    }

    public DfaConstructor(Set<State> states, Alphabet alphabet, State start, Set<State> finalStates, Dtf transition){
        this.states = new HashSet<>();
        this.states.addAll(states);

        this.alphabet = new Alphabet();
        this.alphabet.addAll(alphabet);

        this.start = start;

        this.finalStates = new HashSet<>();
        this.finalStates.addAll(finalStates);

        this.transition = new Dtf();
        this.transition.addRules(transition.getRules());
    }

    public Dfa getDfa(){
        return new Dfa(states, alphabet, start, finalStates, transition);
    }
}
