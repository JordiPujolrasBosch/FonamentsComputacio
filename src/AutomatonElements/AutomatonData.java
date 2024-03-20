package AutomatonElements;

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

    private final List<Rule> transition;
    private Alphabet a;


    public AutomatonData(){
        numberStatesRead = false;
        startStateRead = false;
        finalStatesRead = false;
        alphabetRead = false;

        transition = new ArrayList<>();
        a = null;
    }

    //READ

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
        transition.add(new Rule(o,d,Alphabet.transform(s)));
    }

    //GETTERS

    public int getStates(){
        return numberStates;
    }

    public int getStart() {
        return startState;
    }

    public List<Integer> getFinalStates() {
        return finalStates;
    }

    public Alphabet getAlphabet() {
        return a;
    }

    public List<Rule> getTransitions(){
        return transition;
    }

    //CHECK

    public boolean hasBasic() {
        return numberStatesRead && startStateRead && finalStatesRead && alphabetRead;
    }

    public boolean check(){
        boolean ok = hasBasic();
        ok = ok && numberStates >= 0;
        ok = ok && validState(startState);

        Iterator<Integer> it1 = finalStates.iterator();
        while (it1.hasNext() && ok) ok = validState(it1.next());

        Iterator<String> it2 = alphabet.iterator();
        while (it2.hasNext() && ok) ok = Alphabet.validElement(it2.next());

        Alphabet alp = new Alphabet();
        if (ok) for(String x : alphabet) alp.addElement(x);

        Iterator<Rule> it3 = transition.iterator();
        while (it3.hasNext() && ok){
            Rule r = it3.next();
            ok = validState(r.origin()) && validState(r.destiny()) && alp.contains(r.character());
        }

        if(ok) a = alp;
        return ok;
    }

    public boolean isDeterministic(){
        boolean det = ! alphabet.contains("''");

        if(det){
            int i = 0;
            while (i<numberStates && det){
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
        return transition.size() == numberStates * a.size();
    }

    private boolean validState(int x){
        return x >= 0 && x < numberStates;
    }
}
