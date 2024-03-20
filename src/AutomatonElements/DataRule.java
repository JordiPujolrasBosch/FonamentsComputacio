package AutomatonElements;

public class DataRule {
    private final int origin;
    private final int destiny;
    private final Character transition;

    public DataRule(int o, int d, Character c){
        origin = o;
        destiny = d;
        transition = c;
    }

    public Character character() {
        return transition;
    }

    public int destiny() {
        return destiny;
    }

    public int origin() {
        return origin;
    }
}
