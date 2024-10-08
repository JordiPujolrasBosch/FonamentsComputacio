package Elements.Transitions;

import Elements.State;

import java.util.Objects;

/**
 * Transition of a pda.
 */

public class PdtfTuple {
    private final State origin;
    private final int character;
    private final int pop;
    private final State destiny;
    private final int push;

    public PdtfTuple(State o, int c, int pop, State d, int push){
        this.origin = o;
        this.character = c;
        this.pop = pop;
        this.destiny = d;
        this.push = push;
    }

    //Getters

    /**
     * @return The origin state.
     */
    public State getOrigin(){
        return origin;
    }

    /**
     * @return The value of character word.
     */
    public int getCharacter(){
        return character;
    }

    /**
     * @return The value of character pop.
     */
    public int getPop(){
        return pop;
    }

    /**
     * @return The destiny state.
     */
    public State getDestiny(){
        return destiny;
    }

    /**
     * @return The value of character push.
     */
    public int getPush(){
        return push;
    }

    //Equals

    /**
     * Compares if two objects are equal.
     * @param o The object to compare.
     * @return True if this and o are equal. False otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PdtfTuple that = (PdtfTuple) o;
        return character == that.character && pop == that.pop && push == that.push && origin.equals(that.origin) && destiny.equals(that.destiny);
    }

    /**
     * @return A hash code for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(origin, character, pop, destiny, push);
    }
}
