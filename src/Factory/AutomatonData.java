package Factory;

import AutomatonElements.Alphabet;
import AutomatonElements.RuleData;
import Automatons.Dfa;
import Automatons.Nfa;
import Exceptions.AutomatonReaderException;

import java.util.Arrays;
import java.util.Iterator;

import java.util.List;
import java.util.ArrayList;

public class AutomatonData {
    private int numberStates;
    private boolean numberStatesRead;

    private int startStateNumber;
    private boolean startStateNumberRead;

    private List<Integer> finalStatesNumbers;
    private boolean finalStatesNumbersRead;

    private List<String> alphabetElements;
    private boolean alphabetElementsRead;

    private final List<RuleData> transition;
    private final Alphabet alphabet;
    private final String filename;

    //Constructor

    public AutomatonData(String file){
        numberStatesRead = false;
        startStateNumberRead = false;
        finalStatesNumbersRead = false;
        alphabetElementsRead = false;

        transition = new ArrayList<>();
        alphabet = new Alphabet();
        filename = file;
    }

    //Read

    public void setStates(int s) {
        numberStates = s;
        numberStatesRead = true;
    }

    public void setStart(int s) {
        startStateNumber = s;
        startStateNumberRead = true;
    }

    public void setFinal(String[] finals) throws NumberFormatException {
        finalStatesNumbersRead = true;
        finalStatesNumbers = new ArrayList<>();
        for(String s : finals) finalStatesNumbers.add(Integer.parseInt(s));
    }

    public void setAlphabet(String[] tokens) {
        alphabetElementsRead = true;
        alphabetElements = Arrays.asList(tokens);
    }

    public void addRule(int o, String s, int d) throws Exception {
        if(s.length() == 1) transition.add(new RuleData(o,d,s.charAt(0)));
        else if(TokenFactory.btokensContains(s)) transition.add(new RuleData(o,d,TokenFactory.btokensGet(s)));
        else throw new Exception();
    }

    //Getters

    public int getNumberStates(){
        return numberStates;
    }

    public Alphabet getAlphabet() {
        return alphabet;
    }

    public int getStart() {
        return startStateNumber;
    }

    public List<Integer> getFinalStates() {
        if(finalStatesNumbers.size() == 1 && finalStatesNumbers.get(0) < 0) return new ArrayList<>();
        return finalStatesNumbers;
    }

    public List<RuleData> getTransitions(){
        return transition;
    }

    public String getFilename() {
        return filename;
    }

    //Check

    public boolean hasBasic() {
        return numberStatesRead && startStateNumberRead && finalStatesNumbersRead && alphabetElementsRead;
    }

    public boolean check(){
        boolean ok = hasBasic();
        ok = ok && numberStates > 0;
        ok = ok && validState(startStateNumber);

        if(ok && finalStatesNumbers.size() == 1 && finalStatesNumbers.get(0) < 0) ok = true;
        else{
            Iterator<Integer> it1 = finalStatesNumbers.iterator();
            while (it1.hasNext() && ok) ok = validState(it1.next());
        }

        Iterator<String> it2 = alphabetElements.iterator();
        while (it2.hasNext() && ok){
            String s = it2.next();
            if(s.length() == 1) alphabet.addChar(s.charAt(0));
            else if(TokenFactory.atokensContains(s)) alphabet.addAll(TokenFactory.atokensGet(s));
            else ok = false;
        }

        Iterator<RuleData> it3 = transition.iterator();
        while (it3.hasNext() && ok){
            RuleData r = it3.next();
            ok = validState(r.origin()) && validState(r.destiny()) && alphabet.contains(r.character());
        }

        return ok;
    }

    public boolean isDeterministic(){
        boolean det = ! alphabet.contains(Alphabet.getEmptyChar());

        if(det){
            int i = 0;
            while (i<numberStates-1 && det){
                int j = i+1;
                while (j<numberStates && det){
                    det = !(transition.get(i).origin() == transition.get(j).origin() && transition.get(i).character() == transition.get(j).character());
                    j++;
                }
                i++;
            }
        }

        return det;
    }

    public boolean isComplete(){
        return transition.size() == numberStates * alphabet.size();
    }

    private boolean validState(int x){
        return x >= 0 && x < numberStates;
    }

    //Transformations

    public Dfa toDfa() throws AutomatonReaderException {
        return AutomatonFactory.dataToDfa(this);
    }

    public Nfa toNfa() throws AutomatonReaderException {
        return AutomatonFactory.dataToNfa(this);
    }
}
