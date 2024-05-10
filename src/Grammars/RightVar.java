package Grammars;

import Elements.Grammars.CfgVariable;

import java.util.Objects;

public class RightVar implements RightNonEmpty {
    private final CfgVariable v;

    public RightVar(CfgVariable v){
        this.v = v;
    }

    public CfgVariable getV(){
        return v;
    }

    public RightNonEmpty getPrefix(int n) {
        if(n != 1) return null;
        return this;
    }

    public boolean hasPrefixTerminalOfSize(int n) {
        return false;
    }

    public CfgVariable getLeftMostVar() {
        return v;
    }

    public RightNonEmpty getSubstitutionLeft(RightNonEmpty subs) {
        return subs;
    }

    public Right getSufix(int n){
        if(n == 1) return this;
        if(n == 0) return new RightEmpty();
        return null;
    }

    public boolean containsVar(CfgVariable v) {
        return this.v.equals(v);
    }

    public boolean containsVar(){
        return true;
    }

    public TypesRight type() {
        return TypesRight.VAR;
    }

    public int length() {
        return 1;
    }

    public boolean containsTerminal() {
        return false;
    }

    public boolean containsTerminal(char c) {
        return false;
    }

    public boolean containsPair(RightConcat pair) {
        return false;
    }

    public Right getChanged(char c, CfgVariable x) {
        return this;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RightVar rightVar = (RightVar) o;
        return Objects.equals(v, rightVar.v);
    }

    @Override
    public String toString() {
        return v.toString();
    }

    public Right toRight()                 {return this;}
    public RightEmpty toRightEmpty()       {return null;}
    public RightNonEmpty toRightNonEmpty() {return this;}
    public RightChar toRightChar()         {return null;}
    public RightVar toRightVar()           {return this;}
    public RightConcat toRightConcat()     {return null;}
}
