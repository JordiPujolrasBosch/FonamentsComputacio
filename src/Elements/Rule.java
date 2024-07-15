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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return character == rule.character && origin.equals(rule.origin) && destiny.equals(rule.destiny);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin, destiny, character);
    }
}
