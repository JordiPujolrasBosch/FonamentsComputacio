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

    public boolean containsVar(){
        return false;
    }

    public Right getSufix(int n){
        if(n == 0) return this;
        return null;
    }

    public boolean hasPrefixTerminalOfSize(int length) {
        return false;
    }

    public CfgVariable getLeftMostVar() {
        return null;
    }

    public RightNonEmpty getSubstitutionLeft(RightNonEmpty toRightNonEmpty) {
        return null;
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

    @Override
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
