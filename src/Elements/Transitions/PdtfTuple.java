package Elements.Transitions;

import Elements.State;

import java.util.Objects;

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

    public State getOrigin(){
        return origin;
    }

    public int getCharacter(){
        return character;
    }

    public int getPop(){
        return pop;
    }

    public State getDestiny(){
        return destiny;
    }

    public int getPush(){
        return push;
    }

    //Equals

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PdtfTuple that = (PdtfTuple) o;
        return character == that.character && pop == that.pop && push == that.push && origin.equals(that.origin) && destiny.equals(that.destiny);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin, character, pop, destiny, push);
    }
}
