package GrammarComparisonArticle;

import Elements.Grammars.Gvar;
import Factory.GrammarTools;
import Grammars.*;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class GrammarComparator {
    public static boolean compare(CfgNonEmpty a, CfgNonEmpty b) {
        if(a.getRules().isEmpty() && b.getRules().isEmpty()) return a.acceptsEmpty() == b.acceptsEmpty();
        if(a.getRules().isEmpty() || b.getRules().isEmpty()) return false;
        if(a.acceptsEmpty() != b.acceptsEmpty()) return false;
        if(!a.getTerminals().equals(b.getTerminals())) return false;

        GrammarComparatorDataAux data = new GrammarComparatorDataAux(a,b);
        Comparison start = new Comparison(new GramexVar(data.cfga.getStart()), new GramexVar(data.cfgb.getStart()), true);
        Set<Comparison> context = new HashSet<>();
        Set<Comparison> toProve = new HashSet<>();
        toProve.add(start);

        boolean stop = false;
        boolean equal = false;
        while(!stop){
            Comparison act = toProve.iterator().next();
            toProve.remove(act);

            if(data.counter > 100000)                                        stop = true;
            else if(canApplyLengthFilter(act) && !lengthFilter(act, data))   stop = true;
            else if(canApplyEmptyOne(act))                                   toProve.addAll(verifyEmpty());
            else if(canApplyEmptyTwo(act))                                   toProve.addAll(verifyEmpty());
            else if(canApplyInduct(act, context))                            toProve.addAll(verifyInduct());
            else if(canApplyEpsilon(act))                                    toProve.add(verifyEpsilon(act));
            else if(canApplyTestcases(act) && checkTestcasesData(act, data)) toProve.add(verifyTestcases(data));
            else if(canApplyInclusion(act) && previousCheckInclusion(act))   toProve.addAll(verifyInclusion(act));
            else if(canApplySplit(act) && checkSplitData(act,data))          toProve.addAll(verifySplit(act, context, data));
            else if(canApplyDist(act))                                       toProve.addAll(verifyDist(act));
            else if(canApplyBranch(act, data))                               toProve.addAll(verifyBranch(act, context, data));
            else if(canApplyInclusion(act))                                  toProve.addAll(verifyInclusion(act));
            else                                                             stop = true;

            data.counter++;
            if(toProve.isEmpty() && !stop){
                stop = true;
                equal = true;
            }
        }

        return equal;
    }

    private static class GrammarComparatorDataAux {
        public CfgNonEmpty cfga;
        public CfgNonEmpty cfgb;
        public WordsGenerator wga;
        public WordsGenerator wgb;

        public int counter;
        public boolean leftIsA;

        public SplitData split;
        public TestcasesData testcases;
        public BranchData branch;

        public GrammarComparatorDataAux(CfgNonEmpty a, CfgNonEmpty b){
            cfga = GrammarTools.renameVars(a,b);
            cfgb = b;
            wga = new WordsGenerator(cfga);
            wgb = new WordsGenerator(cfgb);

            counter = 0;
            leftIsA = true;

            split = new SplitData();
            testcases = new TestcasesData();
            branch = new BranchData();
        }

        public void calculate(Comparison comp) {
            leftIsA = GrammarTools.correctVars(cfga, cfgb, comp.getLeft(), comp.getRight());
        }

        public CfgNonEmpty getCfgLeft() {
            if(leftIsA) return cfga;
            return cfgb;
        }

        public CfgNonEmpty getCfgRight() {
            if(leftIsA) return cfgb;
            return cfga;
        }

        public WordsGenerator getWordsGeneratorLeft() {
            if(leftIsA) return wga;
            return wgb;
        }

        public WordsGenerator getWordsGeneratorRight(){
            if(leftIsA) return wgb;
            return wga;
        }
    }

    private static class SplitData{
        public SplitData(){}
        public List<SplitElement> list;
        public Gramex gamma;
        public Gvar leftVar;
        public boolean everyBetaIsNonEmpty;
    }

    private static class TestcasesData{
        public TestcasesData(){}
        public Comparison result;
    }

    private static class BranchData{
        public BranchData(){}
        public Set<Comparison> result;
    }

    //Rules empty

    private static boolean canApplyEmptyOne(Comparison comp){
        //{} == {}
        return comp.isEquivalence() && comp.getLeft().isEmpty() && comp.getRight().isEmpty();
    }

    private static boolean canApplyEmptyTwo(Comparison comp){
        // {} in x
        return comp.isInclusion() && comp.getLeft().isEmpty();
    }

    private static Set<Comparison> verifyEmpty(){
        return new HashSet<>();
    }

    //Rule epsilon

    private static boolean canApplyEpsilon(Comparison comp){
        // {/} op {/}
        return comp.getLeft().contains(GramexEmpty.getInstance()) && comp.getRight().contains(GramexEmpty.getInstance());
    }

    private static Comparison verifyEpsilon(Comparison comp){
        // {/} op {/}
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

    private static Set<Comparison> verifyInduct(){
        return new HashSet<>();
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

    private static boolean canApplyBranch(Comparison comp, GrammarComparatorDataAux data){
        data.calculate(comp);
        CfgNonEmpty cfgl = data.getCfgLeft();
        CfgNonEmpty cfgr = data.getCfgRight();

        Set<Comparison> set = new HashSet<>();
        for(char c : cfgl.getTerminals().getSet()){
            Set<Gramex> dl = derivativeOfSet(Character.toString(c), comp.getLeft(), cfgl);
            Set<Gramex> dr = derivativeOfSet(Character.toString(c), comp.getRight(), cfgr);
            if(!dl.isEmpty() && !dr.isEmpty()) set.add(new Comparison(dl, dr, comp.isEquivalence()));
        }

        data.branch.result = set;
        return !set.isEmpty();
    }

    private static Set<Comparison> verifyBranch(Comparison comp, Set<Comparison> context, GrammarComparatorDataAux data){
        context.add(comp);
        return data.branch.result;
    }

    //Rule testcases

    private static boolean canApplyTestcases(Comparison comp){
        return comp.getLeft().size() == 1
                && comp.getRight().size() > 1
                && comp.isInclusion()
                && comp.getLeft().iterator().next().type() != TypesGramex.EMPTY;
    }

    private static boolean checkTestcasesData(Comparison comp, GrammarComparatorDataAux data){
        data.calculate(comp);
        CfgNonEmpty cfgr = data.getCfgRight();
        CfgNonEmpty cfgl = data.getCfgLeft();
        WordsGenerator alphal = data.getWordsGeneratorLeft();

        Gramex left = comp.getLeft().iterator().next();
        List<String> wordsBag = alphal.generateWords(GrammarTools.recommendedBagSize(cfgl), left.toGramexNonEmpty());

        Iterator<Gramex> it = comp.getRight().iterator();
        boolean found = false;
        while(it.hasNext() && !found){
            Gramex g = it.next();
            if(g.type() != TypesGramex.EMPTY){
                GramexNonEmpty s = g.toGramexNonEmpty();
                found = GrammarTools.acceptsAllWords(cfgr, s, wordsBag);
                if(found) data.testcases.result = new Comparison(comp.getLeft(), s, false);
            }
        }

        return found;
    }

    private static Comparison verifyTestcases(GrammarComparatorDataAux data){
        return data.testcases.result;
    }

    //Rule split

    private static boolean canApplySplit(Comparison comp){
        boolean ok = comp.getLeft().size() == 1;
        if(ok){
            Gramex left = comp.getLeft().iterator().next();
            ok = ok && left.length() > 1;
            ok = ok && GrammarTools.startsWithVar(left);
        }
        return ok;
    }

    private static boolean checkSplitData(Comparison comp, GrammarComparatorDataAux data){
        data.calculate(comp);
        CfgNonEmpty cfgl = data.getCfgLeft();
        CfgNonEmpty cfgr = data.getCfgRight();

        Gramex r = comp.getLeft().iterator().next();
        Gvar v = GrammarTools.getLeftMostVar(r.toGramexNonEmpty());
        String shortWord = GrammarTools.shortestWordOfGramex(cfgl, new GramexVar(v));
        Gramex gamma = GrammarTools.cut(r, 1).getB();

        boolean everyOmegaCanDeriveShort = true;
        boolean everyBetaIsNonEmpty = true;
        List<SplitElement> list = new ArrayList<>();

        //Iterate

        Iterator<Gramex> it = comp.getRight().iterator();
        while(it.hasNext() && everyOmegaCanDeriveShort){
            Gramex w = it.next();
            SplitElement spe = new SplitElement(w);
            everyOmegaCanDeriveShort = findOmegaBetaRo(spe, shortWord, cfgr);
            if(everyOmegaCanDeriveShort){
                list.add(spe);
                everyBetaIsNonEmpty = everyBetaIsNonEmpty && (spe.beta.type() != TypesGramex.EMPTY);
            }
        }

        data.split.list = list;
        data.split.gamma = gamma;
        data.split.leftVar = v;
        data.split.everyBetaIsNonEmpty = everyBetaIsNonEmpty;
        return everyOmegaCanDeriveShort;
    }

    private static Set<Comparison> verifySplit(Comparison comp, Set<Comparison> context, GrammarComparatorDataAux data){
        Set<Comparison> set = new HashSet<>();
        for(SplitElement spe : data.split.list){
            boolean betaIsEmpty = spe.beta.type() == TypesGramex.EMPTY;
            boolean roIsEmpty = spe.ro.type() == TypesGramex.EMPTY;
            GramexNonEmpty beta = null;
            GramexNonEmpty ro = null;
            if(!betaIsEmpty) beta = spe.beta.toGramexNonEmpty();
            if(!roIsEmpty) ro = spe.ro.toGramexNonEmpty();
            boolean op = comp.isEquivalence();

            // gamma op ro-beta
            if(betaIsEmpty) set.add(new Comparison(data.split.gamma, ro, op));
            else if(roIsEmpty) set.add(new Comparison(data.split.gamma, beta, op));
            else set.add(new Comparison(data.split.gamma, new GramexConcat(ro, beta), op));
            //var-ro op omega
            if(roIsEmpty) set.add(new Comparison(new GramexVar(data.split.leftVar), spe.omega, op));
            else set.add(new Comparison(new GramexConcat(new GramexVar(data.split.leftVar), ro) , spe.omega, op));
        }

        if(data.split.everyBetaIsNonEmpty) context.add(comp);
        return set;
    }

    private static boolean findOmegaBetaRo(SplitElement spe, String x, CfgNonEmpty gtr){
        // w = omega-beta
        // d(x,w) = ro-beta
        Set<Gramex> set = derivative(x, spe.w, gtr);
        if(set.isEmpty()) return false;

        int state = 0; //0=not-read, 1=found-beta-empty, 2=found-beta-non-empty
        for(Gramex roBetaCandidate : set){
            Gramex betaCandidate = GrammarTools.commonSuffix(roBetaCandidate, spe.w);
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

    private static boolean lengthFilter(Comparison comp, GrammarComparatorDataAux data){
        data.calculate(comp);
        CfgNonEmpty cfgl = data.getCfgLeft();
        CfgNonEmpty cfgr = data.getCfgRight();

        Gramex l = comp.getLeft().iterator().next();
        Gramex r = comp.getRight().iterator().next();

        String shortl = GrammarTools.shortestWordOfGramex(cfgl, l);
        String shortr = GrammarTools.shortestWordOfGramex(cfgr, r);

        if(shortl.length() < r.length()) return false;
        if(shortr.length() < l.length()) return false;
        return true;
    }
}
