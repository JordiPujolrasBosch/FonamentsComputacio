package Automatons;

import Elements.Alphabet;
import Elements.Transitions.Pdtf;
import Elements.State;
import Elements.Grammars.CfgVariable;
import Elements.Transitions.PdtfTuple;

import java.util.*;

public class Pda {
    private final Set<State> states;
    private final Alphabet alphabet;
    private final State start;
    private final Set<State> finalStates;
    private final Pdtf transition;

    private final Map<Character, Integer> mapperChar;
    private final Map<CfgVariable, Integer> mapperVar;

    public Pda(Set<State> states, Alphabet alphabet, State start, Set<State> finalStates,
               Pdtf transition, Map<Character,Integer> mapperChar, Map<CfgVariable,Integer> mapperVar) {
        this.states = states;
        this.alphabet = alphabet;
        this.start = start;
        this.finalStates = finalStates;
        this.transition = transition;

        this.mapperChar = mapperChar;
        this.mapperVar = mapperVar;
    }

    public boolean checkWord(String word){
        boolean foundIlegalChar = false;
        int i = 0;
        while(!foundIlegalChar && i < word.length()) foundIlegalChar = !alphabet.contains(word.charAt(i++));
        if(foundIlegalChar) return false;

        List<PdaRunningInstance> instances = new ArrayList<>();
        instances.add(new PdaRunningInstance(start, new Stack<>(), 0));

        int counter = 0;
        boolean stop = false;
        boolean accept = false;
        while(!stop){
            ExitStep x = runningStep(instances, word);
            counter++;
            stop = (x.accept || x.list.isEmpty() || counter == 7000);
            instances = x.list;
            accept = x.accept;
        }

        return accept;
    }

    private ExitStep runningStep(List<PdaRunningInstance> instances, String word){
        ExitStep exit = new ExitStep();
        int e = mapperChar.get(Alphabet.getEmptyChar());
        int c = e;
        int stackTop = e;

        for(PdaRunningInstance ins : instances){
            if(ins.pos < word.length()) c = mapperChar.get(word.charAt(ins.pos));
            if(!ins.stack.empty()) stackTop = ins.stack.peek();

            for(PdtfTuple tuple : transition.stepWithEmpty(ins.act, c, stackTop, e)){
                PdaRunningInstance out = new PdaRunningInstance(tuple.getDestiny(), ins.stack, ins.pos);
                if(tuple.getCharacter() != e) out.pos = out.pos + 1;
                if(tuple.getPop() != e) out.stack.pop();
                if(tuple.getPush() != e) out.stack.push(tuple.getPush());

                if(shortWordInStack(out.stack, word)) exit.list.add(out);
                if(finalStates.contains(out.act) && out.stack.empty() && out.pos == word.length()) exit.accept = true;
            }
        }
        return exit;
    }

    private boolean shortWordInStack(Stack<Integer> stack, String word){
        int charsCounter = 0;
        for(int i : stack){
            if(mapperChar.containsValue(i)) charsCounter++;
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
