package Factory.Builders;

import Elements.Alphabet;
import Automatons.Dfa;
import Automatons.Nfa;
import Exceptions.AutomatonReaderException;
import Factory.Algorithms;
import Factory.TokenFactory;
import Utils.Utility;

import java.util.Arrays;
import java.util.Iterator;

import java.util.List;
import java.util.ArrayList;

public class AutomatonData {
    private boolean numberStatesRead;                //read
    private String numberStatesString;               //string
    private int numberStates;                        //out

    private boolean startStateNumberRead;            //read
    private String startStateString;                 //string
    private int startStateNumber;                    //out

    private boolean finalStatesNumbersRead;          //read
    private List<String> finalStatesString;          //string
    private List<Integer> finalStatesNumbers;        //out

    private boolean alphabetElementsRead;            //read
    private List<String> alphabetElements;           //string
    private Alphabet alphabet;                       //out

    private final List<RuleString> transitionString; //string
    private List<RuleData> transition;               //out

    private final String filename;

    //Constructor

    public AutomatonData(){
        numberStatesRead = false;
        startStateNumberRead = false;
        finalStatesNumbersRead = false;
        alphabetElementsRead = false;
        transitionString = new ArrayList<>();

        filename = null;
    }

    public AutomatonData(String file){
        numberStatesRead = false;
        startStateNumberRead = false;
        finalStatesNumbersRead = false;
        alphabetElementsRead = false;
        transitionString = new ArrayList<>();

        filename = file;
    }

    //Read

    public void setStates(String s) {
        numberStatesString = s;
        numberStatesRead = true;
    }

    public void setStart(String s) {
        startStateString = s;
        startStateNumberRead = true;
    }

    public void setFinal(String[] finals) {
        finalStatesNumbersRead = true;
        finalStatesString = Arrays.asList(finals);
    }

    public void setAlphabet(String[] tokens) {
        alphabetElementsRead = true;
        alphabetElements = Arrays.asList(tokens);
    }

    public void addRule(String o, String c, String d) {
        transitionString.add(new RuleString(o,d,c));
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
        ok = ok && checkNumberStates();
        ok = ok && checkStartState();
        ok = ok && checkFinalStates();
        ok = ok && checkAlphabet();
        ok = ok && checkRules();
        return ok;
    }

    public boolean isDeterministic(){
        boolean det = ! alphabet.containsEmptyChar();

        if(det){
            int i = 0;
            while (i<numberStates-1 && det){
                int j = i+1;
                while (j<numberStates && det){
                    det = !(transition.get(i).getOrigin() == transition.get(j).getOrigin() && transition.get(i).getCharacter() == transition.get(j).getCharacter());
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

    //Transformations

    public Dfa toDfa() throws AutomatonReaderException {
        return Algorithms.dataToDfa(this);
    }

    public Nfa toNfa() throws AutomatonReaderException {
        return Algorithms.dataToNfa(this);
    }

    //Private

    private boolean checkNumberStates(){
        if(!Utility.isNumber(numberStatesString)) return false;
        numberStates = Integer.parseInt(numberStatesString);
        return numberStates > 0;
    }

    private boolean checkStartState(){
        if(!Utility.isNumber(startStateString)) return false;
        startStateNumber = Integer.parseInt(startStateString);
        return validState(startStateNumber);
    }

    private boolean validState(int x){
        return x >= 0 && x < numberStates;
    }

    private boolean checkFinalStates(){
        if(finalStatesString.isEmpty()) return false;
        finalStatesNumbers = new ArrayList<>();
        if(finalStatesString.size() == 1 && Utility.isNegativeNumber(finalStatesString.get(0))){
            finalStatesNumbers.add(Integer.parseInt(finalStatesString.get(0)));
            return true;
        }

        boolean ok = true;
        Iterator<String> it = finalStatesString.iterator();
        while(it.hasNext() && ok){
            String s = it.next();
            ok = Utility.isNumber(s) && validState(Integer.parseInt(s));
            if(ok) finalStatesNumbers.add(Integer.parseInt(s));
        }
        return ok;
    }

    private boolean checkAlphabet(){
        alphabet = new Alphabet();
        boolean ok = true;
        Iterator<String> it = alphabetElements.iterator();
        while (it.hasNext() && ok){
            String s = it.next();
            if(s.length() == 1) alphabet.addChar(s.charAt(0));
            else if(TokenFactory.isAChar(s)) alphabet.addChar(TokenFactory.getAChar(s));
            else if(TokenFactory.isAGroup(s)) alphabet.addAll(TokenFactory.getAGroup(s));
            else if(!s.equals(TokenFactory.getANothing())) ok = false;
        }
        return ok;
    }

    private boolean checkRules(){
        transition = new ArrayList<>();
        boolean ok = true;
        Iterator<RuleString> it = transitionString.iterator();
        while(it.hasNext() && ok){
            RuleString r = it.next();
            ok = Utility.isNumber(r.origin) && validState(Integer.parseInt(r.origin));
            ok = ok && Utility.isNumber(r.destiny) && validState(Integer.parseInt(r.destiny));
            char c = 'A';
            if(r.transition.length() == 1) c = r.transition.charAt(0);
            else if(TokenFactory.isBChar(r.transition)) c = TokenFactory.getBChar(r.transition);
            else ok = false;
            ok = ok && alphabet.contains(c);
            if(ok) transition.add(new RuleData(Integer.parseInt(r.origin), Integer.parseInt(r.destiny), c));
        }
        return ok;
    }

    private static class RuleString{
        public String origin;
        public String destiny;
        public String transition;
        public RuleString(String origin, String destiny, String transition){
            this.origin = origin;
            this.destiny = destiny;
            this.transition = transition;
        }
    }

}
