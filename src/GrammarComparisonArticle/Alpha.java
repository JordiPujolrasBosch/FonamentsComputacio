package GrammarComparisonArticle;

import Elements.Grammars.CfgRule;
import Elements.Grammars.CfgVariable;
import Exceptions.AlphaException;
import Factory.GrammarTools;
import Grammars.*;
import Utils.IntegerInf;
import Utils.Pair;

import java.util.*;

public class Alpha {
    private final GrammarTools gt;
    private InnerChoose innerChoose;
    private final InnerHt innerHt;

    public Alpha(Cfg cfg){
        gt = new GrammarTools(cfg);
        innerChoose = null;
        innerHt = new InnerHt();
    }

    public List<String> generateWords(int n, Gramex rule) {
        return null;
    }

    public Enumerator enumFunction(GramexNonEmpty r, int n) throws AlphaException {
        if(r.type() == TypesGramex.CHAR){
            if(n != 0) throw new AlphaException();
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

    public Pair<GramexNonEmpty,Integer> chooseFunction(GramexVar r, int a) throws AlphaException {
        if(innerChoose == null) {
            innerChoose = new InnerChoose();
            innerChoose.build(this);
        }
        //int n = cfg.getNumberRulesLeft(r.v());
        //int k = innerChoose.findK(r,a);
        //int qq = Math.floorDiv(a - innerChoose.getI(r.v(),k), n - k);
        //int rr = (a - innerChoose.getI(r.v(),k)) % (n - k);
        //return new Pair<RightNonEmpty, Integer>(innerChoose.getRight(r.v(),k+rr),innerChoose.getB(r.v(),k) + qq);
        return null;
    }

    public IntegerInf ht(GramexNonEmpty g){
        innerHt.active = true;
        IntegerInf x = tau(g);
        innerHt.clear();
        return x;
    }

    public IntegerInf tau(GramexNonEmpty g){
        if(innerHt.loop && innerHt.active) return new IntegerInf(true);

        if(g.type() == TypesGramex.CHAR) {
            return new IntegerInf(1);
        }
        else if(g.type() == TypesGramex.CONCAT){
            IntegerInf n = new IntegerInf(1);
            for(GramexNonEmpty gne : g.toGramexConcat().toList()) {
                n = n.multiply(tau(gne));
            }
            return n;
        }
        else{
            CfgVariable v = g.toGramexVar().getV();
            innerHt.start(v);
            IntegerInf n = new IntegerInf(0);
            for(Gramex gv : gt.getRulesRightVar(v)){
                n = n.add(tau(gv.toGramexNonEmpty()));
            }
            innerHt.finish(v);
            return n;
        }
    }


    private static class InnerHt{
        public boolean active;
        public Set<CfgVariable> bag;
        public boolean loop;
        public InnerHt(){
            active = false;
            loop = false;
            bag = new HashSet<>();
        }
        public void start(CfgVariable v){
            if(active && !bag.contains(v)) bag.add(v);
            else if(active && bag.contains(v)) loop = true;
        }
        public void finish(CfgVariable v){
            if(active) bag.remove(v);
        }
        public void clear(){
            active = false;
            bag.clear();
            loop = false;
        }
    }

    private static class InnerChoose {
        private Map<CfgVariable, List<GramexNonEmpty>> orderedRights;
        private Map<CfgVariable, List<IntegerInf>> bs;
        private Map<CfgVariable, List<IntegerInf>> is;
        private Map<Gramex, IntegerInf> mapper;
        private Alpha a;

        public InnerChoose(){}

        public void build(Alpha a) throws AlphaException {
            if(a.gt.hasEmptyRules()) throw new AlphaException();
            this.a = a;
            buildMapper();
            buildLists();
            orderLists();
            buildBs();
            buildIs();
        }

        private void buildMapper(){
            mapper = new HashMap<>();
            for(CfgRule r : a.gt.getAllRules()){
                mapper.put(r.getRight(), a.ht(r.getRight().toGramexNonEmpty()));
            }
        }

        private void buildLists(){
            orderedRights = new HashMap<>();
            for(CfgVariable v : a.gt.getVars()){
                orderedRights.put(v, new ArrayList<>());
                for(Gramex g : a.gt.getRulesRightVar(v)){
                    orderedRights.get(v).add(g.toGramexNonEmpty());
                }
            }
        }

        private void orderLists(){

        }

        private void buildBs(){
            bs = new HashMap<>();
            for(CfgVariable v : a.gt.getVars()){
                bs.put(v, new ArrayList<>());
                bs.get(v).add(new IntegerInf(0));
                for(Gramex g : orderedRights.get(v)){
                    bs.get(v).add(mapper.get(g));
                }
            }
        }

        private void buildIs(){
            is = new HashMap<>();
            for(CfgVariable v : a.gt.getVars()){
                int n = a.gt.getRulesVar(v).size();
                is.put(v, new ArrayList<>());
                for(int m = 0; m <= n; m++){
                    IntegerInf k = bs.get(v).get(m).multiply(new IntegerInf(n-m+1));
                    is.get(v).add(k);
                    IntegerInf adder = new IntegerInf(0);
                    for(int i=0; i<m; i++) adder = adder.add(bs.get(v).get(i));
                    is.get(v).set(m, is.get(v).get(m).add(adder));
                }
            }
        }
    }


    public Pair<Integer,Integer> pi(int z, IntegerInf xb, IntegerInf yb){
        double dxb;
        if(xb.isInfinity()) dxb = inf();
        else dxb = xb.getValue();
        double dyb;
        if(yb.isInfinity()) dyb = inf();
        else dyb = yb.getValue();

        Pair<Double,Double> dxy = piDouble(z, dxb, dyb);

        double dx = dxy.getA();
        double dy = dxy.getB();
        int x = (int) dx;
        int y = (int) dy;

        return new Pair<>(x,y);
    }

    public Pair<Double, Double> pi(double z){
        Pair<Double,Double> tw = simple(z);
        double t = tw.getA();
        double w = tw.getB();
        double y = z - t;
        double x = w - y;
        return new Pair<>(x,y);
    }

    public Pair<Double,Double> piDouble(double z, double xb, double yb){
        double zb = zb(xb,yb);
        double zx = zx(xb);
        double zy = zy(yb);
        Pair<Double,Double> tw;
        if(z >= zb) tw = bskip(z,xb,yb);
        else if(zx <= z && z < zb) tw = xskip(z,xb);
        else if(zy <= z && z < zb) tw = yskip(z,yb);
        else tw = simple(z);
        double t = tw.getA();
        double w = tw.getB();
        double y = z-t;
        double x = w-y;
        return new Pair<>(x,y);
    }

    public Pair<Double,Double> bskip(double z, double xb, double yb){
        double sb = xb*xb+yb*yb;
        double wb = xb+yb;
        double r = 2*wb+1;
        double w = Math.floor((r-Math.ceil(Math.sqrt(r*r-8*z-4*sb+4*yb-4*xb)))/2);
        double t = ((2*wb-1)*w-w*w-sb+wb)/2;
        return new Pair<>(t,w);
    }

    public Pair<Double,Double> yskip(double z, double yb){
        double w = Math.floor((2*z+yb*yb-yb)/(2*yb));
        double t = (2*w*yb-yb*yb+yb)/2;
        return new Pair<>(t,w);
    }

    public Pair<Double,Double> xskip(double z, double xb){
        double w = Math.floor((2*z+xb*xb+xb)/(2*(xb+1)));
        double t = (2*w*xb-xb*xb+xb)/2;
        return new Pair<>(t,w);
    }

    public Pair<Double,Double> simple(double z){
        double w = Math.floor((Math.floor(Math.sqrt(8 * z + 1))-1)/2);
        double t = (w * (w + 1)) / 2;
        return new Pair<>(t,w);
    }

    public double zy(double yb){
        return (yb*(yb+1))/2;
    }

    public double zx(double xb){
        return ((xb+1)*(xb+2))/2;
    }

    public double zb(double xb, double yb){
        double zy = zy(yb);
        double zx = zx(xb);
        if(xb > yb-1) return yb*(xb-yb+1)+zy;
        else if(yb-1 > xb) return (xb+1)*(yb-xb-1)+zx;
        else return zy;
    }

    public double inf(){
        return Double.POSITIVE_INFINITY;
    }

}
