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
                if(o != accept && d != origin) addReplace(o, d, new RegexVoid());
            }
        }
    }

    //Add and remove

    public void addUnion(State origin, State destiny, RegularExpression r) {
        if(!rules.containsKey(origin)) rules.put(origin, new HashMap<>());
        Map<State, RegularExpression> part2 = rules.get(origin);
        if(!part2.containsKey(destiny)) part2.put(destiny, r);
        else part2.put(destiny, new RegexUnion(part2.get(destiny), r));
    }

    public void addReplace(State origin, State destiny, RegularExpression r) {
        if(!rules.containsKey(origin)) rules.put(origin, new HashMap<>());
        rules.get(origin).put(destiny, r);
    }

    public void removeState(State act) {
        rules.remove(act);
        for(State s : rules.keySet()) rules.get(s).remove(act);
    }

    //GET-ADD rules

    public List<RuleGntf> getRules(){
        List<RuleGntf> l = new ArrayList<>();
        for(State o : rules.keySet()){
            for(State d : rules.get(o).keySet()){
                l.add(new RuleGntf(o, d, rules.get(o).get(d)));
            }
        }
        return l;
    }

    public void addRules(List<RuleGntf> l){
        for(RuleGntf r : l) addReplace(r.getOrigin(), r.getDestiny(), r.getRegex());
    }

    //Step

    public RegularExpression step(State origin, State destiny){
        if(!rules.containsKey(origin)) return new RegexVoid();
        if(!rules.get(origin).containsKey(destiny)) return new RegexVoid();
        return rules.get(origin).get(destiny);
    }

}
