import Automatons.Dfa;
import Factory.AutomatonFactory;
import Factory.OutputMessages;
import Factory.Reader;

public class Menu {

    /*
    dfa == dfa
    ====
    CHECK WORD: dfa, nfa, regex
    */

    //COMPARE

    public void equalDfaDfa(String fa, String fb) {
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

    public void equalDfaNfa(String fa, String fb){
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

    public void equalDfaRegex(String fa, String fb){
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

    public void equalNfaNfa(String fa, String fb){
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

    public void equalNfaRegex(String fa, String fb){
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

    public void equalRegexRegex(String fa, String fb){
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

    public void minimizeDfa(String f){
        try{
            System.out.println(Reader.readAutomatonFile(f).toDfa().minimize());
        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }

    public void reverseDfa(String f){
        try{
            System.out.println(AutomatonFactory.reverse(Reader.readAutomatonFile(f).toDfa()));
        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }

    public void complementDfa(String f){
        try{
            System.out.println(AutomatonFactory.complement(Reader.readAutomatonFile(f).toDfa()));
        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }

    //TRANSFORM

    public void transformDfaNfa(String f){
        try{
            System.out.println(Reader.readAutomatonFile(f).toDfa().toNfa());
        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }

    public void transformDfaGnfa(String f){
        try{
            System.out.println(Reader.readAutomatonFile(f).toDfa().toNfa().toGnfa());
        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }

    public void transformDfaRegex(String f){
        try{
            System.out.println(Reader.readAutomatonFile(f).toDfa().toNfa().toGnfa().toRegex());
        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }

    public void transformNfaDfa(String f){
        try{
            System.out.println(Reader.readAutomatonFile(f).toNfa().toDfa());
        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }

    public void transformNfaGnfa(String f){
        try{
            System.out.println(Reader.readAutomatonFile(f).toNfa().toGnfa());
        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }

    public void transformNfaRegex(String f){
        try{
            System.out.println(Reader.readAutomatonFile(f).toNfa().toGnfa().toRegex());
        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }

    public void transformRegexDfa(String f){
        try{
            System.out.println(Reader.readRegularExpressionFile(f).getNfa().toDfa().minimize());
        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }

    public void transformRegexNfa(String f){
        try{
            System.out.println(Reader.readRegularExpressionFile(f).getNfa().toDfa().minimize().toNfa());
        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }

    //CHECK WORDS

    public void checkWordDfa(String f, String fw){

    }
}
