package Grammars;

import Elements.Grammars.CfgVariable;
import Factory.TokenFactory;

public class RightEmpty implements Right {
    public RightEmpty(){}

    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public boolean containsVar(CfgVariable v) {
        return false;
    }

    public TypesRight type() {
        return TypesRight.EMPTY;
    }

    public int length() {
        return 0;
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

    public String toString() {
        return TokenFactory.getGrammarEmpty();
    }

    public Right toRight()                 {return this;}
    public RightEmpty toRightEmpty()       {return this;}
    public RightNonEmpty toRightNonEmpty() {return null;}
    public RightChar toRightChar()         {return null;}
    public RightVar toRightVar()           {return null;}
    public RightConcat toRightConcat()     {return null;}
}
