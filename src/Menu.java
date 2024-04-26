import Automatons.Dfa;
import Exceptions.AutomatonReaderException;
import Factory.AutomatonFactory;
import Factory.OutputMessages;
import Factory.Reader;

import java.io.FileNotFoundException;
import java.util.List;

public class Menu {

    //COMPARE

    public static void equalDfaDfa(String fa, String fb) throws FileNotFoundException, AutomatonReaderException {
        try {
            Dfa a = Reader.readAutomatonFile(fa).toDfa().minimize();
            Dfa b = Reader.readAutomatonFile(fb).toDfa().minimize();
            if (a.equal(b)) System.out.println(OutputMessages.equal(fa,fb));
            else System.out.println(OutputMessages.nonequal(fa,fb));
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public static void equalDfaNfa(String fa, String fb){
        try{
            Dfa a = Reader.readAutomatonFile(fa).toDfa().minimize();
            Dfa b = Reader.readAutomatonFile(fb).toNfa().toDfa().minimize();
            if (a.equal(b)) System.out.println(OutputMessages.equal(fa,fb));
            else System.out.println(OutputMessages.nonequal(fa,fb));
        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }

    public static void equalDfaRegex(String fa, String fb){
        try{
            Dfa a = Reader.readAutomatonFile(fa).toDfa().minimize();
            Dfa b = Reader.readRegularExpressionFile(fb).getNfa().toDfa().minimize();
            if (a.equal(b)) System.out.println(OutputMessages.equal(fa,fb));
            else System.out.println(OutputMessages.nonequal(fa,fb));
        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }

    public static void equalNfaNfa(String fa, String fb){
        try{
            Dfa a = Reader.readAutomatonFile(fa).toNfa().toDfa().minimize();
            Dfa b = Reader.readAutomatonFile(fb).toNfa().toDfa().minimize();
            if (a.equal(b)) System.out.println(OutputMessages.equal(fa,fb));
            else System.out.println(OutputMessages.nonequal(fa,fb));
        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }

    public static void equalNfaRegex(String fa, String fb){
        try{
            Dfa a = Reader.readAutomatonFile(fa).toNfa().toDfa().minimize();
            Dfa b = Reader.readRegularExpressionFile(fb).getNfa().toDfa().minimize();
            if (a.equal(b)) System.out.println(OutputMessages.equal(fa,fb));
            else System.out.println(OutputMessages.nonequal(fa,fb));
        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }

    public static void equalRegexRegex(String fa, String fb){
        try{
            Dfa a = Reader.readRegularExpressionFile(fa).getNfa().toDfa().minimize();
            Dfa b = Reader.readRegularExpressionFile(fb).getNfa().toDfa().minimize();
            if (a.equal(b)) System.out.println(OutputMessages.equal(fa,fb));
            else System.out.println(OutputMessages.nonequal(fa,fb));
        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }

    //DFA transformations

    public static void minimizeDfa(String f){
        try{
            System.out.println(Reader.readAutomatonFile(f).toDfa().minimize());
        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }

    public static void reverseDfa(String f){
        try{
            System.out.println(AutomatonFactory.reverse(Reader.readAutomatonFile(f).toDfa()));
        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }

    public static void complementDfa(String f){
        try{
            System.out.println(AutomatonFactory.complement(Reader.readAutomatonFile(f).toDfa()));
        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }

    //TRANSFORM

    public static void transformDfaNfa(String f){
        try{
            System.out.println(Reader.readAutomatonFile(f).toDfa().toNfa());
        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }

    public static void transformDfaGnfa(String f){
        try{
            System.out.println(Reader.readAutomatonFile(f).toDfa().toNfa().toGnfa());
        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }

    public static void transformDfaRegex(String f){
        try{
            System.out.println(Reader.readAutomatonFile(f).toDfa().toNfa().toGnfa().toRegex());
        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }

    public static void transformNfaDfa(String f){
        try{
            System.out.println(Reader.readAutomatonFile(f).toNfa().toDfa());
        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }

    public static void transformNfaGnfa(String f){
        try{
            System.out.println(Reader.readAutomatonFile(f).toNfa().toGnfa());
        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }

    public static void transformNfaRegex(String f){
        try{
            System.out.println(Reader.readAutomatonFile(f).toNfa().toGnfa().toRegex());
        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }

    public static void transformRegexDfa(String f){
        try{
            System.out.println(Reader.readRegularExpressionFile(f).getNfa().toDfa().minimize());
        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }

    public static void transformRegexDfaNotMinim(String f){
        try{
            System.out.println(Reader.readRegularExpressionFile(f).getNfa().toDfa());
        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }

    public static void transformRegexNfaDirectly(String f){
        try{
            System.out.println(Reader.readRegularExpressionFile(f).getNfa());
        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }

    public static void transformRegexNfa(String f){
        try{
            System.out.println(Reader.readRegularExpressionFile(f).getNfa().toDfa().minimize().toNfa());
        }
        catch (Exception ex){
            System.out.println(ex);
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
            System.out.println(ex);
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
            System.out.println(ex);
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
            System.out.println(ex);
        }
    }
}
