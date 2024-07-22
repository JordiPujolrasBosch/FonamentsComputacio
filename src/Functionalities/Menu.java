package Functionalities;

import Automatons.Dfa;
import Automatons.Nfa;
import Automatons.Pda;
import Factory.Algorithms;
import Factory.Printer;
import GrammarComparisonArticle.WordsGenerator;
import Grammars.Cfg;
import Grammars.CfgNonEmpty;
import RegularExpressions.RegularExpression;
import Utils.Pair;

import java.util.ArrayList;
import java.util.List;

public class Menu {

    //CFG MAIN

    public static String equalCfgCfgArticle(Cfg ga, Cfg gb, String fa, String fb){
        CfgNonEmpty a = ga.simplify().toGriebach();
        CfgNonEmpty b = gb.simplify().toGriebach();
        if(a.compare(b)) return Printer.equal(fa,fb);
        return Printer.nonequal(fa,fb);
    }

    public static String findCounterExampleCfgs(Cfg ga, Cfg gb, String fa, String fb){
        CfgNonEmpty a = ga.simplify().toGriebach();
        CfgNonEmpty b = gb.simplify().toGriebach();
        if(a.acceptsEmpty() != b.acceptsEmpty()){
            return Printer.nonequal(fa,fb) + " :: empty word";
        }
        else if(a.compare(b)){
            return Printer.equal(fa, fb);
        }
        else{
            return Printer.nonequal(fa,fb) + " :: " + Algorithms.findCounterExampleCfgs(a,b);
        }
    }

    public static String checkAmbiguity(Cfg g, String f){
        Pair<Boolean,String> p = g.checkAmbiguity();
        if(p.getA()){
            if(p.getB().equals("")) return Printer.ambiguous(f) + " :: empty word";
            return Printer.unambiguous(f) + " :: " + p.getB();
        }
        return Printer.unambiguous(f);
    }

    //COMPARE

    public static String equalDfaDfa(Dfa a, Dfa b, String fa, String fb) {
        if(a.minimize().compare(b.minimize())) return Printer.equal(fa, fb);
        return Printer.nonequal(fa,fb);
    }

    public static String equalDfaNfa(Dfa a, Nfa b, String fa, String fb){
        if(a.minimize().compare(b.toDfa().minimize())) return Printer.equal(fa, fb);
        return Printer.nonequal(fa,fb);
    }

    public static String equalDfaRegex(Dfa a, RegularExpression b, String fa, String fb){
        if(a.minimize().compare(b.toNfa().toDfa().minimize())) return Printer.equal(fa,fb);
        return Printer.nonequal(fa,fb);
    }

    public static String equalNfaNfa(Nfa a, Nfa b, String fa, String fb){
        if(a.toDfa().minimize().compare(b.toDfa().minimize())) return Printer.equal(fa,fb);
        return Printer.nonequal(fa,fb);
    }

    public static String equalNfaRegex(Nfa a, RegularExpression b, String fa, String fb){
        if(a.toDfa().minimize().compare(b.toNfa().toDfa().minimize())) return Printer.equal(fa,fb);
        return Printer.nonequal(fa,fb);
    }

    public static String equalRegexRegex(RegularExpression a, RegularExpression b, String fa, String fb){
        if(a.toNfa().toDfa().minimize().compare(b.toNfa().toDfa().minimize())) return Printer.equal(fa,fb);
        return Printer.nonequal(fa,fb);
    }

    //DFA transformations

    public static String minimizeDfa(Dfa a){
        return a.minimize().toString();
    }

    public static String minimize2Dfa(Dfa a){
        return Algorithms.minimize2(a).toString();
    }

    public static String reverseDfa(Dfa a){
        return Algorithms.reverse(a).toString();
    }

    public static String complementDfa(Dfa a){
        return Algorithms.complement(a).toString();
    }

    //TRANSFORM

    public static String transformDfaNfa(Dfa a){
        return a.toNfa().toString();
    }

    public static String transformDfaRegex(Dfa a){
        return a.toNfa().toGnfa().toRegex().simplify().toString();
    }

    public static String transformNfaDfa(Nfa a){
        return a.toDfa().toString();
    }

    public static String transformNfaRegex(Nfa a){
        return a.toGnfa().toRegex().simplify().toString();
    }

    public static String transformRegexDfaMinim(RegularExpression a){
        return a.toNfa().toDfa().minimize().toString();
    }

    public static String transformRegexDfaNotMinim(RegularExpression a){
        return a.toNfa().toDfa().toString();
    }

    public static String transformRegexNfaMinim(RegularExpression a){
        return a.toNfa().toDfa().minimize().toNfa().toString();
    }

    public static String transformRegexNfaNotMinim(RegularExpression a){
        return a.toNfa().toString();
    }

    public static String transformDfaCfg(Dfa a){
        return a.toNfa().toGnfa().toRegex().toCfg().simplify().toString();
    }

    public static String transformNfaCfg(Nfa a){
        return a.toGnfa().toRegex().toCfg().simplify().toString();
    }

    public static String transformRegexCfg(RegularExpression a){
        return a.toCfg().simplify().toString();
    }

    //CHECK WORDS

    public static String checkWordsDfa(Dfa a, List<String> words){
        List<String> out = new ArrayList<>();
        for(String word : words){
            if(!a.checkWord(word)) out.add(word);
        }
        return Printer.stringOfWords(out);
    }

    public static String checkWordsNfa(Nfa a, List<String> words){
        return checkWordsDfa(a.toDfa(), words);
    }

    public static String checkWordsRegex(RegularExpression a, List<String> words){
        return checkWordsDfa(a.toNfa().toDfa(), words);
    }

    public static String checkWordsCfg(Cfg g, List<String> words){
        Pda parser = g.toPda();
        List<String> out = new ArrayList<>();
        for(String word : words){
            if(!parser.checkWord(word)) out.add(word);
        }
        return Printer.stringOfWords(out);
    }

    //GENERATE WORDS

    public static String generateWordsDfa(Dfa a, int n){
        return generateWordsRegex(a.toNfa().toGnfa().toRegex(),n);
    }

    public static String generateWordsNfa(Nfa a, int n){
        return generateWordsRegex(a.toGnfa().toRegex(),n);
    }

    public static String generateWordsRegex(RegularExpression regex, int n){
        return Printer.stringOfWords(Algorithms.generateWords(regex,n));
    }

    public static String generateWordsCfg(Cfg g, int n){
        WordsGenerator wg = new WordsGenerator(g.simplify());
        return Printer.stringOfWords(wg.generateWordsStart(n));
    }

    //CFG transformations

    public static String simplifyGrammar(Cfg g){
        return g.simplify().toString();
    }

    public static String transformChomsky(Cfg g){
        return g.simplify().toChomsky().toString();
    }

    public static String transformGriebach(Cfg g){
        return g.simplify().toGriebach().toString();
    }
}