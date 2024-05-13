package Grammars;

import Elements.Alphabet;
import Automatons.Pda;
import Elements.Grammars.*;
import Exceptions.GrammarReaderException;
import Factory.Algorithms;
import Factory.Constructors.CfgConstructor;
import Factory.Printer;

import java.util.HashSet;
import java.util.Set;

public class Cfg {
    private final Alphabet terminals;
    private final Set<Gvar> variables;
    private final Gvar start;
    private final Set<Grule> rules;

    public Cfg(Alphabet terminals, Set<Gvar> variables, Gvar start, Set<Grule> rules){
        this.terminals = terminals;
        this.variables = variables;
        this.start = start;
        this.rules = rules;
    }

    public CfgConstructor getConstructor(){
        return new CfgConstructor(terminals, variables, start, rules);
    }

    //Getters

    public Alphabet getTerminals(){
        Alphabet x = new Alphabet();
        x.addAll(terminals);
        return x;
    }

    public Set<Gvar> getVariables(){
        return new HashSet<>(variables);
    }

    public Gvar getStart(){
        return start;
    }

    public Set<Grule> getRules(){
        return new HashSet<>(rules);
    }

    //Transformations

    public Pda toPda(){
        return Algorithms.cfgToPda(this);
    }

    public CfgNonEmpty simplify() {
        return Algorithms.simplifyGrammar(this);
    }

    /*
    // LL -> LL + Greibach ?

    public boolean isLL(){
        return false;
    }

    public boolean isLLOne(){
        return false;
    }

    public boolean isLLTwo(){
        return false;
    }

    public boolean compare(Cfg x){
        return Beta.compare(this, x);
    }
    */
    @Override
    public String toString() {
        return Printer.stringOfGrammar(this);
    }
}
