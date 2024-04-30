package Factory;

import AutomatonElements.Alphabet;
import AutomatonElements.Rule;
import AutomatonElements.State;

import java.util.*;

public class OutputMessages {
    public static String automatonCheck(String filename) {
        return "Incorrect format for the automaton in file " + filename;
    }

    public static String automatonNondeterministic(String filename) {
        return "The automaton of file " + filename + " is nondeterministic";
    }

    public static String equal(String fa, String fb) {
        return "EQUAL " + fa + " " + fb;
    }

    public static String nonequal(String fa, String fb) {
        return "NOT EQUAL " + fa + " " + fb;
    }

    public static String grammarCheck(String filename) {
        return "Incorrect format for the grammar file " + filename;
    }

    public static String automatonToString(Set<State> states, Alphabet alphabet, State start, Set<State> finalStates, List<Rule> rules){
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
        if(alphabet.contains(Alphabet.getEmptyChar())) res = res + TokenFactory.atokensReverseGet(Alphabet.getEmptyChar()) + ", ";
        for(Character c : alphabet.set()){
            if(TokenFactory.atokensReverseContains(c)) res = res + TokenFactory.atokensReverseGet(c) + ", ";
            else res = res + c + ", ";
        }
        res = res + TokenFactory.atokensGetNothing();

        for(Rule r : rules){
            String character = r.character().toString();
            if(TokenFactory.btokensReverseContains(r.character())) character = TokenFactory.btokensReverseGet(r.character());
            res = res + mapper.get(r.origin()) + " " + character + " " + mapper.get(r.destiny()) + "\n";
        }

        return res;
    }
}
