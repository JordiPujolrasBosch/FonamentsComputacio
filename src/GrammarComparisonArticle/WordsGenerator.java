package GrammarComparisonArticle;

import Elements.Grammars.*;
import Factory.GrammarTools;
import GrammarComparisonArticle.Enumerators.*;
import Grammars.*;
import Utils.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Random;

public class WordsGenerator {
    private final CfgNonEmpty cfg;
    private InnerHt innerHt;
    private Map<GramexNonEmpty,IntegerInf> htMapper;
    private InnerChoose innerChoose;

    public WordsGenerator(CfgNonEmpty cfg){
        this.cfg = cfg;
        build();
    }

    public List<String> generateWords(int n, GramexNonEmpty rule) {
        if(n <= 0) return new ArrayList<>();

        List<String> list = new ArrayList<>();
        IntegerInf ht = ht(rule);

        if(ht.isInfinity()){
            Random rand = new Random();
            for(int i=1; i<=n; i++){
                list.add(enumFunction(rule, rand.nextInt(5*n)).getWord());
            }
        }
        else if(n >= ht.getValue()) {
            for (int i = 0; i < ht.getValue(); i++){
                list.add(enumFunction(rule, i).getWord());
            }
        }
        else{
            Random rand = new Random();
            for(int i=1; i<=n; i++) {
                list.add(enumFunction(rule, rand.nextInt(ht.getValue())).getWord());
            }
        }
        return list;
    }

    public List<String> generateWordsStart(int n) {
        if(n <= 0) return new ArrayList<>();

        List<String> list = new ArrayList<>();
        GramexNonEmpty start = new GramexVar(cfg.getStart());

        if(!cfg.acceptsEmpty()) list.addAll(generateWords(n, start));
        else{
            list.add("");
            list.addAll(generateWords(n-1, start));
        }

        return list;
    }

    //Private functions

    private void build(){
        innerHt = new InnerHt();
        htMapper = new HashMap<>();

        for(char t : cfg.getTerminals().getSet()){
            GramexChar g = new GramexChar(t);
            htMapper.put(g, ht(g));
        }
        for(Gvar v : cfg.getVariables()){
            GramexVar g = new GramexVar(v);
            htMapper.put(g, ht(g));
        }
        for(GruleNonEmpty r : cfg.getRules()){
            GramexNonEmpty g = r.getRight();
            if(!htMapper.containsKey(g)) htMapper.put(g, ht(g));
        }

        innerChoose = new InnerChoose();
        innerChoose.build(this);
    }

    private Enumerator enumFunction(GramexNonEmpty r, int n) {
        if(r.type() == TypesGramex.CHAR){
            if(n != 0) return null;
            return new EnumeratorLeaf(r.toGramexChar());
        }
        else if(r.type() == TypesGramex.VAR){
            Pair<GramexNonEmpty,Integer> choose = chooseFunction(r.toGramexVar(), n);
            return new EnumeratorNode(r.toGramexVar(), enumFunction(choose.getA(), choose.getB()));
        }
        else{
            GramexNonEmpty ra = r.toGramexConcat().getA();
            GramexNonEmpty rb = r.toGramexConcat().getB();
            Pair<Integer, Integer> pi = pi(n, ht(ra).add(new IntegerInf(-1)), ht(rb));
            return new EnumeratorPair(enumFunction(ra, pi.getA()), enumFunction(rb, pi.getB()));
        }
    }

    private Pair<GramexNonEmpty,Integer> chooseFunction(GramexVar r, int a) {
        Gvar v = r.getV();
        int n = GrammarTools.getRulesVar(cfg, v).size();
        int k = innerChoose.findK(v,a);
        int ik = innerChoose.getI(v,k);
        int qq = (a-ik)/(n-k);
        int rr = (a-ik)%(n-k);
        return new Pair<>(innerChoose.getGramex(v,k+rr),qq+innerChoose.getB(v,k));
    }

    private IntegerInf ht(GramexNonEmpty g){
        if(htMapper.containsKey(g)) return htMapper.get(g);

        innerHt.active = true;
        IntegerInf x = tau(g);
        innerHt.clear();

        if(!htMapper.containsKey(g)) htMapper.put(g,x);
        return x;
    }

    private IntegerInf tau(GramexNonEmpty g){
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
            Gvar v = g.toGramexVar().getV();
            innerHt.start(v);
            IntegerInf n = new IntegerInf(0);
            for(GramexNonEmpty gv : GrammarTools.getRulesRightVar(cfg,v)){
                n = n.add(tau(gv));
            }
            innerHt.finish(v);
            return n;
        }
    }

    //Private classes

    private static class InnerHt{
        public boolean active;
        public Set<Gvar> bag;
        public boolean loop;

        public InnerHt(){
            active = false;
            loop = false;
            bag = new HashSet<>();
        }

        public void start(Gvar v){
            if(active && !bag.contains(v)) bag.add(v);
            else if(active && bag.contains(v)) loop = true;
        }

        public void finish(Gvar v){
            if(active) bag.remove(v);
        }

        public void clear(){
            active = false;
            bag.clear();
            loop = false;
        }
    }

    private static class InnerChoose {
        private Map<Gvar, List<GramexNonEmpty>> orderedRights;
        private Map<Gvar, List<IntegerInf>> bs;
        private Map<Gvar, List<IntegerInf>> is;
        private WordsGenerator a;

        public InnerChoose(){}

        public void build(WordsGenerator a) {
            this.a = a;
            buildLists();
            orderLists();
            buildBs();
            buildIs();
        }

        private void buildLists(){
            orderedRights = new HashMap<>();
            for(Gvar v : a.cfg.getVariables()){
                orderedRights.put(v, new ArrayList<>());
                for(GramexNonEmpty g : GrammarTools.getRulesRightVar(a.cfg, v)){
                    orderedRights.get(v).add(g);
                }
            }
        }

        private void orderLists(){
            for(Gvar v : a.cfg.getVariables()){
                List<GramexNonEmpty> list = orderedRights.get(v);
                for(int i = 0; i<list.size()-1; i++){
                    for(int j = i+1; j<list.size(); j++){
                        if(a.ht(list.get(i)).isGreaterThan(a.ht(list.get(j)))){
                            GramexNonEmpty aux = list.get(i);
                            list.set(i, list.get(j));
                            list.set(j, aux);
                        }
                    }
                }
            }
        }

        private void buildBs(){
            bs = new HashMap<>();
            for(Gvar v : a.cfg.getVariables()){
                bs.put(v, new ArrayList<>());
                bs.get(v).add(new IntegerInf(0));
                for(GramexNonEmpty g : orderedRights.get(v)){
                    bs.get(v).add(a.ht(g));
                }
            }
        }

        private void buildIs(){
            is = new HashMap<>();
            for(Gvar v : a.cfg.getVariables()){
                int n = GrammarTools.getRulesVar(a.cfg, v).size();
                is.put(v, new ArrayList<>());
                for(int m = 0; m <= n; m++){
                    IntegerInf adder = new IntegerInf(0);
                    for(int i=0; i<m; i++) adder = adder.add(bs.get(v).get(i));

                    IntegerInf k = bs.get(v).get(m).multiply(new IntegerInf(n-m+1)).add(adder);
                    is.get(v).add(k);
                }
            }
        }

        //Find k

        public int findK(Gvar v, int mid) {
            boolean found = false;
            int k = 0;
            int j = 0;
            while(j < orderedRights.get(v).size()){
                IntegerInf ik1 = is.get(v).get(j);
                IntegerInf ik2 = is.get(v).get(j+1);
                IntegerInf a = new IntegerInf(mid);
                found = ik1.isLesserEqualThan(a) && a.isLesserThan(ik2);
                if(found) k = j;
                j++;
            }
            if(!found) return 0;
            else return k;
        }

        //Getters

        public int getI(Gvar v, int pos) {
            return is.get(v).get(pos).getValue();
        }

        public GramexNonEmpty getGramex(Gvar v, int pos) {
            return orderedRights.get(v).get(pos);
        }

        public int getB(Gvar v, int pos) {
            return bs.get(v).get(pos).getValue();
        }
    }

    // Function pi(z,xb,yb)

    private Pair<Integer,Integer> pi(int z, IntegerInf xb, IntegerInf yb){
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

    private Pair<Double, Double> pi(double z){
        Pair<Double,Double> tw = simple(z);
        double t = tw.getA();
        double w = tw.getB();
        double y = z - t;
        double x = w - y;
        return new Pair<>(x,y);
    }

    private Pair<Double,Double> piDouble(double z, double xb, double yb){
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

    private Pair<Double,Double> bskip(double z, double xb, double yb){
        double sb = xb*xb+yb*yb;
        double wb = xb+yb;
        double r = 2*wb+1;
        double w = Math.floor((r-Math.ceil(Math.sqrt(r*r-8*z-4*sb+4*yb-4*xb)))/2);
        double t = ((2*wb-1)*w-w*w-sb+wb)/2;
        return new Pair<>(t,w);
    }

    private Pair<Double,Double> yskip(double z, double yb){
        double w = Math.floor((2*z+yb*yb-yb)/(2*yb));
        double t = (2*w*yb-yb*yb+yb)/2;
        return new Pair<>(t,w);
    }

    private Pair<Double,Double> xskip(double z, double xb){
        double w = Math.floor((2*z+xb*xb+xb)/(2*(xb+1)));
        double t = (2*w*xb-xb*xb+xb)/2;
        return new Pair<>(t,w);
    }

    private Pair<Double,Double> simple(double z){
        double w = Math.floor((Math.floor(Math.sqrt(8 * z + 1))-1)/2);
        double t = (w * (w + 1)) / 2;
        return new Pair<>(t,w);
    }

    private double zy(double yb){
        return (yb*(yb+1))/2;
    }

    private double zx(double xb){
        return ((xb+1)*(xb+2))/2;
    }

    private double zb(double xb, double yb){
        double zy = zy(yb);
        double zx = zx(xb);
        if(xb > yb-1) return yb*(xb-yb+1)+zy;
        else if(yb-1 > xb) return (xb+1)*(yb-xb-1)+zx;
        else return zy;
    }

    private double inf(){
        return Double.POSITIVE_INFINITY;
    }

}
