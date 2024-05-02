package Elements.Transitions;

import Elements.State;

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

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PdtfTuple tuple = (PdtfTuple) o;
        return character == tuple.character && pop == tuple.pop && push == tuple.push && tuple.origin.equals(origin) && tuple.destiny.equals(destiny);
    }
}
