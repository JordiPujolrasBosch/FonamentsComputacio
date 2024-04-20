package Factory;

import Automatons.AutomatonData;
import ContextFreeGrammars.Cfg;
import ContextFreeGrammars.GrammarData;
import Exceptions.AutomatonReaderException;
import Exceptions.GrammarReaderException;
import Exceptions.RegexReaderException;
import RegularExpressions.Builder;
import RegularExpressions.RegularExpression;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Reader {

    public static AutomatonData readAutomatonFile(String filename) throws FileNotFoundException, AutomatonReaderException {
        Scanner sc = new Scanner(new File(filename));
        AutomatonData data = new AutomatonData(filename);

        try {
            while (!data.hasBasic()) {
                String x = sc.next();
                switch (x) {
                    case "states:"   -> data.setStates(sc.nextInt());
                    case "start:"    -> data.setStart(sc.nextInt());
                    case "final:"    -> data.setFinal(sc.nextLine().replace(" ", "").split(","));
                    case "alphabet:" -> data.setAlphabet(sc.nextLine().replace(" ", "").split(","));
                    default          -> throw new Exception();
                }
            }

            while (sc.hasNextLine()) {
                data.addRule(sc.nextInt(), sc.next(), sc.nextInt());
            }
        }
        catch (Exception ex){
            throw new AutomatonReaderException(OutputMessages.automatonCheck(filename));
        }

        return data;
    }

    public static RegularExpression readRegularExpressionFile(String filename) throws FileNotFoundException, RegexReaderException {
        Scanner sc = new Scanner(new File(filename));
        StringBuilder r = new StringBuilder();
        while(sc.hasNextLine()) r.append(sc.nextLine());
        return Builder.buildRegex(r.toString());
    }

    public static List<String> readWordsFile(String filename) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(filename));
        List<String> list = new ArrayList<>();
        while(sc.hasNextLine()) list.add(sc.nextLine());
        return list;
    }

    public static Cfg readGrammarFile(String filename) throws FileNotFoundException, GrammarReaderException {
        Scanner sc = new Scanner(new File(filename));
        GrammarData data = new GrammarData(filename);

        try{
            while(!data.hasBasic()){
                String x = sc.next();
                switch (x) {
                    case "terminals:" -> data.setTerminals(sc.nextLine().replace(" ", "").split(","));
                    case "variables:" -> data.setVariables(sc.nextLine().replace(" ", "").split(","));
                    case "start:"     -> data.setStart(sc.next());
                    default -> throw new Exception();
                }
            }

            while(sc.hasNextLine()){
                data.addRule(sc.nextLine().split(" "));
            }
        }
        catch (Exception ex){
            throw new GrammarReaderException(OutputMessages.grammarCheck(filename));
        }

        if(!data.check()) throw new GrammarReaderException(OutputMessages.grammarCheck(filename));

        return data.getCfg();
    }

}
