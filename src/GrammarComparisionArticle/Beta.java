package GrammarComparisionArticle;

import Elements.Grammars.CfgRule;
import Elements.Grammars.CfgVariable;
import Grammars.*;

import java.util.HashSet;
import java.util.Set;

public class Beta {
    private final Cfg cfg;

    public Beta(Cfg cfg){
        this.cfg = cfg;
    }

    public Set<Right> derivative(String word, Right rule){
        Set<Right> set = new HashSet<>();
        if(wordIsPrefixOfRight(word, rule)) set.add(sufixOfWord(word, rule));
        else set.addAll(findSufixsInGrammar(word, rule));
        return set;
    }

    private Set<Right> derivativeOfSet(String word, Set<Right> left) {
        Set<Right> set = new HashSet<>();
        for(Right r : left) set.addAll(derivative(word, r));
        return set;
    }

    public boolean verifyEmptyOne(SetPairCompare comp){
        // empty == empty
        return comp.getLeft().isEmpty() && comp.getRight().isEmpty() && comp.isEquivalence();
    }

    public boolean verifyEmptyTwo(SetPairCompare comp){
        // empty in x
        return comp.getLeft().isEmpty() && comp.isInclusion();
    }

    public boolean verifyInduct(SetPairCompare comp, Set<SetPairCompare> context){
        // check in context
        Set<Right> left  = comp.getLeft();
        Set<Right> right = comp.getRight();

        SetPairCompare xeqy = new SetPairCompare(left, right, true);
        SetPairCompare yeqx = new SetPairCompare(right, left, true);
        SetPairCompare xiny = new SetPairCompare(left, right, false);

        if(comp.isEquivalence()) return context.contains(xeqy) || context.contains(yeqx);
        return context.contains(xeqy) || context.contains(yeqx) || context.contains(xiny);
    }

    public boolean canApplyEpsilon(SetPairCompare comp){
        // {e} op {e}
        return comp.getLeft().contains(new RightEmpty()) && comp.getRight().contains(new RightEmpty());
    }

    public SetPairCompare verifyEpsilon(SetPairCompare comp){
        // {e} op {e}
        Set<Right> left  = new HashSet<>(comp.getLeft());
        left.remove(new RightEmpty());
        Set<Right> right = new HashSet<>(comp.getRight());
        right.remove(new RightEmpty());
        return new SetPairCompare(left, right, comp.isEquivalence());
    }

    public boolean canApplyInclusion(SetPairCompare comp){
        return comp.isEquivalence();
    }

    public Set<SetPairCompare> verifyInclusion(SetPairCompare comp){
        Set<SetPairCompare> set = new HashSet<>();
        set.add(new SetPairCompare(comp.getLeft(), comp.getRight(), false));
        set.add(new SetPairCompare(comp.getRight(), comp.getLeft(), false));
        return set;
    }

    public boolean canApplyDist(SetPairCompare comp){
        return comp.getLeft().size() > 1 && comp.isInclusion();
    }

    public Set<SetPairCompare> verifyDist(SetPairCompare comp){
        Set<SetPairCompare> set = new HashSet<>();
        for(Right x : comp.getLeft()){
            set.add(new SetPairCompare(x, comp.getRight(), false));
        }
        return set;
    }

    public Set<SetPairCompare> verifyBranch(SetPairCompare comp, Set<SetPairCompare> context){
        Set<SetPairCompare> set = new HashSet<>();
        for(char c : cfg.getTerminals()){
            Set<Right> dl = derivativeOfSet(Character.toString(c), comp.getLeft());
            Set<Right> dr = derivativeOfSet(Character.toString(c), comp.getRight());
            set.add(new SetPairCompare(dl, dr, comp.isEquivalence()));
        }
        context.add(comp);
        return set;
    }

    //Derivative private

    private boolean wordIsPrefixOfRight(String word, Right rule){
        //r = wB
        if(word.isEmpty()) return true; // r = ""B
        if(rule.type() == TypesRight.VAR) return false; // A = wB
        if(rule.type() == TypesRight.CHAR) return word.equals(rule.toRightChar().toString()); // a = a""
        if(rule.type() == TypesRight.EMPTY) return false; // "" = *B

        if(word.length() > rule.length()) return false; // *** = ****B
        if(word.length() == rule.length()) return !rule.containsVar() && word.equals(rule.toString()); // *** = ***""

        RightNonEmpty r = rule.toRightConcat().getPrefix(word.length()); // **** = **B
        return !r.containsVar() && word.equals(r.toString()); // ** = **B
    }

    private Right sufixOfWord(String word, Right rule) {
        // r = wS
        if(rule.length() == word.length()) return new RightEmpty();
        if(rule.length() > word.length()) return rule.getSufix(rule.length()-word.length());
        return null;
    }

    private Set<Right> findSufixsInGrammar(String word, Right rule){
        Set<Right> set = new HashSet<>();
        Set<Right> expanded = new HashSet<>();
        Set<Right> toexpand = new HashSet<>();
        if(rule.hasPrefixTerminalOfSize(word.length())) expanded.add(rule);
        else toexpand.add(rule);

        while(!toexpand.isEmpty()){
            Set<RightNonEmpty> aux = new HashSet<>();
            for(Right r : toexpand) aux.addAll(expandOneStepLeft(r));
            toexpand.clear();

            for(RightNonEmpty r : aux){
                if(r.hasPrefixTerminalOfSize(word.length())) expanded.add(r);
                else toexpand.add(r);
            }
        }

        for(Right r : expanded){
            if(wordIsPrefixOfRight(word, r)) set.add(sufixOfWord(word, r));
        }

        return set;
    }

    private Set<RightNonEmpty> expandOneStepLeft(Right r) {
        CfgVariable v = r.getLeftMostVar();
        if(v == null) return new HashSet<>();

        Set<RightNonEmpty> set = new HashSet<>();
        for(CfgRule rule : cfg.getRulesLeft(v)){
            set.add(r.getSubstitutionLeft(rule.getRight().toRightNonEmpty()));
        }
        return set;
    }
}
