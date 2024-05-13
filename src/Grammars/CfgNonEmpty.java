package Grammars;

import Elements.Alphabet;
import Elements.Grammars.GruleNonEmpty;
import Elements.Grammars.Gvar;
import Factory.Algorithms;
import Factory.Constructors.CfgNonEmptyConstructor;

import java.util.HashSet;
import java.util.Set;

public class CfgNonEmpty {
    private final Alphabet terminals;
    private final Set<Gvar> variables;
    private final Gvar start;
    private final Set<GruleNonEmpty> rules;
    private final boolean acceptsEmptyWord;

    public CfgNonEmpty(){
        terminals = new Alphabet();
        start = new Gvar('A',0);
        variables = new HashSet<>();
        variables.add(start);
        rules = new HashSet<>();
        acceptsEmptyWord = false;
    }

    public CfgNonEmpty(Alphabet terminals, Set<Gvar> variables, Gvar start, Set<GruleNonEmpty> rules, boolean empty){
        this.terminals = terminals;
        this.variables = variables;
        this.start = start;
        this.rules = rules;
        this.acceptsEmptyWord = empty;
    }

    public CfgNonEmptyConstructor getConstructor(){
        return new CfgNonEmptyConstructor(terminals, variables, start, rules, acceptsEmptyWord);
    }

    public boolean compare(CfgNonEmpty b){
        return Algorithms.equalsCfgs(this,b);
    }

    //Getters

    public Alphabet getTerminals() {
        Alphabet x = new Alphabet();
        x.addAll(terminals);
        return x;
    }

    public Gvar getStart() {
        return start;
    }

    public Set<GruleNonEmpty> getRules() {
        return new HashSet<>(rules);
    }

    public Set<Gvar> getVariables() {
        return new HashSet<>(variables);
    }

    public boolean acceptsEmpty(){
        return acceptsEmptyWord;
    }

    //Transformations

    public Cfg toCfg(){
        return Algorithms.cgfNonEmptyToCfg(this);
    }

    public CfgNonEmpty toChomsky(){
        return Algorithms.chomsky(this);
    }

    public CfgNonEmpty toGriebach(){
        return Algorithms.griebach(this);
    }

    //String

    @Override
    public String toString() {
        return toCfg().toString();
    }
}
