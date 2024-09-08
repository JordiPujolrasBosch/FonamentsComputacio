package GrammarComparisonArticle;

import Automatons.Pda;
import Elements.Grammars.Grule;
import Elements.Grammars.GruleNonEmpty;
import Elements.Grammars.Gvar;
import Factory.Algorithms;
import Factory.Constructors.CfgConstructor;
import Factory.Constructors.CfgNonEmptyConstructor;
import Factory.GrammarTools;
import Grammars.*;

import java.util.*;

public class GrammarComparator {
    public static boolean compare(CfgNonEmpty a, CfgNonEmpty b) {
        if(a.getRules().isEmpty() && b.getRules().isEmpty()) return a.acceptsEmpty() == b.acceptsEmpty();
        if(a.getRules().isEmpty() || b.getRules().isEmpty()) return false;
        if(a.acceptsEmpty() != b.acceptsEmpty()) return false;
        if(!a.getTerminals().equals(b.getTerminals())) return false;

        GrammarComparatorData data = new GrammarComparatorData(a,b);
        Comparison start = new Comparison(new GramexVar(data.cfga.getStart()), new GramexVar(data.cfgb.getStart()), true);
        Set<Comparison> context = new HashSet<>();
        Set<Comparison> toProve = new HashSet<>();
        toProve.add(start);

        boolean stop = false;
        boolean equal = false;
        while(!stop){
            Comparison act = toProve.iterator().next();
            toProve.remove(act);

            if(data.counter > 100000)                                         stop = true;
            else if(canApplyLengthFilter(act) && !lengthFilter(act, data))    stop = true;
            else if(emptyCorrect(act))                                        nothing();
            else if(emptyNotCorrect(act))                                     stop = true;
            else if(epsilonCorrect(act))                                      toProve.add(verifyEpsilon(act));
            else if(epsilonNotCorrect(act))                                   stop = true;
            else if(canApplyInduct(act, context))                             nothing();
            else if(canApplySelf(act))                                        nothing();
            else if(canApplyTail(act,context))                                nothing();
            else if(canApplyLong(act) && checkLongData(act,data))             nothing();
            else if(canApplyLong(act))                                        stop = true;
            else if(canApplyInclusion(act) && previousCheckInclusion(act))    toProve.addAll(verifyInclusion(act));
            else if(canApplySplit(act) && checkSplitData(act,data))           toProve.addAll(verifySplit(act, context, data));
            else if(canApplyDist(act))                                        toProve.addAll(verifyDist(act));
            else if(canApplyTestcases(act) && checkTestcasesData(act, data))  toProve.add(verifyTestcases(data));
            else                                                              toProve.addAll(verifyBranch(act, context, data));

            data.counter++;
            if(toProve.isEmpty() && !stop){
                stop = true;
                equal = true;
            }
        }

        return equal;
    }

    private static class GrammarComparatorData {
        public CfgNonEmpty cfga;
        public CfgNonEmpty cfgb;
        public CfgNonEmpty cfgfusion;
        public WordsGenerator wgfusion;
        public int counter;
        public SplitData split;
        public TestcasesData testcases;

        public GrammarComparatorData(CfgNonEmpty a, CfgNonEmpty b){
            cfga = GrammarTools.renameVars(a,b);
            cfgb = b;
            cfgfusion = fusion(cfga,cfgb);
            wgfusion = new WordsGenerator(cfgfusion);
            counter = 0;
            split = new SplitData();
            testcases = new TestcasesData();
        }

        public CfgNonEmpty fusion(CfgNonEmpty a, CfgNonEmpty b){
            int v = 0;
            while(a.getVariables().contains(new Gvar('Z',v)) || b.getVariables().contains(new Gvar('Z',v))) v++;

            Gvar start = new Gvar('Z',v);

            Set<Gvar> vars = new HashSet<>();
            vars.addAll(a.getVariables());
            vars.addAll(b.getVariables());
            vars.add(start);

            Set<GruleNonEmpty> rules = new HashSet<>();
            rules.addAll(a.getRules());
            rules.addAll(b.getRules());
            rules.add(new GruleNonEmpty(start, new GramexVar(a.getStart())));
            rules.add(new GruleNonEmpty(start, new GramexVar(b.getStart())));

            return new CfgNonEmpty(a.getTerminals(), vars, start, rules, a.acceptsEmpty());
        }
    }

    private static class SplitData{
        public SplitData(){}
        public List<SplitElement> list;
        public Gramex gamma;
        public GramexNonEmpty alpha;
        public boolean everyBetaIsNonEmpty;
        public String x;
    }

    private static class TestcasesData{
        public TestcasesData(){}
        public Comparison result;
    }

    private static void nothing(){}

    //Rule empty

    private static boolean emptyCorrect(Comparison comp){
        // {} == {}, {} in {}, {} in {x}
        boolean a = comp.getLeft().isEmpty() && comp.getRight().isEmpty()  && comp.isEquivalence();
        boolean b = comp.getLeft().isEmpty() && comp.getRight().isEmpty()  && comp.isInclusion();
        boolean c = comp.getLeft().isEmpty() && !comp.getRight().isEmpty() && comp.isInclusion();
        return a || b || c;
    }

    private static boolean emptyNotCorrect(Comparison comp){
        // {} == {x}, {x} == {}, {x} in {}
        boolean a = comp.getLeft().isEmpty()  && !comp.getRight().isEmpty() && comp.isEquivalence();
        boolean b = !comp.getLeft().isEmpty() && comp.getRight().isEmpty()  && comp.isEquivalence();
        boolean c = !comp.getLeft().isEmpty() && comp.getRight().isEmpty()  && comp.isInclusion();
        return a || b || c;
    }

    //Rule epsilon

    private static boolean epsilonCorrect(Comparison comp){
        // {/} == {/}, {/} in {/}, {x} in {/}
        boolean l = comp.getLeft().contains(GramexEmpty.getInstance());
        boolean r = comp.getRight().contains(GramexEmpty.getInstance());

        boolean a = l  && r && comp.isEquivalence();
        boolean b = l  && r && comp.isInclusion();
        boolean c = !l && r && comp.isInclusion();
        return a || b || c;
    }

    private static boolean epsilonNotCorrect(Comparison comp){
        // {/} == {x}, {x} == {/}, {/} in {x}
        boolean l = comp.getLeft().contains(GramexEmpty.getInstance());
        boolean r = comp.getRight().contains(GramexEmpty.getInstance());

        boolean a = l  && !r && comp.isEquivalence();
        boolean b = !l && r  && comp.isEquivalence();
        boolean c = l  && !r && comp.isInclusion();
        return a || b || c;
    }

    private static Comparison verifyEpsilon(Comparison comp){
        Set<Gramex> left  = new HashSet<>(comp.getLeft());
        left.remove(GramexEmpty.getInstance());
        Set<Gramex> right = new HashSet<>(comp.getRight());
        right.remove(GramexEmpty.getInstance());
        return new Comparison(left, right, comp.isEquivalence());
    }

    //Rule induct

    private static boolean canApplyInduct(Comparison comp, Set<Comparison> context){
        // check in context
        Set<Gramex> left  = comp.getLeft();
        Set<Gramex> right = comp.getRight();

        Comparison xeqy = new Comparison(left, right, true);
        Comparison yeqx = new Comparison(right, left, true);
        Comparison xiny = new Comparison(left, right, false);

        if(comp.isEquivalence()) return context.contains(xeqy) || context.contains(yeqx);
        return context.contains(xeqy) || context.contains(yeqx) || context.contains(xiny);
    }

    //Rule self

    private static boolean canApplySelf(Comparison comp){
        // {x} in {x}, {x} in {x,y}
        boolean ok = comp.getLeft().size() == 1 && comp.isInclusion() && !comp.getRight().isEmpty();
        ok = ok && comp.getRight().contains(comp.getLeft().iterator().next());
        return ok;
    }

    //Rule tail

    private static boolean canApplyTail(Comparison comp, Set<Comparison> context){
        //{a} in {c,d} act
        //{a} in {d}   context
        boolean found = false;
        Iterator<Comparison> it = context.iterator();
        while (it.hasNext() && !found){
            Comparison aux = it.next();
            found = comp.getLeft().equals(aux.getLeft())
                    && comp.isInclusion()
                    && aux.isInclusion()
                    && comp.getRight().containsAll(aux.getRight());
        }
        return found;
    }

    //Rule dist

    private static boolean canApplyDist(Comparison comp){
        return comp.getLeft().size() > 1 && comp.isInclusion();
    }

    private static Set<Comparison> verifyDist(Comparison comp){
        Set<Comparison> set = new HashSet<>();
        for(Gramex x : comp.getLeft()){
            set.add(new Comparison(x, comp.getRight(), false));
        }
        return set;
    }

    //Rule inclusion

    private static boolean canApplyInclusion(Comparison comp){
        return comp.isEquivalence();
    }

    private static boolean previousCheckInclusion(Comparison comp){
        return comp.getLeft().size() > 1 || comp.getRight().size() > 1;
    }

    private static Set<Comparison> verifyInclusion(Comparison comp){
        Set<Comparison> set = new HashSet<>();
        set.add(new Comparison(comp.getLeft(), comp.getRight(), false));
        set.add(new Comparison(comp.getRight(), comp.getLeft(), false));
        return set;
    }

    //Rule branch

    private static Set<Comparison> verifyBranch(Comparison comp, Set<Comparison> context, GrammarComparatorData data){
        Set<Comparison> set = new HashSet<>();
        for(char c : data.cfga.getTerminals().getSet()){
            Set<Gramex> dl = derivativeOfSet(Character.toString(c), comp.getLeft(), data.cfgfusion);
            Set<Gramex> dr = derivativeOfSet(Character.toString(c), comp.getRight(), data.cfgfusion);
            set.add(new Comparison(dl, dr, comp.isEquivalence()));
        }
        context.add(comp);
        return set;
    }

    //Rule long

    private static boolean canApplyLong(Comparison comp){
        boolean found = false;
        Iterator<Gramex> it = comp.getLeft().iterator();
        while (it.hasNext() && !found) found = it.next().length() > 6;
        return (comp.size() > 30 || found) && comp.isInclusion();
    }

    private static boolean checkLongData(Comparison comp, GrammarComparatorData data){
        Set<String> wordsBag = new HashSet<>();
        int n = 100;
        if(comp.getLeft().size() < 4) n = 200;
        for(Gramex g : comp.getLeft()) wordsBag.addAll(data.wgfusion.generateWords(n, g.toGramexNonEmpty()));

        CfgConstructor aux = data.cfgfusion.toCfg().getConstructor();
        Gvar newStart = aux.generate(aux.start);
        aux.start = newStart;
        for(Gramex g : comp.getRight()) aux.rules.add(new Grule(newStart, g));
        Pda parser = aux.getCfg().simplify().toCfg().toPda();

        boolean accepts = true;
        Iterator<String> it = wordsBag.iterator();
        while (it.hasNext() && accepts) accepts = parser.checkWord(it.next());

        return accepts;
    }

    //Rule testcases

    private static boolean canApplyTestcases(Comparison comp){
        return comp.getLeft().size() == 1
                && comp.getRight().size() > 1
                && comp.isInclusion()
                && comp.getLeft().iterator().next().type() != TypesGramex.EMPTY;
    }

    private static boolean checkTestcasesData(Comparison comp, GrammarComparatorData data){
        //words
        Gramex left = comp.getLeft().iterator().next();
        List<String> wordsBag = new ArrayList<>();
        if(left.length() < 6) wordsBag.addAll(generateShortWords(left.toGramexNonEmpty(),data));
        wordsBag.addAll(data.wgfusion.generateWords(200, left.toGramexNonEmpty()));

        //search var
        boolean foundVar = false;
        GramexVar var = null;
        Iterator<Gramex> it1 = comp.getRight().iterator();
        while (it1.hasNext() && !foundVar) {
            Gramex g = it1.next();
            foundVar = g.type() == TypesGramex.VAR;
            if(foundVar) var = g.toGramexVar();
        }

        //check var
        if(foundVar){
            if (GrammarTools.acceptsAllWords(data.cfgfusion, var, wordsBag)){
                data.testcases.result = new Comparison(comp.getLeft(), var, false);
                return true;
            }
        }

        //sort gramex
        List<Gramex> list = new ArrayList<>(comp.getRight().stream().toList());
        list.sort(Comparator.comparingInt(Gramex::length));

        //check for length
        int i = list.size()-1;
        boolean found = false;
        while(i>=0 && !found){
            Gramex g = list.get(i);
            Set<Gramex> aux = new HashSet<>(comp.getRight());
            aux.remove(g);
            found = GrammarTools.setAcceptsAllWords(data.cfgfusion, aux, wordsBag);
            if(found) data.testcases.result = new Comparison(comp.getLeft(), aux, false);
            i--;
        }

        return found;
    }

    private static List<String> generateShortWords(GramexNonEmpty g, GrammarComparatorData data){
        CfgNonEmptyConstructor cc = data.cfgfusion.getConstructor();
        Gvar newStart = cc.generate(cc.start);
        cc.start = newStart;
        cc.rules.add(new GruleNonEmpty(newStart, g));
        CfgNonEmpty aux = cc.getCfgNonEmpty().toCfg().simplify();

        String x = GrammarTools.shortestWordOfGramex(data.cfgfusion, g);
        CfgNonEmpty shortCfg = Algorithms.buildCfgLengthFromTo(aux.toChomsky(), x.length(), x.length()+2);
        WordsGenerator wg = new WordsGenerator(shortCfg);
        return wg.generateAllWordsStart();
    }

    private static Comparison verifyTestcases(GrammarComparatorData data){
        return data.testcases.result;
    }

    //Rule split

    private static boolean canApplySplit(Comparison comp){
        boolean ok = comp.getLeft().size() == 1
                && comp.getRight().size() > 0
                && comp.getLeft().iterator().next().length() > 1;
        if(ok){
            boolean found = false;
            Iterator<Gramex> it = comp.getRight().iterator();
            while (it.hasNext() && !found) {
                Gramex x = it.next();
                found = (x.length() < 2 || !GrammarTools.startsWithVar(x));
            }
            if(found) ok = false;
        }
        return ok;
    }

    private static boolean checkSplitData(Comparison comp, GrammarComparatorData data){
        Gramex r = comp.getLeft().iterator().next();
        GramexNonEmpty a = GrammarTools.getLeftMostElement(r.toGramexNonEmpty()).toGramexNonEmpty();
        String shortWord = GrammarTools.shortestWordOfGramex(data.cfgfusion, a);
        Gramex gamma = GrammarTools.cut(r, 1).getB();

        boolean everyOmegaCanDeriveShort = true;
        boolean everyBetaIsNonEmpty = true;
        List<SplitElement> list = new ArrayList<>();

        //Iterate

        Iterator<Gramex> it = comp.getRight().iterator();
        while(it.hasNext() && everyOmegaCanDeriveShort){
            SplitElement spe = new SplitElement(it.next());
            everyOmegaCanDeriveShort = findOmegaBetaRo(spe, shortWord, data.cfgfusion);
            if(everyOmegaCanDeriveShort){
                list.add(spe);
                everyBetaIsNonEmpty = everyBetaIsNonEmpty && (spe.beta.type() != TypesGramex.EMPTY);
            }
        }

        data.split.list = list;
        data.split.gamma = gamma;
        data.split.alpha = a;
        data.split.everyBetaIsNonEmpty = everyBetaIsNonEmpty;
        data.split.x = shortWord;
        return everyOmegaCanDeriveShort;
    }

    private static Set<Comparison> verifySplit(Comparison comp, Set<Comparison> context, GrammarComparatorData data){
        Set<Comparison> set = new HashSet<>();
        //gamma op d(x,Right)
        set.add(new Comparison(data.split.gamma, derivativeOfSet(data.split.x, comp.getRight(), data.cfgfusion) , comp.isEquivalence()));
        for(SplitElement spe : data.split.list){
            boolean roIsEmpty = spe.ro.type() == TypesGramex.EMPTY;
            GramexNonEmpty ro = null;
            if(!roIsEmpty) ro = spe.ro.toGramexNonEmpty();
            boolean op = comp.isEquivalence();

            //alpha-ro op omega
            if(roIsEmpty) set.add(new Comparison(data.split.alpha, spe.omega, op));
            else set.add(new Comparison(new GramexConcat(data.split.alpha, ro) , spe.omega, op));
        }

        if(data.split.everyBetaIsNonEmpty) context.add(comp);
        return set;
    }

    private static boolean findOmegaBetaRo(SplitElement spe, String x, CfgNonEmpty cfg){
        // w = omega-beta
        // d(x,w) = ro-beta
        Set<Gramex> set = derivative(x, spe.w, cfg);
        if(set.isEmpty()) return false;

        int state = 0; //0=not-read, 1=found-beta-empty, 2=found-beta-non-empty
        for(Gramex roBetaCandidate : set){
            Gramex betaCandidate = GrammarTools.commonSuffix(roBetaCandidate, spe.w);
            if(spe.w.equals(betaCandidate)) betaCandidate = GramexEmpty.getInstance();

            if(state == 0){
                spe.beta = betaCandidate;
                spe.ro = GrammarTools.cut(roBetaCandidate, roBetaCandidate.length() - betaCandidate.length()).getA();
                spe.omega = GrammarTools.cut(spe.w, spe.w.length() - betaCandidate.length()).getA();
                if(betaCandidate.type() == TypesGramex.EMPTY) state = 1;
                else state = 2;
            }
            else if(state == 1 && betaCandidate.type() != TypesGramex.EMPTY){
                spe.beta = betaCandidate;
                spe.ro = GrammarTools.cut(roBetaCandidate, roBetaCandidate.length() - betaCandidate.length()).getA();
                spe.omega = GrammarTools.cut(spe.w, spe.w.length() - betaCandidate.length()).getA();
                state = 2;
            }
        }
        return true;
    }

    private static class SplitElement {
        public Gramex w;
        public Gramex omega;
        public Gramex beta;
        public Gramex ro;
        public SplitElement(Gramex w){this.w = w;}
    }

    //Derivative

    private static Set<Gramex> derivative(String word, Gramex rule, CfgNonEmpty cfg){
        Set<Gramex> set = new HashSet<>();
        if(GrammarTools.terminalPrefix(rule).startsWith(word)) set.add(GrammarTools.cut(rule, word.length()).getB());
        else set.addAll(GrammarTools.findSuffixes(cfg, rule, word));
        return set;
    }

    private static Set<Gramex> derivativeOfSet(String word, Set<Gramex> rules, CfgNonEmpty cfg) {
        Set<Gramex> set = new HashSet<>();
        for(Gramex r : rules) set.addAll(derivative(word, r, cfg));
        return set;
    }

    //Filters

    private static boolean canApplyLengthFilter(Comparison comp){
        return comp.getLeft().size() == 1 && comp.getRight().size() == 1 && comp.isEquivalence();
    }

    private static boolean lengthFilter(Comparison comp, GrammarComparatorData data){
        Gramex l = comp.getLeft().iterator().next();
        Gramex r = comp.getRight().iterator().next();

        String shortl = GrammarTools.shortestWordOfGramex(data.cfgfusion, l);
        String shortr = GrammarTools.shortestWordOfGramex(data.cfgfusion, r);

        return shortl.length() >= r.length() && shortr.length() >= l.length();
    }
}
