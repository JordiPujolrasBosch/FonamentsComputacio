package Elements.Transitions;

import Elements.Rule;
import Elements.State;

import java.util.Map;
import java.util.HashMap;

import java.util.List;
import java.util.ArrayList;

/**
 * Deterministic transition function. The transitions for a dfa. t(state origin, character) = state destiny
 */

public class Dtf {
    private final Map<State, Map<Character, State>> rules;

    //Constructor

    public Dtf(){
        rules = new HashMap<>();
    }

    //Add and remove

    /**
     * Adds the transition: t(origin, character) = destiny.
     * @param origin State origin.
     * @param destiny State destiny.
     * @param character Character.
     * @return False if this contains the transition. False otherwise.
     */
    public boolean add(State origin, State destiny, char character) {
        if(!rules.containsKey(origin)) rules.put(origin, new HashMap<>());
        if(rules.get(origin).containsKey(character)) return false;
        rules.get(origin).put(character, destiny);
        return true;
    }

    /**
     * Removes a state.
     * @param s State to remove.
     */
    public void removeState(State s) {
        rules.remove(s);
    }

    //Step

    /**
     * Finds the state destiny of an input.
     * @param o State origin.
     * @param c Character.
     * @return The state destiny of t(o,c).
     */
    public State step(State o, char c){
        if(!rules.containsKey(o)) return null;
        if(!rules.get(o).containsKey(c)) return null;
        return rules.get(o).get(c);
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
                l.add(new Rule(o, rules.get(o).get(c), c));
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

    //Consult

    /**
     * Checks if this contains the rule t(o,c)
     * @param o State origin.
     * @param c Character.
     * @return True if this contains t(o,c). False otherwise.
     */
    public boolean hasRule(State o, char c) {
        if(!rules.containsKey(o)) return false;
        return rules.get(o).containsKey(c);
    }

    /**
     * Counts the transition rules.
     * @return The number of transition rules.
     */
    public int size(){
        int n = 0;
        for(State s : rules.keySet()) n += rules.get(s).size();
        return n;
    }
}
