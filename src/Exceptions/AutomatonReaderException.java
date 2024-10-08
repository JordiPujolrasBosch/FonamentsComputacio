package Exceptions;

/**
 * Exception for an incorrect format automaton.
 */

public class AutomatonReaderException extends Exception {
    public AutomatonReaderException(){
        super();
    }

    public AutomatonReaderException(String s){
        super(s);
    }
}
