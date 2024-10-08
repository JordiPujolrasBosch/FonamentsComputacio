package Factory;

import Automatons.*;
import Elements.*;
import Elements.Grammars.*;
import Elements.Transitions.*;
import Exceptions.*;
import Factory.Builders.*;
import Factory.Constructors.*;
import GrammarComparisonArticle.WordsGenerator;
import Grammars.*;
import RegularExpressions.*;
import Utils.*;

import java.util.*;

public class Algorithms {

    // DFA == DFA

    public static boolean equalsDfas(Dfa a, Dfa b){
        DfaConstructor ac = a.getConstructor();
        DfaConstructor bc = b.getConstructor();

        boolean eq = ac.states.size() == bc.states.size();
        eq = eq && ac.finalStates.size() == bc.finalStates.size();
        eq = eq && ac.alphabet.equals(bc.alphabet);

        if(!eq) return false;

        Map<State,State> mapper = new HashMap<>();
        mapper.put(ac.start, bc.start);

        Set<State> tocheck   = new HashSet<>(ac.states);
        Set<State> checking  = new HashSet<>();
        Set<State> nextcheck = new HashSet<>();

        tocheck.remove(ac.start);
        checking.add(ac.start);

        while(eq && !tocheck.isEmpty()) {
            Iterator<State> its = checking.iterator();
            while (eq && its.hasNext()) {
                State origina = its.next();
                State originb = mapper.get(origina);

                Iterator<Character> itc = ac.alphabet.getSet().iterator();
                while (eq && itc.hasNext()) {
                    char c = itc.next();
                    State destinya = ac.transition.step(origina, c);
                    State destinyb = bc.transition.step(originb, c);

                    if (!mapper.containsKey(destinya)) mapper.put(destinya, destinyb);
                    else eq = destinyb == mapper.get(destinya);

                    if (tocheck.contains(destinya)) nextcheck.add(destinya);
                }
            }

            checking.clear();
            checking.addAll(nextcheck);
            tocheck.removeAll(nextcheck);
            nextcheck.clear();

            if(!tocheck.isEmpty() && checking.isEmpty()) eq = false;
        }

        if(eq){
            Iterator<State> itf = ac.finalStates.iterator();
            while(eq && itf.hasNext()){
                eq = bc.finalStates.contains(mapper.get(itf.next()));
            }
        }

        return eq;
    }

    // MINIMIZE DFA

    public static Dfa minimize(Dfa x){
        DfaConstructor dc = x.getConstructor();
        MinimizePrivate.deleteUnusedStates(dc);
        return MinimizePrivate.defineMinimal(dc, MinimizePrivate.findPartition(dc)).getDfa();
    }

    private static class MinimizePrivate {
        static void deleteUnusedStates(DfaConstructor dc){
            Set<State> unused = new HashSet<>(dc.states);
            unused.remove(dc.start);

            Set<State> checking = new HashSet<>();
            checking.add(dc.start);

            boolean changed = true;

            while(changed){
                changed = false;

                Set<State> goTo = new HashSet<>();
                for(State o : checking){
                    for(char c : dc.alphabet.getSet()){
                        goTo.add(dc.transition.step(o,c));
                    }
                }

                checking.clear();
                for(State s : goTo){
                    if(unused.contains(s)){
                        checking.add(s);
                        unused.remove(s);
                        changed = true;
                    }
                }
            }

            for(State s : unused){
                dc.states.remove(s);
                dc.finalStates.remove(s);
                dc.transition.removeState(s);
            }
        }

        static Set<Set<State>> findPartition(DfaConstructor dc){
            Set<Set<State>> partition = new HashSet<>();
            Set<State> nonFinal = new HashSet<>();
            for(State s : dc.states){
                if(!dc.finalStates.contains(s)) nonFinal.add(s);
            }
            partition.add(nonFinal);
            partition.add(dc.finalStates);

            boolean consolidated = false;
            Iterator<Set<State>> ita, itb;
            Iterator<Character> itc;
            Set<State> sa, sb, right, left;
            char c;

            while(!consolidated){
                consolidated = true;

                ita = partition.iterator();
                while(ita.hasNext() && consolidated){
                    sa = ita.next();
                    itb = partition.iterator();
                    while(itb.hasNext() && consolidated){
                        sb = itb.next();
                        itc = dc.alphabet.getSet().iterator();
                        while(itc.hasNext() && consolidated){
                            c = itc.next();
                            right = new HashSet<>();
                            left = new HashSet<>();
                            for(State a : sa){
                                if(sb.contains(dc.transition.step(a,c))) right.add(a);
                                else left.add(a);
                            }

                            if(!right.isEmpty() && right.size()!=sa.size()){
                                consolidated = false;
                                partition.remove(sa);
                                partition.add(right);
                                partition.add(left);
                            }
                        }
                    }
                }
            }

            partition.remove(new HashSet<State>());
            return partition;
        }

        static Set<State> findWhichSetContains(Set<Set<State>> x, State s){
            boolean found = false;
            Set<State> act = null;
            Iterator<Set<State>> it = x.iterator();
            while(it.hasNext() && !found){
                act = it.next();
                found = act.contains(s);
            }
            return act;
        }

        static DfaConstructor defineMinimal(DfaConstructor dc, Set<Set<State>> partition){
            Map<Set<State>, State> mapper = new HashMap<>();
            for(Set<State> ss : partition) mapper.put(ss, new State());

            DfaConstructor res = new DfaConstructor();

            res.states.addAll(mapper.values());

            res.alphabet.addAll(dc.alphabet);

            res.start = mapper.get(findWhichSetContains(partition, dc.start));

            for(Set<State> ss : partition){
                if(!Utility.conjunctionIsEmpty(ss,dc.finalStates)){
                    res.finalStates.add(mapper.get(ss));
                }
            }

            for(Set<State> oo : partition){
                for(Character c : dc.alphabet.getSet()){
                    State inOrigin = oo.iterator().next();
                    State inDestiny = dc.transition.step(inOrigin, c);

                    State origin = mapper.get(oo);
                    State destiny = mapper.get(findWhichSetContains(partition, inDestiny));

                    res.transition.add(origin, destiny, c);
                }
            }

            return res;
        }
    }

    public static Dfa minimize2(Dfa x){
        DfaConstructor dc = x.getConstructor();

        MinimizeTwoPrivate.deleteUnusedStates(dc);

        //Pair int <-> state

        Map<State,Integer> mapper  = new HashMap<>();
        Map<Integer,State> reverse = new HashMap<>();
        int i = 0;
        for(State s : dc.states){
            mapper.put(s,i);
            reverse.put(i,s);
            i++;
        }

        Map<State,Map<State,Boolean>> matrix = MinimizeTwoPrivate.startMatrix(dc, reverse);
        MinimizeTwoPrivate.iterateMatrix(dc, reverse, mapper, matrix);
        Set<Set<State>> partition = MinimizeTwoPrivate.buildPartition(dc, mapper, matrix);
        return MinimizeTwoPrivate.defineMinimal(dc, partition).getDfa();
    }

    private static class MinimizeTwoPrivate {
        static void deleteUnusedStates(DfaConstructor dc){
            Set<State> unused = new HashSet<>(dc.states);
            unused.remove(dc.start);

            Set<State> checking = new HashSet<>();
            checking.add(dc.start);

            boolean changed = true;

            while(changed){
                changed = false;

                Set<State> goTo = new HashSet<>();
                for(State o : checking){
                    for(char c : dc.alphabet.getSet()){
                        goTo.add(dc.transition.step(o,c));
                    }
                }

                checking.clear();
                for(State s : goTo){
                    if(unused.contains(s)){
                        checking.add(s);
                        unused.remove(s);
                        changed = true;
                    }
                }
            }

            for(State s : unused){
                dc.states.remove(s);
                dc.finalStates.remove(s);
                dc.transition.removeState(s);
            }
        }

        static Map<State,Map<State,Boolean>> startMatrix(DfaConstructor dc, Map<Integer,State> reverse){
            Map<State,Map<State,Boolean>> matrix = new HashMap<>();
            int n = dc.states.size();

            for(int a = 0; a <= n-2; a++){
                State sa = reverse.get(a);
                boolean accepta = dc.finalStates.contains(sa);
                for(int b = a+1; b <= n-1; b++){
                    State sb = reverse.get(b);
                    boolean acceptb = dc.finalStates.contains(sb);

                    if(!matrix.containsKey(sa)) matrix.put(sa, new HashMap<>());
                    if(accepta != acceptb) matrix.get(sa).put(sb,false);
                    else matrix.get(sa).put(sb,true);
                }
            }

            return matrix;
        }

        static void iterateMatrix(DfaConstructor dc, Map<Integer,State> reverse, Map<State,Integer> mapper, Map<State,Map<State,Boolean>> matrix){
            int n = dc.states.size();

            boolean consolidated = false;
            while(!consolidated){
                consolidated = true;
                for(int a = 0; a <= n-2; a++){
                    State sa = reverse.get(a);
                    for(int b = a+1; b <= n-1; b++){
                        State sb = reverse.get(b);
                        for(char c : dc.alphabet.getSet()){
                            State destinya = dc.transition.step(sa,c);
                            State destinyb = dc.transition.step(sb,c);

                            if(destinya != destinyb){
                                boolean value;
                                if(mapper.get(destinya) < mapper.get(destinyb)) value = matrix.get(destinya).get(destinyb);
                                else value = matrix.get(destinyb).get(destinya);

                                if(!value && matrix.get(sa).get(sb)){
                                    consolidated = false;
                                    matrix.get(sa).put(sb,false);
                                }
                            }
                        }
                    }
                }
            }
        }

        static Set<Set<State>> buildPartition(DfaConstructor dc, Map<State,Integer> mapper, Map<State,Map<State,Boolean>> matrix){
            Set<State> bag = new HashSet<>(dc.states);
            Set<Set<State>> partition = new HashSet<>();

            while(!bag.isEmpty()){
                State s = bag.iterator().next();
                bag.remove(s);

                Set<State> toadd = new HashSet<>();
                Set<State> act = new HashSet<>();
                act.add(s);
                for(State q : bag){
                    if(mapper.get(s) < mapper.get(q)){
                        if(matrix.get(s).get(q)) toadd.add(q);
                    }
                    else{
                        if(matrix.get(q).get(s)) toadd.add(q);
                    }
                }

                act.addAll(toadd);
                bag.removeAll(toadd);
                partition.add(act);
            }

            return partition;
        }

        static DfaConstructor defineMinimal(DfaConstructor dc, Set<Set<State>> partition){
            Map<Set<State>, State> mapper = new HashMap<>();
            for(Set<State> ss : partition) mapper.put(ss, new State());

            DfaConstructor res = new DfaConstructor();

            res.states.addAll(mapper.values());

            res.alphabet.addAll(dc.alphabet);

            res.start = mapper.get(findWhichSetContains(partition, dc.start));

            for(Set<State> ss : partition){
                if(!Utility.conjunctionIsEmpty(ss,dc.finalStates)){
                    res.finalStates.add(mapper.get(ss));
                }
            }

            for(Set<State> oo : partition){
                for(Character c : dc.alphabet.getSet()){
                    State inOrigin = oo.iterator().next();
                    State inDestiny = dc.transition.step(inOrigin, c);

                    State origin = mapper.get(oo);
                    State destiny = mapper.get(findWhichSetContains(partition, inDestiny));

                    res.transition.add(origin, destiny, c);
                }
            }

            return res;
        }

        static Set<State> findWhichSetContains(Set<Set<State>> x, State s){
            boolean found = false;
            Set<State> act = null;
            Iterator<Set<State>> it = x.iterator();
            while(it.hasNext() && !found){
                act = it.next();
                found = act.contains(s);
            }
            return act;
        }
    }

    // NFA TO DFA

    public static Dfa nfaToDfa(Nfa x){
        return DeterminePrivate.defineDetermine(x, DeterminePrivate.findDeterminingSet(x)).getDfa();
    }

    private static class DeterminePrivate {
        static DfaConstructor defineDetermine(Nfa x, Set<Set<State>> dsss){
            NfaConstructor nc = x.getConstructor();
            DfaConstructor dc = new DfaConstructor();

            Map<Set<State>, State> mapper = new HashMap<>();
            for(Set<State> ss : dsss) mapper.put(ss, new State());

            dc.states.addAll(mapper.values());

            dc.alphabet.addAll(nc.alphabet);
            dc.alphabet.removeEmptyChar();

            dc.start = mapper.get(nc.transition.stateExtended(nc.start));

            for(Set<State> ss : dsss){
                if(!Utility.conjunctionIsEmpty(ss, nc.finalStates)) dc.finalStates.add(mapper.get(ss));
            }

            for(Set<State> ss : dsss){
                for(char c : dc.alphabet.getSet()){
                    dc.transition.add(mapper.get(ss), mapper.get(destinyOf(nc,ss,c)), c);
                }
            }

            return dc;
        }

        static Set<State> destinyOf(NfaConstructor nc, Set<State> ss, char c){
            Set<State> res = new HashSet<>();
            for(State o : ss){
                for(State d : nc.transition.step(o,c)){
                    res.addAll(nc.transition.stateExtended(d));
                }
            }
            return res;
        }

        static Set<Set<State>> findDeterminingSet(Nfa x){
            NfaConstructor nc = x.getConstructor();
            Set<Set<State>> before = new HashSet<>();
            Set<Set<State>> mid    = new HashSet<>();
            Set<Set<State>> after  = new HashSet<>();

            mid.add(nc.transition.stateExtended(nc.start));

            boolean consolidated = false;
            while(!consolidated){
                consolidated = true;

                for(Set<State> ss : mid){
                    if(!after.contains(ss)){
                        after.add(ss);
                        for(char c : nc.alphabet.getSet()) before.add(destinyOf(nc,ss,c));
                        consolidated = false;
                    }
                }

                mid.clear();
                mid.addAll(before);
                before.clear();
            }

            return after;
        }
    }

    // TRANSFORMATIONS BETWEEN AUTOMATONS

    public static Nfa dataToNfa(AutomatonData data) throws AutomatonReaderException {
        if(!data.check()){
            throw new AutomatonReaderException(Printer.exceptionMessage);
        }
        if(!data.getAlphabet().containsEmptyChar()){
            Printer.automatonCheckEmptyChar();
            throw new AutomatonReaderException(Printer.exceptionMessage);
        }

        List<State> array = new ArrayList<>();
        for(int i = 0; i < data.getNumberStates(); i++) {
            array.add(new State());
        }

        NfaConstructor nc = new NfaConstructor();

        nc.states.addAll(array);

        nc.alphabet = data.getAlphabet();

        nc.start = array.get(data.getStart());

        for(int x : data.getFinalStates()) {
            nc.finalStates.add(array.get(x));
        }

        for(RuleData r : data.getTransitions()) {
            nc.transition.add(array.get(r.getOrigin()), array.get(r.getDestiny()), r.getCharacter());
        }

        return nc.getNfa();
    }

    public static Dfa dataToDfa(AutomatonData data) throws AutomatonReaderException {
        if(!data.check()) {
            throw new AutomatonReaderException(Printer.exceptionMessage);
        }
        if(!data.isDeterministic()) {
            Printer.automatonNondeterministic();
            throw new AutomatonReaderException(Printer.exceptionMessage);
        }

        List<State> array = new ArrayList<>();
        for(int i = 0; i < data.getNumberStates(); i++) {
            array.add(new State());
        }

        DfaConstructor dc = new DfaConstructor();

        dc.states.addAll(array);

        dc.alphabet = data.getAlphabet();

        dc.start = array.get(data.getStart());

        for(int x : data.getFinalStates()) {
            dc.finalStates.add(array.get(x));
        }

        for(RuleData r : data.getTransitions()) {
            dc.transition.add(array.get(r.getOrigin()), array.get(r.getDestiny()), r.getCharacter());
        }

        if(!data.isComplete()){
            State sink = new State();
            dc.states.add(sink);

            for(State o : dc.states){
                for(char c : dc.alphabet.getSet()){
                    if(!dc.transition.hasRule(o,c)) dc.transition.add(o,sink,c);
                }
            }
        }

        return dc.getDfa();
    }

    public static Nfa dfaToNfa(Dfa x){
        NfaConstructor nc = new NfaConstructor();
        DfaConstructor dcx = x.getConstructor();

        nc.states.addAll(dcx.states);

        nc.alphabet.addAll(dcx.alphabet);
        nc.alphabet.addEmptyChar();

        nc.start = dcx.start;

        nc.finalStates.addAll(dcx.finalStates);

        nc.transition.addRules(dcx.transition.getRules());

        return nc.getNfa();
    }

    public static Gnfa nfaToGnfa(Nfa x){
        NfaConstructor ncx = x.getConstructor();
        GnfaConstructor gc = new GnfaConstructor();

        gc.start  = new State();
        gc.accept = new State();

        gc.states.addAll(ncx.states);
        gc.states.add(gc.start);
        gc.states.add(gc.accept);

        gc.alphabet.addAll(ncx.alphabet);

        gc.transition = new Gntf(gc.start, gc.accept, gc.states);
        gc.transition.addReplace(gc.start, ncx.start, RegexEmptyChar.getInstance());
        for(State s : ncx.finalStates){
            gc.transition.addReplace(s, gc.accept, RegexEmptyChar.getInstance());
        }
        for(Rule r : ncx.transition.getRules()){
            if(r.getCharacter() == Alphabet.getEmptyChar()){
                gc.transition.addUnion(r.getOrigin(), r.getDestiny(), RegexEmptyChar.getInstance());
            }
            else{
                gc.transition.addUnion(r.getOrigin(), r.getDestiny(), new RegexChar(r.getCharacter()));
            }
        }

        return gc.getGnfa();
    }

    public static RegularExpression gnfaToRegex(Gnfa x){
        GnfaConstructor gc = x.getConstructor();

        while(gc.states.size() > 2){
            //ACT
            Iterator<State> itact = gc.states.iterator();
            State act = itact.next();
            while(act == gc.start || act == gc.accept) act = itact.next();

            //ORIGIN
            Set<State> so = new HashSet<>(gc.states);
            so.remove(gc.accept);
            so.remove(act);

            //DESTINY
            Set<State> sd = new HashSet<>(gc.states);
            sd.remove(gc.start);
            sd.remove(act);

            //REPLACE rules
            for(State o : so){
                for(State d : sd){
                    RegularExpression a = gc.transition.step(o,act);
                    RegularExpression b = gc.transition.step(act,act);
                    RegularExpression c = gc.transition.step(act, d);

                    gc.transition.addUnion(o, d, regexReductionStep(a,b,c));
                }
            }

            //DELETE state
            gc.states.remove(act);
            gc.transition.removeState(act);
        }

        return gc.transition.step(gc.start, gc.accept);
    }

    private static RegularExpression regexReductionStep(RegularExpression a, RegularExpression b, RegularExpression c){
        return new RegexConcat(new RegexConcat(a,new RegexStar(b)),c);
    }

    public static Dfa complement(Dfa x){
        DfaConstructor dc = x.getConstructor();

        Set<State> old = new HashSet<>(dc.finalStates);
        dc.finalStates.clear();

        for(State s : dc.states){
            if(!old.contains(s)) dc.finalStates.add(s);
        }

        return dc.getDfa();
    }

    public static Nfa reverse(Dfa x){
        DfaConstructor dcx = x.getConstructor();
        NfaConstructor nc = new NfaConstructor();
        State newStart = new State();

        nc.states.addAll(dcx.states);
        nc.states.add(newStart);

        nc.alphabet.addAll(dcx.alphabet);
        nc.alphabet.addEmptyChar();

        nc.start = newStart;

        nc.finalStates.add(dcx.start);

        for(Rule r : dcx.transition.getRules()){
            nc.transition.add(r.getDestiny(), r.getOrigin(), r.getCharacter());
        }
        for(State s : dcx.finalStates){
            nc.transition.add(newStart, s, Alphabet.getEmptyChar());
        }

        return nc.getNfa();
    }

    // REGEX TO NFA

    public static Nfa regexCharToNfa(char c){
        NfaConstructor nc = new NfaConstructor();
        State start = new State();
        State finish = new State();

        nc.states.add(start);
        nc.states.add(finish);

        nc.alphabet.addChar(c);
        nc.alphabet.addEmptyChar();

        nc.start = start;

        nc.finalStates.add(finish);

        nc.transition.add(start, finish, c);

        return nc.getNfa();
    }

    public static Nfa regexEmptyCharToNfa() {
        NfaConstructor nc = new NfaConstructor();
        State s = new State();

        nc.states.add(s);
        nc.alphabet.addEmptyChar();
        nc.start = s;
        nc.finalStates.add(s);
        return nc.getNfa();
    }

    public static Nfa regexVoidToNfa(){
        NfaConstructor nc = new NfaConstructor();
        State s = new State();

        nc.states.add(s);
        nc.alphabet.addEmptyChar();
        nc.start = s;
        return nc.getNfa();
    }

    public static Nfa star(Nfa x){
        NfaConstructor ncx = x.getConstructor();
        NfaConstructor nc = new NfaConstructor();
        State s = new State();

        nc.states.addAll(ncx.states);
        nc.states.add(s);

        nc.alphabet.addAll(ncx.alphabet);
        nc.alphabet.addEmptyChar();

        nc.start = s;

        nc.finalStates.addAll(ncx.finalStates);
        nc.finalStates.add(s);

        nc.transition.addRules(ncx.transition.getRules());
        nc.transition.add(s, ncx.start, Alphabet.getEmptyChar());
        for(State a : ncx.finalStates) nc.transition.add(a, ncx.start, Alphabet.getEmptyChar());

        return nc.getNfa();
    }

    public static Nfa union(Nfa a, Nfa b){
        NfaConstructor nca = a.getConstructor();
        NfaConstructor ncb = b.getConstructor();
        NfaConstructor nc = new NfaConstructor();
        State s = new State();

        nc.states.addAll(nca.states);
        nc.states.addAll(ncb.states);
        nc.states.add(s);

        nc.alphabet.addAll(nca.alphabet);
        nc.alphabet.addAll(ncb.alphabet);
        nc.alphabet.addEmptyChar();

        nc.start = s;

        nc.finalStates.addAll(nca.finalStates);
        nc.finalStates.addAll(ncb.finalStates);

        nc.transition.addRules(nca.transition.getRules());
        nc.transition.addRules(ncb.transition.getRules());
        nc.transition.add(s, nca.start, Alphabet.getEmptyChar());
        nc.transition.add(s, ncb.start, Alphabet.getEmptyChar());

        return nc.getNfa();
    }

    public static Nfa concatenation(Nfa a, Nfa b) {
        NfaConstructor nca = a.getConstructor();
        NfaConstructor ncb = b.getConstructor();
        NfaConstructor nc = new NfaConstructor();

        nc.states.addAll(nca.states);
        nc.states.addAll(ncb.states);

        nc.alphabet.addAll(nca.alphabet);
        nc.alphabet.addAll(ncb.alphabet);
        nc.alphabet.addEmptyChar();

        nc.start = nca.start;

        nc.finalStates.addAll(ncb.finalStates);

        nc.transition.addRules(nca.transition.getRules());
        nc.transition.addRules(ncb.transition.getRules());
        for(State s : nca.finalStates) nc.transition.add(s, ncb.start, Alphabet.getEmptyChar());

        return nc.getNfa();
    }

    // REGEX TO CFG

    public static Cfg regexToCfg(RegularExpression regex){
        CfgConstructor cc = new CfgConstructor();

        Map<RegularExpression, Gvar> mapper = RegexToCfgPrivate.buildRegexCfgMapper(regex, cc);
        cc.start = mapper.get(regex);

        for(RegularExpression x : mapper.keySet()){
            Gvar v = mapper.get(x);

            switch (x.type()){
                case CHAR -> {
                    RegexChar c = (RegexChar) x;
                    cc.terminals.addChar(c.getC());
                    cc.rules.add(new Grule(v, new GramexChar(c.getC())));
                }
                case EMPTY -> {
                    cc.terminals.addEmptyChar();
                    cc.rules.add(new Grule(v, GramexEmpty.getInstance()));
                }
                case CONCAT -> {
                    RegexConcat c = (RegexConcat) x;
                    GramexVar a = new GramexVar(mapper.get(c.getA()));
                    GramexVar b = new GramexVar(mapper.get(c.getB()));
                    cc.rules.add(new Grule(v, new GramexConcat(a, b)));
                }
                case STAR -> {
                    RegexStar s = (RegexStar) x;
                    GramexVar in = new GramexVar(mapper.get(s.getX()));
                    cc.rules.add(new Grule(v, GramexEmpty.getInstance()));
                    cc.rules.add(new Grule(v, new GramexConcat(in, new GramexVar(v))));
                    cc.terminals.addEmptyChar();
                }
                case UNION -> {
                    RegexUnion u = (RegexUnion) x;
                    GramexVar a = new GramexVar(mapper.get(u.getA()));
                    GramexVar b = new GramexVar(mapper.get(u.getB()));
                    cc.rules.add(new Grule(v, a));
                    cc.rules.add(new Grule(v, b));
                }
            }
        }

        return cc.getCfg();
    }

    private static class RegexToCfgPrivate{
        static Map<RegularExpression, Gvar> buildRegexCfgMapper(RegularExpression r, CfgConstructor cc){
            Map<RegularExpression, Gvar> mapper = new HashMap<>();
            Gvar v = new Gvar('R',0);
            Set<RegularExpression> set = buildRegexSet(r);
            for(RegularExpression x : set) mapper.put(x, cc.generate(v));
            return mapper;
        }

        static Set<RegularExpression> buildRegexSet(RegularExpression r){
            Set<RegularExpression> set = new HashSet<>();

            set.add(r);
            if(r.type() == TypesRegex.CONCAT){
                RegexConcat c = (RegexConcat) r;
                set.addAll(buildRegexSet(c.getA()));
                set.addAll(buildRegexSet(c.getB()));
            }
            else if(r.type() == TypesRegex.STAR){
                RegexStar s = (RegexStar) r;
                set.addAll(buildRegexSet(s.getX()));
            }
            else if(r.type() == TypesRegex.UNION){
                RegexUnion u = (RegexUnion) r;
                set.addAll(buildRegexSet(u.getA()));
                set.addAll(buildRegexSet(u.getB()));
            }

            return set;
        }
    }

    // GENERATE WORDS REGEX

    public static List<String> generateWords(RegularExpression regex, int n){
        RegularExpression simplified = regex.simplify();
        IntegerInf bagSize = simplified.wordsCount();
        if(n <= 0 || bagSize.isZero() || n > 500) return new ArrayList<>();

        Set<String> set = new HashSet<>();

        int m = n;
        if(!bagSize.isInfinity() && bagSize.getValue() < n) m = bagSize.getValue();

        int starSize = m;
        if(Math.pow(10, GenerateWordsPrivate.starCounter(simplified)) > m) starSize = 20;

        while(set.size() < m) set.add(GenerateWordsPrivate.generateOneWord(simplified, starSize));
        return set.stream().toList();
    }

    private static class GenerateWordsPrivate{
        static String generateOneWord(RegularExpression regex, int starSize){
            String out = "";

            switch (regex.type()){
                case UNION -> {
                    RegexUnion u = (RegexUnion) regex;
                    out = generateOneWordUnion(u, starSize);
                }
                case STAR -> {
                    Random rand = new Random();
                    RegexStar s = (RegexStar) regex;
                    for(int i=1; i <= rand.nextInt(starSize); i++) out = out + generateOneWord(s.getX(), starSize);
                }
                case CONCAT -> {
                    RegexConcat c = (RegexConcat) regex;
                    out = generateOneWord(c.getA(), starSize) + generateOneWord(c.getB(), starSize);
                }
                case CHAR -> {
                    RegexChar c = (RegexChar) regex;
                    out = String.valueOf(c.getC());
                }
            }

            return out;
        }

        static String generateOneWordUnion(RegexUnion u, int starSize){
            List<RegularExpression> list = new ArrayList<>();
            List<RegexUnion> unions = new ArrayList<>();
            unions.add(u);

            while(!unions.isEmpty()){
                RegexUnion last = unions.get(unions.size()-1);
                unions.remove(unions.size()-1);

                if(last.getA().type() == TypesRegex.UNION) unions.add((RegexUnion) last.getA());
                else list.add(last.getA());

                if(last.getB().type() == TypesRegex.UNION) unions.add((RegexUnion) last.getB());
                else list.add(last.getB());
            }

            Random rand = new Random();
            return generateOneWord(list.get(rand.nextInt(list.size())),starSize);
        }

        static int starCounter(RegularExpression regex){
            int x = 0;

            switch (regex.type()){
                case STAR -> {
                    RegexStar s = (RegexStar) regex;
                    x = 1 + starCounter(s.getX());
                }
                case CONCAT -> {
                    RegexConcat c = (RegexConcat) regex;
                    x = starCounter(c.getA()) + starCounter(c.getB());
                }
                case UNION -> {
                    RegexUnion u = (RegexUnion) regex;
                    x = Math.min(starCounter(u.getA()), starCounter(u.getB()));
                }
            }

            return x;
        }
    }

    // CFG TO PDA

    public static Pda cfgToPda(Cfg x) {
        CfgConstructor ccx = x.getConstructor();
        PdaConstructor pc = new PdaConstructor();

        State start = new State();
        State accept = new State();
        State loop =  new State();

        pc.states.add(start);
        pc.states.add(accept);
        pc.states.add(loop);

        pc.start = start;

        pc.finalStates.add(accept);

        pc.alphabet.addAll(ccx.terminals);
        pc.alphabet.addEmptyChar();
        pc.alphabet.addChar(Alphabet.getStackChar());

        for(char c : pc.alphabet.getSet()) pc.addMapper(c);
        for(Gvar v : ccx.variables) pc.addMapper(v);
        pc.addMapper(Alphabet.getEmptyChar());

        int e = pc.getMapper(Alphabet.getEmptyChar());
        State mid = new State();
        pc.states.add(mid);
        pc.transition.add(start, e, e, mid, pc.getMapper(Alphabet.getStackChar()));
        pc.transition.add(mid, e, e, loop, pc.getMapper(ccx.start));
        pc.transition.add(loop, e, pc.getMapper(Alphabet.getStackChar()), accept, e);

        for(char c : ccx.terminals.getSet()){
            pc.transition.add(loop, pc.getMapper(c), pc.getMapper(c), loop, e);
        }

        CfgToPdaPrivate.buildRules(pc, ccx, loop);

        return pc.getCfg();
    }

    private static class CfgToPdaPrivate {
        static void buildRules(PdaConstructor pc, CfgConstructor ccx, State loop){
            int e = pc.getMapper(Alphabet.getEmptyChar());
            for(Grule r : ccx.rules){
                if(r.getRight().length() == 0) {
                    pc.transition.add(loop, e, pc.getMapper(r.getLeft()), loop, e);
                }
                else if(r.getRight().length() == 1){
                    int k;
                    if(r.getRight().type() == TypesGramex.VAR) k = pc.getMapper(r.getRight().toGramexVar().getV());
                    else k = pc.getMapper(r.getRight().toGramexChar().getC());
                    pc.transition.add(loop, e, pc.getMapper(r.getLeft()), loop, k);
                }
                else{
                    List<Integer> list = makeList(r.getRight(), pc);

                    State pre;
                    State post = null;
                    for(int i = list.size()-1; i >= 0; i--){
                        if(i == list.size()-1){
                            pre = loop;
                            post = new State();
                            pc.states.add(post);
                            pc.transition.add(pre, e, pc.getMapper(r.getLeft()), post, list.get(i));
                        }
                        else if(i == 0){
                            pre = post;
                            post = loop;
                            pc.transition.add(pre, e, e, post, list.get(i));
                        }
                        else{
                            pre = post;
                            post = new State();
                            pc.states.add(post);
                            pc.transition.add(pre, e, e, post, list.get(i));
                        }
                    }
                }
            }
        }

        static List<Integer> makeList(Gramex right, PdaConstructor pc) {
            List<Integer> list = new ArrayList<>();
            if(right.type() == TypesGramex.VAR){
                list.add(pc.getMapper(right.toGramexVar().getV()));
            }
            else if(right.type() == TypesGramex.CHAR){
                list.add(pc.getMapper(right.toGramexChar().getC()));
            }
            else{
                GramexNonEmpty a = right.toGramexConcat().getA();
                GramexNonEmpty b = right.toGramexConcat().getB();
                list.addAll(makeList(a, pc));
                list.addAll(makeList(b, pc));
            }
            return list;
        }
    }

    // TRANSFORMATIONS CFG

    public static Cfg cgfNonEmptyToCfg(CfgNonEmpty x) {
        CfgNonEmptyConstructor ccx = x.getConstructor();
        CfgConstructor cc = new CfgConstructor();

        cc.variables.addAll(ccx.variables);
        cc.terminals.addAll(ccx.terminals);
        cc.start = ccx.start;

        for(GruleNonEmpty r : ccx.rules){
            cc.rules.add(new Grule(r.getLeft(), r.getRight()));
        }

        if(ccx.acceptsEmptyWord) cc.rules.add(new Grule(ccx.start, GramexEmpty.getInstance()));

        return cc.getCfg();
    }

    public static CfgNonEmpty simplifyGrammar(Cfg x) {
        CfgConstructor ccx = x.getConstructor();
        SimplifyGrammarPrivate.addNewStart(ccx);
        SimplifyGrammarPrivate.removeNonDerivableVars(ccx);
        if(!SimplifyGrammarPrivate.startIsDerivable(ccx)) return new CfgNonEmpty();
        SimplifyGrammarPrivate.removeNonReachableVarsAndTerminals(ccx);
        SimplifyGrammarPrivate.removeEmptyRules(ccx);
        SimplifyGrammarPrivate.removeUnitRules(ccx);
        SimplifyGrammarPrivate.removeNonReachableVarsAndTerminals(ccx);
        return SimplifyGrammarPrivate.cfgToCfgNonEmpty(ccx).getCfgNonEmpty();
    }

    private static class SimplifyGrammarPrivate{
        static void addNewStart(CfgConstructor ccx) {
            Gvar newStart = ccx.generate(ccx.start);
            ccx.rules.add(new Grule(newStart, new GramexVar(ccx.start)));
            ccx.start = newStart;
        }

        static void removeNonDerivableVars(CfgConstructor ccx){
            for(Gvar v : findNonDerivableVars(ccx)) removeVar(ccx, v);
        }

        static boolean startIsDerivable(CfgConstructor ccx){
            return ccx.variables.contains(ccx.start);
        }

        static void removeNonReachableVarsAndTerminals(CfgConstructor ccx){
            Set<Gvar> unusedVars = new HashSet<>();
            Set<Character> unusedTerminals = new HashSet<>();
            findUnreachableVarsAndTerminals(ccx, unusedVars, unusedTerminals);

            for(char c : unusedTerminals) ccx.terminals.removeChar(c);
            for(Gvar v : unusedVars) removeVar(ccx, v);
        }

        static void removeEmptyRules(CfgConstructor ccx){
            Set<Gvar> anulable = new HashSet<>();
            Set<Gvar> onlyAnulable = new HashSet<>();
            findAnulableVars(ccx, anulable, onlyAnulable);
            if(onlyAnulable.contains(ccx.start)){
                ccx.rules.clear();
                ccx.rules.add(new Grule(ccx.start, GramexEmpty.getInstance()));
                ccx.variables.clear();
                ccx.variables.add(ccx.start);
            }
            else if(!anulable.isEmpty()){
                Set<Grule> checked = new HashSet<>();
                Set<Grule> toCheck = new HashSet<>(ccx.rules);

                if(anulable.contains(ccx.start)) checked.add(new Grule(ccx.start, GramexEmpty.getInstance()));
                else ccx.terminals.removeEmptyChar();

                while(!toCheck.isEmpty()){
                    Grule act = toCheck.iterator().next();
                    toCheck.remove(act);

                    if(act.getRight().type() == TypesGramex.CHAR) checked.add(act);
                    else if(act.getRight().type() == TypesGramex.VAR){
                        Gvar v = act.getRight().toGramexVar().getV();
                        if(!onlyAnulable.contains(v)) checked.add(act);
                    }
                    else if(act.getRight().type() == TypesGramex.CONCAT){
                        Set<Gramex> sc = removeAnulableInConcat(act.getRight().toGramexConcat(), anulable, onlyAnulable);
                        for(Gramex g : sc) checked.add(new Grule(act.getLeft(), g));
                    }
                }

                ccx.rules = checked;
                ccx.variables.removeAll(onlyAnulable);
            }
        }

        static void removeUnitRules(CfgConstructor ccx) {
            Set<Grule> unitRules = new HashSet<>();
            for(Grule r : ccx.rules){
                if(r.getRight().type() == TypesGramex.VAR) unitRules.add(r);
            }

            Set<Grule> removed = new HashSet<>();
            while(!unitRules.isEmpty()){
                Grule rule = unitRules.iterator().next();
                Gvar v = rule.getRight().toGramexVar().getV();

                if (!rule.getLeft().equals(v)) {
                    Set<Grule> toAdd = new HashSet<>();
                    for (Grule r : GrammarTools.getRulesVar(ccx.getCfg(), v)) {
                        Grule aux = new Grule(rule.getLeft(), r.getRight());
                        if (aux.getRight().type() != TypesGramex.VAR) toAdd.add(aux);
                        else if (!removed.contains(aux)) {
                            toAdd.add(aux);
                            unitRules.add(aux);
                        }
                    }

                    ccx.rules.addAll(toAdd);
                }
                ccx.rules.remove(rule);
                removed.add(rule);
                unitRules.remove(rule);
            }
        }

        static CfgNonEmptyConstructor cfgToCfgNonEmpty(CfgConstructor ccx){
            CfgNonEmptyConstructor cnec = new CfgNonEmptyConstructor();

            cnec.acceptsEmptyWord = ccx.rules.contains(new Grule(ccx.start, GramexEmpty.getInstance()));
            cnec.start = ccx.start;
            cnec.terminals.addAll(ccx.terminals);
            cnec.variables.addAll(ccx.variables);
            for(Grule g : ccx.rules){
                if(g.getRight().type() != TypesGramex.EMPTY) {
                    cnec.rules.add(new GruleNonEmpty(g.getLeft(), g.getRight().toGramexNonEmpty()));
                }
            }

            return cnec;
        }

        //Inner

        static Set<Gvar> findNonDerivableVars(CfgConstructor ccx){
            Set<Gvar> toRemove = new HashSet<>(ccx.variables);

            //Start
            for(Grule r : ccx.rules){
                if(!GrammarTools.containsVar(r.getRight())) toRemove.remove(r.getLeft());
            }

            //Recursion
            Cfg cfg = ccx.getCfg();
            boolean consolidated = toRemove.isEmpty();
            while (!consolidated){
                consolidated = true;
                Set<Gvar> derivables = new HashSet<>();
                for (Gvar v : toRemove) {
                    if(hasDerivableRule(GrammarTools.getRulesVar(cfg,v), toRemove)){
                        derivables.add(v);
                        consolidated = false;
                    }
                }
                toRemove.removeAll(derivables);
                if(toRemove.isEmpty()) consolidated = true;
            }

            return toRemove;
        }

        static boolean hasDerivableRule(Set<Grule> rules, Set<Gvar> nonDerivableVars){
            boolean found = false;
            Iterator<Grule> it = rules.iterator();
            while(it.hasNext() && !found){
                Gramex g = it.next().getRight();
                switch (g.type()){
                    case EMPTY, CHAR -> found = true;
                    case VAR -> found = !nonDerivableVars.contains(g.toGramexVar().getV());
                    case CONCAT -> {
                        boolean foundNonDerivableVarInConcat = false;
                        Iterator<GramexNonEmpty> itc = g.toGramexConcat().toList().iterator();
                        while(itc.hasNext() && !foundNonDerivableVarInConcat){
                            GramexNonEmpty gne = itc.next();
                            foundNonDerivableVarInConcat = gne.type() == TypesGramex.VAR && nonDerivableVars.contains(gne.toGramexVar().getV());
                        }
                        found = !foundNonDerivableVarInConcat;
                    }
                }
            }
            return found;
        }

        static void findUnreachableVarsAndTerminals(CfgConstructor ccx, Set<Gvar> unusedVars, Set<Character> unusedTerminals){
            Set<Character> reachedTerminals = new HashSet<>();
            Set<Gvar> reachedVars = new HashSet<>();
            reachedVars.add(ccx.start);

            Cfg cfg = ccx.getCfg();
            Set<Grule> checking = GrammarTools.getRulesVar(cfg, ccx.start);
            Set<Grule> nextCheck = new HashSet<>();

            boolean consolidated = false;
            while(!consolidated){
                consolidated = true;
                for(Grule g : checking){
                    if(g.getRight().type() == TypesGramex.EMPTY && !reachedTerminals.contains(Alphabet.getEmptyChar())){
                        reachedTerminals.add(Alphabet.getEmptyChar());
                        consolidated = false;
                    }
                    else if(g.getRight().type() == TypesGramex.CHAR && !reachedTerminals.contains(g.getRight().toGramexChar().getC())){
                        reachedTerminals.add(g.getRight().toGramexChar().getC());
                        consolidated = false;
                    }
                    else if(g.getRight().type() == TypesGramex.VAR && !reachedVars.contains(g.getRight().toGramexVar().getV())){
                        reachedVars.add(g.getRight().toGramexVar().getV());
                        nextCheck.addAll(GrammarTools.getRulesVar(cfg, g.getRight().toGramexVar().getV()));
                        consolidated = false;
                    }
                    else if(g.getRight().type() == TypesGramex.CONCAT){
                        for(GramexNonEmpty gne : g.getRight().toGramexConcat().toList()){
                            if(gne.type() == TypesGramex.CHAR && !reachedTerminals.contains(gne.toGramexChar().getC())){
                                reachedTerminals.add(gne.toGramexChar().getC());
                                consolidated = false;
                            }
                            else if(gne.type() == TypesGramex.VAR && !reachedVars.contains(gne.toGramexVar().getV())){
                                reachedVars.add(gne.toGramexVar().getV());
                                nextCheck.addAll(GrammarTools.getRulesVar(cfg, gne.toGramexVar().getV()));
                                consolidated = false;
                            }
                        }
                    }
                }
                checking.clear();
                checking.addAll(nextCheck);
                nextCheck.clear();
            }

            unusedVars.addAll(ccx.variables);
            unusedVars.removeAll(reachedVars);

            unusedTerminals.addAll(ccx.terminals.getSet());
            if(ccx.terminals.containsEmptyChar()) unusedTerminals.add(Alphabet.getEmptyChar());
            unusedTerminals.removeAll(reachedTerminals);
        }

        static void removeVar(CfgConstructor ccx, Gvar v){
            Set<Grule> toRemove = new HashSet<>();
            ccx.variables.remove(v);
            for(Grule r : ccx.rules){
                if(r.getLeft().equals(v) || GrammarTools.containsVar(r.getRight(), v)) toRemove.add(r);
            }
            ccx.rules.removeAll(toRemove);
        }

        static void findAnulableVars(CfgConstructor ccx, Set<Gvar> anulable, Set<Gvar> onlyAnulable){
            for(Grule g : ccx.rules){
                if(g.getRight().type() == TypesGramex.EMPTY) anulable.add(g.getLeft());
            }

            boolean consolidated = false;
            while(!consolidated){
                consolidated = true;
                for(Gvar v : ccx.variables){
                    if(!anulable.contains(v) && isAnulable(ccx, v, anulable)){
                        anulable.add(v);
                        consolidated = false;
                    }
                }
            }

            for(Gvar v : anulable){
                Set<Gvar> aux = new HashSet<>();
                if(isOnlyAnulable(ccx, v, aux)) onlyAnulable.add(v);
            }
        }

        static boolean isAnulable(CfgConstructor ccx, Gvar v, Set<Gvar> anulable){
            boolean found = false;
            Iterator<Grule> it = GrammarTools.getRulesVar(ccx.getCfg(), v).iterator();
            while(it.hasNext() && !found){
                Grule r = it.next();
                if(r.getRight().type() == TypesGramex.VAR && anulable.contains(r.getRight().toGramexVar().getV())) found = true;
                else if(r.getRight().type() == TypesGramex.CONCAT){
                    boolean foundNotAnulableInConcat = false;
                    Iterator<GramexNonEmpty> itc = r.getRight().toGramexConcat().toList().iterator();
                    while(itc.hasNext() && !foundNotAnulableInConcat){
                        GramexNonEmpty gne = itc.next();
                        if(gne.type() == TypesGramex.CHAR) foundNotAnulableInConcat = true;
                        else if(!anulable.contains(gne.toGramexVar().getV())) foundNotAnulableInConcat = true;
                    }
                    found = !foundNotAnulableInConcat;
                }
            }
            return found;
        }

        static boolean isOnlyAnulable(CfgConstructor ccx, Gvar v, Set<Gvar> aux){
            boolean foundNotAnulable = false;
            aux.add(v);
            Iterator<Grule> it = GrammarTools.getRulesVar(ccx.getCfg(), v).iterator();
            while(it.hasNext() && !foundNotAnulable){
                Grule r = it.next();
                if(r.getRight().type() == TypesGramex.CHAR) foundNotAnulable = true;
                else if(r.getRight().type() == TypesGramex.VAR) {
                    if(!aux.contains(r.getRight().toGramexVar().getV())){
                        foundNotAnulable = !isOnlyAnulable(ccx,r.getRight().toGramexVar().getV(), aux);
                    }
                }
                else if(r.getRight().type() == TypesGramex.CONCAT){
                    Iterator<GramexNonEmpty> itc = r.getRight().toGramexConcat().toList().iterator();
                    while(itc.hasNext() && !foundNotAnulable){
                        GramexNonEmpty gne = itc.next();
                        if(gne.type() == TypesGramex.CHAR) foundNotAnulable = true;
                        else{
                            if(!aux.contains(gne.toGramexVar().getV())){
                                foundNotAnulable = !isOnlyAnulable(ccx,gne.toGramexVar().getV(),aux);
                            }
                        }
                    }
                }
            }
            return !foundNotAnulable;
        }

        static Set<Gramex> removeAnulableInConcat(GramexConcat gc, Set<Gvar> anulable, Set<Gvar> onlyAnulable){
            Set<Gramex> resA = new HashSet<>();
            Set<Gramex> resB = new HashSet<>();

            switch (gc.getA().type()){
                case CHAR -> resA.add(gc.getA());
                case CONCAT -> resA.addAll(removeAnulableInConcat(gc.getA().toGramexConcat(), anulable, onlyAnulable));
                case VAR -> {
                    if(onlyAnulable.contains(gc.getA().toGramexVar().getV())){
                        resA.add(GramexEmpty.getInstance());
                    }
                    else if(anulable.contains(gc.getA().toGramexVar().getV())){
                        resA.add(GramexEmpty.getInstance());
                        resA.add(gc.getA());
                    }
                    else{
                        resA.add(gc.getA());
                    }
                }
            }

            switch (gc.getB().type()){
                case CHAR -> resB.add(gc.getB());
                case CONCAT -> resB.addAll(removeAnulableInConcat(gc.getB().toGramexConcat(), anulable, onlyAnulable));
                case VAR -> {
                    if(onlyAnulable.contains(gc.getB().toGramexVar().getV())){
                        resB.add(GramexEmpty.getInstance());
                    }
                    else if(anulable.contains(gc.getB().toGramexVar().getV())){
                        resB.add(GramexEmpty.getInstance());
                        resB.add(gc.getB());
                    }
                    else{
                        resB.add(gc.getB());
                    }
                }
            }

            Set<Gramex> res = new HashSet<>();
            if(resA.isEmpty()){
                for(Gramex b : resB){
                    if(b.type() != TypesGramex.EMPTY) res.add(b);
                }
            }
            else if(resB.isEmpty()){
                for(Gramex a : resA){
                    if(a.type() != TypesGramex.EMPTY) res.add(a);
                }
            }
            else{
                for(Gramex a : resA){
                    for(Gramex b : resB){
                        if(a.type() != TypesGramex.EMPTY && b.type() != TypesGramex.EMPTY) {
                            res.add(new GramexConcat(a.toGramexNonEmpty(), b.toGramexNonEmpty()));
                        }
                        else if(a.type() == TypesGramex.EMPTY && b.type() != TypesGramex.EMPTY){
                            res.add(b);
                        }
                        else if(a.type() != TypesGramex.EMPTY && b.type() == TypesGramex.EMPTY){
                            res.add(a);
                        }
                    }
                }
            }

            return res;
        }
    }

    public static CfgNonEmpty chomsky(CfgNonEmpty x){
        CfgNonEmptyConstructor ccx = x.getConstructor();
        ChomskyPrivate.replaceTerminals(ccx);
        ChomskyPrivate.makeBinary(ccx);
        return ccx.getCfgNonEmpty();
    }

    private static class ChomskyPrivate {
        static void replaceTerminals(CfgNonEmptyConstructor ccx){
            Map<Character,Gvar> mapper = new HashMap<>();
            Set<GruleNonEmpty> toAdd = new HashSet<>();
            Set<GruleNonEmpty> toRemove = new HashSet<>();

            for(GruleNonEmpty r : ccx.rules){
                if(r.getRight().type() == TypesGramex.CONCAT && GrammarTools.containsChar(r.getRight())){
                    toRemove.add(r);
                    GramexNonEmpty aux = r.getRight();
                    while(GrammarTools.containsChar(aux)){
                        GramexChar gc = new GramexChar(GrammarTools.getLeftMostChar(aux));
                        if(!mapper.containsKey(gc.getC())) mapper.put(gc.getC(), ccx.generate(new Gvar('Z',0)));
                        aux = GrammarTools.substituteAllChars(aux, gc.getC(), mapper.get(gc.getC())).toGramexNonEmpty();
                    }
                    toAdd.add(new GruleNonEmpty(r.getLeft(), aux));
                }
            }

            ccx.rules.removeAll(toRemove);
            ccx.rules.addAll(toAdd);
            for(char c : mapper.keySet()){
                ccx.rules.add(new GruleNonEmpty(mapper.get(c), new GramexChar(c)));
            }
        }

        static void makeBinary(CfgNonEmptyConstructor ccx){
            boolean foundNonBinaryRule = true;
            while(foundNonBinaryRule){
                foundNonBinaryRule = false;
                GruleNonEmpty nonBinaryRule = null;

                Iterator<GruleNonEmpty> it = ccx.rules.iterator();
                while(it.hasNext() && !foundNonBinaryRule){
                    nonBinaryRule = it.next();
                    foundNonBinaryRule = nonBinaryRule.getRight().length() > 2;
                }

                if(foundNonBinaryRule){
                    GramexConcat pair = GrammarTools.getRightMostPair(nonBinaryRule.getRight().toGramexConcat());
                    GruleNonEmpty newRule = new GruleNonEmpty(ccx.generate(pair.getA().toGramexVar().getV()), pair);

                    Set<GruleNonEmpty> toRemove = new HashSet<>();
                    Set<GruleNonEmpty> toAdd = new HashSet<>();
                    for(GruleNonEmpty r : ccx.rules){
                        if(GrammarTools.containsPair(r.getRight(), pair) && r.getRight().length() > 2){
                            GramexNonEmpty substituted = GrammarTools.substituteConcats(r.getRight().toGramexConcat(), pair, newRule.getLeft()).toGramexNonEmpty();
                            toRemove.add(r);
                            toAdd.add(new GruleNonEmpty(r.getLeft(), substituted));
                        }
                    }
                    ccx.rules.addAll(toAdd);
                    ccx.rules.removeAll(toRemove);
                    ccx.rules.add(newRule);
                }
            }
        }
    }

    public static CfgNonEmpty greibach(CfgNonEmpty x) {
        CfgNonEmptyConstructor ccx = x.getConstructor();

        List<Gvar> order = GreibachPrivate.findOrder(ccx);
        GreibachPrivate.verifyOrder(ccx,order);
        GreibachPrivate.applyGreibachSubstitution(ccx, order);

        return ccx.getCfgNonEmpty();
    }

    private static class GreibachPrivate {
        static List<Gvar> findOrder(CfgNonEmptyConstructor ccx){
            List<Gvar> order = new ArrayList<>();

            Set<GruleNonEmpty> postRules = new HashSet<>();
            for(GruleNonEmpty r : ccx.rules){
                if(!GrammarTools.startsWithVar(r.getRight())) postRules.add(r);
                else{
                    Gvar left = r.getLeft();
                    Gvar right = GrammarTools.getLeftMostVar(r.getRight());
                    if(!order.contains(left) && !order.contains(right)){
                        order.add(left);
                        order.add(right);
                    }
                    else if(order.contains(left) && !order.contains(right)){
                        order.add(order.indexOf(left)+1, right);
                    }
                    else if(!order.contains(left) && order.contains(right)){
                        order.add(order.indexOf(right), left);
                    }
                }
            }
            for(GruleNonEmpty r : postRules){
                if(!order.contains(r.getLeft())) order.add(r.getLeft());
            }

            return order;
        }

        static void verifyOrder(CfgNonEmptyConstructor ccx, List<Gvar> order){
            int i=0;
            while(i<order.size()-1){
                if(haveLeftRecursivity(GrammarTools.getRulesVar(ccx.getCfgNonEmpty(), order.get(i)))){
                    Gvar aux = removeLeftRecursivity(ccx, order.get(i));
                    order.add(0, aux);
                }
                else if(!orderOfRulesIsCorrect(order, GrammarTools.getRulesVar(ccx.getCfgNonEmpty(), order.get(i)))){
                    applySubstitutionIncorrectRule(ccx, order.get(i), order);
                }
                else {
                    i++;
                }
            }
        }

        static void applyGreibachSubstitution(CfgNonEmptyConstructor ccx, List<Gvar> order){
            for(int i = order.size()-2; i>=0; i--){
                Set<GruleNonEmpty> rules = GrammarTools.getRulesVar(ccx.getCfgNonEmpty(), order.get(i));
                for(GruleNonEmpty r : rules){
                    if(GrammarTools.startsWithVar(r.getRight())){
                        Gvar leftMost = GrammarTools.getLeftMostVar(r.getRight());
                        Set<GramexNonEmpty> subs = GrammarTools.getRulesRightVar(ccx.getCfgNonEmpty(), leftMost);
                        for(GramexNonEmpty g : subs){
                            GramexNonEmpty substituted = GrammarTools.substituteLeftMost(r.getRight(), leftMost, g).toGramexNonEmpty();
                            ccx.rules.add(new GruleNonEmpty(r.getLeft(), substituted));
                        }
                        ccx.rules.remove(r);
                    }
                }
            }
        }

        static boolean haveLeftRecursivity(Set<GruleNonEmpty> rules){
            boolean found = false;
            Iterator<GruleNonEmpty> it = rules.iterator();
            while(it.hasNext() && !found){
                GruleNonEmpty g = it.next();
                found = GrammarTools.startsWithVar(g.getRight(), g.getLeft());
            }
            return found;
        }

        static Gvar removeLeftRecursivity(CfgNonEmptyConstructor ccx, Gvar v){
            Set<GruleNonEmpty> all = GrammarTools.getRulesVar(ccx.getCfgNonEmpty(), v);
            Set<GruleNonEmpty> have = new HashSet<>();
            Set<GruleNonEmpty> noHave = new HashSet<>();
            for(GruleNonEmpty g : all){
                if(GrammarTools.startsWithVar(g.getRight(), g.getLeft())) have.add(g);
                else noHave.add(g);
            }

            Gvar newVar = ccx.generate(v);
            Set<GruleNonEmpty> toAdd = new HashSet<>();
            for(GruleNonEmpty g : noHave){
                toAdd.add(new GruleNonEmpty(v, new GramexConcat(g.getRight(), new GramexVar(newVar))));
            }
            for(GruleNonEmpty g : have){
                GramexNonEmpty aux = GrammarTools.substituteLeftMost(g.getRight(), v, GramexEmpty.getInstance()).toGramexNonEmpty();
                toAdd.add(new GruleNonEmpty(newVar, aux));
                toAdd.add(new GruleNonEmpty(newVar, new GramexConcat(aux, new GramexVar(newVar))));
            }

            ccx.rules.addAll(toAdd);
            ccx.rules.removeAll(have);
            return newVar;
        }

        static boolean orderOfRulesIsCorrect(List<Gvar> order, Set<GruleNonEmpty> rules){
            boolean foundIncorrect = false;
            Iterator<GruleNonEmpty> it = rules.iterator();
            while(it.hasNext() && !foundIncorrect){
                GruleNonEmpty r = it.next();
                if(GrammarTools.startsWithVar(r.getRight())){
                    foundIncorrect = order.indexOf(r.getLeft()) > order.indexOf(GrammarTools.getLeftMostVar(r.getRight()));
                }
            }
            return !foundIncorrect;
        }

        static void applySubstitutionIncorrectRule(CfgNonEmptyConstructor ccx, Gvar v, List<Gvar> order){
            Set<GruleNonEmpty> rules = GrammarTools.getRulesVar(ccx.getCfgNonEmpty(), v);

            GruleNonEmpty incorrectRule = null;
            boolean foundIncorrect = false;
            Iterator<GruleNonEmpty> it = rules.iterator();
            while(it.hasNext() && !foundIncorrect){
                GruleNonEmpty r = it.next();
                if(GrammarTools.startsWithVar(r.getRight())){
                    foundIncorrect = order.indexOf(r.getLeft()) > order.indexOf(GrammarTools.getLeftMostVar(r.getRight()));
                }
                if(foundIncorrect) incorrectRule = r;
            }

            if(foundIncorrect){
                Gvar leftMost = GrammarTools.getLeftMostVar(incorrectRule.getRight());
                Set<GramexNonEmpty> subs = GrammarTools.getRulesRightVar(ccx.getCfgNonEmpty(), leftMost);
                for(GramexNonEmpty g : subs){
                    GramexNonEmpty substituted = GrammarTools.substituteLeftMost(incorrectRule.getRight(), leftMost, g).toGramexNonEmpty();
                    ccx.rules.add(new GruleNonEmpty(incorrectRule.getLeft(), substituted));
                }
                ccx.rules.remove(incorrectRule);
            }
        }
    }

    // AMBIGUITY

    public static Pair<Boolean, String> checkAmbiguity(Cfg x, int length){
        CheckAmbiguityPrivate.ExitAmbiguity exit = new CheckAmbiguityPrivate.ExitAmbiguity();

        CheckAmbiguityPrivate.simplifyBasic(x, exit);
        if(exit.finished) return exit.ret;
        CheckAmbiguityPrivate.removeEmptyRules(exit.cfg, exit);
        if(exit.finished) return exit.ret;
        CheckAmbiguityPrivate.removeUnitRules(exit.cfg, exit);
        if(exit.finished) return exit.ret;

        int l = 1;
        CfgNonEmpty ch = exit.cne.toChomsky();
        Set<String> set = new HashSet<>();
        WordsGenerator wg;
        Iterator<String> it;
        while(l <= length && !exit.finished){
            wg = new WordsGenerator(buildCfgLengthFromTo(ch, l, Math.min(l+4, length)));
            it = wg.generateAllWordsStart().iterator();
            set.clear();

            while(it.hasNext() && !exit.finished){
                String s = it.next();
                if(!set.contains(s)) set.add(s);
                else{
                    exit.finished = true;
                    exit.ret = new Pair<>(true, s);
                }
            }

            l = l + 5;
        }

        if(!exit.finished) return new Pair<>(false, "");
        return exit.ret;
    }

    private static class CheckAmbiguityPrivate{
        static void simplifyBasic(Cfg x, ExitAmbiguity exit){
            CfgConstructor ccx = x.getConstructor();
            SimplifyGrammarPrivate.addNewStart(ccx);
            SimplifyGrammarPrivate.removeNonDerivableVars(ccx);
            if(!SimplifyGrammarPrivate.startIsDerivable(ccx)){
                exit.finished = true;
                exit.ret = new Pair<>(false, "");
            }
            else{
                SimplifyGrammarPrivate.removeNonReachableVarsAndTerminals(ccx);
                exit.cfg = ccx.getCfg();
            }
        }

        static void removeEmptyRules(Cfg x, ExitAmbiguity exit){
            checkEmpty(x, exit);
            if(!exit.finished){
                CfgConstructor ccx = x.getConstructor();
                SimplifyGrammarPrivate.removeEmptyRules(ccx);
                exit.cfg = ccx.getCfg();
            }
        }

        static void removeUnitRules(Cfg x, ExitAmbiguity exit){
            checkUnit(x, exit);
            if(!exit.finished){
                CfgConstructor ccx = x.getConstructor();
                SimplifyGrammarPrivate.removeUnitRules(ccx);
                SimplifyGrammarPrivate.removeNonReachableVarsAndTerminals(ccx);
                exit.cne = SimplifyGrammarPrivate.cfgToCfgNonEmpty(ccx).getCfgNonEmpty();
            }
        }

        //Inner

        static String findWordDerived(Cfg x, Grule v){
            GramexVar start = new GramexVar(x.getStart());
            Set<Gramex> pre = new HashSet<>();
            Set<Gramex> post = new HashSet<>();
            pre.add(start);

            boolean foundVar = false;
            Gramex g = null;
            Iterator<Gramex> it = pre.iterator();
            while(it.hasNext() && !foundVar){
                Gramex a = it.next();
                foundVar = GrammarTools.containsVar(a,v.getLeft());
                if(foundVar) g = a;
            }

            while(!foundVar && !pre.isEmpty()){
                it = pre.iterator();
                while(it.hasNext()){
                    Gramex act = it.next();
                    if(GrammarTools.containsVar(act)) post.addAll(GrammarTools.step(x,act));
                }

                pre = post;
                post = new HashSet<>();

                it = pre.iterator();
                while(it.hasNext() && !foundVar){
                    Gramex a = it.next();
                    foundVar = GrammarTools.containsVar(a,v.getLeft());
                    if(foundVar) g = a;
                }
            }

            g = GrammarTools.substituteLeftMost(g, v.getLeft(), v.getRight());

            return GrammarTools.shortestWordOfGramex(x,g);
        }

        static void checkEmpty(Cfg x, ExitAmbiguity exit){
            CfgConstructor ccx = x.getConstructor();
            Set<Gvar> anulable = new HashSet<>();
            Set<Gvar> onlyAnulable = new HashSet<>();
            SimplifyGrammarPrivate.findAnulableVars(ccx, anulable, onlyAnulable);

            if(!anulable.isEmpty()){
                Set<Grule> checked = new HashSet<>();
                Set<Grule> toCheck = new HashSet<>(ccx.rules);

                while (!toCheck.isEmpty() && !exit.finished){
                    Grule act = toCheck.iterator().next();
                    toCheck.remove(act);

                    if(checked.contains(act)){
                        exit.finished = true;
                        exit.ret = new Pair<>(true, findWordDerived(x,act));
                    }
                    else{
                        switch (act.getRight().type()){
                            case EMPTY, CHAR -> checked.add(act);
                            case VAR -> {
                                checked.add(act);
                                if(anulable.contains(act.getRight().toGramexVar().getV())){
                                    Grule g = new Grule(act.getLeft(), GramexEmpty.getInstance());
                                    if(!checked.contains(g)) checked.add(g);
                                    else{
                                        exit.finished = true;
                                        exit.ret = new Pair<>(true, findWordDerived(x,g));
                                    }
                                }
                            }
                            case CONCAT -> {
                                List<Gramex> lc = removeAnulableInConcat(act.getRight().toGramexConcat(), anulable);
                                for(Gramex ex : lc){
                                    Grule g = new Grule(act.getLeft(), ex);
                                    if(!checked.contains(g)) checked.add(g);
                                    else{
                                        exit.finished = true;
                                        exit.ret = new Pair<>(true, findWordDerived(x, g));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        static List<Gramex> removeAnulableInConcat(GramexConcat gc, Set<Gvar> anulable){
            List<Gramex> resA = new ArrayList<>();
            List<Gramex> resB = new ArrayList<>();

            switch (gc.getA().type()){
                case CHAR -> resA.add(gc.getA());
                case CONCAT -> resA.addAll(removeAnulableInConcat(gc.getA().toGramexConcat(), anulable));
                case VAR -> {
                    if(anulable.contains(gc.getA().toGramexVar().getV())){
                        resA.add(GramexEmpty.getInstance());
                        resA.add(gc.getA());
                    }
                    else{
                        resA.add(gc.getA());
                    }
                }
            }

            switch (gc.getB().type()){
                case CHAR -> resB.add(gc.getB());
                case CONCAT -> resB.addAll(removeAnulableInConcat(gc.getB().toGramexConcat(), anulable));
                case VAR -> {
                    if(anulable.contains(gc.getB().toGramexVar().getV())){
                        resB.add(GramexEmpty.getInstance());
                        resB.add(gc.getB());
                    }
                    else{
                        resB.add(gc.getB());
                    }
                }
            }

            List<Gramex> res = new ArrayList<>();
            if(resA.isEmpty()) res.addAll(resB);
            else if(resB.isEmpty()) res.addAll(resA);
            else{
                for(Gramex a : resA){
                    for(Gramex b : resB){
                        if(b.type() == TypesGramex.EMPTY) res.add(a);
                        else if(a.type() == TypesGramex.EMPTY) res.add(b);
                        else res.add(new GramexConcat(a.toGramexNonEmpty(), b.toGramexNonEmpty()));
                    }
                }
            }

            return res;
        }

        static void checkUnit(Cfg x, ExitAmbiguity exit){
            Set<Grule> checked = new HashSet<>();
            Set<Grule> toCheck = new HashSet<>();
            for(Grule r : x.getRules()){
                if(r.getRight().type() == TypesGramex.VAR) toCheck.add(r);
                else checked.add(r);
            }

            while(!toCheck.isEmpty() && !exit.finished){
                Grule act = toCheck.iterator().next();
                toCheck.remove(act);
                Gvar v = act.getRight().toGramexVar().getV();
                if(act.getLeft().equals(v)){
                    exit.finished = true;
                    exit.ret = new Pair<>(true, findWordDerived(x, act));
                }
                else{
                    for(Grule g : GrammarTools.getRulesVar(x,v)){
                        Grule aux = new Grule(act.getLeft(), g.getRight());
                        if(g.getRight().type() == TypesGramex.VAR){
                            if(act.equals(aux)){
                                exit.finished = true;
                                exit.ret = new Pair<>(true, findWordDerived(x,aux));
                            }
                            else if(!toCheck.contains(aux)){
                                toCheck.add(aux);
                            }
                            else{
                                exit.finished = true;
                                exit.ret = new Pair<>(true, findWordDerived(x,aux));
                            }
                        }
                        else{
                            if(!checked.contains(aux)) checked.add(aux);
                            else{
                                exit.finished = true;
                                exit.ret = new Pair<>(true, findWordDerived(x,aux));
                            }
                        }
                    }
                }
            }
        }

        static class ExitAmbiguity{
            public boolean finished;
            public Pair<Boolean,String> ret;
            public Cfg cfg;
            public CfgNonEmpty cne;
            public ExitAmbiguity(){
                finished = false;
                ret = null;
                cfg = null;
                cne = null;
            }
        }
    }

    // CHECK WORD

    public static boolean checkWordDfa(Dfa dfa, String word) {
        DfaConstructor dc = dfa.getConstructor();
        boolean in = true;
        State act = dc.start;
        int i = 0;

        while(i < word.length() && in){
            char c = word.charAt(i);
            in = dc.alphabet.contains(c);
            if(in) act = dc.transition.step(act,c);
            i++;
        }

        return in && dc.finalStates.contains(act);
    }

    public static boolean checkWordPda(Pda pda, String word) {
        PdaConstructor pc = pda.getConstructor();

        boolean foundIlegalChar = false;
        int i = 0;
        while(!foundIlegalChar && i < word.length()) foundIlegalChar = !pc.alphabet.contains(word.charAt(i++));
        if(foundIlegalChar) return false;

        List<CheckWordPdaPrivate.PdaRunningInstance> instances = new ArrayList<>();
        instances.add(new CheckWordPdaPrivate.PdaRunningInstance(pc.start, new Stack<>(), 0));

        int counter = 0;
        boolean stop = false;
        boolean accept = false;
        while(!stop){
            CheckWordPdaPrivate.ExitStep x = CheckWordPdaPrivate.runningStep(instances, word, pc);
            counter++;
            stop = (x.accept || x.list.isEmpty() || counter == 7000);
            instances = x.list;
            accept = x.accept;
        }

        return accept;
    }

    private static class CheckWordPdaPrivate {
        static ExitStep runningStep(List<PdaRunningInstance> instances, String word, PdaConstructor pc){
            ExitStep exit = new ExitStep();
            int e = pc.getMapper(Alphabet.getEmptyChar());
            int c = e;
            int stackTop = e;

            for(PdaRunningInstance ins : instances){
                if(ins.pos < word.length()) c = pc.getMapper(word.charAt(ins.pos));
                if(!ins.stack.empty()) stackTop = ins.stack.peek();

                for(PdtfTuple tuple : pc.transition.stepWithEmpty(ins.act, c, stackTop, e)){
                    PdaRunningInstance out = new PdaRunningInstance(tuple.getDestiny(), ins.stack, ins.pos);
                    if(tuple.getCharacter() != e) out.pos = out.pos + 1;
                    if(tuple.getPop() != e) out.stack.pop();
                    if(tuple.getPush() != e) out.stack.push(tuple.getPush());

                    if(out.stack.size() < word.length()+3) exit.list.add(out);
                    if(pc.finalStates.contains(out.act) && out.stack.empty() && out.pos == word.length()) exit.accept = true;
                }
            }
            return exit;
        }

        private static class PdaRunningInstance {
            public State act;
            public Stack<Integer> stack;
            public int pos;

            public PdaRunningInstance(State act, Stack<Integer> stack, int pos){
                this.act = act;
                this.stack = new Stack<>();
                this.stack.addAll(stack);
                this.pos = pos;
            }
        }

        private static class ExitStep {
            public List<PdaRunningInstance> list;
            public boolean accept;

            public ExitStep(){
                this.list = new ArrayList<>();
                this.accept = false;
            }

            public ExitStep(List<PdaRunningInstance> list, boolean accept){
                this.list = list;
                this.accept = accept;
            }
        }
    }

    //COUNTER EXAMPLE

    public static String findCounterExampleCfg(CfgNonEmpty a, CfgNonEmpty b, int length) {
        Pda parserA = a.toCfg().toPda();
        Pda parserB = b.toCfg().toPda();
        CfgNonEmpty chomskyA = a.toChomsky();
        CfgNonEmpty chomskyB = b.toChomsky();

        boolean found = false;
        int l = 1;
        String act = "";
        WordsGenerator wg;
        Iterator<String> it;

        while (!found && l <= length){
            wg = new WordsGenerator(buildCfgLengthFromTo(chomskyA, l, Math.min(l+4, length)));
            it = wg.generateAllWordsStart().iterator();
            while (it.hasNext() && !found){
                act = it.next();
                found = !parserB.checkWord(act);
            }

            if(!found){
                wg = new WordsGenerator(buildCfgLengthFromTo(chomskyB, l, Math.min(l+4,length)));
                it = wg.generateAllWordsStart().iterator();
                while(it.hasNext() && !found){
                    act = it.next();
                    found = !parserA.checkWord(act);
                }
            }

            l = l+5;
        }

        if(found) return act;
        return Printer.counterexampleNotFound();
    }

    public static List<String> findManyCounterExamplesCfg(CfgNonEmpty a, CfgNonEmpty b){
        Pda parserA = a.toCfg().toPda();
        Pda parserB = b.toCfg().toPda();
        CfgNonEmpty chomskyA = a.toChomsky();
        CfgNonEmpty chomskyB = b.toChomsky();

        Set<String> set = new HashSet<>();
        int length = 1;

        WordsGenerator wg;
        Iterator<String> it;
        String act;

        if(a.acceptsEmpty() != b.acceptsEmpty()) set.add("");

        while(set.size() <= 100 && length <= 21){
            wg = new WordsGenerator(buildCfgLengthFromTo(chomskyA, length, length+9));
            it = wg.generateAllWordsStart().iterator();
            while (it.hasNext() && set.size() <= 100){
                act = it.next();
                if(!parserB.checkWord(act)) set.add(act);
            }

            if(set.size() <= 100){
                wg = new WordsGenerator(buildCfgLengthFromTo(chomskyB, length, length+9));
                it = wg.generateAllWordsStart().iterator();
                while(it.hasNext() && set.size() <= 100){
                    act = it.next();
                    if(!parserA.checkWord(act)) set.add(act);
                }
            }

            length = length+10;
        }

        return set.stream().toList();
    }

    //BUILD CFG WORDS OF LENGTH

    public static CfgNonEmpty buildCfgLengthFromTo(CfgNonEmpty x, int lengthStart, int lengthFinish){
        if(lengthStart < 0 || lengthFinish < 0 || lengthStart >= lengthFinish) return new CfgNonEmpty();
        CfgNonEmptyConstructor ccx = x.getConstructor();
        Map<Gvar, Map<Integer, Gvar>> originalToNew = new HashMap<>();
        Map<Gvar, Pair<Gvar, Integer>> newToOriginal = new HashMap<>();

        for(Gvar v : new HashSet<>(ccx.variables)){
            for(int i=1; i<=lengthFinish; i++){
                Gvar newVar = ccx.generate(v);
                if(!originalToNew.containsKey(v)) originalToNew.put(v, new HashMap<>());
                originalToNew.get(v).put(i,newVar);
                newToOriginal.put(newVar, new Pair<>(v,i));
            }
        }

        CfgNonEmptyConstructor res = new CfgNonEmptyConstructor();
        res.terminals = ccx.terminals;
        res.acceptsEmptyWord = ccx.acceptsEmptyWord && lengthStart == 0;
        res.variables.addAll(newToOriginal.keySet());

        for(GruleNonEmpty r : ccx.rules){
            if(r.getRight().type() == TypesGramex.CHAR){
                res.rules.add(new GruleNonEmpty(originalToNew.get(r.getLeft()).get(1), r.getRight()));
            }
            else{
                Gvar left = r.getLeft();
                Gvar a = r.getRight().toGramexConcat().getA().toGramexVar().getV();
                Gvar b = r.getRight().toGramexConcat().getB().toGramexVar().getV();
                for(int i=2; i<=lengthFinish; i++){
                    Gvar nl = originalToNew.get(left).get(i);
                    for(int j=1; j<i; j++){
                        Gvar na = originalToNew.get(a).get(j);
                        Gvar nb = originalToNew.get(b).get(i-j);
                        res.rules.add(new GruleNonEmpty(nl, new GramexConcat(new GramexVar(na), new GramexVar(nb))));
                    }
                }
            }
        }

        Gvar newStart = res.generate(ccx.start);
        for(int i=lengthStart; i<=lengthFinish; i++) res.rules.add(new GruleNonEmpty(newStart, new GramexVar(originalToNew.get(ccx.start).get(i))));
        res.start = newStart;

        return res.getCfgNonEmpty().toCfg().simplify();
    }

}
