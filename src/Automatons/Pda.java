package Automatons;

import AutomatonElements.Alphabet;
import AutomatonElements.Pdtf;
import AutomatonElements.State;
import ContextFreeGrammars.CfgVariable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Pda {
    private final Set<State> states;
    private final Alphabet alphabet;
    private final State start;
    private final Set<State> finalStates;
    private final Pdtf transition;

    private final Map<Character, Integer> mapperChar;
    private final Map<CfgVariable, Integer> mapperVar;

    public Pda(
            Set<State> states,
            Alphabet alphabet,
            State start,
            Set<State> finalStates,
            Pdtf transition,
            Map<Character,Integer> mapperChar,
            Map<CfgVariable,Integer> mapperVar)
    {
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
}
