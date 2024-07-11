package Factory;

import Automatons.*;
import Elements.*;
import Elements.Grammars.*;
import Elements.Transitions.*;
import Exceptions.*;
import Factory.Builders.*;
import Factory.Constructors.*;
import Grammars.*;
import RegularExpressions.*;

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
        private static void deleteUnusedStates(DfaConstructor dc){
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

        private static Set<Set<State>> findPartition(DfaConstructor dc){
            Set<Set<State>> partition = new HashSet<>();
            Set<State> nonFinal = new HashSet<>();
            for(State s : dc.states){
                if(!dc.finalStates.contains(s)) nonFinal.add(s);
            }
            partition.add(nonFinal);
            partition.add(dc.finalStates);

            boolean consolidated = false;
            Iterator<Set<State>> ita, itb = null;
            Iterator<Character> itc = null;
            Set<State> sa, sb, right, left = null;
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

            return partition;
        }

        private static Set<State> findWhichSetContains(Set<Set<State>> x, State s){
            boolean found = false;
            Set<State> act = null;
            Iterator<Set<State>> it = x.iterator();
            while(it.hasNext() && !found){
                act = it.next();
                found = act.contains(s);
            }
            return act;
        }

        private static DfaConstructor defineMinimal(DfaConstructor dc, Set<Set<State>> partition){
            Map<Set<State>, State> mapper = new HashMap<>();
            for(Set<State> ss : partition) mapper.put(ss, new State());

            DfaConstructor res = new DfaConstructor();

            res.states.addAll(mapper.values());

            res.alphabet.addAll(dc.alphabet);

            res.start = mapper.get(findWhichSetContains(partition, dc.start));

            for(Set<State> ss : partition){
                if(!conjunctionIsEmpty(ss,dc.finalStates)){
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

        private static boolean conjunctionIsEmpty(Set<State> a, Set<State> b) {
            boolean found = false;
            Iterator<State> it = b.iterator();
            while(it.hasNext() && !found){
                found = a.contains(it.next());
            }
            return !found;
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
        private static void deleteUnusedStates(DfaConstructor dc){
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

        private static Map<State,Map<State,Boolean>> startMatrix(DfaConstructor dc, Map<Integer,State> reverse){
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

        private static void iterateMatrix(DfaConstructor dc, Map<Integer,State> reverse, Map<State,Integer> mapper, Map<State,Map<State,Boolean>> matrix){
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

        private static Set<Set<State>> buildPartition(DfaConstructor dc, Map<State,Integer> mapper, Map<State,Map<State,Boolean>> matrix){
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

        private static DfaConstructor defineMinimal(DfaConstructor dc, Set<Set<State>> partition){
            Map<Set<State>, State> mapper = new HashMap<>();
            for(Set<State> ss : partition) mapper.put(ss, new State());

            DfaConstructor res = new DfaConstructor();

            res.states.addAll(mapper.values());

            res.alphabet.addAll(dc.alphabet);

            res.start = mapper.get(findWhichSetContains(partition, dc.start));

            for(Set<State> ss : partition){
                if(!conjunctionIsEmpty(ss,dc.finalStates)){
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

        private static boolean conjunctionIsEmpty(Set<State> a, Set<State> b) {
            boolean found = false;
            Iterator<State> it = b.iterator();
            while(it.hasNext() && !found){
                found = a.contains(it.next());
            }
            return !found;
        }

        private static Set<State> findWhichSetContains(Set<Set<State>> x, State s){
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
        private static DfaConstructor defineDetermine(Nfa x, Set<Set<State>> dsss){
            NfaConstructor nc = x.getConstructor();
            DfaConstructor dc = new DfaConstructor();

            Map<Set<State>, State> mapper = new HashMap<>();
            for(Set<State> ss : dsss) mapper.put(ss, new State());

            dc.states.addAll(mapper.values());

            dc.alphabet.addAll(nc.alphabet);
            dc.alphabet.removeEmptyChar();

            dc.start = mapper.get(nc.transition.stateExtended(nc.start));

            for(Set<State> ss : dsss){
                if(!conjunctionIsEmpty(ss, nc.finalStates)) dc.finalStates.add(mapper.get(ss));
            }

            for(Set<State> ss : dsss){
                for(char c : dc.alphabet.getSet()){
                    dc.transition.add(mapper.get(ss), mapper.get(destinyOf(nc,ss,c)), c);
                }
            }

            return dc;
        }

        private static Set<State> destinyOf(NfaConstructor nc, Set<State> ss, char c){
            Set<State> res = new HashSet<>();
            for(State o : ss){
                for(State d : nc.transition.step(o,c)){
                    res.addAll(nc.transition.stateExtended(d));
                }
            }
            return res;
        }

        private static Set<Set<State>> findDeterminingSet(Nfa x){
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

        private static boolean conjunctionIsEmpty(Set<State> a, Set<State> b) {
            boolean found = false;
            Iterator<State> it = b.iterator();
            while(it.hasNext() && !found){
                found = a.contains(it.next());
            }
            return !found;
        }

    }

    // TRANSFORMATIONS BETWEEN AUTOMATONS

    public static Nfa dataToNfa(AutomatonData data) throws AutomatonReaderException {
        if(!data.check()){
            throw new AutomatonReaderException(Printer.automatonCheck(data.getFilename()));
        }
        if(!data.getAlphabet().containsEmptyChar()){
            throw new AutomatonReaderException(Printer.automatonCheck(data.getFilename()));
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
            throw new AutomatonReaderException(Printer.automatonCheck(data.getFilename()));
        }
        if(!data.isDeterministic()) {
            throw new AutomatonReaderException(Printer.automatonNondeterministic(data.getFilename()));
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
        private static void buildRules(PdaConstructor pc, CfgConstructor ccx, State loop){
            int e = pc.getMapper(Alphabet.getEmptyChar());
            for(Grule r : ccx.rules){
                if(r.getRight().length() == 0) {
                    pc.transition.add(loop, e, pc.getMapper(r.getLeft()), loop, e);
                }
                else if(r.getRight().length() == 1){
                    int k = 0;
                    if(r.getRight().type() == TypesGramex.VAR) k = pc.getMapper(r.getRight().toGramexVar().getV());
                    else k = pc.getMapper(r.getRight().toGramexChar().getC());
                    pc.transition.add(loop, e, pc.getMapper(r.getLeft()), loop, k);
                }
                else{
                    List<Integer> list = makeList(r.getRight(), pc);

                    State pre = null;
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

        private static List<Integer> makeList(Gramex right, PdaConstructor pc) {
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
            Map<Gvar,Set<Gramex>> mapper = GrammarTools.getMapperRules(ccx.getCfg());

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
                if(isOnlyAnulable(ccx, v, anulable)) onlyAnulable.add(v);
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

        static boolean isOnlyAnulable(CfgConstructor ccx, Gvar v, Set<Gvar> anulable){
            boolean foundNotAnulable = false;
            Iterator<Grule> it = GrammarTools.getRulesVar(ccx.getCfg(), v).iterator();
            while(it.hasNext() && !foundNotAnulable){
                Grule r = it.next();
                if(r.getRight().type() == TypesGramex.CHAR) foundNotAnulable = true;
                else if(r.getRight().type() == TypesGramex.VAR) foundNotAnulable = !isOnlyAnulable(ccx, r.getRight().toGramexVar().getV(), anulable);
                else if(r.getRight().type() == TypesGramex.CONCAT){
                    Iterator<GramexNonEmpty> itc = r.getRight().toGramexConcat().toList().iterator();
                    while(itc.hasNext() && !foundNotAnulable){
                        GramexNonEmpty gne = itc.next();
                        if(gne.type() == TypesGramex.CHAR) foundNotAnulable = true;
                        else if(!anulable.contains(gne.toGramexVar().getV())) foundNotAnulable = true;
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
                        GramexChar gc = GrammarTools.getLeftMostChar(aux);
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

    public static CfgNonEmpty griebach(CfgNonEmpty x) {
        CfgNonEmptyConstructor ccx = x.getConstructor();

        List<Gvar> order = GriebachPrivate.findOrder(ccx);
        GriebachPrivate.verifyOrder(ccx,order);
        GriebachPrivate.applyGriebachSubstitution(ccx, order);

        return ccx.getCfgNonEmpty();
    }

    private static class GriebachPrivate {
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
            int i=order.size()-1;
            while(i>=0){
                if(haveLeftRecursivity(GrammarTools.getRulesVar(ccx.getCfgNonEmpty(), order.get(i)))) {
                    Gvar aux = removeLeftRecursivity(ccx, order.get(i));
                    order.add(i+1, aux);
                    i++;
                }
                else if(!orderOfRulesIsCorrect(order, GrammarTools.getRulesVar(ccx.getCfgNonEmpty(), order.get(i)))){
                    applySubstitutionIncorrectRule(ccx, order.get(i), order);
                }
                else{
                    i--;
                }
            }
        }

        static void applyGriebachSubstitution(CfgNonEmptyConstructor ccx, List<Gvar> order){
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
        private static ExitStep runningStep(List<PdaRunningInstance> instances, String word, PdaConstructor pc){
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

                    if(shortWordInStack(out.stack, word, pc)) exit.list.add(out);
                    if(pc.finalStates.contains(out.act) && out.stack.empty() && out.pos == word.length()) exit.accept = true;
                }
            }
            return exit;
        }

        private static boolean shortWordInStack(Stack<Integer> stack, String word, PdaConstructor pc){
            int charsCounter = 0;
            for(int i : stack){
                if(pc.mapperChar.containsValue(i)) charsCounter++;
            }
            return charsCounter < word.length()+1;
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

}
