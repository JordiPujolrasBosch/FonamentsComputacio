package Elements;

import java.util.Objects;

public class Rule {
    private final State origin;
    private final State destiny;
    private final char character;

    public Rule(State o, State d, char c){
        origin = o;
        destiny = d;
        character = c;
    }

    //Getters

    public State getOrigin(){
        return origin;
    }

    public State getDestiny(){
        return destiny;
    }

    public char getCharacter(){
        return character;
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
        Rule that = (Rule) o;
        return character == that.character && origin.equals(that.origin) && destiny.equals(that.destiny);
    }

    /**
     * @return A hash code for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(origin, destiny, character);
    }
}
