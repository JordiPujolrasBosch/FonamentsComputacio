package AutomatonElements;

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

    private final List<DataRule> transition;
    private Alphabet alphabet;

    //Constructor

    public AutomatonData(){
        numberStatesRead = false;
        startStateNumberRead = false;
        finalStatesNumbersRead = false;
        alphabetElementsRead = false;

        transition = new ArrayList<>();
        alphabet = new Alphabet();
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
        transition.add(new DataRule(o,d,Alphabet.transform(s)));
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
        return finalStatesNumbers;
    }

    public List<DataRule> getTransitions(){
        return transition;
    }

    //Check

    public boolean hasBasic() {
        return numberStatesRead && startStateNumberRead && finalStatesNumbersRead && alphabetElementsRead;
    }

    public boolean check(){
        boolean ok = hasBasic();
        ok = ok && numberStates > 0;
        ok = ok && validState(startStateNumber);

        Iterator<Integer> it1 = finalStatesNumbers.iterator();
        while (it1.hasNext() && ok) ok = validState(it1.next());

        Iterator<String> it2 = alphabetElements.iterator();
        while (it2.hasNext() && ok) ok = Alphabet.validElement(it2.next());

        Alphabet alp = new Alphabet();
        if (ok) for(String x : alphabetElements) alp.addElement(x);

        Iterator<DataRule> it3 = transition.iterator();
        while (it3.hasNext() && ok){
            DataRule r = it3.next();
            ok = validState(r.origin()) && validState(r.destiny()) && alp.contains(r.character());
        }

        if(ok) alphabet = alp;
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

}
