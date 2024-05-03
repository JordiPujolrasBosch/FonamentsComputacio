package Factory.Constructors;

import Elements.Alphabet;
import Grammars.Cfg;
import Elements.Grammars.CfgRule;
import Elements.Grammars.CfgVariable;

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
            if(!generator.containsKey(var.getC())) generator.put(var.getC(), var.getN());
            else if(var.getN() > generator.get(var.getC())) generator.put(var.getC(), var.getN());
        }
    }

    public CfgVariable generate(CfgVariable v){
        if(!generator.containsKey(v.getC())) {
            generator.put(v.getC(), v.getN());
            variables.add(v);
            return v;
        }
        CfgVariable ret = new CfgVariable(v.getC(), generator.get(v.getC())+1);
        variables.add(ret);
        generator.put(ret.getC(), ret.getN());
        return ret;
    }

    public Cfg getCfg(){
        return new Cfg(terminals, variables, start, rules);
    }
}
