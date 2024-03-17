package RegularExpresions;

import AutomatonElements.Alphabet;
import AutomatonElements.NondeterministicTransitionFunction;
import AutomatonElements.State;
import Automatons.NondeterministicFiniteAutomaton;

import java.util.HashSet;
import java.util.Set;

public class RegexEmptyChar implements RegularExpresion {
    public RegexEmptyChar(){

    }

    public NondeterministicFiniteAutomaton getNfa() {
        State s = new State();

        Set<State> states = new HashSet<>();
        states.add(s);

        Alphabet alp = new Alphabet();

        Set<State> finalStates = new HashSet<>();
        finalStates.add(s);

        NondeterministicTransitionFunction tf =  new NondeterministicTransitionFunction();

        return new NondeterministicFiniteAutomaton(states, alp, s, finalStates, tf);
    }
}
