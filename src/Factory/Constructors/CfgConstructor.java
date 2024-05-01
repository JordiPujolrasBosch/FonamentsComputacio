package Factory.Constructors;

import AutomatonElements.Alphabet;
import ContextFreeGrammars.Cfg;
import ContextFreeGrammars.CfgRule;
import ContextFreeGrammars.CfgVariable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CfgConstructor {
    public Alphabet terminals;
    public Set<CfgVariable> variables;
    public CfgVariable start;
    public Set<CfgRule> rules;

    private final Map<Character, Integer> generator;

    public CfgConstructor(){
        this.terminals = new Alphabet();
        this.variables = new HashSet<>();
        this.start = null;
        this.rules = new HashSet<>();

        this.generator = new HashMap<>();
    }

    public CfgConstructor(Alphabet terminals, Set<CfgVariable> variables, CfgVariable start, Set<CfgRule> rules){
        this.terminals = new Alphabet();
        this.terminals.addAll(terminals);

        this.variables = new HashSet<>();
        this.variables.addAll(variables);

        this.start = start;

        this.rules = new HashSet<>();
        this.rules.addAll(rules);

        generator = new HashMap<>();
        for(CfgVariable var : variables){
            if(!generator.containsKey(var.c())) generator.put(var.c(), var.n());
            else if(var.n() > generator.get(var.c())) generator.put(var.c(), var.n());
        }
    }

    public CfgVariable generate(CfgVariable v){
        if(!generator.containsKey(v.c())) {
            generator.put(v.c(), v.n());
            variables.add(v);
            return v;
        }
        CfgVariable ret = new CfgVariable(v.c(), generator.get(v.c())+1);
        variables.add(ret);
        generator.put(ret.c(), ret.n());
        return ret;
    }

    public Cfg getCfg(){
        return new Cfg(terminals, variables, start, rules);
    }
}
