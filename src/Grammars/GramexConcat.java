package Grammars;

import java.util.ArrayList;
import java.util.List;

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

}
