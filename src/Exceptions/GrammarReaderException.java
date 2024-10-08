package Exceptions;

/**
 * Exception for an incorrect format grammar.
 */

public class GrammarReaderException extends Exception {
    public GrammarReaderException(){
        super();
    }

    public GrammarReaderException(String s){
        super(s);
    }
}
