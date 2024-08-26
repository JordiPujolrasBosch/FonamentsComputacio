package Factory;

import Automatons.Pda;
import Elements.Grammars.*;
import Factory.Constructors.CfgNonEmptyConstructor;
import Grammars.*;
import Utils.Pair;

import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GrammarTools {
    // Gramex: starts, ends and contains

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

    //Getters gramex

    public static Gramex getLeftMostElement(Gramex g){
        if(g.type() == TypesGramex.CONCAT) return getLeftMostElement(g.toGramexConcat().getA());
        return g;
    }

    public static Gramex getRightMostElement(Gramex g) {
        if(g.type() == TypesGramex.CONCAT) return getRightMostElement(g.toGramexConcat().getB());
        return g;
    }

    public static Gvar getLeftMostVar(Gramex g){
        Gvar x = null;
        switch (g.type()){
            case VAR -> x = g.toGramexVar().getV();
            case CONCAT -> {
                x = getLeftMostVar(g.toGramexConcat().getA());
                if(x == null) x = getLeftMostVar(g.toGramexConcat().getB());
            }
        }
        return x;
    }

    public static Gvar getRightMostVar(Gramex g){
        Gvar x = null;
        switch (g.type()){
            case VAR -> x = g.toGramexVar().getV();
            case CONCAT -> {
                x = getRightMostVar(g.toGramexConcat().getB());
                if(x == null) x = getRightMostVar(g.toGramexConcat().getA());
            }
        }
        return x;
    }

    public static Character getLeftMostChar(Gramex g){
        Character x = null;
        switch (g.type()){
            case CHAR -> x = g.toGramexChar().getC();
            case CONCAT -> {
                x = getLeftMostChar(g.toGramexConcat().getA());
                if(x == null) x = getLeftMostChar(g.toGramexConcat().getB());
            }
        }
        return x;
    }

    public static Character getRightMostChar(Gramex g){
        Character x = null;
        switch (g.type()){
            case CHAR -> x = g.toGramexChar().getC();
            case CONCAT -> {
                x = getRightMostChar(g.toGramexConcat().getB());
                if(x == null) x = getRightMostChar(g.toGramexConcat().getA());
            }
        }
        return x;
    }

    public static GramexConcat getLeftMostPair(GramexConcat g){
        if(g.getA().length() == 2) return g.getA().toGramexConcat();
        if(g.getA().length() > 2)  return getLeftMostPair(g.getA().toGramexConcat());
        if(g.getB().length() == 2) return g.getB().toGramexConcat();
        if(g.getB().length() > 2)  return getLeftMostPair(g.getB().toGramexConcat());
        return g;
    }

    public static GramexConcat getRightMostPair(GramexConcat g){
        if(g.getB().length() == 2) return g.getB().toGramexConcat();
        if(g.getB().length() > 2)  return getRightMostPair(g.getB().toGramexConcat());
        if(g.getA().length() == 2) return g.getA().toGramexConcat();
        if(g.getA().length() > 2)  return getRightMostPair(g.getA().toGramexConcat());
        return g;
    }

    //Consult gramex

    public static String terminalPrefix(Gramex g) {
        String x = "";
        switch (g.type()){
            case CHAR -> x = Character.toString(g.toGramexChar().getC());
            case CONCAT -> {
                GramexConcat c = g.toGramexConcat();
                if(containsVar(c.getA()) || startsWithVar(c.getB())) x = terminalPrefix(c.getA());
                else x = terminalPrefix(c.getA()) + terminalPrefix(c.getB());
            }
        }
        return x;
    }

    public static Pair<Gramex, Gramex> cut(Gramex g, int n) {
        Pair<Gramex, Gramex> p = new Pair<>(GramexEmpty.getInstance(), GramexEmpty.getInstance());
        switch (g.type()){
            case VAR, CHAR -> {
                if(n > 0) p = new Pair<>(g, GramexEmpty.getInstance());
                else p = new Pair<>(GramexEmpty.getInstance(), g);
            }
            case CONCAT -> {
                GramexConcat c = g.toGramexConcat();
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

    public static String makeWord(Gramex g){
        if(g == null) return "";
        String out = "";

        switch (g.type()){
            case CONCAT -> out = makeWord(g.toGramexConcat().getA()) + makeWord(g.toGramexConcat().getB());
            case CHAR -> out = String.valueOf(g.toGramexChar().getC());
            case VAR -> out = g.toGramexVar().toString();
        }

        return out;
    }

    //Step

    public static Set<Gramex> step(Cfg cfg, Gramex g){
        Random rand = new Random();
        return rand.nextBoolean() ? stepLeft(cfg,g) : stepRight(cfg,g);
    }

    public static Set<Gramex> step(CfgNonEmpty cfg, Gramex g){
        Random rand = new Random();
        return rand.nextBoolean() ? stepLeft(cfg,g) : stepRight(cfg,g);
    }

    public static Set<Gramex> stepLeft(Cfg cfg, Gramex g){
        Set<Gramex> set = new HashSet<>();
        if(containsVar(g)){
            Gvar v = getLeftMostVar(g.toGramexNonEmpty());
            for(Gramex s : getRulesRightVar(cfg,v)) set.add(substituteLeftMost(g, v, s));
        }
        return set;
    }

    public static Set<Gramex> stepLeft(CfgNonEmpty cfg, Gramex g){
        Set<Gramex> set = new HashSet<>();
        if(containsVar(g)){
            Gvar v = getLeftMostVar(g.toGramexNonEmpty());
            for(Gramex s : getRulesRightVar(cfg,v)) set.add(substituteLeftMost(g, v, s));
        }
        return set;
    }

    public static Set<Gramex> stepRight(Cfg cfg, Gramex g){
        Set<Gramex> set = new HashSet<>();
        if(containsVar(g)){
            Gvar v = getRightMostVar(g.toGramexNonEmpty());
            for(Gramex s : getRulesRightVar(cfg,v)) set.add(substituteRightMost(g, v, s));
        }
        return set;
    }

    public static Set<Gramex> stepRight(CfgNonEmpty cfg, Gramex g){
        Set<Gramex> set = new HashSet<>();
        if(containsVar(g)){
            Gvar v = getRightMostVar(g.toGramexNonEmpty());
            for(Gramex s : getRulesRightVar(cfg,v)) set.add(substituteRightMost(g, v, s));
        }
        return set;
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

    public static Gramex substituteRightMost(Gramex original, Gvar v, Gramex substitute) {
        Gramex x = original;
        switch (original.type()){
            case VAR -> {
                if(original.toGramexVar().getV().equals(v)) x = substitute;
            }
            case CONCAT -> {
                GramexConcat c = original.toGramexConcat();
                if(containsVar(c.getB(), v)){
                    if(c.getB().type() == TypesGramex.VAR && substitute.type() == TypesGramex.EMPTY) x = c.getA();
                    else x = new GramexConcat(c.getA(), substituteRightMost(c.getB(), v, substitute).toGramexNonEmpty());
                }
                else if(containsVar(c.getA(), v)){
                    if(c.getA().type() == TypesGramex.VAR && substitute.type() == TypesGramex.EMPTY) x = c.getB();
                    else x = new GramexConcat(substituteRightMost(c.getA(), v, substitute).toGramexNonEmpty(), c.getB());
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
            GramexConcat c = original.toGramexConcat();
            if(equalsGramexConcats(c,pair)) x = new GramexVar(substitute);
            else{
                GramexNonEmpty a = substituteConcats(c.getA(), pair, substitute).toGramexNonEmpty();
                GramexNonEmpty b = substituteConcats(c.getB(), pair, substitute).toGramexNonEmpty();
                x = new GramexConcat(a,b);
            }
        }
        return x;
    }

    public static Gramex substituteVarsToVars(Gramex original, Map<Gvar, Gvar> mapper){
        Gramex x = original;
        switch (original.type()){
            case VAR -> {
                if(mapper.containsKey(original.toGramexVar().getV())) x = new GramexVar(mapper.get(original.toGramexVar().getV()));
            }
            case CONCAT -> {
                GramexNonEmpty a = substituteVarsToVars(original.toGramexConcat().getA(),mapper).toGramexNonEmpty();
                GramexNonEmpty b = substituteVarsToVars(original.toGramexConcat().getB(),mapper).toGramexNonEmpty();
                x = new GramexConcat(a,b);
            }
        }
        return x;
    }

    //Consult gramex-gramex

    public static boolean equalsGramexConcats(GramexConcat a, GramexConcat b){
        boolean eq = a.getA().type() == b.getA().type() && a.getB().type() == b.getB().type();
        if(eq){
            if(a.getA().type() != TypesGramex.CONCAT) eq = a.getA().equals(b.getA());
            else eq = equalsGramexConcats(a.getA().toGramexConcat(), b.getA().toGramexConcat());
            if(a.getB().type() != TypesGramex.CONCAT) eq = eq && a.getB().equals(b.getB());
            else eq = eq && equalsGramexConcats(a.getB().toGramexConcat(), b.getB().toGramexConcat());
        }
        return eq;
    }

    public static Gramex commonSuffix(Gramex a, Gramex b) {
        if(a.type() == TypesGramex.EMPTY || b.type() == TypesGramex.EMPTY) return GramexEmpty.getInstance();

        GramexNonEmpty gnea = getRightMostElement(a).toGramexNonEmpty();
        GramexNonEmpty gneb = getRightMostElement(b).toGramexNonEmpty();

        if(gnea.type() != gneb.type()) return GramexEmpty.getInstance();
        if(gnea.equals(gneb)) return gnea;
        return GramexEmpty.getInstance();
    }

    public static boolean containsPair(Gramex g, GramexConcat pair){
        boolean x = false;
        if(g.type() == TypesGramex.CONCAT){
            GramexConcat c = g.toGramexConcat();
            x = equalsGramexConcats(c,pair) || containsPair(c.getA(), pair) || containsPair(c.getB(), pair);
        }
        return x;
    }

    //Consult cfg-gramex

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

    public static String shortestWordOfGramex(Cfg cfg, Gramex g) {
        if(!containsVar(g)) return makeWord(g);

        Gramex shortest = null;
        Set<Gramex> pre = new HashSet<>();
        Set<Gramex> post = new HashSet<>();

        pre.add(g);
        while(!pre.isEmpty()){
            for(Gramex r : pre) post.addAll(step(cfg,r));
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

        return makeWord(shortest);
    }

    public static String shortestWordOfGramex(CfgNonEmpty cfg, Gramex g) {
        if(!containsVar(g)) return makeWord(g);

        Gramex shortest = null;
        Set<Gramex> pre = new HashSet<>();
        Set<Gramex> post = new HashSet<>();

        pre.add(g);
        while(!pre.isEmpty()){
            for(Gramex r : pre) post.addAll(step(cfg,r));
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

        return makeWord(shortest);
    }

    public static Set<Gramex> findSuffixes(CfgNonEmpty cfg, Gramex g, String prefix) {
        Set<Gramex> set = new HashSet<>();

        int counter = 0;
        Set<Gramex> expanded = new HashSet<>();
        Set<Gramex> toexpand = new HashSet<>();
        if(terminalPrefix(g).length() >= prefix.length()) expanded.add(g);
        else toexpand.add(g);

        while(!toexpand.isEmpty() && counter < prefix.length()+2){
            Set<Gramex> aux = new HashSet<>();
            for(Gramex r : toexpand) {
                if(containsVar(r)) aux.addAll(stepLeft(cfg,r));
                else expanded.add(r);
            }
            toexpand.clear();

            for(Gramex r : aux){
                if(terminalPrefix(r).length() >= prefix.length()) expanded.add(r);
                else toexpand.add(r);
            }

            counter++;
        }

        for(Gramex r : expanded){
            if(terminalPrefix(r).startsWith(prefix)) set.add(cut(r,prefix.length()).getB());
        }

        return set;
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

    //Other

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
            GramexNonEmpty right = substituteVarsToVars(r.getRight(), mapper).toGramexNonEmpty();
            cc.rules.add(new GruleNonEmpty(left, right));
        }

        return cc.getCfgNonEmpty();
    }

    public static boolean correctVars(CfgNonEmpty cfga, CfgNonEmpty cfgb, Set<Gramex> left, Set<Gramex> right) {
        Gvar varLeft = null;

        Iterator<Gramex> itl = left.iterator();
        while(itl.hasNext() && varLeft == null){
            Gramex k = itl.next();
            if(containsVar(k)) varLeft = getLeftMostVar(k.toGramexNonEmpty());
        }

        if(varLeft != null) return cfga.getVariables().contains(varLeft);

        Gvar varRight = null;

        Iterator<Gramex> itr = right.iterator();
        while(itr.hasNext() && varRight == null){
            Gramex k = itr.next();
            if(containsVar(k)) varRight = getLeftMostVar(k.toGramexNonEmpty());
        }

        if(varRight != null) return cfgb.getVariables().contains(varRight);
        return true;
    }

    public static int recommendedBagSize(CfgNonEmpty cfg) {
        return Math.min(5 * cfg.getVariables().size() * cfg.getRules().size(), 200);
    }

}
