package Grammars;

import Elements.Grammars.CfgVariable;

import java.util.ArrayList;
import java.util.List;
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

    @Override
    public String toString() {
        return a.toString() + " " + b.toString();
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

    public List<RightChar> getListChars() {
        List<RightChar> list = new ArrayList<>();
        if(a.type() == TypesRight.CHAR) list.add(a.toRightChar());
        else if(a.type() == TypesRight.CONCAT) list.addAll(a.toRightConcat().getListChars());
        if(b.type() == TypesRight.CHAR) list.add(b.toRightChar());
        else if(b.type() == TypesRight.CONCAT) list.addAll(b.toRightConcat().getListChars());
        return list;
    }

    public List<RightVar> getListVars() {
        List<RightVar> list = new ArrayList<>();
        if(a.type() == TypesRight.VAR) list.add(a.toRightVar());
        else if(a.type() == TypesRight.CONCAT) list.addAll(a.toRightConcat().getListVars());
        if(b.type() == TypesRight.VAR) list.add(b.toRightVar());
        else if(b.type() == TypesRight.CONCAT) list.addAll(b.toRightConcat().getListVars());
        return list;
    }

    public Right toRight()                 {return this;}
    public RightEmpty toRightEmpty()       {return null;}
    public RightNonEmpty toRightNonEmpty() {return this;}
    public RightChar toRightChar()         {return null;}
    public RightVar toRightVar()           {return null;}
    public RightConcat toRightConcat()     {return this;}

}
