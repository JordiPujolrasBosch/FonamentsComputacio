package GrammarComparisionArticle;

import Grammars.Cfg;
import Grammars.Right;
import Grammars.RightEmpty;
import Grammars.RightNonEmpty;

import java.util.HashSet;
import java.util.Set;

public class Beta {
    private final Cfg cfg;

    public Beta(Cfg cfg){
        this.cfg = cfg;
    }

    public Set<RightNonEmpty> derivative(String word, RightNonEmpty rule){
        Set<RightNonEmpty> set = new HashSet<>();

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
        Set<Right> right = new HashSet<>(comp.getRight());
        return new SetPairCompare(left, right, comp.isEquivalence());
    }

    public boolean canApplyInclusion(SetPairCompare comp){
        return comp.isEquivalence();
    }

    public Set<SetPairCompare> verifyInclusion(SetPairCompare comp){
        Set<Right> la = new HashSet<>(comp.getLeft());
        Set<Right> ra = new HashSet<>(comp.getRight());
        Set<Right> lb = new HashSet<>(comp.getLeft());
        Set<Right> rb = new HashSet<>(comp.getRight());

        Set<SetPairCompare> set = new HashSet<>();
        set.add(new SetPairCompare(la, ra, false));
        set.add(new SetPairCompare(rb, lb, false));
        return set;
    }

    public boolean canApplyDist(SetPairCompare comp){
        return comp.getLeft().size() > 1 && comp.isInclusion();
    }

    public Set<SetPairCompare> verifyDist(SetPairCompare comp){
        Set<Right> left = comp.getLeft();
        Set<SetPairCompare> set = new HashSet<>();
        for(Right x : left){
            Set<Right> newRight = new HashSet<>(comp.getRight());
            Set<Right> newLeft = new HashSet<>();
            newLeft.add(x);
            set.add(new SetPairCompare(newLeft, newRight, false));
        }
        return set;
    }
}
