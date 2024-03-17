package AutomatonElements;

import RegularExpresions.RegularExpresion;

import java.util.HashMap;
import java.util.Map;

public class GeneralizedNondeterministicTransitionFunction {
    private final Map<State, Map<State, RegularExpresion>> rules;

    public GeneralizedNondeterministicTransitionFunction(){
        rules = new HashMap<>();
    }
}
