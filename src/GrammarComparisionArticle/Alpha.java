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

    public List<String> generateWords(int n, Gramex rule) {
        return null;
    }

    public Enumerator enumFunction(GramexNonEmpty r, int n) throws EnumeratorException {
        if(r.type() == TypesGramex.CHAR){
            if(n != 0) throw new EnumeratorException();
            return new EnumeratorLeaf(r.toGramexChar());
        }
        else if(r.type() == TypesGramex.VAR){
            Pair<GramexNonEmpty,Integer> choose = chooseFunction(r.toGramexVar(), n);
            return new EnumeratorNode(r.toGramexVar(), enumFunction(choose.getA(), choose.getB()));
        }
        else{
            GramexNonEmpty ra = r.toGramexConcat().getA();
            GramexNonEmpty rb = r.toGramexConcat().getB();
            Pair<Integer, Integer> pi = pi(n, ht(ra), ht(rb));
            return new EnumeratorPair(enumFunction(ra, pi.getA()), enumFunction(rb, pi.getB()));
        }
    }

    public Pair<GramexNonEmpty,Integer> chooseFunction(GramexVar r, int a){
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

    public int ht(GramexNonEmpty r){
        return 0;
    }

    public int tau(GramexNonEmpty r){
        if(r.type() == TypesGramex.CHAR){
            return 1;
        }
        else if(r.type() == TypesGramex.VAR){
            Set<CfgRule> rules = cfg.getRulesLeft(r.toGramexVar().getV());
            int n = 0;
            for(CfgRule rule : rules){
                n += tau(rule.getRight().toGramexNonEmpty());
            }
            return n;
        }
        else{
            List<GramexChar> cl = r.toGramexConcat().getListChars();
            List<GramexVar> vl = r.toGramexConcat().getListVars();
            int n = 1;
            for(GramexChar rc : cl) n *= tau(rc);
            for(GramexVar rv : vl)  n *= tau(rv);
            return n;
        }
    }



    private static class InnerChoose {
        private Map<CfgVariable, List<GramexNonEmpty>> orderedRights;
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
