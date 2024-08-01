package Factory;

import Automatons.Dfa;
import Automatons.Nfa;
import Elements.Alphabet;
import Elements.Grammars.Grule;
import Elements.Grammars.Gvar;
import Elements.Rule;
import Elements.State;
import Factory.Constructors.DfaConstructor;
import Factory.Constructors.NfaConstructor;
import Grammars.Cfg;
import Grammars.Gramex;

import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class Printer {

    //Output messages

    public static String automatonCheck(String filename) {
        if(filename == null) return "Incorrect format for the automaton";
        return "Incorrect format for the automaton in file " + filename;
    }

    public static String automatonNondeterministic(String filename) {
        if(filename == null) return "The automaton is nondeterministic";
        return "The automaton in file " + filename + " is nondeterministic";
    }

    public static String equal(String fa, String fb) {
        if(fa == null || fb == null) return "EQUAL";
        return "EQUAL " + fa + " " + fb;
    }

    public static String nonequal(String fa, String fb) {
        if(fa == null || fb == null) return "NOT EQUAL";
        return "NOT EQUAL " + fa + " " + fb;
    }

    public static String nonequalCounterexmaple(String fa, String fb, String counter){
        String out = "NOT EQUAL";
        if(fa != null && fb != null) out = out + " " + fa + " " + fb;
        if(counter.isEmpty()) out = out + " :: empty word";
        else out = out + " :: " + counter;
        return out;
    }

    public static String counterexampleNotFound(){
        return "Counter-example not found";
    }

    public static String grammarCheck(String filename) {
        if(filename == null) return "Incorrect format for the grammar";
        return "Incorrect format for the grammar in file " + filename;
    }

    public static String regexCheck(String filename){
        if(filename == null) return "Incorrect format for the regular expression";
        return "Incorrect format for the regular expression in file " + filename;
    }

    public static String ambiguous(String filename) {
        if(filename == null) return "The grammar is ambiguous";
        return "The grammar in file " + filename + " is ambiguous";
    }

    public static String unambiguous(String filename){
        if(filename == null) return "The grammar is probably unambiguous";
        return "The grammar in file " + filename + " is probably unambiguous";
    }

    public static String acceptsAllWords(){
        return "The language accepts all the words";
    }

    public static String notAcceptAllWords(int n, int max){
        return "The language doesn't accept all words: " + n + "/" + max;
    }

    public static String sizeOutOfRange(){
        return "Size out of range: from 0 to 500";
    }

    public static String lengthOutOfRange() {
        return "Length out of range: from 0 to 50";
    }

    public static String incorrectArguments(){
        return "Incorrect arguments (try \"--help\")";
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
        String res = "";
        res = res + alphabetPrinter(cfg.getTerminals(), "terminals") + "\n";
        res = res + variablesCfgPrinter(cfg.getVariables()) + "\n";
        res = res + "start: " + cfg.getStart() + "\n";

        Map<Gvar,Set<Gramex>> mapper = GrammarTools.getMapperRules(cfg);
        for(Gvar v : mapper.keySet()){
            for(Gramex g : mapper.get(v)){
                Grule r = new Grule(v,g);
                res = res + r + "\n";
            }
        }
        return res;
    }

    //Words file output

    public static String stringOfWords(List<String> list){
        if(list.isEmpty()) return "";
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
