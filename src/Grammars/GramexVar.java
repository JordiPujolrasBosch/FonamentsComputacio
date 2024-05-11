package Grammars;

import Elements.Grammars.CfgVariable;

import java.util.Objects;

public class GramexVar implements GramexNonEmpty {
    private final CfgVariable v;

    public GramexVar(CfgVariable v){
        this.v = v;
    }

    public CfgVariable getV(){
        return v;
    }

    public TypesGramex type() {
        return TypesGramex.VAR;
    }

    public int length() {
        return 1;
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
        return this;
    }

    public GramexConcat toGramexConcat() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GramexVar rightVar = (GramexVar) o;
        return Objects.equals(v, rightVar.v);
    }

    @Override
    public String toString() {
        return v.toString();
    }

}
