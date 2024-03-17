package AutomatonElements;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class NondeterministicTransitionFunction {
    private final Map<State, Map<Character, Set<State>>> rules;

    public NondeterministicTransitionFunction(){
        rules = new HashMap<>();
    }

    public void addAll(NondeterministicTransitionFunction ntf) {
        for(State o : ntf.rules.keySet()){
            for(Character c : ntf.rules.get(o).keySet()){
                for(State d : ntf.rules.get(o).get(c)){
                    add(o,d,c);
                }
            }
        }
    }

    public void add(State origin, State destiny, Character character) {
        if(!rules.containsKey(origin)) rules.put(origin, new HashMap<>());
        Map<Character,Set<State>> part2 = rules.get(origin);
        if(!part2.containsKey(character)) part2.put(character, new HashSet<>());
        Set<State> part3 = part2.get(character);
        part3.add(destiny);
    }

    public Set<State> step(State o, Character c){
        if(!rules.containsKey(o)) return new HashSet<>();
        if(!rules.get(o).containsKey(c)) return new HashSet<>();
        return rules.get(o).get(c);
    }
}
