package Grammars;

import Elements.Alphabet;
import Automatons.Pda;
import Elements.Grammars.*;
import Factory.Algorithms;
import Factory.Constructors.CfgConstructor;
import Factory.Printer;

import java.util.HashSet;
import java.util.Set;

public class Cfg {
    private final Alphabet terminals;
    private final Set<CfgVariable> variables;
    private final CfgVariable start;
    private final Set<CfgRule> rules;

    public Cfg(Alphabet terminals, Set<CfgVariable> variables, CfgVariable start, Set<CfgRule> rules){
        this.terminals = terminals;
        this.variables = variables;
        this.start = start;
        this.rules = rules;
    }

    public CfgConstructor getConstructor(){
        return new CfgConstructor(terminals, variables, start, rules);
    }

    public Set<CfgRule> getRulesLeft(CfgVariable v) {
        Set<CfgRule> ret = new HashSet<>();
        for(CfgRule r : rules){
            if(r.getLeft().equals(v)) ret.add(r);
        }
        return ret;
    }

    public Set<Character> getTerminals(){
        return new HashSet<>(terminals.getSet());
    }

    public Pda toPda(){
        return Algorithms.cfgToPda(this);
    }

    public Cfg toChomsky(){
        return Algorithms.chomsky(this);
    }

    public Cfg toGriebach(){
        return null;
    }

    public boolean isLL(){
        return false;
    }

    public boolean isLLOne(){
        return false;
    }

    public boolean isLLTwo(){
        return false;
    }

    @Override
    public String toString() {
        return Printer.stringOfGrammar(this);
    }
}
