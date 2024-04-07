package Automatons;

import AutomatonElements.Alphabet;
import AutomatonElements.Dtf;
import AutomatonElements.State;
import Factory.DfaConstructor;

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

        Map<State, State> mapper = new HashMap<>();
        if(eq){
            mapper.put(start, b.start);

            Set<State> tocheck = new HashSet<>(states);
            tocheck.remove(start);

            Set<State> checking = new HashSet<>();
            checking.add(start);

            while(eq && !tocheck.isEmpty()){
                Iterator<State> its = checking.iterator();
                while(eq && its.hasNext()){
                    Iterator<Character> itc = alphabet.set().iterator();
                    while(eq && itc.hasNext()){
                        Character c = itc.next();
                        State origina = its.next();
                        State originb = mapper.get(origina);
                        State destinya = transition.step(origina, c);
                        State destinyb = b.transition.step(originb, c);

                        if(!mapper.containsKey(destinya)){
                            mapper.put(destinya, destinyb);
                        }
                        else{
                            eq = destinyb == mapper.get(destinya);
                        }

                        if(tocheck.contains(destinya)){
                            tocheck.remove(destinya);
                            checking.add(destinya);
                        }
                    }
                }
            }
        }

        if(eq){
            Iterator<State> itf = finalStates.iterator();
            while(eq && itf.hasNext()){
                eq = b.finalStates.contains(mapper.get(itf.next()));
            }
        }

        return eq;
    }

}
