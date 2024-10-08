package Exceptions;

/**
 * Exception for an incorrect format regular expression.
 */

public class RegexReaderException extends Exception {
    public RegexReaderException(){
        super();
    }

    public RegexReaderException(String s){
        super(s);
    }
}
