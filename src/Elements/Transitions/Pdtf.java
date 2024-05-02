package Elements.Transitions;

import Elements.State;

import java.util.*;

public class Pdtf {
    private final Map<PdtfInput, Set<PdtfOutput>> rules;

    public Pdtf(){
        rules = new HashMap<>();
    }

    public void add(State origin, int c, int pop, State destiny, int push){
        PdtfInput input = new PdtfInput(origin, c, pop);
        PdtfOutput output = new PdtfOutput(destiny, push);
        if(!rules.containsKey(input)) rules.put(input, new HashSet<>());
        rules.get(input).add(output);
    }

    public boolean hasRule(State origin, int c, int pop){
        return rules.containsKey(new PdtfInput(origin, c, pop));
    }

    public Set<PdtfTuple> stepWithEmpty(State origin, int c, int pop, int e){
        Set<PdtfTuple> res = new HashSet<>();
        if(hasRule(origin,e,e)){
            for(PdtfOutput out : rules.get(new PdtfInput(origin,e,e))){
                res.add(new PdtfTuple(origin, e, e, out.destiny, out.push));
            }
        }
        if(hasRule(origin,c,e) && c != e){
            for(PdtfOutput out : rules.get(new PdtfInput(origin,c,e))){
                res.add(new PdtfTuple(origin, c, e, out.destiny, out.push));
            }
        }
        if(hasRule(origin,e,pop) && pop != e){
            for(PdtfOutput out : rules.get(new PdtfInput(origin,e,pop))){
                res.add(new PdtfTuple(origin, e, pop, out.destiny, out.push));
            }
        }
        if(hasRule(origin,c,pop) && c != e && pop != e){
            for(PdtfOutput out : rules.get(new PdtfInput(origin,c,pop))){
                res.add(new PdtfTuple(origin, c, pop, out.destiny, out.push));
            }
        }
        return res;
    }

    private static class PdtfInput {
        public State origin;
        public int character;
        public int pop;

        public PdtfInput(State o, int c, int s){
            origin = o;
            character = c;
            pop = s;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PdtfInput pdtfInput = (PdtfInput) o;
            return character == pdtfInput.character && pop == pdtfInput.pop && Objects.equals(origin, pdtfInput.origin);
        }

        @Override
        public int hashCode() {
            return Objects.hash(origin, character, pop);
        }
    }

    private static class PdtfOutput {
        public State destiny;
        public int push;

        public PdtfOutput(State d, int s){
            destiny = d;
            push = s;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PdtfOutput that = (PdtfOutput) o;
            return push == that.push && that.destiny.equals(destiny);
        }
    }
}
