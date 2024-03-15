package AutomatonElements;

import java.util.HashMap;
import java.util.Map;

public class DeterministicTransitionFunction {
    private Map<State, Map<Character,State>> rules;

    public DeterministicTransitionFunction() {
        rules = new HashMap<>();
    }

    public void add(State origin, State destiny, Character character) {
        if(!rules.containsKey(origin)){
            rules.put(origin, new HashMap<>());
        }

        Map<Character, State> part2 = rules.get(origin);

        if(!part2.containsKey(character)){
            part2.put(character,destiny);
        }
        else throw new Exception();
    }
}
