package Elements.Transitions;

import Elements.Alphabet;
import Elements.Rule;
import Elements.State;

import java.util.Map;
import java.util.HashMap;

import java.util.Set;
import java.util.HashSet;

import java.util.List;
import java.util.ArrayList;

/**
 * Nondeterministic transition function. The transitions for a nfa. t(state origin, character) = set of states destiny
 */

public class Ntf {
    private final Map<State, Map<Character, Set<State>>> rules;

    //Constructor

    public Ntf(){
        rules = new HashMap<>();
    }

    //Add

    /**
     * Adds the transition: t(origin, character) = destiny.
     * @param origin State origin.
     * @param destiny State destiny.
     * @param character Character.
     */
    public void add(State origin, State destiny, char character) {
        if(!rules.containsKey(origin)) rules.put(origin, new HashMap<>());
        if(!rules.get(origin).containsKey(character)) rules.get(origin).put(character, new HashSet<>());
        rules.get(origin).get(character).add(destiny);
    }

    //GET-ADD rules

    /**
     * Get all the transitions rules.
     * @return A list of the transition rules.
     */
    public List<Rule> getRules(){
        List<Rule> l = new ArrayList<>();
        for(State o : rules.keySet()){
            for(char c : rules.get(o).keySet()){
                for(State d : rules.get(o).get(c)){
                    l.add(new Rule(o,d,c));
                }
            }
        }
        return l;
    }

    /**
     * Add a list of transition rules.
     * @param l List of the transition rules.
     */
    public void addRules(List<Rule> l){
        for(Rule r : l) add(r.getOrigin(), r.getDestiny(), r.getCharacter());
    }

    //Step

    /**
     * Finds the set of states destiny of an input.
     * @param o State origin.
     * @param c Character.
     * @return The set of states destiny of t(o,c).
     */
    public Set<State> step(State o, char c) {
        if(!rules.containsKey(o)) return new HashSet<>();
        if(!rules.get(o).containsKey(c)) return new HashSet<>();
        return rules.get(o).get(c);
    }

    /**
     * Finds the set of states destiny which a state origin can go with empty transitions.
     * @param x State origin
     * @return Set of states destiny.
     */
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
