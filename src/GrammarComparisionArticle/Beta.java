package GrammarComparisionArticle;

import Elements.Grammars.CfgVariable;
import Factory.GrammarTools;
import Grammars.*;
import Utils.Pair;

import java.util.*;

public class Beta {
    private final GrammarTools gt;
    private final Alpha alpha;

    public Beta(Cfg cfg){
        gt = new GrammarTools(cfg);
        alpha = new Alpha(cfg);
    }

    //Derivative

    public Set<Gramex> derivative(String word, Gramex rule){
        Set<Gramex> set = new HashSet<>();
        if(GrammarTools.terminalPrefix(rule).startsWith(word)) set.add(GrammarTools.cut(rule, word.length()).getB());
        else set.addAll(gt.findSuffixes(rule, word));
        return set;
    }

    private Set<Gramex> derivativeOfSet(String word, Set<Gramex> rules) {
        Set<Gramex> set = new HashSet<>();
        for(Gramex r : rules) set.addAll(derivative(word, r));
        return set;
    }

    //Rules empty

    public boolean verifyEmptyOne(SetPairCompare comp){
        // empty == empty
        return comp.getLeft().isEmpty() && comp.getRight().isEmpty() && comp.isEquivalence();
    }

    public boolean verifyEmptyTwo(SetPairCompare comp){
        // empty in x
        return comp.getLeft().isEmpty() && comp.isInclusion();
    }

    //Rule induct

    public boolean verifyInduct(SetPairCompare comp, Set<SetPairCompare> context){
        // check in context
        Set<Gramex> left  = comp.getLeft();
        Set<Gramex> right = comp.getRight();

        SetPairCompare xeqy = new SetPairCompare(left, right, true);
        SetPairCompare yeqx = new SetPairCompare(right, left, true);
        SetPairCompare xiny = new SetPairCompare(left, right, false);

        if(comp.isEquivalence()) return context.contains(xeqy) || context.contains(yeqx);
        return context.contains(xeqy) || context.contains(yeqx) || context.contains(xiny);
    }

    //Rule epsilon

    public boolean canApplyEpsilon(SetPairCompare comp){
        // {e} op {e}
        return comp.getLeft().contains(new GramexEmpty()) && comp.getRight().contains(new GramexEmpty());
    }

    public SetPairCompare verifyEpsilon(SetPairCompare comp){
        // {e} op {e}
        Set<Gramex> left  = new HashSet<>(comp.getLeft());
        left.remove(new GramexEmpty());
        Set<Gramex> right = new HashSet<>(comp.getRight());
        right.remove(new GramexEmpty());
        return new SetPairCompare(left, right, comp.isEquivalence());
    }

    //Rule inclusion

    public boolean canApplyInclusion(SetPairCompare comp){
        return comp.isEquivalence();
    }

    public Set<SetPairCompare> verifyInclusion(SetPairCompare comp){
        Set<SetPairCompare> set = new HashSet<>();
        set.add(new SetPairCompare(comp.getLeft(), comp.getRight(), false));
        set.add(new SetPairCompare(comp.getRight(), comp.getLeft(), false));
        return set;
    }

    //Rule dist

    public boolean canApplyDist(SetPairCompare comp){
        return comp.getLeft().size() > 1 && comp.isInclusion();
    }

    public Set<SetPairCompare> verifyDist(SetPairCompare comp){
        Set<SetPairCompare> set = new HashSet<>();
        for(Gramex x : comp.getLeft()){
            set.add(new SetPairCompare(x, comp.getRight(), false));
        }
        return set;
    }

    //Rule branch

    public Set<SetPairCompare> verifyBranch(SetPairCompare comp, Set<SetPairCompare> context){
        Set<SetPairCompare> set = new HashSet<>();
        for(char c : gt.getTerminals()){
            Set<Gramex> dl = derivativeOfSet(Character.toString(c), comp.getLeft());
            Set<Gramex> dr = derivativeOfSet(Character.toString(c), comp.getRight());
            set.add(new SetPairCompare(dl, dr, comp.isEquivalence()));
        }
        context.add(comp);
        return set;
    }

    //Rule split

    public boolean canApplySplit(SetPairCompare comp){
        boolean ok = comp.getLeft().size() == 1;
        Gramex left = comp.getLeft().iterator().next();
        ok = ok && left.length() > 1;
        ok = ok && GrammarTools.startsWithVar(left);
        return ok;
    }

    public Set<SetPairCompare> verifySplit(SetPairCompare comp, Set<SetPairCompare> context){
        Gramex r = comp.getLeft().iterator().next();
        CfgVariable v = GrammarTools.getLeftMostVar(r.toGramexNonEmpty());
        String shortWord = gt.shortestWordOfVar(v);
        Gramex gamma = GrammarTools.cut(r, 1).getB();

        boolean everyOmegaCanDeriveShort = true;
        boolean everyBetaIsNonEmpty = true;
        List<SplitElement> list = new ArrayList<>();

        //Iterate

        Iterator<Gramex> it = comp.getRight().iterator();
        while(it.hasNext() && everyOmegaCanDeriveShort){
            Gramex w = it.next();
            SplitElement spe = new SplitElement(w);
            everyOmegaCanDeriveShort = findOmegaBetaRo(spe, shortWord);
            everyBetaIsNonEmpty = everyBetaIsNonEmpty && (spe.beta.type() != TypesGramex.EMPTY);
            if(everyOmegaCanDeriveShort) list.add(spe);
        }

        if(!everyOmegaCanDeriveShort) return new HashSet<>();

        //Result

        Set<SetPairCompare> set = new HashSet<>();
        for(SplitElement spe : list){
            boolean betaIsEmpty = spe.beta.type() == TypesGramex.EMPTY;
            GramexNonEmpty ro = spe.ro.toGramexNonEmpty();
            GramexNonEmpty beta = null;
            if(!betaIsEmpty) beta = spe.beta.toGramexNonEmpty();
            boolean op = comp.isEquivalence();

            if(betaIsEmpty) set.add(new SetPairCompare(gamma, ro, op));
            else set.add(new SetPairCompare(gamma, new GramexConcat(ro, beta), op));
            set.add(new SetPairCompare(new GramexConcat(new GramexVar(v), ro) , spe.omega, op));
        }

        if(everyBetaIsNonEmpty) context.add(comp);

        return set;
    }

    private boolean findOmegaBetaRo(SplitElement spe, String x){
        // w = omega-beta
        // d(x,w) = ro-beta
        Set<Gramex> set = derivative(x,spe.w);
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

    public boolean canApplyTestcases(SetPairCompare comp){
        return comp.getLeft().size() == 1 && comp.getRight().size() > 1 && comp.isInclusion();
    }

    public Pair<SetPairCompare, Boolean> verifyTestcases(SetPairCompare comp){
        SetPairCompare res = null;
        Gramex left = comp.getLeft().iterator().next();
        Iterator<Gramex> it = comp.getRight().iterator();
        boolean found = false;
        while(it.hasNext() && !found){
            Gramex s = it.next();
            found = gt.acceptsAllWords(s, alpha.generateWords(gt.recommendedBagSize(),left));
            if(found) res = new SetPairCompare(comp.getLeft(), s, false);
        }
        return new Pair<>(res, found);
    }

}
