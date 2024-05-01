package Grammars;

import Elements.Grammars.CfgVariable;

import java.util.Objects;

public class RightConcat implements RightNonEmpty {
    private final RightNonEmpty a;
    private final RightNonEmpty b;

    public RightConcat(RightNonEmpty a, RightNonEmpty b){
        this.a = a;
        this.b = b;
    }

    public RightNonEmpty a(){
        return a;
    }

    public RightNonEmpty b(){
        return b;
    }

    public boolean containsVar(CfgVariable v) {
        return a.containsVar(v) || b.containsVar(v);
    }

    public TypesRight type() {
        return TypesRight.CONCAT;
    }

    public int length() {
        return a.length() + b.length();
    }

    public boolean containsTerminal() {
        return a.containsTerminal() || b.containsTerminal();
    }

    public boolean containsTerminal(char c) {
        return a.containsTerminal(c) || b.containsTerminal(c);
    }

    public boolean containsPair(RightConcat pair) {
        if(this.equals(pair)) return true;
        return a.containsPair(pair) || b.containsPair(pair);
    }

    public RightConcat getPair() {
        if(b.length() == 2) return b.toRightConcat();
        if(b.length() > 2) return b.toRightConcat().getPair();
        if(a.length() == 2) return a.toRightConcat();
        if(a.length() > 2) return a.toRightConcat().getPair();
        return this;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RightConcat that = (RightConcat) o;
        return Objects.equals(a, that.a) && Objects.equals(b, that.b);
    }

    public RightNonEmpty getChanged(RightConcat pair, CfgVariable x) {
        if(this.equals(pair)) {
            return new RightVar(x);
        }
        if(a.containsPair(pair) && b.containsPair(pair)) {
            RightNonEmpty auxa = a.toRightConcat().getChanged(pair, x);
            RightNonEmpty auxb = b.toRightConcat().getChanged(pair, x);
            return new RightConcat(auxa, auxb);
        }
        if(a.containsPair(pair)){
            RightNonEmpty auxa = a.toRightConcat().getChanged(pair, x);
            return new RightConcat(auxa, b);
        }
        if(b.containsPair(pair)) {
            RightNonEmpty auxb = b.toRightConcat().getChanged(pair, x);
            return new RightConcat(a, auxb);
        }
        return this;
    }

    public Right getChanged(char c, CfgVariable x) {
        return new RightConcat(a.getChanged(c,x).toRightNonEmpty(), b.getChanged(c,x).toRightNonEmpty());
    }

    public Right toRight()                 {return this;}
    public RightEmpty toRightEmpty()       {return null;}
    public RightNonEmpty toRightNonEmpty() {return this;}
    public RightChar toRightChar()         {return null;}
    public RightVar toRightVar()           {return null;}
    public RightConcat toRightConcat()     {return this;}

}
