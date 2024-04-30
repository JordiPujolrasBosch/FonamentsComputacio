package Factory;

import AutomatonElements.Alphabet;
import ContextFreeGrammars.Cfg;
import ContextFreeGrammars.CfgRule;
import ContextFreeGrammars.CfgVariable;

import java.util.*;

public class GrammarData {
    private List<String> terminalElements;
    private Alphabet alphabet;
    private boolean terminalElementsRead;

    private List<String> variables;
    private Set<CfgVariable> setvar;
    private boolean variablesRead;

    private String startString;
    private CfgVariable start;
    private boolean startRead;

    private List<CfgRule> rules;
    private List<List<String>> rulesString;

    private final String filename;

    public GrammarData(String filename){
        terminalElementsRead = false;
        variablesRead = false;
        startRead = false;
        rulesString = new ArrayList<>();

        this.filename = filename;
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

    public void setStart(String s) {
        startRead = true;
        startString = s;
    }

    public void addRule(String[] s) {
        List<String> l = new ArrayList<>();
        for(String x : s) if(!x.isEmpty()) l.add(x);
        rulesString.add(l);
    }

    public boolean check() {
        boolean ok = hasBasic();





        return false;
    }

    public Cfg getCfg() {
        return null;
    }
}