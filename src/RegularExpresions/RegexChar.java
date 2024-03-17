package RegularExpresions;

import AutomatonElements.Alphabet;
import AutomatonElements.NondeterministicTransitionFunction;
import AutomatonElements.State;
import Automatons.NondeterministicFiniteAutomaton;

import java.util.HashSet;
import java.util.Set;

public class RegexChar implements RegularExpresion {
    private final char c;

    public RegexChar(char c){
        this.c = c;
    }

    public NondeterministicFiniteAutomaton getNfa() {
        State start = new State();
        State finish = new State();

        Set<State> states = new HashSet<>();
        states.add(start);
        states.add(finish);

        Alphabet alp = new Alphabet();
        alp.add(c);

        Set<State> finalStates = new HashSet<>();
        finalStates.add(finish);

        NondeterministicTransitionFunction tf = new NondeterministicTransitionFunction();
        tf.add(start,finish,c);

        return new NondeterministicFiniteAutomaton(states, alp, start, finalStates, tf);
    }
}
