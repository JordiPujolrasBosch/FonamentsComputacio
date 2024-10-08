package Elements.Transitions;

import Elements.State;

import java.util.Set;
import java.util.Map;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Objects;

/**
 * Pushdown transition function. The transitions for a pda. t(state origin, character word, character pop) = (state destiny, character push)
 */

public class Pdtf {
    private final Map<PdtfInput, Set<PdtfOutput>> rules;

    //Constructor

    public Pdtf(){
        rules = new HashMap<>();
    }

    //Add

    /**
     * Adds the transition: t(origin, c, pop) = (destiny, push).
     * @param origin State origin.
     * @param c Value of character word.
     * @param pop Value of character pop.
     * @param destiny State destiny.
     * @param push Value of character push.
     */
    public void add(State origin, int c, int pop, State destiny, int push){
        PdtfInput input = new PdtfInput(origin, c, pop);
        PdtfOutput output = new PdtfOutput(destiny, push);
        if(!rules.containsKey(input)) rules.put(input, new HashSet<>());
        rules.get(input).add(output);
    }

    //Consult

    /**
     * Checks if this contains the rule t(origin, c, pop).
     * @param origin State origin.
     * @param c Value of character word.
     * @param pop Value of character pop.
     * @return True if this contains t(origin, c, pop). False otherwise.
     */
    public boolean hasRule(State origin, int c, int pop){
        return rules.containsKey(new PdtfInput(origin, c, pop));
    }

    //Step

    /**
     * Finds the set of states of the inputs: t(origin,c,pop) + t(origin,e,pop) + t(origin,c,e) + t(origin,e,e)
     * @param origin State origin.
     * @param c Value of character word.
     * @param pop Value of character pop.
     * @param e Value of character empty.
     * @return The set of transitions of t(origin,c/e,pop/e).
     */
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

    //Private classes

    private static class PdtfInput {
        public State origin;
        public int character;
        public int pop;

        public PdtfInput(State o, int c, int s){
            origin = o;
            character = c;
            pop = s;
        }

        /**
         * Compares if two objects are equal.
         * @param o The object to compare.
         * @return True if this and o are equal. False otherwise.
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PdtfInput that = (PdtfInput) o;
            return character == that.character && pop == that.pop && origin.equals(that.origin);
        }

        /**
         * @return A hash code for this object.
         */
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

        /**
         * Compares if two objects are equal.
         * @param o The object to compare.
         * @return True if this and o are equal. False otherwise.
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PdtfOutput that = (PdtfOutput) o;
            return push == that.push && destiny.equals(that.destiny);
        }

        /**
         * @return A hash code for this object.
         */
        @Override
        public int hashCode() {
            return Objects.hash(destiny, push);
        }
    }
}
