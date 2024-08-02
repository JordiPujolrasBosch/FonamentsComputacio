package Grammars;

import Elements.Alphabet;
import Automatons.Pda;
import Elements.Grammars.*;
import Factory.Algorithms;
import Factory.Constructors.CfgConstructor;
import Factory.Printer;
import Utils.Pair;

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

    public Pair<Boolean, String> checkAmbiguity(int l){
        return Algorithms.checkAmbiguity(this, l);
    }

    @Override
    public String toString() {
        return Printer.stringOfGrammar(this);
    }
}
