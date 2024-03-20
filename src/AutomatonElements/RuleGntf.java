package AutomatonElements;

import RegularExpressions.RegularExpression;

public class RuleGntf {
    private final State origin;
    private final State destiny;
    private final RegularExpression regex;

    //Constructor

    public RuleGntf(State o, State d, RegularExpression r){
        origin = o;
        destiny = d;
        regex = r;
    }

    //Getters

    public State origin(){
        return origin;
    }

    public State destiny(){
        return destiny;
    }

    public RegularExpression regex(){
        return regex;
    }
}
