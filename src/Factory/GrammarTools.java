package Factory;

import Automatons.Pda;
import Elements.Grammars.CfgRule;
import Elements.Grammars.CfgVariable;
import Factory.Constructors.CfgConstructor;
import Grammars.*;
import Utils.Pair;

import java.util.*;

public class GrammarTools {
    private final CfgConstructor cfg;

    public GrammarTools(Cfg cfg){
        this.cfg = cfg.getConstructor();
    }

    public String terminalPrefix(Right r) {
        String x = "";
        switch (r.type()){
            case CHAR -> x = Character.toString(r.toRightChar().getC());
            case CONCAT -> {
                RightConcat c = r.toRightConcat();
                if(c.getA().containsVar()) x = terminalPrefix(c.getA());
                else x = c.getA().toString() + terminalPrefix(c.getB());
            }
        }
        return x;
    }

    public Pair<Right,Right> cut(Right r, int n) {
        Pair<Right,Right> p = new Pair<>(new RightEmpty(), new RightEmpty());
        switch (r.type()){
            case VAR, CHAR -> {
                if(n > 0) p = new Pair<>(r, new RightEmpty());
                else p = new Pair<>(new RightEmpty(), r);
            }
            case CONCAT -> {
                RightConcat c = r.toRightConcat();
                if(n == c.getA().length()) {
                    p = new Pair<>(c.getA(), c.getB());
                }
                else if(n < c.getA().length()){
                    Pair<Right,Right> cut = cut(c.getA(), n);
                    p = new Pair<>(cut.getA(), new RightConcat(cut.getB().toRightNonEmpty(), c.getB()));
                }
                else {
                    Pair<Right,Right> cut = cut(c.getB(), n-c.getA().length());
                    p = new Pair<>(new RightConcat(c.getA(), cut.getA().toRightNonEmpty()), cut.getB());
                }
            }
        }
        return p;
    }

    public Set<Right> findSuffixes(Right r, String prefix) {
        Set<Right> set = new HashSet<>();

        Set<Right> expanded = new HashSet<>();
        Set<Right> toexpand = new HashSet<>();
        if(terminalPrefix(r).length() >= prefix.length()) expanded.add(r);
        else toexpand.add(r);

        while(!toexpand.isEmpty()){
            Set<Right> aux = new HashSet<>();
            for(Right ri : toexpand) {
                if(ri.containsVar()) aux.addAll(stepLeftMost(ri));
                else expanded.add(ri);
            }
            toexpand.clear();

            for(Right ri : aux){
                if(terminalPrefix(ri).length() >= prefix.length()) expanded.add(ri);
                else toexpand.add(ri);
            }
        }

        for(Right ri : expanded){
            if(terminalPrefix(ri).startsWith(prefix)) set.add(cut(r,prefix.length()).getB());
        }

        return set;
    }

    public Set<Right> stepLeftMost(Right r) {
        Set<Right> set = new HashSet<>();
        if(r.containsVar()){
            CfgVariable v = getLeftMostVar(r.toRightNonEmpty());
            for(CfgRule rule : getRulesVar(v)){
                set.add(substituteLeftMost(r, v, rule.getRight()));
            }
        }
        return set;
    }

    public CfgVariable getLeftMostVar(RightNonEmpty r){
        CfgVariable x = null;
        switch (r.type()){
            case VAR -> x = r.toRightVar().getV();
            case CONCAT -> {
                RightConcat c = r.toRightConcat();
                if(c.getA().containsVar()) x = getLeftMostVar(c.getA());
                else x = getLeftMostVar(c.getB());
            }
        }
        return x;
    }

    public Set<CfgRule> getRulesVar(CfgVariable v){
        Set<CfgRule> set = new HashSet<>();
        for(CfgRule rule : cfg.rules){
            if(rule.getLeft().equals(v)) set.add(rule);
        }
        return set;
    }

    public Right substituteLeftMost(Right original, CfgVariable v, Right substitute) {
        Right x = original;
        switch (original.type()){
            case VAR -> {
                if(original.toRightVar().getV().equals(v)) x = substitute;
            }
            case CONCAT -> {
                RightConcat c = original.toRightConcat();
                if(c.getA().containsVar(v)){
                    x = new RightConcat(substituteLeftMost(c.getA(), v, substitute).toRightNonEmpty(), c.getB());
                }
                else if(c.getB().containsVar(v)){
                    x = new RightConcat(c.getA(), substituteLeftMost(c.getB(), v, substitute).toRightNonEmpty());
                }
            }
        }
        return x;
    }

    public Set<Character> getTerminals() {
        return cfg.terminals.getSet();
    }

    public boolean acceptsAllWords(Right rule, List<String> words) {
        CfgConstructor aux = cfg.getCfg().getConstructor();
        CfgVariable newStart = aux.generate(aux.start);
        aux.start = newStart;
        aux.rules.add(new CfgRule(newStart, rule));
        Pda parser = aux.getCfg().toPda();

        boolean accepts = true;
        Iterator<String> it = words.iterator();
        while(it.hasNext() && accepts) accepts = parser.checkWord(it.next());

        return accepts;
    }

    public int recommendedBagSize() {
        return 5 * cfg.variables.size() * cfg.rules.size();
    }

    public boolean startsWithVar(Right r) {
        boolean x = false;
        switch (r.type()){
            case VAR -> x = true;
            case CONCAT -> x = startsWithVar(r.toRightConcat().getA());
        }
        return x;
    }

    public String shortestWordOfVar(CfgVariable v) {
        Map<CfgVariable, Set<Right>> mapper = new HashMap<>();
        for(CfgRule rule : cfg.rules){
            if(!mapper.containsKey(rule.getLeft())) mapper.put(rule.getLeft(), new HashSet<>());
            mapper.get(rule.getLeft()).add(rule.getRight());
        }

        Right shortest = null;
        Set<Right> pre = new HashSet<>();
        Set<Right> post = new HashSet<>();
        pre.add(new RightVar(v));
        while(!pre.isEmpty()){
            for(Right r : pre){
                CfgVariable var = getLeftMostVar(r.toRightNonEmpty());
                for(Right subs : mapper.get(var)) post.add(substituteLeftMost(r, var, subs));
            }
            pre.clear();
            for(Right r : post){
                if(!r.containsVar()){
                    if(shortest == null) shortest = r;
                    else if(shortest.length() > r.length()) shortest = r;
                }
                else{
                    if(shortest == null) pre.add(r);
                    else if(r.length() < shortest.length()) pre.add(r);
                }
            }
            post.clear();
        }

        return shortest.toString();
    }
}
