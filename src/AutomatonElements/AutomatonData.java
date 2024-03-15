package AutomatonElements;

import Automatons.DeterministicFiniteAutomaton;
import Readers.AutomatonReaderException;

import java.util.*;

public class AutomatonData {
    private int numberStates;
    private boolean numberStatesRead;

    private int startState;
    private boolean startStateRead;

    private List<Integer> finalStates;
    private boolean finalStatesRead;

    private List<String> alphabet;
    private boolean alphabetRead;

    private List<Rule> transition;


    public AutomatonData(){
        numberStatesRead = false;
        startStateRead = false;
        finalStatesRead = false;
        alphabetRead = false;

        transition = new ArrayList<>();
    }

    public boolean hasBasic() {
        return numberStatesRead && startStateRead && finalStatesRead && alphabetRead;
    }

    public void setStates(int s) {
        numberStates = s;
        numberStatesRead = true;
    }

    public void setStart(int s) {
        startState = s;
        startStateRead = true;
    }

    public void setFinal(String[] finals) throws NumberFormatException {
        finalStatesRead = true;
        finalStates = new ArrayList<>();
        for(String s : finals) finalStates.add(Integer.parseInt(s));
    }

    public void setAlphabet(String[] tokens) {
        alphabetRead = true;
        alphabet = Arrays.asList(tokens);
    }

    public void addRule(int o, String s, int d) throws Exception {
        Character c = Alphabet.transform(s);
        transition.add(new Rule(o,d,c));
    }

    public DeterministicFiniteAutomaton getAutomaton() throws AutomatonReaderException {
        //Basic
        if(!hasBasic()) throw new AutomatonReaderException();

        //Array
        if(numberStates <= 0) throw new AutomatonReaderException();
        State[] array = new State[numberStates];
        for(int i=0; i<numberStates; i++) array[i] = new State();

        //Set states
        Set<State> setStates = new HashSet<>(Arrays.asList(array));

        //Start state
        if(startState < 0 || startState >= numberStates) throw new AutomatonReaderException();
        State start = array[startState];

        //Final states
        Set<State> setFinalStates = new HashSet<>();
        for(int x : finalStates){
            if(x < 0 || x >= numberStates) throw new AutomatonReaderException();
            setFinalStates.add(array[x]);
        }

        Alphabet alp = new Alphabet();
        for(String x : alphabet) alp.addElements(x);

        DeterministicTransitionFunction tf = new DeterministicTransitionFunction();
        for(Rule r : transition){
            if(alp.contains(r.character())) tf.add(array[r.origin()],array[r.destiny()],r.character());
            else throw new Exception();
        }

        return new DeterministicFiniteAutomaton(setStates, alp, start, setFinalStates, tf);
    }


}
