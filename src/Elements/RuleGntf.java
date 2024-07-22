package Elements;

import RegularExpressions.RegularExpression;

import java.util.Objects;

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

    public State getOrigin(){
        return origin;
    }

    public State getDestiny(){
        return destiny;
    }

    public RegularExpression getRegex(){
        return regex;
    }

    //Equals

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RuleGntf that = (RuleGntf) o;
        return origin.equals(that.origin) && destiny.equals(that.destiny) && regex.equals(that.regex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin, destiny, regex);
    }
}
