package Factory.Constructors;

import Elements.Alphabet;
import Elements.Grammars.GruleNonEmpty;
import Elements.Grammars.Gvar;
import Grammars.CfgNonEmpty;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CfgNonEmptyConstructor {
    public Alphabet terminals;
    public Set<Gvar> variables;
    public Gvar start;
    public Set<GruleNonEmpty> rules;
    public boolean acceptsEmptyWord;

    private final Map<Character,Integer> generator;

    public CfgNonEmptyConstructor(){
        this.terminals = new Alphabet();
        this.variables = new HashSet<>();
        this.start = null;
        this.rules = new HashSet<>();
        this.acceptsEmptyWord = false;

        this.generator = new HashMap<>();
    }

    public CfgNonEmptyConstructor(Alphabet terminals, Set<Gvar> variables, Gvar start, Set<GruleNonEmpty> rules, boolean empty){
        this.terminals = new Alphabet();
        this.terminals.addAll(terminals);

        this.variables = new HashSet<>(variables);
        this.start = start;
        this.rules = new HashSet<>(rules);
        this.acceptsEmptyWord = empty;

        generator = new HashMap<>();
        for(Gvar var : variables){
            if(!generator.containsKey(var.getC())) generator.put(var.getC(), var.getN());
            else if(var.getN() > generator.get(var.getC())) generator.put(var.getC(), var.getN());
        }
    }

    public Gvar generate(Gvar v){
        if(!generator.containsKey(v.getC())){
            generator.put(v.getC(), v.getN());
            variables.add(v);
            return v;
        }
        Gvar ret = new Gvar(v.getC(), generator.get(v.getC())+1);
        variables.add(ret);
        generator.put(ret.getC(), ret.getN());
        return ret;
    }

    public CfgNonEmpty getCfgNonEmpty(){
        return new CfgNonEmpty(terminals, variables, start, rules, acceptsEmptyWord);
    }
}
