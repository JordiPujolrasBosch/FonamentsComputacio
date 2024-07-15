package Factory;

import Automatons.Dfa;
import Automatons.Nfa;
import Elements.Alphabet;
import Elements.Grammars.Grule;
import Elements.Grammars.Gvar;
import Elements.Rule;
import Elements.State;
import Factory.Constructors.CfgConstructor;
import Factory.Constructors.DfaConstructor;
import Factory.Constructors.NfaConstructor;
import Grammars.Cfg;

import java.util.*;

public class Printer {

    //Output messages

    public static String automatonCheck(String filename) {
        return "Incorrect format for the automaton in file " + filename;
    }

    public static String automatonNondeterministic(String filename) {
        return "The automaton in file " + filename + " is nondeterministic";
    }

    public static String equal(String fa, String fb) {
        return "EQUAL " + fa + " " + fb;
    }

    public static String nonequal(String fa, String fb) {
        return "NOT EQUAL " + fa + " " + fb;
    }

    public static String grammarCheck(String filename) {
        return "Incorrect format for the grammar in file " + filename;
    }

    public static String regexCheck(String filename){
        return "Incorrect format for the regular expression in file " + filename;
    }

    public static String ambiguous(String filename) {
        return "The grammar in file " + filename + " is ambiguous";
    }

    public static String unambiguous(String filename){
        return "The grammar in file " + filename + " is probably unambiguous";
    }

    //Automatons and grammars

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
        for(Grule rule : cc.rules) res = res + rule + "\n";
        return res;
    }

    //Words file output

    public static String stringOfWords(List<String> list){
        String s = "";
        for(int i = 0; i<list.size()-1; i++) s = s + list.get(i) + "\n";
        s = s + list.get(list.size()-1);
        return s;
    }

    //Private

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
            String character = Character.toString(r.getCharacter());
            if(TokenFactory.isBString(r.getCharacter())) character = TokenFactory.getBString(r.getCharacter());
            res = res + mapper.get(r.getOrigin()) + " " + character + " " + mapper.get(r.getDestiny()) + "\n";
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

    private static String variablesCfgPrinter(Set<Gvar> variables){
        String res = "variables: ";
        Iterator<Gvar> it = variables.iterator();
        while(it.hasNext()){
            Gvar act = it.next();
            if(it.hasNext()) res = res + act + ", ";
            else res = res + act;
        }
        return res;
    }

    private static String alphabetPrinter(Alphabet alphabet, String name){
        String res = name + ": ";
        if(alphabet.containsEmptyChar()){
            res = res + TokenFactory.getAEmptyChar() + ", ";
        }
        for(char c : alphabet.getSet()){
            if(TokenFactory.isAString(c)) res = res + TokenFactory.getAString(c) + ", ";
            else res = res + c + ", ";
        }
        res = res + TokenFactory.getANothing();
        return res;
    }

}
