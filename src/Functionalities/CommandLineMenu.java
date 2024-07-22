package Functionalities;

import Automatons.Dfa;
import Automatons.Nfa;
import Factory.Reader;
import Functionalities.Menu;
import Grammars.Cfg;
import RegularExpressions.RegularExpression;

import java.io.File;
import java.util.List;

public class CommandLineMenu {

    //ARTICLE

    public static void equalCfgCfgArticle(String fa, String fb){
        try{
            File filea = new File(fa);
            File fileb = new File(fb);
            Cfg a = Reader.readGrammarFile(filea);
            Cfg b = Reader.readGrammarFile(fileb);
            System.out.println(Menu.equalCfgCfgArticle(a,b,filea.getName(), fileb.getName()));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void findCounterExampleCfgs(String fa, String fb){
        try{
            File filea = new File(fa);
            File fileb = new File(fb);
            Cfg a = Reader.readGrammarFile(filea);
            Cfg b = Reader.readGrammarFile(fileb);
            System.out.println(Menu.findCounterExampleCfgs(a,b,filea.getName(), fileb.getName()));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void checkAmbiguity(String f){
        try{
            File file = new File(f);
            Cfg g = Reader.readGrammarFile(file);
            System.out.println(Menu.checkAmbiguity(g, file.getName()));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    //COMPARE

    public static void equalDfaDfa(String fa, String fb) {
        try{
            File filea = new File(fa);
            File fileb = new File(fb);
            Dfa a = Reader.readAutomatonFile(filea).toDfa();
            Dfa b = Reader.readAutomatonFile(fileb).toDfa();
            System.out.println(Menu.equalDfaDfa(a,b,filea.getName(), fileb.getName()));
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
            System.out.println(Menu.equalDfaNfa(a,b,filea.getName(), fileb.getName()));
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
            System.out.println(Menu.equalDfaRegex(a,b,filea.getName(), fileb.getName()));
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
            System.out.println(Menu.equalNfaNfa(a,b,filea.getName(), fileb.getName()));
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
            System.out.println(Menu.equalNfaRegex(a,b,filea.getName(),fileb.getName()));
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
            System.out.println(Menu.equalRegexRegex(a,b,filea.getName(),fileb.getName()));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    //DFA transformations

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

    //TRANSFORM

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

    public static void transformNfaRegex(String f){
        try{
            System.out.println(Menu.transformNfaRegex(Reader.readAutomatonFile(new File(f)).toNfa()));
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

    public static void transformRegexDfaNotMinim(String f){
        try{
            System.out.println(Menu.transformRegexDfaNotMinim(Reader.readRegularExpressionFile(new File(f))));
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

    public static void transformRegexNfaNotMinim(String f){
        try{
            System.out.println(Menu.transformRegexNfaNotMinim(Reader.readRegularExpressionFile(new File(f))));
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

    //GENERATE WORDS

    public static void generateWordsDfa(String f){
        try{
            System.out.println(Menu.generateWordsDfa(Reader.readAutomatonFile(new File(f)).toDfa(),100));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void generateWordsDfa(String f, int n){
        try{
            System.out.println(Menu.generateWordsDfa(Reader.readAutomatonFile(new File(f)).toDfa(),n));
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

    public static void generateWordsNfa(String f, int n){
        try{
            System.out.println(Menu.generateWordsNfa(Reader.readAutomatonFile(new File(f)).toNfa(),n));
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

    public static void generateWordsRegex(String f, int n){
        try{
            System.out.println(Menu.generateWordsRegex(Reader.readRegularExpressionFile(new File(f)),n));
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

    public static void generateWordsCfg(String f, int n){
        try{
            System.out.println(Menu.generateWordsCfg(Reader.readGrammarFile(new File(f)),n));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    //CFG transformations

    public static void simplifyGrammar(String f){
        try{
            System.out.println(Menu.simplifyGrammar(Reader.readGrammarFile(new File(f))));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void transformChomsky(String f){
        try{
            System.out.println(Menu.transformChomsky(Reader.readGrammarFile(new File(f))));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void transformGriebach(String f){
        try{
            System.out.println(Menu.transformGriebach(Reader.readGrammarFile(new File(f))));
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}
