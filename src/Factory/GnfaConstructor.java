package Factory;

import AutomatonElements.Alphabet;
import AutomatonElements.Gntf;
import AutomatonElements.State;

import Automatons.Gnfa;

import java.util.HashSet;
import java.util.Set;

public class GnfaConstructor {
    public Set<State> states;
    public Alphabet alphabet;
    public State start;
    public State accept;
    public Gntf transition;

    public GnfaConstructor(){
        states = new HashSet<>();
        alphabet = new Alphabet();
        start = null;
        accept = null;
        transition = new Gntf();
    }

    public GnfaConstructor(Set<State> states, Alphabet alphabet, State start, State accept, Gntf transition){
        this.states = new HashSet<>();
        this.states.addAll(states);

        this.alphabet = new Alphabet();
        this.alphabet.addAll(alphabet);

        this.start = start;
        this.accept = accept;

        this.transition = new Gntf();
        this.transition.addRules(transition.getRules());
    }

    public Gnfa toGnfa(){
        return new Gnfa(states, alphabet, start, accept, transition);
    }
}
