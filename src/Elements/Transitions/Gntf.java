package Elements.Transitions;

import Elements.RuleGntf;
import Elements.State;
import RegularExpressions.RegexUnion;
import RegularExpressions.RegexVoid;
import RegularExpressions.RegularExpression;

import java.util.List;
import java.util.ArrayList;

import java.util.Map;
import java.util.HashMap;

import java.util.Set;

/**
 * Generalized nondeterministic transition function. The transitions for a gnfa. t(state origin, state destiny) = regex
 */

public class Gntf {
    private final Map<State, Map<State, RegularExpression>> rules;

    //Constructors

    public Gntf(){
        rules = new HashMap<>();
    }

    public Gntf(State origin, State accept, Set<State> ss){
        rules = new HashMap<>();

        for(State o : ss){
            for(State d : ss){
                if(o != accept && d != origin) addReplace(o, d, RegexVoid.getInstance());
            }
        }
    }

    //Add and remove

    /**
     * Adds a transition. t(origin, destiny) = union(r,t(origin, destiny))
     * @param origin State origin.
     * @param destiny State destiny.
     * @param r Regular expression.
     */
    public void addUnion(State origin, State destiny, RegularExpression r) {
        if(!rules.containsKey(origin)) rules.put(origin, new HashMap<>());
        Map<State, RegularExpression> part2 = rules.get(origin);
        if(!part2.containsKey(destiny)) part2.put(destiny, r);
        else part2.put(destiny, new RegexUnion(part2.get(destiny), r));
    }

    /**
     * Adds a transition. t(origin, destiny) = r
     * @param origin State origin.
     * @param destiny State destiny.
     * @param r Regular expression.
     */
    public void addReplace(State origin, State destiny, RegularExpression r) {
        if(!rules.containsKey(origin)) rules.put(origin, new HashMap<>());
        rules.get(origin).put(destiny, r);
    }

    /**
     * Removes a state.
     * @param act State to remove.
     */
    public void removeState(State act) {
        rules.remove(act);
        for(State s : rules.keySet()) rules.get(s).remove(act);
    }

    //GET-ADD rules

    /**
     * Get all the transitions rules.
     * @return A list of the transition rules.
     */
    public List<RuleGntf> getRules(){
        List<RuleGntf> l = new ArrayList<>();
        for(State o : rules.keySet()){
            for(State d : rules.get(o).keySet()){
                l.add(new RuleGntf(o, d, rules.get(o).get(d)));
            }
        }
        return l;
    }

    /**
     * Add a list of transition rules.
     * @param l List of the transition rules.
     */
    public void addRules(List<RuleGntf> l){
        for(RuleGntf r : l) addReplace(r.getOrigin(), r.getDestiny(), r.getRegex());
    }

    //Step

    /**
     * Finds the regex destiny of an input.
     * @param origin State origin.
     * @param destiny State destiny.
     * @return The regex of t(origin,destiny).
     */
    public RegularExpression step(State origin, State destiny){
        if(!rules.containsKey(origin)) return RegexVoid.getInstance();
        if(!rules.get(origin).containsKey(destiny)) return RegexVoid.getInstance();
        return rules.get(origin).get(destiny);
    }

}
