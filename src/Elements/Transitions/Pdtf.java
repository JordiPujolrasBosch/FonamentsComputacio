package Elements.Transitions;

import Elements.State;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Pdtf {
    private Map<PdtfInput, Set<PdtfOutput>> rules;

    public Pdtf(){
        rules = new HashMap<>();
    }

    public void add(State origin, Integer c, Integer pop, State destiny, Integer push){
        PdtfInput input = new PdtfInput(origin, c, pop);
        PdtfOutput output = new PdtfOutput(destiny, push);
        if(!rules.containsKey(input)) rules.put(input, new HashSet<>());
        rules.get(input).add(output);
    }

    private static class PdtfInput {
        public State origin;
        public Integer wordChar;
        public Integer popChar;

        public PdtfInput(State o, Integer w, Integer s){
            origin = o;
            wordChar = w;
            popChar = s;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PdtfInput pdtfInput = (PdtfInput) o;
            return origin.equals(pdtfInput.origin) && wordChar.equals(pdtfInput.wordChar) && popChar.equals(pdtfInput.popChar);
        }
    }

    private static class PdtfOutput {
        public State destiny;
        public Integer pushChar;

        public PdtfOutput(State d, Integer s){
            destiny = d;
            pushChar = s;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PdtfOutput that = (PdtfOutput) o;
            return destiny.equals(that.destiny) && pushChar.equals(that.pushChar);
        }
    }
}
