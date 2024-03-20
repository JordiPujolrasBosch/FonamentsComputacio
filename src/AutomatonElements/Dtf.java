package AutomatonElements;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Dtf {
    private final Map<State, Map<Character, State>> rules;

    public Dtf(){
        rules = new HashMap<>();
    }

    //ADD and REMOVE

    public boolean add(State origin, State destiny, Character character) {
        if(!rules.containsKey(origin)) rules.put(origin, new HashMap<>());
        Map<Character, State> part2 = rules.get(origin);
        if(part2.containsKey(character)) return false;
        part2.put(character, destiny);
        return true;
    }

    public void remove(State s) {
        rules.remove(s);
    }

    //STEP

    public State step(State o, Character c){
        if(!rules.containsKey(o)) return null;
        if(!rules.get(o).containsKey(c)) return null;
        return rules.get(o).get(c);
    }

    //CONSULT

    public int size(){
        int n = 0;
        for(State s : rules.keySet()) n += rules.get(s).size();
        return n;
    }

    public boolean isUnused(State x) {
        boolean found = false;
        Iterator<State> it1 = rules.keySet().iterator();
        while(it1.hasNext() && !found){
            State o = it1.next();
            Iterator<Character> it2 = rules.get(o).keySet().iterator();
            while(it2.hasNext() && !found){
                Character c = it2.next();
                found = rules.get(o).get(c) == x;
            }
        }

        return !found;
    }

    public boolean hasRule(State o, Character c) {
        if(!rules.containsKey(o)) return false;
        return rules.get(o).containsKey(c);
    }

    public void addAll(Dtf tf) {
        for(State o : tf.rules.keySet()){
            for(Character c : tf.rules.get(o).keySet()){
                add(o,tf.rules.get(o).get(c),c);
            }
        }
    }
}
