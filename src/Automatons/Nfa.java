package Automatons;

import AutomatonElements.Alphabet;
import AutomatonElements.Ntf;
import AutomatonElements.Rule;
import AutomatonElements.State;
import Factory.AutomatonFactory;
import Factory.NfaConstructor;

import java.util.*;

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

    public NfaConstructor getConstructor() {
        return new NfaConstructor(states, alphabet, start, finalStates, transition);
    }

    //Step

    public Set<State> stepWithEmptyChar(State o, Character c){
        Set<State> res = new HashSet<>();
        for(State s : transition.step(o,c)) res.addAll(transition.stateExtended(s));
        return res;
    }

    public Set<State> stateExtended(State x) {
        return transition.stateExtended(x);
    }

    public Dfa toDfa(){
        return AutomatonFactory.nfaToDfa(this);
    }

    public Gnfa toGnfa(){
        return AutomatonFactory.nfaToGnfa(this);
    }

    public String toString(){
        Map<State, Integer> mapper = new HashMap<>();
        int i = 0;
        for(State s : states) mapper.put(s, i++);

        String res = "";
        res = res + "states: " + states.size() + "\n";
        res = res + "start: " + mapper.get(start) + "\n";

        res = res + "final: ";
        if(finalStates.isEmpty()) res = res + "-1\n";
        else {
            Iterator<State> it = finalStates.iterator();
            while(it.hasNext()){
                State act = it.next();
                if(it.hasNext()) res = res + mapper.get(act) + ", ";
                else res = res + mapper.get(act) + "\n";
            }
        }

        res = res + "alphabet: ";
        if(alphabet.contains(Alphabet.getEmptyChar())) res = res + "'', ";
        for(Character c : alphabet.set()){
            if(c.equals(' ')) res = res + "space, ";
            else if(c.equals(',')) res = res + "comma, ";
            else res = res + c + ", ";
        }
        res = res + "nothing\n";

        for(Rule r : transition.getRules()){
            String character = r.character().toString();
            if(r.character().equals(Alphabet.getEmptyChar())) character = "''";
            else if(r.character().equals(' ')) character = "space";
            res = res + mapper.get(r.origin()) + " " + character + " " + mapper.get(r.destiny()) + "\n";
        }

        return res;
    }

}
