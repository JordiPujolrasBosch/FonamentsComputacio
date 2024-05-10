package Grammars;

import Elements.Grammars.CfgVariable;

public class RightChar implements RightNonEmpty {
    private final char c;

    public RightChar(char c){
        this.c = c;
    }

    public char getC() {
        return c;
    }

    public RightNonEmpty getPrefix(int n) {
        if(n != 1) return null;
        return this;
    }

    public boolean hasPrefixTerminalOfSize(int n) {
        return n == 1;
    }

    public CfgVariable getLeftMostVar() {
        return null;
    }

    public RightNonEmpty getSubstitutionLeft(RightNonEmpty subs) {
        return null;
    }

    public Right getSufix(int n){
        if(n == 1) return this;
        if(n == 0) return new RightEmpty();
        return null;
    }

    public boolean containsVar(CfgVariable v) {
        return false;
    }

    public boolean containsVar(){
        return false;
    }

    public int length() {
        return 1;
    }

    public boolean containsTerminal() {
        return true;
    }

    public boolean containsTerminal(char c) {
        return this.c == c;
    }

    public boolean containsPair(RightConcat pair) {
        return false;
    }

    public Right getChanged(char c, CfgVariable x) {
        return new RightVar(x);
    }

    @Override
    public String toString() {
        return Character.toString(c);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RightChar rightChar = (RightChar) o;
        return c == rightChar.c;
    }

    public TypesRight type() {
        return TypesRight.CHAR;
    }

    public Right toRight()                 {return this;}
    public RightEmpty toRightEmpty()       {return null;}
    public RightNonEmpty toRightNonEmpty() {return this;}
    public RightChar toRightChar()         {return this;}
    public RightVar toRightVar()           {return null;}
    public RightConcat toRightConcat()     {return null;}


}
