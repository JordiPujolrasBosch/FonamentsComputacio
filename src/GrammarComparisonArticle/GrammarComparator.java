package GrammarComparisonArticle;

import Elements.Grammars.Gvar;
import Factory.GrammarTools;
import Grammars.*;

import java.util.*;

public class GrammarComparator {
    public static boolean compareAux(CfgNonEmpty a, CfgNonEmpty b) {
        if(a.getRules().isEmpty() && b.getRules().isEmpty()) return a.acceptsEmpty() == b.acceptsEmpty();
        if(a.getRules().isEmpty() || b.getRules().isEmpty()) return false;
        if(a.acceptsEmpty() != b.acceptsEmpty()) return false;
        if(!a.getTerminals().equals(b.getTerminals())) return false;


        GrammarComparatorDataAux data = new GrammarComparatorDataAux();
        data.cfga = GrammarTools.renameVars(a,b);
        data.cfgb = b;
        data.wga = new WordsGenerator(data.cfga);
        data.wgb = new WordsGenerator(data.cfgb);
        data.counter = 0;
        data.split = new SplitData();
        data.testcases = new TestcasesData();

        Comparison start = new Comparison(new GramexVar(data.cfga.getStart()), new GramexVar(data.cfgb.getStart()), true);
        Set<Comparison> context = new HashSet<>();
        Set<Comparison> toProve = verifyBranch(start, context, data);

        boolean stop = toProve.isEmpty();
        boolean equal = false;
        while(!stop){
            Comparison act = toProve.iterator().next();
            toProve.remove(act);

            if(data.counter > 70000) stop = true;
            else if(canApplyInclusion2(act) && previousCheckInclusion2(act))   toProve.addAll(verifyInclusion2(act));
            else if(canApplySplit2(act) && checkSplitData2(act,data))          toProve.addAll(verifySplit2(act, context, data));
            else if(canApplyEmptyOne2(act))                                    toProve.addAll(verifyEmpty2());
            else if(canApplyEmptyTwo2(act))                                    toProve.addAll(verifyEmpty2());
            else if(canApplyInduct2(act, context))                             toProve.addAll(verifyInduct2());
            else if(canApplyEpsilon2(act))                                     toProve.add(verifyEpsilon2(act));
            else if(canApplyDist2(act))                                        toProve.addAll(verifyDist2(act));
            else if(canApplyTestcases2(act) && checkTestcasesData2(act, data)) toProve.add(verifyTestcases2(data));
            else if(canApplyInclusion2(act))                                   toProve.addAll(verifyInclusion2(act));
            else if(canApplyBranch2(act, context, data))                       toProve.addAll(verifyBranch2(act, context, data));
            else stop = true;

            data.counter++;
            if(toProve.isEmpty()){
                stop = true;
                equal = true;
            }
        }

        return equal;
    }

    private static class GrammarComparatorDataAux {
        public GrammarComparatorDataAux(){}
        public CfgNonEmpty cfga;
        public CfgNonEmpty cfgb;
        public WordsGenerator wga;
        public WordsGenerator wgb;
        public int counter;
        public SplitData split;
        public TestcasesData testcases;
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

    //Rules empty

    public static boolean canApplyEmptyOne2(Comparison comp){
        // empty == empty
        return comp.getLeft().isEmpty() && comp.getRight().isEmpty() && comp.isEquivalence();
    }

    public static boolean canApplyEmptyTwo2(Comparison comp){
        // empty in x
        return comp.getLeft().isEmpty() && comp.isInclusion();
    }

    private static Set<Comparison> verifyEmpty2(){
        return new HashSet<>();
    }

    //Rule epsilon

    private static boolean canApplyEpsilon2(Comparison comp){
        // {e} op {e}
        return comp.getLeft().contains(new GramexEmpty()) && comp.getRight().contains(new GramexEmpty());
    }

    private static Comparison verifyEpsilon2(Comparison comp){
        // {e} op {e}
        Set<Gramex> left  = new HashSet<>(comp.getLeft());
        left.remove(new GramexEmpty());
        Set<Gramex> right = new HashSet<>(comp.getRight());
        right.remove(new GramexEmpty());
        return new Comparison(left, right, comp.isEquivalence());
    }

    //Rule induct

    private static boolean canApplyInduct2(Comparison comp, Set<Comparison> context){
        // check in context
        Set<Gramex> left  = comp.getLeft();
        Set<Gramex> right = comp.getRight();

        Comparison xeqy = new Comparison(left, right, true);
        Comparison yeqx = new Comparison(right, left, true);
        Comparison xiny = new Comparison(left, right, false);

        if(comp.isEquivalence()) return context.contains(xeqy) || context.contains(yeqx);
        return context.contains(xeqy) || context.contains(yeqx) || context.contains(xiny);
    }

    private static Set<Comparison> verifyInduct2(){
        return new HashSet<>();
    }

    //Rule dist

    private static boolean canApplyDist2(Comparison comp){
        return comp.getLeft().size() > 1 && comp.isInclusion();
    }

    public static Set<Comparison> verifyDist2(Comparison comp){
        Set<Comparison> set = new HashSet<>();
        for(Gramex x : comp.getLeft()){
            set.add(new Comparison(x, comp.getRight(), false));
        }
        return set;
    }

    //Rule inclusion

    private static boolean canApplyInclusion2(Comparison comp){
        return comp.isEquivalence();
    }

    private static boolean previousCheckInclusion2(Comparison comp){
        return comp.getLeft().size() > 1 || comp.getRight().size() > 1;
    }

    private static Set<Comparison> verifyInclusion2(Comparison comp){
        Set<Comparison> set = new HashSet<>();
        set.add(new Comparison(comp.getLeft(), comp.getRight(), false));
        set.add(new Comparison(comp.getRight(), comp.getLeft(), false));
        return set;
    }

    //Rule testcases

    private static boolean canApplyTestcases2(Comparison comp){
        return comp.getLeft().size() == 1 && comp.getRight().size() > 1 && comp.isInclusion();
    }

    private static boolean checkTestcasesData2(Comparison comp, GrammarComparatorDataAux data){
        CfgNonEmpty cfgr = data.getCfgRight(comp);
        CfgNonEmpty cfgl = data.getCfgLeft(comp);
        WordsGenerator alphal = data.getWordsGeneratorLeft(comp);

        Comparison res = null;
        Gramex left = comp.getLeft().iterator().next();
        List<String> wordsBag = alphal.generateWords(GrammarTools.recommendedBagSize(cfgl), left.toGramexNonEmpty());
        Iterator<Gramex> it = comp.getRight().iterator();
        boolean found = false;
        while(it.hasNext() && !found){
            GramexNonEmpty s = it.next().toGramexNonEmpty();
            found = GrammarTools.acceptsAllWords(cfgr, s, wordsBag);
            if(found) res = new Comparison(comp.getLeft(), s, false);
        }

        data.testcases.result = res;
        return found;
    }

    private static Comparison verifyTestcases2(GrammarComparatorDataAux data){
        return data.testcases.result;
    }

    //Rule split

    public static boolean canApplySplit2(Comparison comp){
        boolean ok = comp.getLeft().size() == 1;
        Gramex left = comp.getLeft().iterator().next();
        ok = ok && left.length() > 1;
        ok = ok && GrammarTools.startsWithVar(left);
        return ok;
    }

    public static boolean checkSplitData2(Comparison comp, GrammarComparatorDataAux data){
        CfgNonEmpty cfgl = data.getCfgLeft(comp);
        CfgNonEmpty cfgr = data.getCfgRight(comp);

        Gramex r = comp.getLeft().iterator().next();
        Gvar v = GrammarTools.getLeftMostVar(r.toGramexNonEmpty());
        String shortWord = GrammarTools.shortestWordOfVar(cfgl, v);
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

    public static Set<Comparison> verifySplit2(Comparison comp, Set<Comparison> context, GrammarComparatorDataAux data){
        Set<Comparison> set = new HashSet<>();
        for(SplitElement spe : data.split.list){
            boolean betaIsEmpty = spe.beta.type() == TypesGramex.EMPTY;
            GramexNonEmpty ro = spe.ro.toGramexNonEmpty();
            GramexNonEmpty beta = null;
            if(!betaIsEmpty) beta = spe.beta.toGramexNonEmpty();
            boolean op = comp.isEquivalence();

            if(betaIsEmpty) set.add(new Comparison(data.split.gamma, ro, op));
            else set.add(new Comparison(data.split.gamma, new GramexConcat(ro, beta), op));
            set.add(new Comparison(new GramexConcat(new GramexVar(data.split.leftVar), ro) , spe.omega, op));
        }

        if(data.split.everyBetaIsNonEmpty) context.add(comp);

        return set;
    }












    public static boolean compare(CfgNonEmpty a, CfgNonEmpty b) {
        CfgNonEmpty aRenamed = GrammarTools.renameVars(a,b);
        AlgorithmData data = new AlgorithmData();
        data.cfga = aRenamed;
        data.cfgb = b;
        data.gena = new WordsGenerator(aRenamed);
        data.genb = new WordsGenerator(b);

        if(!data.cfga.getTerminals().equals(data.cfgb.getTerminals())) return false;

        Comparison start = new Comparison(new GramexVar(a.getStart()), new GramexVar(b.getStart()), true);
        Set<Comparison> context = new HashSet<>();
        Set<Comparison> toProve = verifyBranch(start,context,data);

        int counter = 0;
        int threshold = 2;
        Set<Comparison> naySplit = new HashSet<>();
        Set<Comparison> nayTestcases = new HashSet<>();
        boolean midStop = false;

        boolean finished = toProve.isEmpty();
        boolean equals = false;
        while(!finished){
            Comparison act = toProve.iterator().next();
            if((act.getLeft().size() > 1 || act.getRight().size() > 1) && canApplyInclusion(act)){
                toProve.remove(act);
                toProve.addAll(verifyInclusion(act));
            }
            else if(canApplySplit(act) && act.getLeft().iterator().next().length() == threshold && !naySplit.contains(act)){
                Set<Comparison> split = verifySplit(act, context, data);

                if(split.isEmpty()) {
                    naySplit.add(act);
                    midStop = true;
                }
                else{
                    toProve.remove(act);
                    toProve.addAll(split);
                }
            }
            else if(verifyEmptyOne(act)) toProve.remove(act);
            else if(verifyEmptyTwo(act)) toProve.remove(act);
            else if(verifyInduct(act, context)) toProve.remove(act);
            else if(canApplyEpsilon(act)){
                toProve.remove(act);
                toProve.add(verifyEpsilon(act));
            }
            else if(canApplyDist(act)){
                toProve.remove(act);
                toProve.addAll(verifyDist(act));
            }
            else if(canApplyTestcases(act)){
                Comparison testcases = verifyTestcases(act, data);
                if(testcases != null){
                    toProve.remove(act);
                    toProve.add(testcases);
                }
                else{
                    nayTestcases.add(act);
                    midStop = true;
                }
            }
            else {
                Set<Comparison> branch = verifyBranch(act, context, data);
                if(!branch.isEmpty()){
                    toProve.remove(act);
                    toProve.addAll(branch);
                }
            }

            if(toProve.isEmpty()){
                finished = true;
                equals = true;
            }
            else if(toProve.contains(act) && !midStop){
                finished = true;
            }
            midStop = false;
            counter++;
            if(counter == 70000) finished = true;
        }

        return equals;
    }

    //Derivative

    public static Set<Gramex> derivative(String word, Gramex rule, CfgNonEmpty cfg){
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

    //Rules empty

    public static boolean verifyEmptyOne(Comparison comp){
        // empty == empty
        return comp.getLeft().isEmpty() && comp.getRight().isEmpty() && comp.isEquivalence();
    }

    public static boolean verifyEmptyTwo(Comparison comp){
        // empty in x
        return comp.getLeft().isEmpty() && comp.isInclusion();
    }

    //Rule induct

    public static boolean verifyInduct(Comparison comp, Set<Comparison> context){
        // check in context
        Set<Gramex> left  = comp.getLeft();
        Set<Gramex> right = comp.getRight();

        Comparison xeqy = new Comparison(left, right, true);
        Comparison yeqx = new Comparison(right, left, true);
        Comparison xiny = new Comparison(left, right, false);

        if(comp.isEquivalence()) return context.contains(xeqy) || context.contains(yeqx);
        return context.contains(xeqy) || context.contains(yeqx) || context.contains(xiny);
    }

    //Rule epsilon

    public static boolean canApplyEpsilon(Comparison comp){
        // {e} op {e}
        return comp.getLeft().contains(new GramexEmpty()) && comp.getRight().contains(new GramexEmpty());
    }

    public static Comparison verifyEpsilon(Comparison comp){
        // {e} op {e}
        Set<Gramex> left  = new HashSet<>(comp.getLeft());
        left.remove(new GramexEmpty());
        Set<Gramex> right = new HashSet<>(comp.getRight());
        right.remove(new GramexEmpty());
        return new Comparison(left, right, comp.isEquivalence());
    }

    //Rule inclusion

    public static boolean canApplyInclusion(Comparison comp){
        return comp.isEquivalence();
    }

    public static Set<Comparison> verifyInclusion(Comparison comp){
        Set<Comparison> set = new HashSet<>();
        set.add(new Comparison(comp.getLeft(), comp.getRight(), false));
        set.add(new Comparison(comp.getRight(), comp.getLeft(), false));
        return set;
    }

    //Rule dist

    public static boolean canApplyDist(Comparison comp){
        return comp.getLeft().size() > 1 && comp.isInclusion();
    }

    public static Set<Comparison> verifyDist(Comparison comp){
        Set<Comparison> set = new HashSet<>();
        for(Gramex x : comp.getLeft()){
            set.add(new Comparison(x, comp.getRight(), false));
        }
        return set;
    }

    //Rule branch

    public static Set<Comparison> verifyBranch(Comparison comp, Set<Comparison> context, AlgorithmData data){
        CfgNonEmpty cfgl, cfgr;
        boolean leftIsA = GrammarTools.correctVars(data.cfga, data.cfgb, comp.getLeft(), comp.getRight());
        if(leftIsA){
            cfgl = data.cfga;
            cfgr = data.cfgb;
        }
        else{
            cfgl = data.cfgb;
            cfgr = data.cfga;
        }

        Set<Comparison> set = new HashSet<>();
        for(char c : cfgl.getTerminals().getSet()){
            Set<Gramex> dl = derivativeOfSet(Character.toString(c), comp.getLeft(), cfgl);
            Set<Gramex> dr = derivativeOfSet(Character.toString(c), comp.getRight(), cfgr);
            set.add(new Comparison(dl, dr, comp.isEquivalence()));
        }
        if(!set.isEmpty()) context.add(comp);
        return set;
    }

    //Rule split

    public static boolean canApplySplit(Comparison comp){
        boolean ok = comp.getLeft().size() == 1;
        Gramex left = comp.getLeft().iterator().next();
        ok = ok && left.length() > 1;
        ok = ok && GrammarTools.startsWithVar(left);
        return ok;
    }

    public static Set<Comparison> verifySplit(Comparison comp, Set<Comparison> context, AlgorithmData data){
        CfgNonEmpty cfgl, cfgr;
        boolean leftIsA = GrammarTools.correctVars(data.cfga, data.cfgb, comp.getLeft(), comp.getRight());
        if(leftIsA){
            cfgl = data.cfga;
            cfgr = data.cfgb;
        }
        else{
            cfgl = data.cfga;
            cfgr = data.cfgb;
        }

        Gramex r = comp.getLeft().iterator().next();
        Gvar v = GrammarTools.getLeftMostVar(r.toGramexNonEmpty());
        String shortWord = GrammarTools.shortestWordOfVar(cfgl, v);
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

        if(!everyOmegaCanDeriveShort) return new HashSet<>();

        //Result

        Set<Comparison> set = new HashSet<>();
        for(SplitElement spe : list){
            boolean betaIsEmpty = spe.beta.type() == TypesGramex.EMPTY;
            GramexNonEmpty ro = spe.ro.toGramexNonEmpty();
            GramexNonEmpty beta = null;
            if(!betaIsEmpty) beta = spe.beta.toGramexNonEmpty();
            boolean op = comp.isEquivalence();

            if(betaIsEmpty) set.add(new Comparison(gamma, ro, op));
            else set.add(new Comparison(gamma, new GramexConcat(ro, beta), op));
            set.add(new Comparison(new GramexConcat(new GramexVar(v), ro) , spe.omega, op));
        }

        if(everyBetaIsNonEmpty) context.add(comp);

        return set;
    }

    private static boolean findOmegaBetaRo(SplitElement spe, String x, CfgNonEmpty gtr){
        // w = omega-beta
        // d(x,w) = ro-beta
        Set<Gramex> set = derivative(x,spe.w, gtr);
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

    //Rule testcases

    public static boolean canApplyTestcases(Comparison comp){
        return comp.getLeft().size() == 1 && comp.getRight().size() > 1 && comp.isInclusion();
    }

    public static Comparison verifyTestcases(Comparison comp, AlgorithmData data){
        CfgNonEmpty cfgr, cfgl;
        WordsGenerator alphal;
        boolean leftIsA = GrammarTools.correctVars(data.cfga, data.cfgb, comp.getLeft(), comp.getRight());
        if(leftIsA){
            cfgl = data.cfga;
            cfgr = data.cfgb;
            alphal = data.gena;
        }
        else{
            cfgl = data.cfgb;
            cfgr = data.cfga;
            alphal = data.genb;
        }

        Comparison res = null;
        Gramex left = comp.getLeft().iterator().next();
        Iterator<Gramex> it = comp.getRight().iterator();
        boolean found = false;
        while(it.hasNext() && !found){
            GramexNonEmpty s = it.next().toGramexNonEmpty();
            found = GrammarTools.acceptsAllWords(cfgr, s, alphal.generateWords(GrammarTools.recommendedBagSize(cfgl),left.toGramexNonEmpty()));
            if(found) res = new Comparison(comp.getLeft(), s, false);
        }
        return res;
    }

    private static class AlgorithmData{
        public CfgNonEmpty cfga;
        public CfgNonEmpty cfgb;
        public WordsGenerator gena;
        public WordsGenerator genb;
        public AlgorithmData(){}
    }

}
