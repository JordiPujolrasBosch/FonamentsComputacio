package Elements;

import RegularExpressions.RegularExpression;

import java.util.Objects;

/**
 * Transition of a gnfa.
 */

public class RuleGntf {
    private final State origin;
    private final State destiny;
    private final RegularExpression regex;

    public RuleGntf(State o, State d, RegularExpression r){
        origin = o;
        destiny = d;
        regex = r;
    }

    //Getters

    /**
     * @return The origin state.
     */
    public State getOrigin(){
        return origin;
    }

    /**
     * @return The destiny state.
     */
    public State getDestiny(){
        return destiny;
    }

    /**
     * @return The regular expression.
     */
    public RegularExpression getRegex(){
        return regex;
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
        RuleGntf that = (RuleGntf) o;
        return origin.equals(that.origin) && destiny.equals(that.destiny) && regex.equals(that.regex);
    }

    /**
     * @return A hash code for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(origin, destiny, regex);
    }
}
