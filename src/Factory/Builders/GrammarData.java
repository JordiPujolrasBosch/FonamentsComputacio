package Factory.Builders;

import Elements.Alphabet;
import Grammars.*;
import Elements.Grammars.Grule;
import Elements.Grammars.Gvar;
import Factory.TokenFactory;
import Utils.Utility;

import java.util.Arrays;
import java.util.Iterator;

import java.util.List;
import java.util.ArrayList;

import java.util.Set;
import java.util.HashSet;

public class GrammarData {
    private boolean terminalElementsRead;
    private List<String> terminalElements;
    private final Alphabet alphabet;

    private boolean variablesRead;
    private List<String> variables;
    private final Set<Gvar> setvar;

    private boolean startRead;
    private String startString;
    private Gvar start;

    private final List<List<String>> rulesString;
    private final List<Grule> rules;

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

    //Check

    public boolean hasBasic() {
        return terminalElementsRead && variablesRead && startRead;
    }

    public boolean check() {
        boolean ok = hasBasic();

        ok = ok && checkTerminals();
        ok = ok && checkVariables();
        ok = ok && checkStart();
        ok = ok && checkRules();

        return ok;
    }

    //Transform

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
            else if(TokenFactory.isAChar(s)) alphabet.addChar(TokenFactory.getAChar(s));
            else if(TokenFactory.isAGroup(s)) alphabet.addAll(TokenFactory.getAGroup(s));
            else if(!s.equals(TokenFactory.getANothing())) ok = false;
        }
        return ok;
    }

    private boolean checkVariables() {
        boolean ok = true;
        Iterator<String> it = variables.iterator();
        while(it.hasNext() && ok){
            String s = it.next();
            char last = ' ';
            if(!s.isEmpty()) last = s.charAt(s.length()-1);

            if(s.isEmpty()) {
                ok = false;
            }
            else if(!s.contains("-")){
                ok = isVariable(s);
                if(ok) setvar.add(toVariable(s));
            }
            else if(last >= 'A' && last <= 'Z'){
                ok = isGroupOne(s);
                if(ok) setvar.addAll(toVariablesOne(s));
            }
            else if(last >= '0' && last <= '9'){
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
        boolean ok = isVariable(leftString) && setvar.contains(toVariable(leftString)) && arrow.equals(TokenFactory.getGArrow());
        if(!ok) return false;

        if(!correctFormRight(list)) return false;

        Gvar left = toVariable(leftString);
        List<Gramex> listRight = getListRight(list);
        for(Gramex right : listRight) rules.add(new Grule(left, right));
        return true;
    }

    private boolean correctFormRight(List<String> list){
        boolean ok = true;
        int i = 0;
        while(ok && i < list.size()){
            String act = list.get(i);
            boolean isFirst = i == 0;
            boolean isLast = i == list.size()-1;
            String union = TokenFactory.getGUnion();
            String empty = TokenFactory.getGEmptyChar();

            if(isVariable(act)){
                ok = setvar.contains(toVariable(act));
            }
            else if(TokenFactory.isGChar(act)){
                ok = alphabet.contains(TokenFactory.getGChar(act));
            }
            else if(TokenFactory.isGGroup(act)){
                ok = alphabet.getSet().containsAll(TokenFactory.getGGroup(act));
                if(ok){
                    if(!isFirst) ok = list.get(i-1).equals(union);
                    if(!isLast)  ok = ok && list.get(i+1).equals(union);
                }
            }
            else if(act.equals(union)){
                if(isFirst || isLast) ok = false;
                else ok = !list.get(i-1).equals(union) && !list.get(i+1).equals(union);
            }
            else if(act.equals(empty)){
                ok = alphabet.containsEmptyChar();
                if(ok){
                    if(!isFirst) ok = list.get(i-1).equals(union);
                    if(!isLast)  ok = ok && list.get(i+1).equals(union);
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
            else if(TokenFactory.isGChar(act)){
                if(right == null) right = new GramexChar(TokenFactory.getGChar(act));
                else right = new GramexConcat(right, new GramexChar(TokenFactory.getGChar(act)));
            }
            else if(TokenFactory.isGGroup(act)){
                for(char c : TokenFactory.getGGroup(act)) listRight.add(new GramexChar(c));
            }
            else if(act.equals(TokenFactory.getGEmptyChar())){
                listRight.add(GramexEmpty.getInstance());
            }
            else if(act.equals(TokenFactory.getGUnion())){
                if(right != null) listRight.add(right);
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
        return s.length() > 1 && s.charAt(0) >= 'A' && s.charAt(0) <= 'Z' && Utility.isNumber(s.substring(1));
    }

    private Gvar toVariable(String s) {
        return new Gvar(s.charAt(0), Integer.parseInt(s.substring(1)));
    }

    private boolean isGroupOne(String s) {
        if(s.length() != 3) return false;
        if(s.charAt(1) != '-') return false;
        boolean c1 = s.charAt(0) >= 'A' && s.charAt(0) <= 'Z';
        boolean c2 = s.charAt(2) >= 'A' && s.charAt(2) <= 'Z';
        return c1 && c2 && s.charAt(0) < s.charAt(2);
    }

    private Set<Gvar> toVariablesOne(String s) {
        Set<Gvar> set = new HashSet<>();
        for(char c = s.charAt(0); c <= s.charAt(2); c++) set.add(new Gvar(c, 0));
        return set;
    }

    private boolean isGroupTwo(String s) {
        if(s.length()<4) return false;
        boolean c = s.charAt(0) >= 'A' && s.charAt(0) <= 'Z';
        String[] pair = s.substring(1).split("-");
        if(pair.length != 2) return false;
        return c && Utility.isNumber(pair[0]) && Utility.isNumber(pair[1]) && Integer.parseInt(pair[0]) < Integer.parseInt(pair[1]);
    }

    private Set<Gvar> toVariablesTwo(String s) {
        char c = s.charAt(0);
        String[] pair = s.substring(1).split("-");
        Set<Gvar> set = new HashSet<>();
        for(int i = Integer.parseInt(pair[0]); i <= Integer.parseInt(pair[1]); i++) set.add(new Gvar(c, i));
        return set;
    }

}