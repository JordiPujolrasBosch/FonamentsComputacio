package RegularExpressions;

import Factory.Algorithms;
import Automatons.Nfa;
import Factory.TokenFactory;

import java.util.Objects;

public class RegexStar implements RegularExpression {
    private RegularExpression x;

    public RegexStar(RegularExpression x){
        this.x = x;
    }

    public Nfa getNfa(){
        return Algorithms.star(x.getNfa());
    }

    public TypesRegex type() {
        return TypesRegex.STAR;
    }

    public RegularExpression simplify() {
        x = x.simplify();
        if(x.type() == TypesRegex.EMPTY || x.type() == TypesRegex.STAR) return x;
        if(x.type() == TypesRegex.VOID) return RegexEmptyChar.getInstance();
        return this;
    }

    @Override
    public String toString() {
        if(x.type() == TypesRegex.CONCAT || x.type() == TypesRegex.STAR || x.type() == TypesRegex.UNION){
            return "(" + x + ")" + TokenFactory.getRStar();
        }
        return x + TokenFactory.getRStar();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegexStar b = (RegexStar) o;
        return x.getNfa().toDfa().minimize().compare(b.x.getNfa().toDfa().minimize());
    }

    @Override
    public int hashCode() {
        return Objects.hash(x);
    }
}
