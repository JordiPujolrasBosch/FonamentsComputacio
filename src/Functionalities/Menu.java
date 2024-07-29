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

    //CONTEXT FREE LANGUAGES TESTS

    public static String equalCfgCfg(Cfg ga, Cfg gb, String fa, String fb){
        CfgNonEmpty a = ga.simplify().toGreibach();
        CfgNonEmpty b = gb.simplify().toGreibach();
        if(a.compare(b)) return Printer.equal(fa,fb);
        return Printer.nonequal(fa,fb);
    }

    public static String findCounterExampleCfg(Cfg ga, Cfg gb, String fa, String fb){
        CfgNonEmpty a = ga.simplify().toGreibach();
        CfgNonEmpty b = gb.simplify().toGreibach();
        if(a.acceptsEmpty() != b.acceptsEmpty()) return Printer.nonequalCounterexmaple(fa, fb, "");
        else if(a.compare(b)) return Printer.equal(fa, fb);
        else return Printer.nonequalCounterexmaple(fa,fb, Algorithms.findCounterExampleCfg(a,b));
    }

    public static String findManyCounterExamplesCfg(Cfg ga, Cfg gb){
        CfgNonEmpty a = ga.simplify().toGreibach();
        CfgNonEmpty b = gb.simplify().toGreibach();
        List<String> list = new ArrayList<>();
        if(a.compare(b)) return "";
        return Printer.stringOfWords(Algorithms.findManyCounterExamplesCfg2(a,b));
    }

    public static String checkAmbiguity(Cfg g, String f){
        Pair<Boolean,String> p = g.checkAmbiguity();
        if(p.getA()){
            if(p.getB().equals("")) return Printer.ambiguous(f) + " :: empty word";
            return Printer.ambiguous(f) + " :: " + p.getB();
        }
        return Printer.unambiguous(f);
    }

    //REGULAR LANGUAGES COMPARISONS

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

    public static String equalNfaDfa(Nfa a, Dfa b, String fa, String fb){
        if(a.toDfa().minimize().compare(b.minimize())) return Printer.equal(fa,fb);
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

    public static String equalRegexDfa(RegularExpression a, Dfa b, String fa, String fb){
        if(a.toNfa().toDfa().minimize().compare(b.minimize())) return Printer.equal(fa,fb);
        return Printer.nonequal(fa,fb);
    }

    public static String equalRegexNfa(RegularExpression a, Nfa b, String fa, String fb){
        if(a.toNfa().toDfa().minimize().compare(b.toDfa().minimize())) return Printer.equal(fa,fb);
        return Printer.nonequal(fa,fb);
    }

    public static String equalRegexRegex(RegularExpression a, RegularExpression b, String fa, String fb){
        if(a.toNfa().toDfa().minimize().compare(b.toNfa().toDfa().minimize())) return Printer.equal(fa,fb);
        return Printer.nonequal(fa,fb);
    }

    //REGULAR LANGUAGES TRANSFORMATIONS

    public static String transformDfaNfa(Dfa a){
        return a.toNfa().toString();
    }

    public static String transformDfaRegex(Dfa a){
        return a.toNfa().toGnfa().toRegex().simplify().toString();
    }

    public static String transformNfaDfa(Nfa a){
        return a.toDfa().toString();
    }

    public static String transformNfaDfaMinim(Nfa a){
        return a.toDfa().minimize().toString();
    }

    public static String transformNfaRegex(Nfa a){
        return a.toGnfa().toRegex().simplify().toString();
    }

    public static String transformRegexDfa(RegularExpression a){
        return a.toNfa().toDfa().toString();
    }

    public static String transformRegexDfaMinim(RegularExpression a){
        return a.toNfa().toDfa().minimize().toString();
    }

    public static String transformRegexNfa(RegularExpression a){
        return a.toNfa().toString();
    }

    public static String transformRegexNfaMinim(RegularExpression a){
        return a.toNfa().toDfa().minimize().toNfa().toString();
    }

    public static String transformDfaCfg(Dfa a){
        return a.minimize().toNfa().toGnfa().toRegex().toCfg().simplify().toString();
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
            if(a.checkWord(word)) out.add(word);
        }
        if(out.size() == words.size()) return Printer.acceptsAllWords();
        return Printer.notAcceptAllWords(out.size(), words.size());
    }

    public static String checkWordsDfaYes(Dfa a, List<String> words){
        List<String> out = new ArrayList<>();
        for(String word : words){
            if(a.checkWord(word)) out.add(word);
        }
        return Printer.stringOfWords(out);
    }

    public static String checkWordsDfaNo(Dfa a, List<String> words){
        List<String> out = new ArrayList<>();
        for(String word : words){
            if(!a.checkWord(word)) out.add(word);
        }
        return Printer.stringOfWords(out);
    }

    public static String checkWordsNfa(Nfa a, List<String> words){
        return checkWordsDfa(a.toDfa(), words);
    }

    public static String checkWordsNfaYes(Nfa a, List<String> words){
        return checkWordsDfaYes(a.toDfa(), words);
    }

    public static String checkWordsNfaNo(Nfa a, List<String> words){
        return checkWordsDfaNo(a.toDfa(), words);
    }

    public static String checkWordsRegex(RegularExpression a, List<String> words){
        return checkWordsDfa(a.toNfa().toDfa(), words);
    }

    public static String checkWordsRegexYes(RegularExpression a, List<String> words){
        return checkWordsDfaYes(a.toNfa().toDfa(), words);
    }

    public static String checkWordsRegexNo(RegularExpression a, List<String> words){
        return checkWordsDfaNo(a.toNfa().toDfa(), words);
    }

    public static String checkWordsCfg(Cfg g, List<String> words){
        Pda parser = g.toPda();
        List<String> out = new ArrayList<>();
        for(String word : words){
            if(parser.checkWord(word)) out.add(word);
        }
        if(out.size() == words.size()) return Printer.acceptsAllWords();
        return Printer.notAcceptAllWords(out.size(), words.size());
    }

    public static String checkWordsCfgYes(Cfg g, List<String> words){
        Pda parser = g.toPda();
        List<String> out = new ArrayList<>();
        for(String word : words){
            if(parser.checkWord(word)) out.add(word);
        }
        return Printer.stringOfWords(out);
    }

    public static String checkWordsCfgNo(Cfg g, List<String> words){
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

    //DFA TRANSFORMATIONS

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

    //CFG TRANSFORMATIONS

    public static String simplifyCfg(Cfg g){
        return g.simplify().toString();
    }

    public static String chomskyCfg(Cfg g){
        return g.simplify().toChomsky().toString();
    }

    public static String greibachCfg(Cfg g){
        return g.simplify().toGreibach().toString();
    }
}
