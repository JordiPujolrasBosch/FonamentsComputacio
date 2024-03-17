package AutomatonElements;

import java.util.HashMap;
import java.util.Map;

public class DeterministicTransitionFunction {
    private final Map<State, Map<Character,State>> rules;

    public DeterministicTransitionFunction() {
        rules = new HashMap<>();
    }

    public boolean add(State origin, State destiny, Character character) {
        if(!rules.containsKey(origin)) rules.put(origin, new HashMap<>());
        Map<Character, State> part2 = rules.get(origin);
        if(part2.containsKey(character)) return false;
        part2.put(character, destiny);
        return true;
    }

    public State step(State o, Character c){
        if(!rules.containsKey(o)) return null;
        if(!rules.get(o).containsKey(c)) return null;
        return rules.get(o).get(c);
    }
}
