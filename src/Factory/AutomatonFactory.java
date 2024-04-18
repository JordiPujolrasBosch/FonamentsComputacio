package Factory;

import Automatons.*;
import AutomatonElements.*;
import RegularExpressions.*;

import java.util.*;

public class AutomatonFactory {

    //TRANSFORMATION BETWEEN TYPES

    /**
     * Generates a nfa from the data.
     * @param data Data of the automaton.
     * @return The nfa generated from the data.
     * @throws AutomatonReaderException If the data is incorrect.
     */
    public static Nfa dataToNfa(AutomatonData data) throws AutomatonReaderException {
        if(!data.check()){
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
            nc.transition.add(array.get(r.origin()), array.get(r.destiny()), r.character());
        }

        return nc.getNfa();
    }

    /**
     * Generates a dfa from the data.
     * @param data Data of the automaton.
     * @return The dfa generated from the data.
     * @throws AutomatonReaderException If the data is incorrect or the automaton is not deterministic.
     */
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
            dc.transition.add(array.get(r.origin()), array.get(r.destiny()), r.character());
        }

        if(!data.isComplete()){
            State sink = new State();
            dc.states.add(sink);

            for(State o : dc.states){
                for(Character c : dc.alphabet.set()){
                    if(!dc.transition.hasRule(o,c)) dc.transition.add(o,sink,c);
                }
            }
        }

        return dc.getDfa();
    }

    /**
     * Transforms a dfa to a nfa.
     * @param x Dfa to transform.
     * @return Nfa equivalent to x.
     */
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

    /**
     * Transforms a nfa to a dfa.
     * @param x Nfa to transform.
     * @return Dfa equivalent to x.
     */
    public static Dfa nfaToDfa(Nfa x){
        NfaConstructor ncx = x.getConstructor();
        Set<Set<State>> ps = powerset(ncx.states);

        Map<Set<State>, State> mapper = new HashMap<>();
        for(Set<State> ss : ps) mapper.put(ss, new State());

        DfaConstructor dc = new DfaConstructor();

        dc.states.addAll(mapper.values());

        dc.alphabet.addAll(ncx.alphabet);
        dc.alphabet.removeEmptyChar();

        dc.start = mapper.get(x.stateExtended(ncx.start));

        for(Set<State> ss : mapper.keySet()){
            if(!conjunctionIsEmpty(ss, ncx.finalStates)) dc.finalStates.add(mapper.get(ss));
        }

        for(Set<State> ss : mapper.keySet()){
            for(Character c : dc.alphabet.set()){
                dc.transition.add(mapper.get(ss), mapper.get(destinyOf(x,ss,c)), c);
            }
        }

        deleteUnusedStates(dc);
        return dc.getDfa();
    }

    /**
     * Transforms a nfa to a gnfa.
     * @param x Nfa to transform.
     * @return Gnfa equivalent to x.
     */
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
            if(r.character().equals(Alphabet.getEmptyChar())){
                gc.transition.addUnion(r.origin(), r.destiny(), new RegexEmptyChar());
            }
            else{
                gc.transition.addUnion(r.origin(), r.destiny(), new RegexChar(r.character()));
            }
        }

        return gc.toGnfa();
    }

    /**
     * Transforms a gnfa to a regular expression.
     * @param x Gnfa to transform.
     * @return Regular expression equivalent to x.
     */
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

    //DFA TRANSFORMATIONS

    /**
     * Generates the complement of a dfa. The complement accept the words
     * that the original don't.
     * @param x Dfa original.
     * @return Dfa complement of x.
     */
    public static Dfa complement(Dfa x){
        DfaConstructor dc = x.getConstructor();

        Set<State> old = new HashSet<>(dc.finalStates);
        dc.finalStates.clear();

        for(State s : dc.states){
            if(!old.contains(s)) dc.finalStates.add(s);
        }

        return dc.getDfa();
    }

    /**
     * Generates the reverse of a dfa. The reverse accept the words of the original in reverse order.
     * @param x Dfa original.
     * @return Nfa reverse of x.
     */
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
            nc.transition.add(r.destiny(), r.origin(), r.character());
        }
        for(State s : dcx.finalStates){
            nc.transition.add(newStart, s, Alphabet.getEmptyChar());
        }

        return nc.getNfa();
    }

    /**
     * Generates the dfa equivalent to the original with minimum number of states.
     * @param x Dfa original
     * @return Dfa minimum of x.
     */
    public static Dfa minimize(Dfa x){
        DfaConstructor dc = x.getConstructor();
        deleteUnusedStates(dc);
        return defineMinimal(dc, findPartition(dc)).getDfa();
    }

    //REGULAR EXPRESSIONS

    /**
     * Generates a Nfa that only accepts the character c.
     * @param c Character that the nfa accept.
     * @return Nfa that only accepts the character c.
     */
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

    /**
     * Generates a nfa that doesn't accept anything.
     * @return Nfa that doesn't accept anything.
     */
    public static Nfa regexVoidToNfa(){
        NfaConstructor nc = new NfaConstructor();
        State s = new State();

        nc.states.add(s);
        nc.alphabet.addEmptyChar();
        nc.start = s;
        return nc.getNfa();
    }

    /**
     * Generates a nfa that only accepts nothing.
     * @return Nfa that only accepts nothing.
     */
    public static Nfa regexEmptyCharToNfa() {
        NfaConstructor nc = new NfaConstructor();
        State s = new State();

        nc.states.add(s);
        nc.alphabet.addEmptyChar();
        nc.start = s;
        nc.finalStates.add(s);
        return nc.getNfa();
    }

    /**
     * Generates a nfa that concatenates a and b.
     * @param a First nfa.
     * @param b Second nfa.
     * @return Nfa that is a concatenation of a and b.
     */
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

    /**
     * Generates a nfa that accepts a and b.
     * @param a First nfa.
     * @param b Second nfa.
     * @return Nfa that is the union of a and b.
     */
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

    /**
     * Generates a nfa that accepts x n(>=0) times.
     * @param x Original Nfa.
     * @return Nfa that is the star of x.
     */
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

    //private methods

    private static Set<Set<State>> powerset(Set<State> ss){
        Set<Set<State>> list = new HashSet<>();
        list.add(new HashSet<>());
        List<State> toadd = new ArrayList<>(ss);

        return powersetIm(list, toadd);
    }

    private static Set<Set<State>> powersetIm(Set<Set<State>> list, List<State> toadd){
        if(toadd.isEmpty()) return list;

        List<Set<State>> added = new ArrayList<>();
        State act = toadd.removeFirst();

        for (Set<State> ss : list){
            Set<State> aux = new HashSet<>(ss);
            aux.add(act);
            added.add(aux);
        }

        list.addAll(added);
        return powersetIm(list, toadd);
    }

    private static boolean conjunctionIsEmpty(Set<State> a, Set<State> b) {
        boolean found = false;
        Iterator<State> it = b.iterator();
        while(it.hasNext() && !found){
            found = a.contains(it.next());
        }
        return !found;
    }

    private static Set<State> destinyOf(Nfa x, Set<State> ss, Character c){
        Set<State> res = new HashSet<>();
        for(State s : ss) res.addAll(x.stepWithEmptyChar(s,c));
        return res;
    }

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
                for(Character c : dc.alphabet.set()){
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

    private static RegularExpression regexReductionStep(RegularExpression a, RegularExpression b, RegularExpression c){
        return new RegexConcat(new RegexConcat(a,new RegexStar(b)),c);
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

        while(!consolidated){
            consolidated = true;

            for(Set<State> ss : partition){
                for(Set<State> tt : partition){
                    for(Character a : dc.alphabet.set()){
                        Set<State> temp = new HashSet<>();
                        for(State q : ss){
                            if(tt.contains(dc.transition.step(q,a))) temp.add(q);
                        }

                        if(!temp.isEmpty() && !temp.equals(ss)){
                            consolidated = false;
                            partition.remove(ss);
                            partition.add(temp);
                            Set<State> smt = new HashSet<>(ss);
                            for(State k : temp) smt.remove(k);
                            partition.add(smt);
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
            for(Character c : dc.alphabet.set()){
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
