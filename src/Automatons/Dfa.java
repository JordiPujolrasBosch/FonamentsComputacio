package Automatons;

import AutomatonElements.Alphabet;
import AutomatonElements.Dtf;
import AutomatonElements.Rule;
import AutomatonElements.State;
import Factory.AutomatonFactory;
import Factory.Constructors.DfaConstructor;
import Factory.OutputMessages;

import java.util.Set;
import java.util.HashSet;

import java.util.Map;
import java.util.HashMap;

import java.util.Iterator;

public class Dfa {
    private final Set<State> states;
    private final Set<State> finalStates;
    private final Alphabet alphabet;
    private final State start;
    private final Dtf transition;

    public Dfa(Set<State> states, Alphabet alphabet, State start, Set<State> finalStates, Dtf transition){
        this.states = states;
        this.alphabet = alphabet;
        this.start = start;
        this.finalStates = finalStates;
        this.transition = transition;
    }

    public DfaConstructor getConstructor() {
        return new DfaConstructor(states, alphabet, start, finalStates, transition);
    }

    public boolean checkWord(String word){
        boolean in = true;
        State act = start;
        int i = 0;
        char c;

        while(i<word.length() && in){
            c = word.charAt(i);
            in = alphabet.contains(c);
            if(in) act = transition.step(act,c);
            i++;
        }

        return in && finalStates.contains(act);
    }

    public boolean equal(Dfa b){
        boolean eq = states.size() == b.states.size();
        eq = eq && finalStates.size() == b.finalStates.size();
        eq = eq && alphabet.equal(b.alphabet);

        if(!eq) return false;

        Map<State, State> mapper = new HashMap<>();
        mapper.put(start, b.start);

        Set<State> tocheck = new HashSet<>(states);
        tocheck.remove(start);

        Set<State> checking = new HashSet<>();
        checking.add(start);

        Set<State> nextcheck = new HashSet<>();

        Iterator<State> its = null;
        Iterator<Character> itc = null;
        State origina, originb, destinya, destinyb = null;
        Character c = null;

        while(eq && !tocheck.isEmpty()){
            its = checking.iterator();
            while(eq && its.hasNext()){
                origina = its.next();
                originb = mapper.get(origina);

                itc = alphabet.set().iterator();
                while(eq && itc.hasNext()){
                    c = itc.next();
                    destinya = transition.step(origina, c);
                    destinyb = b.transition.step(originb, c);

                    if(!mapper.containsKey(destinya)){
                        mapper.put(destinya, destinyb);
                    }
                    else{
                        eq = destinyb == mapper.get(destinya);
                    }

                    if(tocheck.contains(destinya)){
                        nextcheck.add(destinya);
                    }
                }
            }

            checking.clear();
            checking.addAll(nextcheck);
            tocheck.removeAll(nextcheck);
            nextcheck.clear();
        }

        if(eq){
            Iterator<State> itf = finalStates.iterator();
            while(eq && itf.hasNext()){
                eq = b.finalStates.contains(mapper.get(itf.next()));
            }
        }

        return eq;
    }

    public Nfa toNfa(){
        return AutomatonFactory.dfaToNfa(this);
    }

    public Dfa minimize(){
        return AutomatonFactory.minimize(this);
    }

    public String toString(){
        return OutputMessages.automatonToString(states, alphabet, start, finalStates, transition.getRules());
    }
}
