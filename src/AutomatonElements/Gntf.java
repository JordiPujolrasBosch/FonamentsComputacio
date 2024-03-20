package AutomatonElements;

import RegularExpresions.RegexUnion;
import RegularExpresions.RegexVoid;
import RegularExpresions.RegularExpresion;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Gntf {
    private final Map<State, Map<State, RegularExpresion>> rules;

    public Gntf(){
        rules = new HashMap<>();
    }

    public void addUnion(State origin, State destiny, RegularExpresion r) {
        if(!rules.containsKey(origin)) rules.put(origin, new HashMap<>());
        Map<State, RegularExpresion> part2 = rules.get(origin);
        if(!part2.containsKey(destiny)) part2.put(destiny, r);
        else part2.put(destiny, new RegexUnion(part2.get(destiny), r));
    }

    public void addReplace(State origin, State destiny, RegularExpresion r) {
        if(!rules.containsKey(origin)) rules.put(origin, new HashMap<>());
        Map<State, RegularExpresion> part2 = rules.get(origin);
        part2.put(destiny, r);
    }

    public RegularExpresion step(State origin, State destiny){
        if(!rules.containsKey(origin)) return new RegexVoid();
        if(!rules.get(origin).containsKey(destiny)) return new RegexVoid();
        return rules.get(origin).get(destiny);
    }

    public void addAll(Gntf tf) {
        for(State o : tf.rules.keySet()){
            for(State d : tf.rules.get(o).keySet()){
                addReplace(o,d,rules.get(o).get(d));
            }
        }
    }

    public void removeState(State act) {
        rules.remove(act);
        for(State s : rules.keySet()) rules.get(s).remove(act);
    }
}
