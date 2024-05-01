package Grammars;

import Elements.Grammars.CfgVariable;

public class RightChar implements RightNonEmpty {
    private final char c;

    public RightChar(char c){
        this.c = c;
    }

    public char c() {
        return c;
    }

    public boolean containsVar(CfgVariable v) {
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
