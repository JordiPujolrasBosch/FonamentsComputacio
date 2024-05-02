package Factory.Constructors;

import Elements.Alphabet;
import Elements.Transitions.Pdtf;
import Elements.State;
import Automatons.Pda;
import Elements.Grammars.CfgVariable;

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

    private final Map<Character, Integer> mapperChar;
    private final Map<CfgVariable, Integer> mapperVar;

    public PdaConstructor(){
        states = new HashSet<>();
        alphabet = new Alphabet();
        start = null;
        finalStates = new HashSet<>();
        transition = new Pdtf();

        mapperChar = new HashMap<>();
        mapperVar = new HashMap<>();
    }

    private int generateInt(){
        return mapperVar.size() + mapperChar.size();
    }

    public void addMapper(Character c) {
        if(!mapperChar.containsKey(c)) mapperChar.put(c, generateInt());
    }

    public void addMapper(CfgVariable v) {
        if(!mapperVar.containsKey(v)) mapperVar.put(v, generateInt());
    }

    public int getMapper(Character c) {
        return mapperChar.get(c);
    }

    public int getMapper(CfgVariable v) {
        return mapperVar.get(v);
    }

    public Pda getCfg() {
        return new Pda(states, alphabet, start, finalStates, transition, mapperChar, mapperVar);
    }
}
