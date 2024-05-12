import Automatons.Dfa;
import Automatons.Pda;
import Factory.Algorithms;
import Factory.Printer;
import Factory.Reader;

import java.util.List;

public class Menu {

    //COMPARE

    public static void equalDfaDfa(String fa, String fb) {
        try {
            Dfa a = Reader.readAutomatonFile(fa).toDfa().minimize();
            Dfa b = Reader.readAutomatonFile(fb).toDfa().minimize();
            if (a.compare(b)) System.out.println(Printer.equal(fa,fb));
            else System.out.println(Printer.nonequal(fa,fb));
        }
        catch (Exception ex) {
            String m = ex.toString();
            System.out.println(ex.toString());
        }
    }

    public static void equalDfaNfa(String fa, String fb){
        try{
            Dfa a = Reader.readAutomatonFile(fa).toDfa().minimize();
            Dfa b = Reader.readAutomatonFile(fb).toNfa().toDfa().minimize();
            if (a.compare(b)) System.out.println(Printer.equal(fa,fb));
            else System.out.println(Printer.nonequal(fa,fb));
        }
        catch (Exception ex){
            System.out.println(ex.toString());
        }
    }

    public static void equalDfaRegex(String fa, String fb){
        try{
            Dfa a = Reader.readAutomatonFile(fa).toDfa().minimize();
            Dfa b = Reader.readRegularExpressionFile(fb).getNfa().toDfa().minimize();
            if (a.compare(b)) System.out.println(Printer.equal(fa,fb));
            else System.out.println(Printer.nonequal(fa,fb));
        }
        catch (Exception ex){
            System.out.println(ex.toString());
        }
    }

    public static void equalNfaNfa(String fa, String fb){
        try{
            Dfa a = Reader.readAutomatonFile(fa).toNfa().toDfa().minimize();
            Dfa b = Reader.readAutomatonFile(fb).toNfa().toDfa().minimize();
            if (a.compare(b)) System.out.println(Printer.equal(fa,fb));
            else System.out.println(Printer.nonequal(fa,fb));
        }
        catch (Exception ex){
            System.out.println(ex.toString());
        }
    }

    public static void equalNfaRegex(String fa, String fb){
        try{
            Dfa a = Reader.readAutomatonFile(fa).toNfa().toDfa().minimize();
            Dfa b = Reader.readRegularExpressionFile(fb).getNfa().toDfa().minimize();
            if (a.compare(b)) System.out.println(Printer.equal(fa,fb));
            else System.out.println(Printer.nonequal(fa,fb));
        }
        catch (Exception ex){
            System.out.println(ex.toString());
        }
    }

    public static void equalRegexRegex(String fa, String fb){
        try{
            Dfa a = Reader.readRegularExpressionFile(fa).getNfa().toDfa().minimize();
            Dfa b = Reader.readRegularExpressionFile(fb).getNfa().toDfa().minimize();
            if (a.compare(b)) System.out.println(Printer.equal(fa,fb));
            else System.out.println(Printer.nonequal(fa,fb));
        }
        catch (Exception ex){
            System.out.println(ex.toString());
        }
    }

    //DFA transformations

    public static void minimizeDfa(String f){
        try{
            System.out.println(Reader.readAutomatonFile(f).toDfa().minimize());
        }
        catch (Exception ex){
            System.out.println(ex.toString());
        }
    }

    public static void minimize2Dfa(String f){
        try{
            System.out.println(Algorithms.minimize2(Reader.readAutomatonFile(f).toDfa()));
        }
        catch (Exception ex){
            System.out.println(ex.toString());
        }
    }

    public static void reverseDfa(String f){
        try{
            System.out.println(Algorithms.reverse(Reader.readAutomatonFile(f).toDfa()));
        }
        catch (Exception ex){
            System.out.println(ex.toString());
        }
    }

    public static void complementDfa(String f){
        try{
            System.out.println(Algorithms.complement(Reader.readAutomatonFile(f).toDfa()));
        }
        catch (Exception ex){
            System.out.println(ex.toString());
        }
    }

    //TRANSFORM

    public static void transformDfaNfa(String f){
        try{
            System.out.println(Reader.readAutomatonFile(f).toDfa().toNfa());
        }
        catch (Exception ex){
            System.out.println(ex.toString());
        }
    }

    public static void transformDfaRegex(String f){
        try{
            System.out.println(Reader.readAutomatonFile(f).toDfa().toNfa().toGnfa().toRegex());
        }
        catch (Exception ex){
            System.out.println(ex.toString());
        }
    }

    public static void transformNfaDfa(String f){
        try{
            System.out.println(Reader.readAutomatonFile(f).toNfa().toDfa());
        }
        catch (Exception ex){
            System.out.println(ex.toString());
        }
    }

    public static void transformNfaRegex(String f){
        try{
            System.out.println(Reader.readAutomatonFile(f).toNfa().toGnfa().toRegex());
        }
        catch (Exception ex){
            System.out.println(ex.toString());
        }
    }

    public static void transformRegexDfaMinim(String f){
        try{
            System.out.println(Reader.readRegularExpressionFile(f).getNfa().toDfa().minimize());
        }
        catch (Exception ex){
            System.out.println(ex.toString());
        }
    }

    public static void transformRegexDfaNotMinim(String f){
        try{
            System.out.println(Reader.readRegularExpressionFile(f).getNfa().toDfa());
        }
        catch (Exception ex){
            System.out.println(ex.toString());
        }
    }

    public static void transformRegexNfaNotMinim(String f){
        try{
            System.out.println(Reader.readRegularExpressionFile(f).getNfa());
        }
        catch (Exception ex){
            System.out.println(ex.toString());
        }
    }

    public static void transformRegexNfaMinim(String f){
        try{
            System.out.println(Reader.readRegularExpressionFile(f).getNfa().toDfa().minimize().toNfa());
        }
        catch (Exception ex){
            System.out.println(ex.toString());
        }
    }

    //CHECK WORDS

    public static void checkWordsDfa(String f, String fw){
        try{
            Dfa dfa = Reader.readAutomatonFile(f).toDfa();
            List<String> list = Reader.readWordsFile(fw);
            for(String word : list){
                if(!dfa.checkWord(word)) System.out.println(word);
            }
        }
        catch (Exception ex){
            System.out.println(ex.toString());
        }
    }

    public static void checkWordsNfa(String f, String fw){
        try{
            Dfa dfa = Reader.readAutomatonFile(f).toNfa().toDfa();
            List<String> list = Reader.readWordsFile(fw);
            for(String word : list){
                if(!dfa.checkWord(word)) System.out.println(word);
            }
        }
        catch (Exception ex){
            System.out.println(ex.toString());
        }
    }

    public static void checkWordsRegex(String f, String fw){
        try{
            Dfa dfa = Reader.readRegularExpressionFile(f).getNfa().toDfa();
            List<String> list = Reader.readWordsFile(fw);
            for(String word : list){
                if(!dfa.checkWord(word)) System.out.println(word);
            }
        }
        catch (Exception ex){
            System.out.println(ex.toString());
        }
    }

    public static void checkWordsCfg(String f, String fw){
        try{
            Pda pda = Reader.readGrammarFile(f).toPda();
            List<String> list = Reader.readWordsFile(fw);
            for(String word : list){
                if(!pda.checkWord(word)) System.out.println(word);
            }
        }
        catch (Exception ex){
            System.out.println(ex.toString());
        }
    }

    //CFG transformations

    public static void transformChomsky(String f){
        try{
            System.out.println(Reader.readGrammarFile(f).simplify().toChomsky());
        }
        catch (Exception ex){
            System.out.println(ex.toString());
        }
    }
}
