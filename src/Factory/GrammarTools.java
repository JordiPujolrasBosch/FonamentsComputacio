package Factory;

import Automatons.Pda;
import Elements.Grammars.*;
import Factory.Constructors.CfgNonEmptyConstructor;
import Grammars.*;
import Utils.Pair;

import java.util.*;

public class GrammarTools {
    // Gramex: starts, ends and contains: vars and chars

    public static boolean startsWithVar(Gramex g) {
        boolean x = false;
        switch (g.type()){
            case VAR -> x = true;
            case CONCAT -> x = startsWithVar(g.toGramexConcat().getA());
        }
        return x;
    }

    public static boolean startsWithVar(Gramex g, Gvar v){
        boolean x = false;
        switch (g.type()){
            case VAR -> x = g.toGramexVar().getV().equals(v);
            case CONCAT -> x = startsWithVar(g.toGramexConcat().getA(), v);
        }
        return x;
    }

    public static boolean startsWithChar(Gramex g) {
        boolean x = false;
        switch (g.type()){
            case CHAR -> x = true;
            case CONCAT -> x = startsWithChar(g.toGramexConcat().getA());
        }
        return x;
    }

    public static boolean startsWithChar(Gramex g, char c){
        boolean x = false;
        switch (g.type()){
            case CHAR -> x = g.toGramexChar().getC() == c;
            case CONCAT -> x = startsWithChar(g.toGramexConcat().getA(), c);
        }
        return x;
    }

    public static boolean endsWithVar(Gramex g){
        boolean x = false;
        switch (g.type()){
            case VAR -> x = true;
            case CONCAT -> x = endsWithVar(g.toGramexConcat().getB());
        }
        return x;
    }

    public static boolean endsWithVar(Gramex g, Gvar v){
        boolean x = false;
        switch (g.type()){
            case VAR -> x = g.toGramexVar().getV().equals(v);
            case CONCAT -> x = endsWithVar(g.toGramexConcat().getB(), v);
        }
        return x;
    }

    public static boolean endsWithChar(Gramex g){
        boolean x = false;
        switch (g.type()){
            case CHAR -> x = true;
            case CONCAT -> x = endsWithChar(g.toGramexConcat().getB());
        }
        return x;
    }

    public static boolean endsWithChar(Gramex g, char c){
        boolean x = false;
        switch (g.type()){
            case CHAR -> x = g.toGramexChar().getC() == c;
            case CONCAT -> x = endsWithChar(g.toGramexConcat().getB(), c);
        }
        return x;
    }

    public static boolean containsVar(Gramex g){
        boolean x = false;
        switch (g.type()){
            case VAR -> x = true;
            case CONCAT -> x = containsVar(g.toGramexConcat().getA()) || containsVar(g.toGramexConcat().getB());
        }
        return x;
    }

    public static boolean containsVar(Gramex g, Gvar v){
        boolean x = false;
        switch (g.type()){
            case VAR -> x = g.toGramexVar().getV().equals(v);
            case CONCAT -> x = containsVar(g.toGramexConcat().getA(), v) || containsVar(g.toGramexConcat().getB(), v);
        }
        return x;
    }

    public static boolean containsChar(Gramex g){
        boolean x = false;
        switch (g.type()){
            case CHAR -> x = true;
            case CONCAT -> x = containsChar(g.toGramexConcat().getA()) || containsChar(g.toGramexConcat().getB());
        }
        return x;
    }

    public static boolean containsChar(Gramex g, char c){
        boolean x = false;
        switch (g.type()){
            case CHAR -> x = g.toGramexChar().getC() == c;
            case CONCAT -> x = containsChar(g.toGramexConcat().getA(), c) || containsChar(g.toGramexConcat().getB(), c);
        }
        return x;
    }

    //Substitute gramex

    public static Gramex substituteLeftMost(Gramex original, Gvar v, Gramex substitute) {
        Gramex x = original;
        switch (original.type()){
            case VAR -> {
                if(original.toGramexVar().getV().equals(v)) x = substitute;
            }
            case CONCAT -> {
                GramexConcat c = original.toGramexConcat();
                if(containsVar(c.getA(), v)){
                    if(c.getA().type() == TypesGramex.VAR && substitute.type() == TypesGramex.EMPTY) x = c.getB();
                    else x = new GramexConcat(substituteLeftMost(c.getA(), v, substitute).toGramexNonEmpty(), c.getB());
                }
                else if(containsVar(c.getB(), v)){
                    if(c.getB().type() == TypesGramex.VAR && substitute.type() == TypesGramex.EMPTY) x = c.getA();
                    else x = new GramexConcat(c.getA(), substituteLeftMost(c.getB(), v, substitute).toGramexNonEmpty());
                }
            }
        }
        return x;
    }

    public static Gramex substituteAllChars(Gramex original, char c, Gvar substitute){
        Gramex x = original;
        switch (original.type()){
            case CHAR -> {
                if(original.toGramexChar().getC() == c) x = new GramexVar(substitute);
            }
            case CONCAT -> {
                GramexNonEmpty a = substituteAllChars(original.toGramexConcat().getA(), c, substitute).toGramexNonEmpty();
                GramexNonEmpty b = substituteAllChars(original.toGramexConcat().getB(), c, substitute).toGramexNonEmpty();
                x = new GramexConcat(a,b);
            }
        }
        return x;
    }

    public static Gramex substituteConcats(Gramex original, GramexConcat pair, Gvar substitute){
        Gramex x = original;
        if(original.type() == TypesGramex.CONCAT){
            GramexVar subs = new GramexVar(substitute);
            GramexConcat c = original.toGramexConcat();
            if(equalsGramexConcats(c,pair)) x = subs;
            else{
                GramexNonEmpty a = substituteConcats(c.getA(), pair, substitute).toGramexNonEmpty();
                GramexNonEmpty b = substituteConcats(c.getB(), pair, substitute).toGramexNonEmpty();
                x = new GramexConcat(a,b);
            }
        }
        return x;
    }

    public static GramexNonEmpty substituteVarsToVars(GramexNonEmpty original, Map<Gvar, Gvar> mapper){
        GramexNonEmpty x = original;
        switch (original.type()){
            case VAR -> x = new GramexVar(mapper.get(original.toGramexVar().getV()));
            case CONCAT -> {
                GramexNonEmpty a = substituteVarsToVars(original.toGramexConcat().getA(),mapper).toGramexNonEmpty();
                GramexNonEmpty b = substituteVarsToVars(original.toGramexConcat().getB(),mapper).toGramexNonEmpty();
                x = new GramexConcat(a,b);
            }
        }
        return x;
    }

    //Getters gramex

    public static GramexNonEmpty getLeftMost(GramexConcat g){
        if(g.getA().type() != TypesGramex.CONCAT) return g.getA();
        return getLeftMost(g.getA().toGramexConcat());
    }

    public static Gvar getLeftMostVar(GramexNonEmpty r){
        Gvar x = null;
        switch (r.type()){
            case VAR -> x = r.toGramexVar().getV();
            case CONCAT -> {
                GramexConcat c = r.toGramexConcat();
                if(containsVar(c.getA())) x = getLeftMostVar(c.getA());
                else x = getLeftMostVar(c.getB());
            }
        }
        return x;
    }

    public static GramexChar getLeftMostChar(GramexNonEmpty r){
        GramexChar x = null;
        switch (r.type()){
            case CHAR -> x = r.toGramexChar();
            case CONCAT -> {
                GramexConcat c = r.toGramexConcat();
                if(containsChar(c.getA())) x = getLeftMostChar(c.getA());
                else x = getLeftMostChar(c.getB());
            }
        }
        return x;
    }

    public static GramexConcat getRightMostPair(GramexConcat g){
        if(g.getB().length() == 2) return g.getB().toGramexConcat();
        if(g.getB().length() > 2)  return getRightMostPair(g.getB().toGramexConcat());
        if(g.getA().length() == 2) return g.getA().toGramexConcat();
        if(g.getA().length() > 2)  return getRightMostPair(g.getA().toGramexConcat());
        return g;
    }

    public static String terminalPrefix(Gramex r) {
        String x = "";
        switch (r.type()){
            case CHAR -> x = Character.toString(r.toGramexChar().getC());
            case CONCAT -> {
                GramexConcat c = r.toGramexConcat();
                if(containsVar(c.getA())) x = terminalPrefix(c.getA());
                else x = c.getA().toString() + terminalPrefix(c.getB());
            }
        }
        return x;
    }

    public static Pair<Gramex, Gramex> cut(Gramex r, int n) {
        Pair<Gramex, Gramex> p = new Pair<>(new GramexEmpty(), new GramexEmpty());
        switch (r.type()){
            case VAR, CHAR -> {
                if(n > 0) p = new Pair<>(r, new GramexEmpty());
                else p = new Pair<>(new GramexEmpty(), r);
            }
            case CONCAT -> {
                GramexConcat c = r.toGramexConcat();
                if(n == c.getA().length()) {
                    p = new Pair<>(c.getA(), c.getB());
                }
                else if(n < c.getA().length()){
                    Pair<Gramex, Gramex> cut = cut(c.getA(), n);
                    p = new Pair<>(cut.getA(), new GramexConcat(cut.getB().toGramexNonEmpty(), c.getB()));
                }
                else {
                    Pair<Gramex, Gramex> cut = cut(c.getB(), n-c.getA().length());
                    p = new Pair<>(new GramexConcat(c.getA(), cut.getA().toGramexNonEmpty()), cut.getB());
                }
            }
        }
        return p;
    }

    public static boolean equalsGramexConcats(GramexConcat a, GramexConcat b){
        boolean eq = a.getA().type() == b.getA().type();
        eq = eq && a.getB().type() == b.getB().type();
        if(eq){
            if(a.getA().type() != TypesGramex.CONCAT) eq = a.getA().equals(b.getA());
            else eq = equalsGramexConcats(a.getA().toGramexConcat(), b.getA().toGramexConcat());
            if(a.getB().type() != TypesGramex.CONCAT) eq = eq && a.getB().equals(b.getB());
            else eq = eq && equalsGramexConcats(a.getB().toGramexConcat(), b.getB().toGramexConcat());
        }
        return eq;
    }

    public static Gramex commonSuffix(Gramex a, Gramex b) {
        Gramex x = new GramexEmpty();
        if(a.equals(b)) x = a;
        else if(a.type() == TypesGramex.CHAR && b.type() == TypesGramex.CONCAT){
            x = null;
        }
        return null;
        /*
        empty empty  -> empty
        empty char   -> empty
        empty var    -> empty
        empty concat -> empty
        char empty   -> empty
        char char    -> empty || char if a=b
        char var     -> empty
        char concat  -> empty || char if concat.endsWith-char
        var empty    -> empty
        var char     -> empty
        var var      -> empty || var if a=b
        var concat   -> empty || var if concat.endswith-var
        concat empty -> empty
        concat char  -> empty || char if a.endswith-char
        concat var   -> empty || var if a.endswith-var
        concat concat->
         */
    }

    //More

    public static CfgNonEmpty renameVars(CfgNonEmpty a, CfgNonEmpty b){
        //Renames a
        CfgNonEmptyConstructor generator = b.getConstructor();
        Map<Gvar, Gvar> mapper = new HashMap<>();
        for(Gvar act : a.getVariables()){
            mapper.put(act, generator.generate(act));
        }

        CfgNonEmptyConstructor cc = new CfgNonEmptyConstructor();
        cc.variables.addAll(mapper.values());
        cc.terminals.addAll(a.getTerminals());
        cc.start = mapper.get(a.getStart());
        cc.acceptsEmptyWord = a.acceptsEmpty();

        for(GruleNonEmpty r : a.getRules()){
            Gvar left = mapper.get(r.getLeft());
            GramexNonEmpty right = substituteVarsToVars(r.getRight(), mapper);
            cc.rules.add(new GruleNonEmpty(left, right));
        }

        return cc.getCfgNonEmpty();
    }

    public static boolean correctVars(CfgNonEmpty cfga, CfgNonEmpty cfgb, Set<Gramex> left, Set<Gramex> right) {
        Gvar varLeft = null;
        Gvar varRight = null;

        Iterator<Gramex> itl = left.iterator();
        while(itl.hasNext() && varLeft == null){
            Gramex k = itl.next();
            if(containsVar(k)) varLeft = getLeftMostVar(k.toGramexNonEmpty());
        }

        Iterator<Gramex> itr = right.iterator();
        while(itr.hasNext() && varRight == null){
            Gramex k = itr.next();
            if(containsVar(k)) varRight = getLeftMostVar(k.toGramexNonEmpty());
        }

        if(varLeft != null && cfga.getVariables().contains(varLeft)) return true;
        if(varLeft != null && cfgb.getVariables().contains(varLeft)) return false;
        if(varRight != null && cfga.getVariables().contains(varRight)) return false;
        if(varRight != null && cfgb.getVariables().contains(varRight)) return true;
        return true;
    }

    public static Set<Gramex> findSuffixes(CfgNonEmpty cfg, Gramex r, String prefix) {
        Set<Gramex> set = new HashSet<>();

        Set<Gramex> expanded = new HashSet<>();
        Set<Gramex> toexpand = new HashSet<>();
        if(terminalPrefix(r).length() >= prefix.length()) expanded.add(r);
        else toexpand.add(r);

        while(!toexpand.isEmpty()){
            Set<Gramex> aux = new HashSet<>();
            for(Gramex ri : toexpand) {
                if(containsVar(ri)) aux.addAll(stepLeftMost(cfg,ri));
                else expanded.add(ri);
            }
            toexpand.clear();

            for(Gramex ri : aux){
                if(terminalPrefix(ri).length() >= prefix.length()) expanded.add(ri);
                else toexpand.add(ri);
            }
        }

        for(Gramex ri : expanded){
            if(terminalPrefix(ri).startsWith(prefix)) set.add(cut(r,prefix.length()).getB());
        }

        return set;
    }

    public static Set<Gramex> stepLeftMost(CfgNonEmpty cfg, Gramex r) {
        Set<Gramex> set = new HashSet<>();
        if(containsVar(r)){
            Gvar v = getLeftMostVar(r.toGramexNonEmpty());
            for(Gramex g : getRulesRightVar(cfg,v)){
                set.add(substituteLeftMost(r, v, g));
            }
        }
        return set;
    }

    public static String shortestWordOfVar(CfgNonEmpty cfg, Gvar v) {
        Map<Gvar, Set<Gramex>> mapper = new HashMap<>();
        for(GruleNonEmpty rule : cfg.getRules()){
            if(!mapper.containsKey(rule.getLeft())) mapper.put(rule.getLeft(), new HashSet<>());
            mapper.get(rule.getLeft()).add(rule.getRight());
        }

        Gramex shortest = null;
        Set<Gramex> pre = new HashSet<>();
        Set<Gramex> post = new HashSet<>();
        pre.add(new GramexVar(v));
        while(!pre.isEmpty()){
            for(Gramex r : pre){
                Gvar var = getLeftMostVar(r.toGramexNonEmpty());
                for(Gramex subs : mapper.get(var)) post.add(substituteLeftMost(r, var, subs));
            }
            pre.clear();
            for(Gramex r : post){
                if(!containsVar(r)){
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

    public static boolean acceptsAllWords(CfgNonEmpty cfg, GramexNonEmpty rule, List<String> words) {
        CfgNonEmptyConstructor aux = cfg.getConstructor();
        Gvar newStart = aux.generate(aux.start);
        aux.start = newStart;
        aux.rules.add(new GruleNonEmpty(newStart, rule));
        Pda parser = aux.getCfgNonEmpty().toCfg().toPda();

        boolean accepts = true;
        Iterator<String> it = words.iterator();
        while(it.hasNext() && accepts) accepts = parser.checkWord(it.next());

        return accepts;
    }

    public static int recommendedBagSize(CfgNonEmpty cfg) {
        return 5 * cfg.getVariables().size() * cfg.getRules().size();
    }

    public static boolean containsPair(Gramex g, GramexConcat pair){
        boolean x = false;
        if(g.type() == TypesGramex.CONCAT){
            GramexConcat c = g.toGramexConcat();
            x = c.equals(pair) || containsPair(c.getA(), pair) || containsPair(c.getB(), pair);
        }
        return x;
    }

    //Getters cfg

    public static Set<Grule> getRulesVar(Cfg cfg, Gvar v){
        Set<Grule> set = new HashSet<>();
        for(Grule r : cfg.getRules()){
            if(r.getLeft().equals(v)) set.add(r);
        }
        return set;
    }

    public static Set<GruleNonEmpty> getRulesVar(CfgNonEmpty cfg, Gvar v){
        Set<GruleNonEmpty> set = new HashSet<>();
        for(GruleNonEmpty r : cfg.getRules()){
            if(r.getLeft().equals(v)) set.add(r);
        }
        return set;
    }

    public static Set<Gramex> getRulesRightVar(Cfg cfg, Gvar v){
        Set<Gramex> set = new HashSet<>();
        for(Grule r : cfg.getRules()){
            if(r.getLeft().equals(v)) set.add(r.getRight());
        }
        return set;
    }

    public static Set<GramexNonEmpty> getRulesRightVar(CfgNonEmpty cfg, Gvar v){
        Set<GramexNonEmpty> set = new HashSet<>();
        for(GruleNonEmpty r : cfg.getRules()){
            if(r.getLeft().equals(v)) set.add(r.getRight());
        }
        return set;
    }

    public static Map<Gvar,Set<Gramex>> getMapperRules(Cfg cfg){
        Map<Gvar,Set<Gramex>> mapper = new HashMap<>();
        for(Grule r : cfg.getRules()){
            if(!mapper.containsKey(r.getLeft())) mapper.put(r.getLeft(), new HashSet<>());
            mapper.get(r.getLeft()).add(r.getRight());
        }
        return mapper;
    }

    public static Map<Gvar,Set<GramexNonEmpty>> getMapperRules(CfgNonEmpty cfg){
        Map<Gvar,Set<GramexNonEmpty>> mapper = new HashMap<>();
        for(GruleNonEmpty r : cfg.getRules()){
            if(!mapper.containsKey(r.getLeft())) mapper.put(r.getLeft(), new HashSet<>());
            mapper.get(r.getLeft()).add(r.getRight());
        }
        return mapper;
    }

}
