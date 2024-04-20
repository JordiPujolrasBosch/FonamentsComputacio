package ContextFreeGrammars;

import AutomatonElements.Alphabet;

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
}
