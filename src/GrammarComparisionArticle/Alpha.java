package GrammarComparisionArticle;

import Elements.Grammars.CfgRule;
import Elements.Grammars.CfgVariable;
import Exceptions.EnumeratorException;
import Grammars.*;
import Utils.IntegerInf;
import Utils.Pair;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Alpha {
    private final Cfg cfg;
    private InnerChoose innerChoose;

    public Alpha(Cfg cfg){
        this.cfg = cfg;
        innerChoose = null;
    }

    public Enumerator enumFunction(RightNonEmpty r, int n) throws EnumeratorException {
        if(r.type() == TypesRight.CHAR){
            if(n != 0) throw new EnumeratorException();
            return new EnumeratorLeaf(r.toRightChar());
        }
        else if(r.type() == TypesRight.VAR){
            Pair<RightNonEmpty,Integer> choose = chooseFunction(r.toRightVar(), n);
            return new EnumeratorNode(r.toRightVar(), enumFunction(choose.getA(), choose.getB()));
        }
        else{
            RightNonEmpty ra = r.toRightConcat().getA();
            RightNonEmpty rb = r.toRightConcat().getB();
            Pair<Integer, Integer> pi = pi(n, ht(ra), ht(rb));
            return new EnumeratorPair(enumFunction(ra, pi.getA()), enumFunction(rb, pi.getB()));
        }
    }

    public Pair<RightNonEmpty,Integer> chooseFunction(RightVar r, int a){
        if(innerChoose == null) innerChoose = new InnerChoose(this);
        //int n = cfg.getNumberRulesLeft(r.v());
        //int k = innerChoose.findK(r,a);
        //int qq = Math.floorDiv(a - innerChoose.getI(r.v(),k), n - k);
        //int rr = (a - innerChoose.getI(r.v(),k)) % (n - k);
        //return new Pair<RightNonEmpty, Integer>(innerChoose.getRight(r.v(),k+rr),innerChoose.getB(r.v(),k) + qq);
        return null;
    }

    public Pair<Integer,Integer> pi(int a, int b, int c){
        return null;
    }

    public int ht(RightNonEmpty r){
        return 0;
    }

    public int tau(RightNonEmpty r){
        if(r.type() == TypesRight.CHAR){
            return 1;
        }
        else if(r.type() == TypesRight.VAR){
            Set<CfgRule> rules = cfg.getRulesLeft(r.toRightVar().getV());
            int n = 0;
            for(CfgRule rule : rules){
                n += tau(rule.getRight().toRightNonEmpty());
            }
            return n;
        }
        else{
            List<RightChar> cl = r.toRightConcat().getListChars();
            List<RightVar> vl = r.toRightConcat().getListVars();
            int n = 1;
            for(RightChar rc : cl) n *= tau(rc);
            for(RightVar rv : vl)  n *= tau(rv);
            return n;
        }
    }

    private static class InnerChoose {
        private Map<CfgVariable, List<RightNonEmpty>> orderedRights;
        private Map<CfgVariable, List<IntegerInf>> bs;
        private Map<CfgVariable, List<IntegerInf>> is;
        private final Alpha a;

        public InnerChoose(Alpha a){
            this.a = a;
            build(a);
        }

        private void build(Alpha a){

        }
    }

}
