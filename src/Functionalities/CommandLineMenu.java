package Functionalities;

import Automatons.Dfa;
import Automatons.Nfa;
import Factory.Printer;
import Factory.Reader;
import Grammars.Cfg;
import RegularExpressions.RegularExpression;
import Utils.Utility;

import java.io.File;
import java.util.List;

public class CommandLineMenu {

    //CONTEXT FREE LANGUAGES TESTS

    public static void equalCfgCfgArticle(String fa, String fb){
        try{
            File filea = new File(fa);
            File fileb = new File(fb);
            Cfg a = Reader.readGrammarFile(filea);
            Cfg b = Reader.readGrammarFile(fileb);
            System.out.println(Menu.equalCfgCfg(a, b, filea.getName(), fileb.getName()));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void findCounterExampleCfg(String fa, String fb){
        try{
            File filea = new File(fa);
            File fileb = new File(fb);
            Cfg a = Reader.readGrammarFile(filea);
            Cfg b = Reader.readGrammarFile(fileb);
            System.out.println(Menu.findCounterExampleCfg(a, b, filea.getName(), fileb.getName(), 10));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    /*public static void findManyCounterExamplesCfg(String fa, String fb){
        try{
            Cfg a = Reader.readGrammarFile(new File(fa));
            Cfg b = Reader.readGrammarFile(new File(fb));
            System.out.println(Menu.findManyCounterExamplesCfg(a, b));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }*/

    public static void findCounterExampleCfgLength(String fa, String fb, String l){
        try{
            if(!Utility.isNumber(l)) throw new Exception(Printer.lengthOutOfRange());
            if(Integer.parseInt(l) < 0 || Integer.parseInt(l) > 50) throw new Exception(Printer.lengthOutOfRange());
            File filea = new File(fa);
            File fileb = new File(fb);
            Cfg a = Reader.readGrammarFile(filea);
            Cfg b = Reader.readGrammarFile(fileb);
            System.out.println(Menu.findCounterExampleCfg(a, b, filea.getName(), fileb.getName(), Integer.parseInt(l)));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void checkAmbiguity(String f){
        try{
            File file = new File(f);
            Cfg g = Reader.readGrammarFile(file);
            System.out.println(Menu.checkAmbiguity(g, file.getName(), 10));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void checkAmbiguityLength(String f, String l){
        try{
            if(!Utility.isNumber(l)) throw new Exception(Printer.lengthOutOfRange());
            if(Integer.parseInt(l) < 0 || Integer.parseInt(l) > 50) throw new Exception(Printer.lengthOutOfRange());
            File file = new File(f);
            Cfg g = Reader.readGrammarFile(file);
            System.out.println(Menu.checkAmbiguity(g, file.getName(), Integer.parseInt(l)));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    //REGULAR LANGUAGES COMPARISONS

    public static void equalDfaDfa(String fa, String fb) {
        try{
            File filea = new File(fa);
            File fileb = new File(fb);
            Dfa a = Reader.readAutomatonFile(filea).toDfa();
            Dfa b = Reader.readAutomatonFile(fileb).toDfa();
            System.out.println(Menu.equalDfaDfa(a, b, filea.getName(), fileb.getName()));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void equalDfaNfa(String fa, String fb){
        try{
            File filea = new File(fa);
            File fileb = new File(fb);
            Dfa a = Reader.readAutomatonFile(filea).toDfa();
            Nfa b = Reader.readAutomatonFile(fileb).toNfa();
            System.out.println(Menu.equalDfaNfa(a, b, filea.getName(), fileb.getName()));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void equalDfaRegex(String fa, String fb){
        try{
            File filea = new File(fa);
            File fileb = new File(fb);
            Dfa a = Reader.readAutomatonFile(filea).toDfa();
            RegularExpression b = Reader.readRegularExpressionFile(fileb);
            System.out.println(Menu.equalDfaRegex(a, b, filea.getName(), fileb.getName()));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void equalNfaDfa(String fa, String fb){
        try{
            File filea = new File(fa);
            File fileb = new File(fb);
            Nfa a = Reader.readAutomatonFile(filea).toNfa();
            Dfa b = Reader.readAutomatonFile(fileb).toDfa();
            System.out.println(Menu.equalNfaDfa(a, b, filea.getName(), fileb.getName()));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void equalNfaNfa(String fa, String fb){
        try{
            File filea = new File(fa);
            File fileb = new File(fb);
            Nfa a = Reader.readAutomatonFile(filea).toNfa();
            Nfa b = Reader.readAutomatonFile(fileb).toNfa();
            System.out.println(Menu.equalNfaNfa(a, b, filea.getName(), fileb.getName()));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void equalNfaRegex(String fa, String fb){
        try{
            File filea = new File(fa);
            File fileb = new File(fb);
            Nfa a = Reader.readAutomatonFile(filea).toNfa();
            RegularExpression b = Reader.readRegularExpressionFile(fileb);
            System.out.println(Menu.equalNfaRegex(a, b, filea.getName(), fileb.getName()));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void equalRegexDfa(String fa, String fb){
        try{
            File filea = new File(fa);
            File fileb = new File(fb);
            RegularExpression a = Reader.readRegularExpressionFile(filea);
            Dfa b = Reader.readAutomatonFile(fileb).toDfa();
            System.out.println(Menu.equalRegexDfa(a, b, filea.getName(), fileb.getName()));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void equalRegexNfa(String fa, String fb){
        try{
            File filea = new File(fa);
            File fileb = new File(fb);
            RegularExpression a = Reader.readRegularExpressionFile(filea);
            Nfa b = Reader.readAutomatonFile(fileb).toNfa();
            System.out.println(Menu.equalRegexNfa(a, b, filea.getName(), fileb.getName()));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void equalRegexRegex(String fa, String fb){
        try{
            File filea = new File(fa);
            File fileb = new File(fb);
            RegularExpression a = Reader.readRegularExpressionFile(filea);
            RegularExpression b = Reader.readRegularExpressionFile(fileb);
            System.out.println(Menu.equalRegexRegex(a, b, filea.getName(), fileb.getName()));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    //REGULAR LANGUAGES TRANSFORMATIONS

    public static void transformDfaNfa(String f){
        try{
            System.out.println(Menu.transformDfaNfa(Reader.readAutomatonFile(new File(f)).toDfa()));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void transformDfaRegex(String f){
        try{
            System.out.println(Menu.transformDfaRegex(Reader.readAutomatonFile(new File(f)).toDfa()));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void transformNfaDfa(String f){
        try{
            System.out.println(Menu.transformNfaDfa(Reader.readAutomatonFile(new File(f)).toNfa()));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void transformNfaDfaMinim(String f){
        try{
            System.out.println(Menu.transformNfaDfaMinim(Reader.readAutomatonFile(new File(f)).toNfa()));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void transformNfaRegex(String f){
        try{
            System.out.println(Menu.transformNfaRegex(Reader.readAutomatonFile(new File(f)).toNfa()));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void transformRegexDfa(String f){
        try{
            System.out.println(Menu.transformRegexDfa(Reader.readRegularExpressionFile(new File(f))));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void transformRegexDfaMinim(String f){
        try{
            System.out.println(Menu.transformRegexDfaMinim(Reader.readRegularExpressionFile(new File(f))));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void transformRegexNfa(String f){
        try{
            System.out.println(Menu.transformRegexNfa(Reader.readRegularExpressionFile(new File(f))));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void transformRegexNfaMinim(String f){
        try{
            System.out.println(Menu.transformRegexNfaMinim(Reader.readRegularExpressionFile(new File(f))));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void transformDfaCfg(String f){
        try{
            System.out.println(Menu.transformDfaCfg(Reader.readAutomatonFile(new File(f)).toDfa()));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void transformNfaCfg(String f){
        try{
            System.out.println(Menu.transformNfaCfg(Reader.readAutomatonFile(new File(f)).toNfa()));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void transformRegexCfg(String f){
        try{
            System.out.println(Menu.transformRegexCfg(Reader.readRegularExpressionFile(new File(f))));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    //CHECK WORDS

    public static void checkWordsDfa(String f, String fw){
        try{
            Dfa dfa = Reader.readAutomatonFile(new File(f)).toDfa();
            List<String> list = Reader.readWordsFile(new File(fw));
            System.out.println(Menu.checkWordsDfa(dfa, list));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void checkWordsDfaYes(String f, String fw){
        try{
            Dfa dfa = Reader.readAutomatonFile(new File(f)).toDfa();
            List<String> list = Reader.readWordsFile(new File(fw));
            System.out.println(Menu.checkWordsDfaYes(dfa, list));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void checkWordsDfaNo(String f, String fw){
        try{
            Dfa dfa = Reader.readAutomatonFile(new File(f)).toDfa();
            List<String> list = Reader.readWordsFile(new File(fw));
            System.out.println(Menu.checkWordsDfaNo(dfa, list));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void checkWordsNfa(String f, String fw){
        try{
            Nfa nfa = Reader.readAutomatonFile(new File(f)).toNfa();
            List<String> list = Reader.readWordsFile(new File(fw));
            System.out.println(Menu.checkWordsNfa(nfa, list));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void checkWordsNfaYes(String f, String fw){
        try{
            Nfa nfa = Reader.readAutomatonFile(new File(f)).toNfa();
            List<String> list = Reader.readWordsFile(new File(fw));
            System.out.println(Menu.checkWordsNfaYes(nfa, list));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void checkWordsNfaNo(String f, String fw){
        try{
            Nfa nfa = Reader.readAutomatonFile(new File(f)).toNfa();
            List<String> list = Reader.readWordsFile(new File(fw));
            System.out.println(Menu.checkWordsNfaNo(nfa, list));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void checkWordsRegex(String f, String fw){
        try{
            RegularExpression regex = Reader.readRegularExpressionFile(new File(f));
            List<String> list = Reader.readWordsFile(new File(fw));
            System.out.println(Menu.checkWordsRegex(regex, list));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void checkWordsRegexYes(String f, String fw){
        try{
            RegularExpression regex = Reader.readRegularExpressionFile(new File(f));
            List<String> list = Reader.readWordsFile(new File(fw));
            System.out.println(Menu.checkWordsRegexYes(regex, list));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void checkWordsRegexNo(String f, String fw){
        try{
            RegularExpression regex = Reader.readRegularExpressionFile(new File(f));
            List<String> list = Reader.readWordsFile(new File(fw));
            System.out.println(Menu.checkWordsRegexNo(regex, list));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void checkWordsCfg(String f, String fw){
        try{
            Cfg g = Reader.readGrammarFile(new File(f));
            List<String> list = Reader.readWordsFile(new File(fw));
            System.out.println(Menu.checkWordsCfg(g, list));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void checkWordsCfgYes(String f, String fw){
        try{
            Cfg g = Reader.readGrammarFile(new File(f));
            List<String> list = Reader.readWordsFile(new File(fw));
            System.out.println(Menu.checkWordsCfgYes(g, list));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void checkWordsCfgNo(String f, String fw){
        try{
            Cfg g = Reader.readGrammarFile(new File(f));
            List<String> list = Reader.readWordsFile(new File(fw));
            System.out.println(Menu.checkWordsCfgNo(g, list));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    //GENERATE WORDS

    public static void generateWordsDfa(String f){
        try{
            System.out.println(Menu.generateWordsDfa(Reader.readAutomatonFile(new File(f)).toDfa(),100));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void generateWordsDfa(String f, String n){
        try{
            if(!Utility.isNumber(n)) throw new Exception(Printer.sizeOutOfRange());
            if(Integer.parseInt(n) < 0 || Integer.parseInt(n) > 500) throw new Exception(Printer.sizeOutOfRange());
            System.out.println(Menu.generateWordsDfa(Reader.readAutomatonFile(new File(f)).toDfa(),Integer.parseInt(n)));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void generateWordsNfa(String f){
        try{
            System.out.println(Menu.generateWordsNfa(Reader.readAutomatonFile(new File(f)).toNfa(),100));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void generateWordsNfa(String f, String n){
        try{
            if(!Utility.isNumber(n)) throw new Exception(Printer.sizeOutOfRange());
            if(Integer.parseInt(n) < 0 || Integer.parseInt(n) > 500) throw new Exception(Printer.sizeOutOfRange());
            System.out.println(Menu.generateWordsNfa(Reader.readAutomatonFile(new File(f)).toNfa(),Integer.parseInt(n)));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void generateWordsRegex(String f){
        try{
            System.out.println(Menu.generateWordsRegex(Reader.readRegularExpressionFile(new File(f)),100));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void generateWordsRegex(String f, String n){
        try{
            if(!Utility.isNumber(n)) throw new Exception(Printer.sizeOutOfRange());
            if(Integer.parseInt(n) < 0 || Integer.parseInt(n) > 500) throw new Exception(Printer.sizeOutOfRange());
            System.out.println(Menu.generateWordsRegex(Reader.readRegularExpressionFile(new File(f)),Integer.parseInt(n)));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void generateWordsCfg(String f){
        try{
            System.out.println(Menu.generateWordsCfg(Reader.readGrammarFile(new File(f)),100));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void generateWordsCfg(String f, String n){
        try{
            if(!Utility.isNumber(n)) throw new Exception(Printer.sizeOutOfRange());
            if(Integer.parseInt(n) < 0 || Integer.parseInt(n) > 500) throw new Exception(Printer.sizeOutOfRange());
            System.out.println(Menu.generateWordsCfg(Reader.readGrammarFile(new File(f)),Integer.parseInt(n)));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    //DFA TRANSFORMATIONS

    public static void minimizeDfa(String f){
        try{
            System.out.println(Menu.minimizeDfa(Reader.readAutomatonFile(new File(f)).toDfa()));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void reverseDfa(String f){
        try{
            System.out.println(Menu.reverseDfa(Reader.readAutomatonFile(new File(f)).toDfa()));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void complementDfa(String f){
        try{
            System.out.println(Menu.complementDfa(Reader.readAutomatonFile(new File(f)).toDfa()));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    //CFG TRANSFORMATIONS

    public static void simplifyCfg(String f){
        try{
            System.out.println(Menu.simplifyCfg(Reader.readGrammarFile(new File(f))));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void chomskyCfg(String f){
        try{
            System.out.println(Menu.chomskyCfg(Reader.readGrammarFile(new File(f))));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void greibachCfg(String f){
        try{
            System.out.println(Menu.greibachCfg(Reader.readGrammarFile(new File(f))));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}
