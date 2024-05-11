package Factory.Builders;

import Elements.Alphabet;
import Grammars.*;
import Elements.Grammars.CfgRule;
import Elements.Grammars.CfgVariable;
import Factory.TokenFactory;

import java.util.*;

public class GrammarData {
    private boolean terminalElementsRead;
    private List<String> terminalElements;
    private final Alphabet alphabet;

    private boolean variablesRead;
    private List<String> variables;
    private final Set<CfgVariable> setvar;

    private boolean startRead;
    private String startString;
    private CfgVariable start;

    private final List<List<String>> rulesString;
    private final List<CfgRule> rules;

    public GrammarData(){
        terminalElementsRead = false;
        variablesRead = false;
        startRead = false;
        rulesString = new ArrayList<>();

        alphabet = new Alphabet();
        setvar = new HashSet<>();
        rules = new ArrayList<>();

        terminalElements = null;
        variables = null;
        startString = null;
        start = null;
    }

    //Setters

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

    public boolean hasBasic() {
        return terminalElementsRead && variablesRead && startRead;
    }

    public boolean check() {
        boolean ok = hasBasic() && !rulesString.isEmpty();

        ok = ok && checkTerminals();
        ok = ok && checkVariables();
        ok = ok && checkStart();
        ok = ok && checkRules();

        return ok;
    }

    public Cfg getCfg() {
        return new Cfg(alphabet, setvar, start, new HashSet<>(rules));
    }

    //In check

    private boolean checkTerminals() {
        boolean ok = true;
        Iterator<String> it = terminalElements.iterator();
        while (it.hasNext() && ok){
            String s = it.next();
            if(s.length() == 1) alphabet.addChar(s.charAt(0));
            else if(TokenFactory.atokensContains(s)) alphabet.addAll(TokenFactory.atokensGet(s));
            else ok = false;
        }
        return ok;
    }

    private boolean checkVariables() {
        boolean ok = true;
        Iterator<String> it = variables.iterator();
        while(it.hasNext() && ok){
            String s = it.next();
            char last = s.charAt(s.length()-1);
            boolean m = s.contains("-");
            boolean lastNumber = last >= '0' && last <= '9';
            boolean lastLetter = last >= 'A' && last <= 'Z';

            if(!m){
                ok = isVariable(s);
                if(ok) setvar.add(toVariable(s));
            }
            else if(lastLetter){
                ok = isGroupOne(s);
                if(ok) setvar.addAll(toVariablesOne(s));
            }
            else if(lastNumber){
                ok = isGroupTwo(s);
                if(ok) setvar.addAll(toVariablesTwo(s));
            }
            else{
                ok = false;
            }
        }
        return ok;
    }

    private boolean checkStart() {
        boolean ok = isVariable(startString) && setvar.contains(toVariable(startString));
        if(ok) start = toVariable(startString);
        return ok;
    }

    private boolean checkRules() {
        boolean ok = true;
        Iterator<List<String>> it = rulesString.iterator();
        while(ok && it.hasNext()){
            ok = checkOneRule(it.next());
        }
        return ok;
    }

    private boolean checkOneRule(List<String> list){
        if(list.size()<3) return false;

        String leftString = list.remove(0);
        String arrow = list.remove(0);
        boolean ok = isVariable(leftString) && setvar.contains(toVariable(leftString)) && arrow.equals("->");
        if(!ok) return false;

        if(!correctFormRight(list)) return false;

        CfgVariable left = toVariable(leftString);
        List<Gramex> listRight = getListRight(list);
        for(Gramex right : listRight) rules.add(new CfgRule(left, right));
        return true;
    }

    public boolean correctFormRight(List<String> list){
        boolean ok = true;
        int i = 0;
        while(ok && i < list.size()){
            String act = list.get(i);
            boolean isFirst = i == 0;
            boolean isLast = i == list.size()-1;
            String union = TokenFactory.getGrammarUnion();
            String empty = TokenFactory.getGrammarEmpty();

            if(isVariable(act)){
                ok = setvar.contains(toVariable(act));
            }
            else if(TokenFactory.gtokenCharContains(act)){
                ok = alphabet.contains(TokenFactory.gtokenCharGet(act));
            }
            else if(TokenFactory.gtokenGroupContains(act)){
                ok = alphabet.getSet().containsAll(TokenFactory.gtokenGroupSet(act));
                if(ok){
                    if(isFirst) ok = list.get(i+1).equals(union);
                    else if(isLast) ok = list.get(i-1).equals(union);
                    else ok = list.get(i-1).equals(union) && list.get(i+1).equals(union);
                }
            }
            else if(act.equals(union)){
                if(isFirst || isLast) ok = false;
                else ok = !list.get(i-1).equals(union) && !list.get(i+1).equals(union);
            }
            else if(act.equals(empty)){
                ok = alphabet.containsEmptyChar();
                if(ok){
                    if(isFirst) ok = list.get(i+1).equals(union);
                    else if(isLast) ok = list.get(i-1).equals(union);
                    else ok = list.get(i-1).equals(union) && list.get(i+1).equals(union);
                }
            }
            else if(act.length()==1){
                ok = alphabet.contains(act.charAt(0));
            }
            else{
                ok = false;
            }

            i++;
        }
        return ok;
    }

    private List<Gramex> getListRight(List<String> list) {
        List<Gramex> listRight = new ArrayList<>();
        GramexNonEmpty right = null;

        int i = 0;
        while(i < list.size()){
            String act = list.get(i);
            if(isVariable(act)){
                if(right == null) right = new GramexVar(toVariable(act));
                else right = new GramexConcat(right, new GramexVar(toVariable(act)));
            }
            else if(TokenFactory.gtokenCharContains(act)){
                if(right == null) right = new GramexChar(TokenFactory.gtokenCharGet(act));
                else right = new GramexConcat(right, new GramexChar(TokenFactory.gtokenCharGet(act)));
            }
            else if(TokenFactory.gtokenGroupContains(act)){
                for(char c : TokenFactory.gtokenGroupSet(act)) listRight.add(new GramexChar(c));
            }
            else if(act.equals(TokenFactory.getGrammarEmpty())){
                listRight.add(new GramexEmpty());
            }
            else if(act.equals(TokenFactory.getGrammarUnion())){
                listRight.add(right);
                right = null;
            }
            else if(act.length()==1){
                if(right == null) right = new GramexChar(act.charAt(0));
                else right = new GramexConcat(right, new GramexChar(act.charAt(0)));
            }
            i++;
        }
        if(right != null) listRight.add(right);
        return listRight;
    }

    //Private

    private boolean isVariable(String s) {
        return s.length() > 1 && s.charAt(0) >= 'A' && s.charAt(0) <= 'Z' && isNumber(s.substring(1));
    }

    private CfgVariable toVariable(String s) {
        return new CfgVariable(s.charAt(0), Integer.parseInt(s.substring(1)));
    }

    private boolean isGroupOne(String s) {
        if(s.length() != 3) return false;
        if(s.charAt(1) != '-') return false;
        boolean c1 = s.charAt(0) >= 'A' && s.charAt(0) <= 'Z';
        boolean c2 = s.charAt(2) >= 'A' && s.charAt(2) <= 'Z';
        return c1 && c2 && s.charAt(0) < s.charAt(2);
    }

    private Set<CfgVariable> toVariablesOne(String s) {
        Set<CfgVariable> set = new HashSet<>();
        for(char c = s.charAt(0); c <= s.charAt(2); c++) set.add(new CfgVariable(c, 0));
        return set;
    }

    private boolean isGroupTwo(String s) {
        boolean c = s.charAt(0) >= 'A' && s.charAt(0) <= 'Z';
        String[] pair = s.substring(1).split("-");
        if(pair.length != 2) return false;
        return c && isNumber(pair[0]) && isNumber(pair[1]) && Integer.parseInt(pair[0]) < Integer.parseInt(pair[1]);
    }

    private Set<CfgVariable> toVariablesTwo(String s) {
        char c = s.charAt(0);
        String[] pair = s.substring(1).split("-");
        Set<CfgVariable> set = new HashSet<>();
        for(int i = Integer.parseInt(pair[0]); i <= Integer.parseInt(pair[1]); i++) set.add(new CfgVariable(c, i));
        return set;
    }

    private boolean isNumber(String s){
        boolean isnumber = true;
        int i = 0;
        while(isnumber && i < s.length()){
            isnumber = s.charAt(i) >= '0' && s.charAt(i) <= '9';
            i++;
        }
        return isnumber;
    }

}