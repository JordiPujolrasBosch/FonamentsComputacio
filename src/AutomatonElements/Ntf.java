package AutomatonElements;

import java.util.*;

public class Ntf {
    private final Map<State, Map<Character, Set<State>>> rules;

    public Ntf(){
        rules = new HashMap<>();
    }

    //ADD RULES

    public void add(State origin, State destiny, Character character) {
        if(!rules.containsKey(origin)) rules.put(origin, new HashMap<>());
        Map<Character,Set<State>> part2 = rules.get(origin);
        if(!part2.containsKey(character)) part2.put(character, new HashSet<>());
        Set<State> part3 = part2.get(character);
        part3.add(destiny);
    }

    public void addAll(Ntf tf) {
        for(State o : tf.rules.keySet()){
            for(Character c : tf.rules.get(o).keySet()){
                for(State d : tf.rules.get(o).get(c)){
                    add(o,d,c);
                }
            }
        }
    }

    //STEP

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

    public Set<State> setStateExtended(Set<State> ss){
        Set<State> res = new HashSet<>();
        for(State s : ss) res.addAll(stateExtended(s));
        return res;
    }
}
