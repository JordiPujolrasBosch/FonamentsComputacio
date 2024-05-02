package Factory;

import Automatons.Dfa;
import Automatons.Nfa;
import Elements.Alphabet;
import Elements.Grammars.CfgRule;
import Elements.Grammars.CfgVariable;
import Elements.Rule;
import Elements.State;
import Factory.Constructors.CfgConstructor;
import Factory.Constructors.DfaConstructor;
import Factory.Constructors.NfaConstructor;
import Grammars.Cfg;

import java.util.*;

public class Printer {

    public static String stringOfDfa(Dfa dfa){
        DfaConstructor dc = dfa.getConstructor();
        return stringOfAutomaton(dc.states, dc.alphabet, dc.start, dc.finalStates, dc.transition.getRules());
    }

    public static String stringOfNfa(Nfa nfa){
        NfaConstructor nc = nfa.getConstructor();
        return stringOfAutomaton(nc.states, nc.alphabet, nc.start, nc.finalStates, nc.transition.getRules());
    }

    public static String stringOfGrammar(Cfg cfg){
        CfgConstructor cc = cfg.getConstructor();

        String res = "";
        res = res + alphabetPrinter(cc.terminals, "terminals") + "\n";
        res = res + variablesCfgPrinter(cc.variables) + "\n";
        res = res + "start: " + cc.start + "\n";
        for(CfgRule rule : cc.rules) res = res + rule + "\n";
        return res;
    }


    private static String stringOfAutomaton(Set<State> states, Alphabet alphabet, State start, Set<State> finalStates, List<Rule> rules){
        Map<State,Integer> mapper = new HashMap<>();
        int i = 0;
        for(State s : states) mapper.put(s, i++);

        String res = "";
        res = res + "states: " + states.size() + "\n";
        res = res + "start: " + mapper.get(start) + "\n";
        res = res + automatonFinalStates(finalStates, mapper) + "\n";
        res = res + alphabetPrinter(alphabet, "alphabet") + "\n";
        res = res + automatonRules(rules, mapper);
        return res;
    }

    private static String automatonRules(List<Rule> rules, Map<State,Integer> mapper){
        String res = "";
        for(Rule r : rules){
            String character = r.character().toString();
            if(TokenFactory.btokensReverseContains(r.character())) character = TokenFactory.btokensReverseGet(r.character());
            res = res + mapper.get(r.origin()) + " " + character + " " + mapper.get(r.destiny()) + "\n";
        }
        return res;
    }

    private static String automatonFinalStates(Set<State> finalStates, Map<State,Integer> mapper){
        String res = "final: ";
        if(finalStates.isEmpty()) res = res + "-1";
        else {
            Iterator<State> it = finalStates.iterator();
            while(it.hasNext()){
                State act = it.next();
                if(it.hasNext()) res = res + mapper.get(act) + ", ";
                else res = res + mapper.get(act);
            }
        }
        return res;
    }

    private static String variablesCfgPrinter(Set<CfgVariable> variables){
        String res = "variables: ";
        Iterator<CfgVariable> it = variables.iterator();
        while(it.hasNext()){
            CfgVariable act = it.next();
            if(it.hasNext()) res = res + act + ", ";
            else res = res + act;
        }
        return res;
    }

    private static String alphabetPrinter(Alphabet alphabet, String name){
        String res = name + ": ";
        if(alphabet.contains(Alphabet.getEmptyChar())){
            res = res + TokenFactory.atokensGetEmptyChar() + ", ";
        }
        for(Character c : alphabet.set()){
            if(TokenFactory.atokensReverseContains(c)) res = res + TokenFactory.atokensReverseGet(c) + ", ";
            else res = res + c + ", ";
        }
        res = res + TokenFactory.atokensGetNothing();
        return res;
    }

}
