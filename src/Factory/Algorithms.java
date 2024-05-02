package Factory;

import Automatons.Dfa;
import Elements.State;
import Factory.Constructors.DfaConstructor;

import java.util.*;

public class Algorithms {

    public static boolean equalsDfas(Dfa a, Dfa b){
        DfaConstructor ac = a.getConstructor();
        DfaConstructor bc = b.getConstructor();

        boolean eq = ac.states.size() == bc.states.size();
        eq = eq && ac.finalStates.size() == bc.finalStates.size();
        eq = eq && ac.alphabet.equal(bc.alphabet);

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

                Iterator<Character> itc = ac.alphabet.set().iterator();
                while (eq && itc.hasNext()) {
                    Character c = itc.next();
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

}
