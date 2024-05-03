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
            throw new AutomatonReaderException(OutputMessages.automatonCheck(data.getFilename()));
        }
        if(!data.getAlphabet().containsEmptyChar()){
            throw new AutomatonReaderException(OutputMessages.automatonCheck(data.getFilename()));
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
            throw new AutomatonReaderException(OutputMessages.automatonCheck(data.getFilename()));
        }
        if(!data.isDeterministic()) {
            throw new AutomatonReaderException(OutputMessages.automatonNondeterministic(data.getFilename()));
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
        gc.transition.addReplace(gc.start, ncx.start, new RegexEmptyChar());
        for(State s : ncx.finalStates){
            gc.transition.addReplace(s, gc.accept, new RegexEmptyChar());
        }
        for(Rule r : ncx.transition.getRules()){
            if(r.getCharacter() == Alphabet.getEmptyChar()){
                gc.transition.addUnion(r.getOrigin(), r.getDestiny(), new RegexEmptyChar());
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
        for(CfgVariable v : ccx.variables) pc.addMapper(v);
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
            for(CfgRule r : ccx.rules){
                if(r.getRight().length() == 0) {
                    pc.transition.add(loop, e, pc.getMapper(r.getLeft()), loop, e);
                }
                else if(r.getRight().length() == 1){
                    int k = 0;
                    if(r.getRight().type() == TypesRight.VAR) k = pc.getMapper(r.getRight().toRightVar().v());
                    else k = pc.getMapper(r.getRight().toRightChar().c());
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

        private static List<Integer> makeList(Right right, PdaConstructor pc) {
            List<Integer> list = new ArrayList<>();
            if(right.type() == TypesRight.VAR){
                list.add(pc.getMapper(right.toRightVar().v()));
            }
            else if(right.type() == TypesRight.CHAR){
                list.add(pc.getMapper(right.toRightChar().c()));
            }
            else{
                RightNonEmpty a = right.toRightConcat().a();
                RightNonEmpty b = right.toRightConcat().b();
                list.addAll(makeList(a, pc));
                list.addAll(makeList(b, pc));
            }
            return list;
        }
    }

    // TRANSFORMATIONS CFG

    public static Cfg chomsky(Cfg x){
        CfgConstructor ccx = x.getConstructor();

        ChomskyPrivate.addNewStart(ccx);
        ChomskyPrivate.eliminateEmptyRules(ccx);
        ChomskyPrivate.removeUnusedVars(ccx);
        ChomskyPrivate.removeUnitRules(ccx);
        ChomskyPrivate.makeRulesBinary(ccx);
        ChomskyPrivate.replaceBinaryTerminals(ccx);

        return ccx.getCfg();
    }

    private static class ChomskyPrivate {
        private static void addNewStart(CfgConstructor ccx) {
            CfgVariable newStart = ccx.generate(ccx.start);
            ccx.rules.add(new CfgRule(newStart, new RightVar(ccx.start)));
            ccx.start = newStart;
        }

        private static void eliminateEmptyRules(CfgConstructor ccx) {
            Set<CfgRule> emptyRules = new HashSet<>();
            for(CfgRule r : ccx.rules){
                if(r.getRight().type() == TypesRight.EMPTY) emptyRules.add(r);
            }

            Set<CfgRule> removed = new HashSet<>();
            while(!emptyRules.isEmpty()){
                if(emptyRules.size() == 1 && emptyRules.iterator().next().getLeft().equals(ccx.start)){
                    emptyRules.clear();
                }
                else{
                    //Get a rule
                    Iterator<CfgRule> it = emptyRules.iterator();
                    CfgRule act = it.next();
                    if(act.getLeft().equals(ccx.start)) act = it.next();

                    //Remove act
                    ccx.rules.remove(act);
                    removed.add(act);

                    //Add rules with occurrences of act.left
                    Set<CfgRule> toadd = new HashSet<>();
                    for(CfgRule r : ccx.rules) toadd = rulesToAddStepTwo(r, act.getLeft(), removed);
                    ccx.rules.addAll(toadd);
                    for(CfgRule r : toadd){
                        if(r.getRight().type() == TypesRight.EMPTY) emptyRules.add(r);
                    }

                    //Rule removed
                    emptyRules.remove(act);
                }
            }
        }

        private static void removeUnusedVars(CfgConstructor ccx){
            Set<CfgVariable> set = new HashSet<>(ccx.variables);
            for(CfgRule r : ccx.rules) set.remove(r.getLeft());

            ccx.variables.removeAll(set);

            Set<CfgRule> rules = new HashSet<>();
            for(CfgRule r : ccx.rules){
                for(CfgVariable v : set){
                    if(r.getRight().containsVar(v)) rules.add(r);
                }
            }

            ccx.rules.removeAll(rules);
        }

        private static void removeUnitRules(CfgConstructor ccx) {
            Set<CfgRule> unitRules = new HashSet<>();
            for(CfgRule r : ccx.rules){
                if(r.getRight().type() == TypesRight.VAR) unitRules.add(r);
            }

            Set<CfgRule> removed = new HashSet<>();
            while(!unitRules.isEmpty()){
                CfgRule rule = unitRules.iterator().next();
                CfgVariable ri = rule.getRight().toRightVar().v();

                if(rule.getLeft().equals(ri)){
                    ccx.rules.remove(rule);
                    removed.add(rule);
                    unitRules.remove(rule);
                }
                else{
                    Set<CfgRule> toadd = new HashSet<>();
                    for(CfgRule r : ccx.rules){
                        if(r.getLeft().equals(ri)){
                            CfgRule aux = new CfgRule(rule.getLeft(), r.getRight());
                            if(aux.getRight().type() != TypesRight.VAR) toadd.add(aux);
                            else if(!removed.contains(aux)){
                                toadd.add(aux);
                                unitRules.add(aux);
                            }
                        }
                    }

                    ccx.rules.addAll(toadd);
                    ccx.rules.remove(rule);
                    removed.add(rule);
                    unitRules.remove(rule);
                }
            }
        }

        private static void makeRulesBinary(CfgConstructor ccx) {
            boolean foundNonBinaryRule = true;
            while(foundNonBinaryRule){
                foundNonBinaryRule = false;
                CfgRule nonBinaryRule = null;

                Iterator<CfgRule> it = ccx.rules.iterator();
                while(it.hasNext() && !foundNonBinaryRule){
                    nonBinaryRule = it.next();
                    foundNonBinaryRule = nonBinaryRule.getRight().length() > 2;
                }

                if(foundNonBinaryRule){
                    RightConcat pair = nonBinaryRule.getRight().toRightConcat().getPair();
                    CfgRule newRule = new CfgRule(ccx.generate(new CfgVariable('Z',0)), pair);

                    Set<CfgRule> toremove = new HashSet<>();
                    Set<CfgRule> toadd = new HashSet<>();
                    for(CfgRule r : ccx.rules){
                        if(r.getRight().containsPair(pair)){
                            RightNonEmpty substituted = r.getRight().toRightConcat().getChanged(pair, newRule.getLeft());
                            toremove.add(r);
                            toadd.add(new CfgRule(r.getLeft(), substituted));
                        }
                    }
                    ccx.rules.addAll(toadd);
                    ccx.rules.removeAll(toremove);
                    ccx.rules.add(newRule);
                }
            }
        }

        private static void replaceBinaryTerminals(CfgConstructor ccx) {
            boolean foundBinaryTerminalRules = true;
            while(foundBinaryTerminalRules){
                foundBinaryTerminalRules = false;
                CfgRule binaryTerminalRule = null;

                Iterator<CfgRule> it = ccx.rules.iterator();
                while(it.hasNext() && !foundBinaryTerminalRules){
                    binaryTerminalRule = it.next();
                    foundBinaryTerminalRules = binaryTerminalRule.getRight().length() == 2 && binaryTerminalRule.getRight().containsTerminal();
                }

                if(foundBinaryTerminalRules){
                    RightConcat rc = binaryTerminalRule.getRight().toRightConcat();
                    char c;
                    if(rc.a().type() == TypesRight.CHAR) c = rc.a().toRightChar().c();
                    else c = rc.b().toRightChar().c();

                    CfgRule newRule = new CfgRule(ccx.generate(new CfgVariable('Z',0)), new RightChar(c));

                    Set<CfgRule> toremove = new HashSet<>();
                    Set<CfgRule> toadd = new HashSet<>();
                    for(CfgRule r : ccx.rules){
                        if(r.getRight().length() == 2 && r.getRight().containsTerminal(c)){
                            Right substituted = r.getRight().getChanged(c, newRule.getLeft());
                            toremove.add(r);
                            toadd.add(new CfgRule(r.getLeft(), substituted));
                        }
                    }
                    ccx.rules.addAll(toadd);
                    ccx.rules.removeAll(toremove);
                    ccx.rules.add(newRule);
                }
            }
        }

        private static Set<CfgRule> rulesToAddStepTwo(CfgRule rule, CfgVariable toremove, Set<CfgRule> removed){
            if(!rule.getRight().containsVar(toremove)) return new HashSet<>();

            Set<CfgRule> set = new HashSet<>();
            if(rule.getRight().type() == TypesRight.VAR){
                CfgRule aux = new CfgRule(rule.getLeft(), new RightEmpty());
                if(!removed.contains(aux)) set.add(aux);
            }
            else{
                Set<Right> setright = stepTwoIm(rule.getRight().toRightConcat(), toremove);
                for(Right r : setright){
                    CfgRule aux = new CfgRule(rule.getLeft(), r);
                    if(r.type() == TypesRight.EMPTY && !removed.contains(aux)) set.add(aux);
                    else set.add(aux);
                }
            }
            return set;
        }

        private static Set<Right> stepTwoIm(RightConcat right, CfgVariable toremove){
            Set<Right> set = new HashSet<>();

            TypesRight var = TypesRight.VAR;
            TypesRight con = TypesRight.CONCAT;
            TypesRight ta = right.a().type();
            TypesRight tb = right.b().type();
            boolean ina = right.a().containsVar(toremove);
            boolean inb = right.b().containsVar(toremove);

            if(ina && inb){
                if(ta == con && tb == con){
                    set.addAll(stepTwoConAB(right, toremove));
                }
                else if(ta == var && tb == con){
                    set.addAll(stepTwoIm(right.b().toRightConcat(), toremove));
                }
                else if(ta == con && tb == var){
                    set.addAll(stepTwoIm(right.a().toRightConcat(), toremove));
                }
                else if(ta == var && tb == var){
                    set.add(new RightEmpty());
                }
            }

            if(ta == con && ina) set.addAll(stepTwoConA(right, toremove));
            if(tb == con && inb) set.addAll(stepTwoConB(right, toremove));
            if(ta == var && ina) set.add(right.b());
            if(tb == var && inb) set.add(right.a());

            return set;
        }

        private static Set<Right> stepTwoConA(RightConcat right, CfgVariable toremove){
            Set<Right> set = new HashSet<>();
            for(Right r : stepTwoIm(right.a().toRightConcat(), toremove)){
                if(r.type() == TypesRight.EMPTY) set.add(right.b());
                else set.add(new RightConcat(r.toRightNonEmpty(), right.b()));
            }
            return set;
        }

        private static Set<Right> stepTwoConB(RightConcat right, CfgVariable toremove){
            Set<Right> set = new HashSet<>();
            for(Right r : stepTwoIm(right.b().toRightConcat(), toremove)){
                if(r.type() == TypesRight.EMPTY) set.add(right.a());
                else set.add(new RightConcat(right.a(), r.toRightNonEmpty()));
            }
            return set;
        }

        private static Set<Right> stepTwoConAB(RightConcat right, CfgVariable toremove){
            Set<Right> set = new HashSet<>();
            for(Right ra : stepTwoIm(right.a().toRightConcat(), toremove)){
                for(Right rb : stepTwoIm(right.b().toRightConcat(), toremove)){
                    if(ra.type() == TypesRight.EMPTY && rb.type() == TypesRight.EMPTY) set.add(ra);
                    else if(ra.type() == TypesRight.EMPTY) set.add(rb);
                    else if(rb.type() == TypesRight.EMPTY) set.add(ra);
                    else set.add(new RightConcat(ra.toRightNonEmpty(), rb.toRightNonEmpty()));
                }
            }
            return set;
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
