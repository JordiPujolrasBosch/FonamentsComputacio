package ContextFreeGrammars;

import AutomatonElements.Alphabet;

import java.util.*;

public class GrammarData {
    private List<String> terminalElements;
    private Alphabet alphabet;
    private boolean terminalElementsRead;

    private List<String> variables;
    private Set<CfgVariable> setvar;
    private boolean variablesRead;

    private CfgVariable start;
    private boolean startRead;

    private final String filename;
    private List<CfgRule> rules;
    private List<String> rulesString;

    public GrammarData(String filename){
        terminalElementsRead = false;
        variablesRead = false;
        startRead = false;

        this.filename = filename;
        rules = new ArrayList<>();
        rulesString = new ArrayList<>();
    }

    public boolean hasBasic() {
        return terminalElementsRead && variablesRead && startRead;
    }

    public void setTerminals(String[] tokens) {
        terminalElementsRead = true;
        terminalElements = Arrays.asList(tokens);
    }

    public void setVariables(String[] vars) {
        variablesRead = true;
        variables = Arrays.asList(vars);
    }

    public void setStart(String s) throws Exception {
        startRead = true;
        start = CfgVariable.transform(s);
    }

    public void addRule(String[] s) {
        rulesString = new ArrayList<>();
        for(String x : s) if(!x.isEmpty()) rulesString.add(x);
    }

    public boolean check() {
        boolean ok = hasBasic();

        Iterator<String> it = terminalElements.iterator();
        while (it.hasNext() && ok) ok = Alphabet.validElement(it.next());
        alphabet = new Alphabet();
        if (ok) for(String x : terminalElements) alphabet.addElement(x);

        Iterator<String> it2 = variables.iterator();
        //while (it2.hasNext() && ok) ok = CfgVariable.validElement(it.next());
        setvar = new HashSet<>();
        //if(ok) for(String x : variables) setvar.addAll(CfgVariable.getElements());



        return false;
    }

    public Cfg getCfg() {
        return null;
    }
}