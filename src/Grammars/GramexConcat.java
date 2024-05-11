package Grammars;

import Elements.Grammars.CfgVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GramexConcat implements GramexNonEmpty {
    private final GramexNonEmpty a;
    private final GramexNonEmpty b;

    public GramexConcat(GramexNonEmpty a, GramexNonEmpty b){
        this.a = a;
        this.b = b;
    }

    public GramexNonEmpty getA(){
        return a;
    }

    public GramexNonEmpty getB(){
        return b;
    }

    public TypesGramex type() {
        return TypesGramex.CONCAT;
    }

    public int length() {
        return a.length() + b.length();
    }

    public Gramex toGramex() {
        return this;
    }

    public GramexEmpty toGramexEmpty() {
        return null;
    }

    public GramexNonEmpty toGramexNonEmpty() {
        return this;
    }

    public GramexChar toGramexChar() {
        return null;
    }

    public GramexVar toGramexVar() {
        return null;
    }

    public GramexConcat toGramexConcat() {
        return this;
    }


    public List<GramexNonEmpty> toList(){
        List<GramexNonEmpty> list = new ArrayList<>();
        if(a.type() == TypesGramex.CONCAT) list.addAll(a.toGramexConcat().toList());
        else list.add(a);
        if(b.type() == TypesGramex.CONCAT) list.addAll(b.toGramexConcat().toList());
        else list.add(b);
        return list;
    }

    public Gramex get(int n){
        if(n < 0 || n >= length()) return new GramexEmpty();
        else return toList().get(n);
    }



    public boolean containsPair(GramexConcat pair) {
        if(this.equals(pair)) return true;
        return a.containsPair(pair) || b.containsPair(pair);
    }

    public GramexConcat getPair() {
        if(b.length() == 2) return b.toGramexConcat();
        if(b.length() > 2) return b.toGramexConcat().getPair();
        if(a.length() == 2) return a.toGramexConcat();
        if(a.length() > 2) return a.toGramexConcat().getPair();
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GramexConcat b = (GramexConcat) o;
        List<GramexNonEmpty> la = toList();
        List<GramexNonEmpty> lb = b.toList();
        if(la.size() != lb.size()) return false;

        boolean equal = true;
        int i = 0;
        while(i < la.size() && equal){
            equal = la.get(i).equals(lb.get(i));
            i++;
        }
        return equal;
    }

    @Override
    public String toString() {
        return a.toString() + " " + b.toString();
    }

    public GramexNonEmpty getChanged(GramexConcat pair, CfgVariable x) {
        if(this.equals(pair)) {
            return new GramexVar(x);
        }
        if(a.containsPair(pair) && b.containsPair(pair)) {
            GramexNonEmpty auxa = a.toGramexConcat().getChanged(pair, x);
            GramexNonEmpty auxb = b.toGramexConcat().getChanged(pair, x);
            return new GramexConcat(auxa, auxb);
        }
        if(a.containsPair(pair)){
            GramexNonEmpty auxa = a.toGramexConcat().getChanged(pair, x);
            return new GramexConcat(auxa, b);
        }
        if(b.containsPair(pair)) {
            GramexNonEmpty auxb = b.toGramexConcat().getChanged(pair, x);
            return new GramexConcat(a, auxb);
        }
        return this;
    }

    public Gramex getChanged(char c, CfgVariable x) {
        return new GramexConcat(a.getChanged(c,x).toGramexNonEmpty(), b.getChanged(c,x).toGramexNonEmpty());
    }

    public List<GramexChar> getListChars() {
        List<GramexChar> list = new ArrayList<>();
        if(a.type() == TypesGramex.CHAR) list.add(a.toGramexChar());
        else if(a.type() == TypesGramex.CONCAT) list.addAll(a.toGramexConcat().getListChars());
        if(b.type() == TypesGramex.CHAR) list.add(b.toGramexChar());
        else if(b.type() == TypesGramex.CONCAT) list.addAll(b.toGramexConcat().getListChars());
        return list;
    }

    public List<GramexVar> getListVars() {
        List<GramexVar> list = new ArrayList<>();
        if(a.type() == TypesGramex.VAR) list.add(a.toGramexVar());
        else if(a.type() == TypesGramex.CONCAT) list.addAll(a.toGramexConcat().getListVars());
        if(b.type() == TypesGramex.VAR) list.add(b.toGramexVar());
        else if(b.type() == TypesGramex.CONCAT) list.addAll(b.toGramexConcat().getListVars());
        return list;
    }


}
