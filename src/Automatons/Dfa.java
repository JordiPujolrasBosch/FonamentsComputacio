package Automatons;

import AutomatonElements.Alphabet;
import AutomatonElements.Dtf;
import AutomatonElements.Rule;
import AutomatonElements.State;
import Factory.AutomatonFactory;
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
        Map<State, Integer> mapper = new HashMap<>();
        int i = 0;
        for(State s : states) mapper.put(s, i++);

        String res = "";
        res = res + "states: " + states.size() + "\n";
        res = res + "start: " + mapper.get(start) + "\n";

        res = res + "final: ";
        if(finalStates.isEmpty()) res = res + "-1\n";
        else {
            Iterator<State> it = finalStates.iterator();
            while(it.hasNext()){
                State act = it.next();
                if(it.hasNext()) res = res + mapper.get(act) + ", ";
                else res = res + mapper.get(act) + "\n";
            }
        }

        res = res + "alphabet: ";
        if(alphabet.contains(Alphabet.getEmptyChar())) res = res + "'', ";
        for(Character c : alphabet.set()){
            if(c.equals(' ')) res = res + "space, ";
            else if(c.equals(',')) res = res + "comma, ";
            else res = res + c + ", ";
        }
        res = res + "nothing\n";

        for(Rule r : transition.getRules()){
            String character = r.character().toString();
            if(r.character().equals(Alphabet.getEmptyChar())) character = "''";
            else if(r.character().equals(' ')) character = "space";
            res = res + mapper.get(r.origin()) + " " + character + " " + mapper.get(r.destiny()) + "\n";
        }

        return res;
    }
}
