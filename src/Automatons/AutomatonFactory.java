package Automatons;

import AutomatonElements.*;
import RegularExpresions.*;

import java.util.*;

public class AutomatonFactory {
    /*private static AutomatonFactory af = null;

    private AutomatonFactory(){}

    public static AutomatonFactory getInstance(){
        if(af == null) af = new AutomatonFactory();
        return af;
    }*/

    //TRANSFORMATIONS

    public static Dfa nfaToDfa(Nfa x){
        List<Set<State>> ps = powerset(x.getStates());

        Map<Set<State>, State> mapper = new HashMap<>();
        for(Set<State> ss : ps) mapper.put(ss, new State());

        DfaConstructor dc = new DfaConstructor();

        dc.states.addAll(mapper.values());

        dc.alphabet.addAll(x.getAlphabet());
        dc.alphabet.removeEmptyChar();

        dc.start = mapper.get(x.stateWithEmptyChar(x.getStart()));

        for(Set<State> ss : mapper.keySet()){
            if(!conjunctionIsEmpty(ss,x.getFinalStates())) dc.finalStates.add(mapper.get(ss));
        }

        for(Set<State> ss : mapper.keySet()){
            for(Character c : dc.alphabet.set()){
                dc.transition.add(mapper.get(ss), mapper.get(destinyOf(x,ss,c)), c);
            }
        }

        deleteUnsusedStates(dc);

        return dc.getDfa();
    }

    public static Nfa dfaToNfa(Dfa x){
        NfaConstructor nc = new NfaConstructor();

        nc.states.addAll(x.getStates());

        nc.start = x.getStart();

        nc.alphabet.addAll(x.getAlphabet());
        nc.alphabet.addEmptyChar();

        nc.finalStates.addAll(x.getFinalStates());

        for(State o : x.getStates()){
            for(Character c : x.getAlphabet().set()){
                nc.transition.add(o, x.getTransition().step(o,c), c);
            }
        }

        return nc.getNfa();
    }

    public static Dfa dataToDfa(AutomatonData data){
        if(!data.check()) return null;
        if(!data.isDeterministic()) return null;

        boolean toComplete = !data.isComplete();
        List<State> array = new ArrayList<>();
        for(int i = 0; i < data.getStates(); i++) array.add(new State());


        DfaConstructor dc = new DfaConstructor();
        dc.states.addAll(array);
        dc.start = array.get(data.getStart());
        for(int x : data.getFinalStates()) dc.finalStates.add(array.get(x));
        dc.alphabet = data.getAlphabet();
        for(Rule r : data.getTransitions()){
            dc.transition.add(array.get(r.origin()), array.get(r.destiny()), r.character());
        }

        if(toComplete){
            State sink = new State();
            dc.states.add(sink);

            for(State s : dc.states){
                for(Character c : dc.alphabet.set()){
                    if(!dc.transition.hasRule(s,c)) dc.transition.add(s,sink,c);
                }
            }
        }

        return dc.getDfa();
    }

    public static Nfa dataToNfa(AutomatonData data){
        if(!data.check()) return null;

        List<State> array = new ArrayList<>();
        for(int i = 0; i < data.getStates(); i++) array.add(new State());

        NfaConstructor nc = new NfaConstructor();
        nc.states.addAll(array);
        nc.start = array.get(data.getStart());
        for(int x : data.getFinalStates()) nc.finalStates.add(array.get(x));
        nc.alphabet = data.getAlphabet();
        for(Rule r : data.getTransitions()){
            nc.transition.add(array.get(r.origin()), array.get(r.destiny()), r.character());
        }

        return nc.getNfa();
    }

    public static Gnfa dfaToGnfa(Dfa x){
        GnfaConstructor gc = new GnfaConstructor();

        gc.states.addAll(x.getStates());

        gc.alphabet.addAll(x.getAlphabet());
        gc.alphabet.addEmptyChar();

        for(State o : x.getStates()){
            for(State d : x.getStates()){
                gc.transition.addReplace(o,d, new RegexVoid());
            }
        }

        for(State s : x.getFinalStates()){
            gc.transition.addReplace(s, gc.accept, new RegexEmptyChar());
        }

        gc.transition.addReplace(gc.start, x.getStart(), new RegexEmptyChar());

        for(State o : x.getStates()){
            for(Character c : x.getAlphabet().set()){
                State d = x.getTransition().step(o,c);
                gc.transition.addUnion(o,d, new RegexChar(c));
            }
        }

        return gc.toGnfa();
    }

    public static RegularExpresion gnfaToRegex(Gnfa x){
        Gnfa y = gnfaCopy(x);
        while(!y.getStates().isEmpty()){
            //ACT
            State act = y.getStates().iterator().next();

            //ORIGIN
            Set<State> so = new HashSet<>(y.getStates());
            so.add(y.getStart());
            so.remove(act);

            //DESTINY
            Set<State> sd = new HashSet<>(y.getStates());
            sd.add(y.getAccept());
            sd.remove(act);

            //REPLACE rules
            for(State o : so){
                for(State d : sd){
                    RegularExpresion a = y.getTransition().step(o,act);
                    RegularExpresion b = y.getTransition().step(act,act);
                    RegularExpresion c = y.getTransition().step(act, d);

                    y.getTransition().addUnion(o,d,regexReductionStep(a,b,c));
                }
            }

            //DELETE state
            y.getStates().remove(act);
            y.getTransition().removeState(act);
        }

        return y.getTransition().step(y.getStart(),y.getAccept());
    }

    // DFA COMPLEMENT and REVERSE

    public static Dfa complement(Dfa x){
        DfaConstructor dc = new DfaConstructor();

        dc.states.addAll(x.getStates());

        dc.alphabet.addAll(x.getAlphabet());

        dc.start = x.getStart();

        dc.transition.addAll(x.getTransition());

        for(State s : x.getStates()){
            if(!x.getFinalStates().contains(s)) dc.finalStates.add(s);
        }

        return dc.getDfa();
    }

    public static Dfa reverse(Dfa x){
        NfaConstructor nc = new NfaConstructor();
        State newStart = new State();

        nc.states.addAll(x.getStates());
        nc.states.add(newStart);

        nc.alphabet.addAll(x.getAlphabet());
        nc.alphabet.addEmptyChar();

        nc.start = newStart;

        nc.finalStates.add(x.getStart());

        for(State o : x.getStates()){
            for(Character c : x.getAlphabet().set()){
                State d = x.getTransition().step(o,c);
                nc.transition.add(d,o,c);
            }
        }
        for(State s : x.getFinalStates()){
            nc.transition.add(newStart, s, Alphabet.getEmptyChar());
        }

        return nfaToDfa(nc.getNfa());
    }

    //REGULAR EXPRESIONS

    public static Nfa regexCharToNfa(char c){
        NfaConstructor nc = new NfaConstructor();
        State start = new State();
        State finish = new State();

        nc.states.add(start);
        nc.states.add(finish);

        nc.finalStates.add(finish);

        nc.alphabet.addChar(c);
        nc.alphabet.addEmptyChar();

        nc.start = start;

        nc.transition.add(start, finish, c);

        return nc.getNfa();
    }

    public static Nfa regexVoidToNfa(){
        NfaConstructor nc = new NfaConstructor();
        State s = new State();

        nc.start = s;

        nc.states.add(s);

        nc.alphabet.addEmptyChar();

        return nc.getNfa();
    }

    public static Nfa regexEmptyCharToNfa() {
        NfaConstructor nc = new NfaConstructor();
        State s = new State();

        nc.states.add(s);

        nc.start = s;

        nc.alphabet.addEmptyChar();

        nc.finalStates.add(s);

        return nc.getNfa();
    }

    public static Nfa concatenation(Nfa a, Nfa b) {
        NfaConstructor nc = new NfaConstructor();

        nc.states.addAll(a.getStates());
        nc.states.addAll(b.getStates());

        nc.finalStates.addAll(b.getFinalStates());

        nc.alphabet.addAll(a.getAlphabet());
        nc.alphabet.addAll(b.getAlphabet());

        nc.start = a.getStart();

        nc.transition.addAll(a.getTransition());
        nc.transition.addAll(b.getTransition());
        for(State s : a.getFinalStates()) nc.transition.add(s, b.getStart(), Alphabet.getEmptyChar());

        return nc.getNfa();
    }

    public static Nfa union(Nfa a, Nfa b){
        NfaConstructor nc = new NfaConstructor();
        State s = new State();

        nc.states.addAll(a.getStates());
        nc.states.addAll(b.getStates());
        nc.states.add(s);

        nc.alphabet.addAll(a.getAlphabet());
        nc.alphabet.addAll(b.getAlphabet());

        nc.start = s;

        nc.finalStates.addAll(a.getFinalStates());
        nc.finalStates.addAll(b.getFinalStates());

        nc.transition.addAll(a.getTransition());
        nc.transition.addAll(b.getTransition());
        nc.transition.add(s, a.getStart(), Alphabet.getEmptyChar());
        nc.transition.add(s, b.getStart(), Alphabet.getEmptyChar());

        return nc.getNfa();
    }

    public static Nfa star(Nfa x){
        NfaConstructor nc = new NfaConstructor();
        State s = new State();

        nc.states.addAll(x.getStates());
        nc.states.add(s);

        nc.start = s;

        nc.alphabet.addAll(x.getAlphabet());

        nc.finalStates.addAll(x.getFinalStates());
        nc.finalStates.add(s);

        nc.transition.addAll(x.getTransition());
        nc.transition.add(s, x.getStart(), Alphabet.getEmptyChar());
        for(State a : x.getFinalStates()) nc.transition.add(a, x.getStart(), Alphabet.getEmptyChar());

        return nc.getNfa();
    }

    //NFA to DFA private methods

    private static List<Set<State>> powerset(Set<State> ss){
        List<Set<State>> list = new ArrayList<>();
        list.add(new HashSet<>());
        List<State> toadd = new ArrayList<>(ss);

        return powersetIm(list, toadd);
    }

    private static List<Set<State>> powersetIm(List<Set<State>> list, List<State> toadd){
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

    private static void deleteUnsusedStates(DfaConstructor dc){
        boolean changed = true;
        while(changed){
            changed = false;
            for(State s : dc.states){
                if(s != dc.start && dc.transition.isUnused(s)){
                    dc.states.remove(s);
                    dc.finalStates.remove(s);
                    dc.transition.remove(s);
                    changed = true;
                }
            }
        }
    }

    //GNFA to REGEX private methods

    private static Gnfa gnfaCopy(Gnfa x){
        GnfaConstructor gc = new GnfaConstructor();

        gc.alphabet.addAll(x.getAlphabet());
        gc.states.addAll(x.getStates());
        gc.transition.addAll(x.getTransition());
        gc.start = x.getStart();
        gc.accept = x.getAccept();

        return gc.toGnfa();
    }

    private static RegularExpresion regexReductionStep(RegularExpresion a, RegularExpresion b, RegularExpresion c){
        return new RegexConcat(new RegexConcat(a,new RegexStar(b)),c);
    }

    //Private automatons constructors

    private static class NfaConstructor {
        public Set<State> states;
        public Set<State> finalStates;
        public Alphabet alphabet;
        public State start;
        public Ntf transition;

        public NfaConstructor(){
            states = new HashSet<>();
            finalStates = new HashSet<>();
            alphabet = new Alphabet();
            start = null;
            transition = new Ntf();
        }

        public Nfa getNfa(){
            return new Nfa(states, alphabet, start, finalStates, transition);
        }
    }

    private static class DfaConstructor {
        public Set<State> states;
        public Set<State> finalStates;
        public Alphabet alphabet;
        public State start;
        public Dtf transition;

        public DfaConstructor(){
            states = new HashSet<>();
            finalStates = new HashSet<>();
            alphabet = new Alphabet();
            start = null;
            transition = new Dtf();
        }

        public Dfa getDfa(){
            return new Dfa(states, alphabet, start, finalStates, transition);
        }
    }

    private static class GnfaConstructor {
        public Set<State> states;
        public Alphabet alphabet;
        public State start;
        public State accept;
        public Gntf transition;

        public GnfaConstructor(){
            states = new HashSet<>();
            alphabet = new Alphabet();
            start = new State();
            accept = new State();
            transition = new Gntf();
        }

        public Gnfa toGnfa(){
            return new Gnfa(states, alphabet, start, accept, transition);
        }
    }
}
