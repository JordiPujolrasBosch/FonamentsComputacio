package Elements;

public class Rule {
    private final State origin;
    private final State destiny;
    private final Character character;

    //Constructor

    public Rule(State o, State d, Character c){
        origin = o;
        destiny = d;
        character = c;
    }

    //Getters

    public State origin(){
        return origin;
    }

    public State destiny(){
        return destiny;
    }

    public Character character(){
        return character;
    }
}
