package Factory.Builders;

import java.util.Objects;

public class RuleData {
    private final int origin;
    private final int destiny;
    private final char transition;

    public RuleData(int o, int d, char c){
        origin = o;
        destiny = d;
        transition = c;
    }

    //Getters

    public char getCharacter() {
        return transition;
    }

    public int getDestiny() {
        return destiny;
    }

    public int getOrigin() {
        return origin;
    }

    //Equals

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RuleData that = (RuleData) o;
        return origin == that.origin && destiny == that.destiny && transition == that.transition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin, destiny, transition);
    }
}
