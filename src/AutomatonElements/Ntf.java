package AutomatonElements;

import java.util.Map;
import java.util.HashMap;

import java.util.Set;
import java.util.HashSet;

import java.util.List;
import java.util.ArrayList;

public class Ntf {
    private final Map<State, Map<Character, Set<State>>> rules;

    //Constructor

    public Ntf(){
        rules = new HashMap<>();
    }

    //Add

    public void add(State origin, State destiny, Character character) {
        if(!rules.containsKey(origin)) rules.put(origin, new HashMap<>());
        if(!rules.get(origin).containsKey(character)) rules.get(origin).put(character, new HashSet<>());
        rules.get(origin).get(character).add(destiny);
    }

    public void addRules(List<Rule> l){
        for(Rule r : l) add(r.origin(), r.destiny(), r.character());
    }

    //Get rules

    public List<Rule> getRules(){
        List<Rule> l = new ArrayList<>();
        for(State o : rules.keySet()){
            for(Character c : rules.get(o).keySet()){
                for(State d : rules.get(o).get(c)){
                    l.add(new Rule(o,d,c));
                }
            }
        }
        return l;
    }

    //Step

    public Set<State> step(State o, Character c) {
        if(!rules.containsKey(o)) return new HashSet<>();
        if(!rules.get(o).containsKey(c)) return new HashSet<>();
        return rules.get(o).get(c);
    }

    public Set<State> stateExtended(State x){
        Set<State> before = new HashSet<>();
        Set<State> after = new HashSet<>();
        after.add(x);

        while(before.size() != after.size()){
            before = after;
            after = new HashSet<>(before);
            for(State s : before){
                after.addAll(step(s, Alphabet.getEmptyChar()));
            }
        }

        return after;
    }

}
