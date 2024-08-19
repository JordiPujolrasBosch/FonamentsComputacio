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

    public static String filename;
    public static String exceptionMessage;

    //Incorrect automaton

    public static String automatonCheck() {
        if(filename == null) return "Incorrect format for the automaton";
        return "Incorrect format for the automaton in file " + filename;
    }

    public static void automatonCheckBasic(){
        exceptionMessage = automatonCheck() + ": [states], [start], [final] or [alphabet] not read";
    }

    public static void automatonCheckRuleFormat(String rule){
        exceptionMessage = automatonCheck() + ": the rule [" + rule + "] is incorrect";
    }

    public static void automatonCheckStates(){
        exceptionMessage = automatonCheck() + ": [states] must be a positive number";
    }

    public static void automatonCheckStart(){
        exceptionMessage = automatonCheck() + ": [start] must be >= 0 and < [states]";
    }

    public static void automatonCheckFinal(){
        exceptionMessage = automatonCheck() + ": [final] must be a negative number or a list of numbers >= 0 and < [states]";
    }

    public static void automatonCheckAlphabet(String token){
        exceptionMessage = automatonCheck() + ": unknown token [" + token + "] in [alphabet]";
    }

    public static void automatonCheckRuleOrigin(String rule){
        exceptionMessage = automatonCheck() + ": the origin of the rule [" + rule + "] is not valid";
    }

    public static void automatonCheckRuleDestiny(String rule){
        exceptionMessage = automatonCheck() + ": the destiny of the rule [" + rule + "] is not valid";
    }

    public static void automatonCheckRuleTransitionUnknown(String rule){
        exceptionMessage = automatonCheck() + ": the transition of the rule [" + rule + "] is an unknown token";
    }

    public static void automatonCheckRuleTransitionNotInAlphabet(String rule, String t){
        exceptionMessage = automatonCheck() + ": the alphabet does not contain the character [" + t + "] of the rule [" + rule + "]";
    }

    public static void automatonCheckEmptyChar(){
        if(filename == null) exceptionMessage = "The nondeterministic automaton must contain the empty char " + TokenFactory.getAEmptyChar() + " in the alphabet";
        else exceptionMessage = "The nondeterministic automaton in file " + filename + " must contain the empty char " + TokenFactory.getAEmptyChar() + " in the alphabet.";
    }

    public static void automatonNondeterministic() {
        if(filename == null) exceptionMessage = "The automaton is nondeterministic";
        else exceptionMessage = "The automaton in file " + filename + " is nondeterministic";
    }

    //Incorrect regex

    public static String regexCheck(){
        if(filename == null) return "Incorrect format for the regular expression";
        return "Incorrect format for the regular expression in file " + filename;
    }

    public static void regexCheckParenthesis(){
        exceptionMessage = regexCheck() + ": the parentheses are wrong";
    }

    public static void regexCheckUnknownToken(String token){
        exceptionMessage = regexCheck() + ": unknown token [" + token + "]";
    }

    //Incorrect grammar

    public static String grammarCheck() {
        if(filename == null) return "Incorrect format for the grammar";
        return "Incorrect format for the grammar in file " + filename;
    }

    public static void grammarCheckBasic(){
        exceptionMessage = grammarCheck() + ": [terminals], [variables] or [start] not read";
    }

    public static void grammarCheckTerminal(String token){
        exceptionMessage = grammarCheck() + ": unknown token [" + token + "] in [terminals]";
    }

    public static void grammarCheckVariable(String token){
        exceptionMessage = grammarCheck() + ": unknown token [" + token + "] in [variables]";
    }

    public static void grammarCheckStart(){
        exceptionMessage = grammarCheck() + ": [start] must be a variable contained in [variables]";
    }

    public static void grammarCheckRule(String rule){
        exceptionMessage = grammarCheck() + ": the rule [" + rule + "] is wrong";
    }

    public static void grammarCheckUnknownToken(String rule, String token){
        exceptionMessage = grammarCheck() + ": unknown token [" + token + "] in rule [" + rule + "]";
    }

    public static void grammarCheckVariableNotInSet(String var){
        exceptionMessage = grammarCheck() + ": [variables] does not contain [" + var + "]";
    }

    public static void grammarCheckTerminalNotInSet(String ter){
        exceptionMessage = grammarCheck() + ": [terminals] does not contain [" + ter + "]";
    }

    public static void grammarCheckTerminalGroupNotInSet(String token){
        exceptionMessage = grammarCheck() + ": [terminals] does not contain all the elements of [" + token + "]";
    }

    public static void grammarCheckWrongConcatGroup(String rule, String token){
        exceptionMessage = grammarCheck() + ": concatenated token of group [" + token + "] in rule [" + rule + "]";
    }

    public static void grammarCheckUnionStartOrEnd(String rule){
        exceptionMessage = grammarCheck() + ": found [" + TokenFactory.getGUnion() + "] at the start or end in rule [" + rule + "]";
    }

    public static void grammarCheckUnionUnion(String rule){
        String u = TokenFactory.getGUnion();
        exceptionMessage = grammarCheck() + ": found double union [" + u + " " + u + "] in the rule [" + rule + "]";
    }

    public static void grammarCheckEmptyCharNotInTerminals(){
        exceptionMessage = grammarCheck() + ": the alphabet does not contain [" + TokenFactory.getAEmptyChar() + "]";
    }

    public static void grammarCheckWrongConcatEmpty(String rule){
        exceptionMessage = grammarCheck() + ": the empty char [/] is concatenated in rule [" + rule + "]";
    }

    //Output messages

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
