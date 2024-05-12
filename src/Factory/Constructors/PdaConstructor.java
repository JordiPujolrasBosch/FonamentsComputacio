package Factory.Constructors;

import Elements.Alphabet;
import Elements.Transitions.Pdtf;
import Elements.State;
import Automatons.Pda;
import Elements.Grammars.Gvar;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PdaConstructor {
    public Set<State> states;
    public Alphabet alphabet;
    public State start;
    public Set<State> finalStates;
    public Pdtf transition;

    public final Map<Character, Integer> mapperChar;
    private final Map<Gvar, Integer> mapperVar;

    public PdaConstructor(){
        states = new HashSet<>();
        alphabet = new Alphabet();
        start = null;
        finalStates = new HashSet<>();
        transition = new Pdtf();

        mapperChar = new HashMap<>();
        mapperVar = new HashMap<>();
    }

    public PdaConstructor(Set<State> states, Alphabet alphabet, State start, Set<State> finalStates, Pdtf transition,
                          Map<Character,Integer> mapperChar, Map<Gvar,Integer> mapperVar){
        this.states = states;
        this.alphabet = alphabet;
        this.start = start;
        this.finalStates = finalStates;
        this.transition = transition;
        this.mapperChar = mapperChar;
        this.mapperVar = mapperVar;
    }

    private int generateInt(){
        return mapperVar.size() + mapperChar.size();
    }

    public void addMapper(char c) {
        if(!mapperChar.containsKey(c)) mapperChar.put(c, generateInt());
    }

    public void addMapper(Gvar v) {
        if(!mapperVar.containsKey(v)) mapperVar.put(v, generateInt());
    }

    public int getMapper(char c) {
        return mapperChar.get(c);
    }

    public int getMapper(Gvar v) {
        return mapperVar.get(v);
    }

    public Pda getCfg() {
        return new Pda(states, alphabet, start, finalStates, transition, mapperChar, mapperVar);
    }
}
