package RegularExpresions;

import Automatons.AutomatonFactory;
import Automatons.Nfa;

public class RegexStar implements RegularExpresion {
    private final RegularExpresion x;

    public RegexStar(RegularExpresion x){
        this.x = x;
    }

    public Nfa getNfa(){
        return AutomatonFactory.star(x.getNfa());
    }
}
