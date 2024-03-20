package RegularExpresions;

import Automatons.AutomatonFactory;
import Automatons.Nfa;

public class RegexUnion implements RegularExpresion {
    private final RegularExpresion a;
    private final RegularExpresion b;

    public RegexUnion(RegularExpresion a, RegularExpresion b){
        this.a = a;
        this.b = b;
    }

    public Nfa getNfa(){
        return AutomatonFactory.union(a.getNfa(),b.getNfa());
    }
}
