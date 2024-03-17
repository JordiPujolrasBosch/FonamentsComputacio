package Automatons;

import AutomatonElements.*;

import java.util.*;

public class NondeterministicFiniteAutomaton {
    private final Set<State> states;
    private final Alphabet alphabet;
    private final State start;
    private final Set<State> finalStates;
    private final NondeterministicTransitionFunction transition;

    public NondeterministicFiniteAutomaton(
            Set<State> states,
            Alphabet alphabet,
            State start,
            Set<State> finalStates,
            NondeterministicTransitionFunction transition){
        this.states = states;
        this.alphabet = alphabet;
        this.transition = transition;
        this.start = start;
        this.finalStates = finalStates;
    }

    public DeterministicFiniteAutomaton toDfa(){
        //Extended states
        List<Set<State>> ext = extended(states);

        //Mapping states
        Map<Set<State>,State> mapper = new HashMap<>();
        for(Set<State> ss : ext) mapper.put(ss, new State());

        //DFA states
        Set<State> dfaStates = new HashSet<>(mapper.values());

        //DFA alphabet
        Alphabet dfaAlphabet = new Alphabet();
        dfaAlphabet.addAll(alphabet);
        dfaAlphabet.removeEmpty();

        //DFA start
        State dfaStart = mapper.get(setWithEmpty(start));

        //DFA final
        Set<State> dfaFinalStates = new HashSet<>();
        for (Set<State> ss : mapper.keySet()){
            if (containsFinalState(ss)){
                dfaFinalStates.add(mapper.get(ss));
            }
        }

        //DFA tf
        DeterministicTransitionFunction dfaTf = new DeterministicTransitionFunction();
        for (Set<State> ss : mapper.keySet()){
            for (Character c : dfaAlphabet.getSet()){
                dfaTf.add(mapper.get(ss), mapper.get(destinyOf(ss,c)), c);
            }
        }

        //delete unused states

        //DFA
        return new DeterministicFiniteAutomaton(dfaStates, dfaAlphabet, dfaStart, dfaFinalStates, dfaTf);
    }

    private Set<State> destinyOf(Set<State> set, Character c) {
        Set<State> destiny = new HashSet<>();

        for(State s : set){
            Set<State> aux = transition.step(s,c);
            for(State k : aux) destiny.addAll(setWithEmpty(k));
        }

        return destiny;
    }

    private boolean containsFinalState(Set<State> ss) {
        boolean found = false;
        Iterator<State> it = ss.iterator();
        while(it.hasNext() && !found){
            found = finalStates.contains(it.next());
        }
        return found;
    }

    private Set<State> setWithEmpty(State x) {
        Set<State> before = new HashSet<>();
        Set<State> after = new HashSet<>();
        after.add(x);

        while(before.size() != after.size()){
            before = after;
            after = new HashSet<>(before);
            for(State s : before){
                after.addAll(transition.step(s,Character.MIN_VALUE));
            }
        }

        return after;
    }

    private List<Set<State>> extended(Set<State> ss){
        List<Set<State>> list = new ArrayList<>();
        list.add(new HashSet<>());
        List<State> toadd = new ArrayList<>(ss);
        return extend(list,toadd);
    }

    private List<Set<State>> extend(List<Set<State>> list, List<State> toadd){
        if(toadd.isEmpty()) return list;

        List<Set<State>> added = new ArrayList<>();
        State act = toadd.removeFirst();

        for (Set<State> ss : list){
            Set<State> aux = new HashSet<>(ss);
            aux.add(act);
            added.add(aux);
        }

        list.addAll(added);
        return extend(list, toadd);
    }

    public static NondeterministicFiniteAutomaton union(NondeterministicFiniteAutomaton nfa1, NondeterministicFiniteAutomaton nfa2){
        State aux = new State();

        Set<State> newStates = new HashSet<>();
        newStates.addAll(nfa1.states);
        newStates.addAll(nfa2.states);
        newStates.add(aux);

        Alphabet newAlphabet = new Alphabet();
        newAlphabet.addAll(nfa1.alphabet);
        newAlphabet.addAll(nfa2.alphabet);

        Set<State> newFinalStates = new HashSet<>();
        newFinalStates.addAll(nfa1.finalStates);
        newFinalStates.addAll(nfa2.finalStates);

        NondeterministicTransitionFunction newTf = new NondeterministicTransitionFunction();
        newTf.addAll(nfa1.transition);
        newTf.addAll(nfa2.transition);
        newTf.add(aux, nfa1.start, Character.MIN_VALUE);
        newTf.add(aux, nfa2.start, Character.MIN_VALUE);

        return new NondeterministicFiniteAutomaton(newStates, newAlphabet, aux, newFinalStates, newTf);
    }

    public static NondeterministicFiniteAutomaton concatenation(NondeterministicFiniteAutomaton nfa1, NondeterministicFiniteAutomaton nfa2) {
        Set<State> newStates = new HashSet<>();
        newStates.addAll(nfa1.states);
        newStates.addAll(nfa2.states);

        Alphabet newAlphabet = new Alphabet();
        newAlphabet.addAll(nfa1.alphabet);
        newAlphabet.addAll(nfa2.alphabet);

        Set<State> newFinalStates = new HashSet<>(nfa2.finalStates);

        NondeterministicTransitionFunction newTf = new NondeterministicTransitionFunction();
        newTf.addAll(nfa1.transition);
        newTf.addAll(nfa2.transition);
        for(State s : nfa1.finalStates) newTf.add(s, nfa2.start, Character.MIN_VALUE);

        return new NondeterministicFiniteAutomaton(newStates, newAlphabet, nfa1.start, newFinalStates, newTf);
    }

    public static NondeterministicFiniteAutomaton star(NondeterministicFiniteAutomaton nfa){
        State aux = new State();

        Set<State> newStates = new HashSet<>(nfa.states);
        newStates.add(aux);

        Alphabet newAlphabet =  new Alphabet();
        newAlphabet.addAll(nfa.alphabet);

        Set<State> newFinalStates = new HashSet<>(nfa.finalStates);
        newFinalStates.add(aux);

        NondeterministicTransitionFunction newTf = new NondeterministicTransitionFunction();
        newTf.addAll(nfa.transition);
        for(State s : nfa.finalStates) newTf.add(s, nfa.start, Character.MIN_VALUE);
        newTf.add(aux, nfa.start, Character.MIN_VALUE);

        return new NondeterministicFiniteAutomaton(newStates, newAlphabet, aux, newFinalStates, newTf);
    }
}
