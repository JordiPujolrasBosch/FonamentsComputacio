package Elements.Transitions;

import Elements.Rule;
import Elements.State;

import java.util.Map;
import java.util.HashMap;

import java.util.List;
import java.util.ArrayList;

public class Dtf {
    private final Map<State, Map<Character, State>> rules;

    //Constructor

    public Dtf(){
        rules = new HashMap<>();
    }

    //Add and remove

    public boolean add(State origin, State destiny, char character) {
        if(!rules.containsKey(origin)) rules.put(origin, new HashMap<>());
        if(rules.get(origin).containsKey(character)) return false;
        rules.get(origin).put(character, destiny);
        return true;
    }

    public void addRules(List<Rule> l){
        for(Rule r : l) add(r.getOrigin(), r.getDestiny(), r.getCharacter());
    }

    public void removeState(State s) {
        rules.remove(s);
    }

    //Step

    public State step(State o, char c){
        if(!rules.containsKey(o)) return null;
        if(!rules.get(o).containsKey(c)) return null;
        return rules.get(o).get(c);
    }

    //Get rules

    public List<Rule> getRules(){
        List<Rule> l = new ArrayList<>();
        for(State o : rules.keySet()){
            for(char c : rules.get(o).keySet()){
                l.add(new Rule(o, rules.get(o).get(c), c));
            }
        }
        return l;
    }

    //Consult

    public boolean hasRule(State o, char c) {
        if(!rules.containsKey(o)) return false;
        return rules.get(o).containsKey(c);
    }

    public int size(){
        int n = 0;
        for(State s : rules.keySet()) n += rules.get(s).size();
        return n;
    }
}
