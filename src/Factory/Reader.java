package Factory;

import Grammars.Cfg;
import Exceptions.*;
import Factory.Builders.*;
import RegularExpressions.RegularExpression;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Reader {

    public static AutomatonData readAutomatonFile(File f) throws FileNotFoundException, AutomatonReaderException {
        Scanner sc = new Scanner(f);
        AutomatonData data = new AutomatonData(f.getName());

        while (!data.hasBasic()) {
            String x = sc.next();
            switch (x) {
                case "states:"   -> data.setStates(sc.next());
                case "start:"    -> data.setStart(sc.next());
                case "final:"    -> data.setFinal(sc.nextLine().replace(" ", "").split(","));
                case "alphabet:" -> data.setAlphabet(sc.nextLine().replace(" ", "").split(","));
                default          -> throw new AutomatonReaderException(Printer.automatonCheck(f.getName()));
            }
        }

        while (sc.hasNextLine()) {
            data.addRule(sc.next(), sc.next(), sc.next());
        }

        return data;
    }

    public static AutomatonData readAutomatonString(String s) throws AutomatonReaderException {
        AutomatonReaderException ex = new AutomatonReaderException(Printer.automatonCheck(null));
        AutomatonData data = new AutomatonData();
        List<String> list = Arrays.stream(s.split("\n")).toList();
        int i = 0;
        while(!data.hasBasic()){
            if(i>=list.size()) throw ex;
            String x = list.get(i);
            if(x.startsWith("states:")){
                x = x.replace("states:","").replace(" ","");
                data.setStates(x);
            }
            else if(x.startsWith("start:")){
                x = x.replace("start:","").replace(" ","");
                data.setStart(x);
            }
            else if(x.startsWith("final:")){
                x = x.replace("final:","").replace(" ","");
                data.setFinal(x.split(","));
            }
            else if(x.startsWith("alphabet:")){
                x = x.replace("alphabet:", "").replace(" ", "");
                data.setAlphabet(x.split(","));
            }
            else if(!x.equals("")){
                throw ex;
            }
            i++;
        }
        while(i<list.size()){
            String x = list.get(i);
            if(!x.isEmpty()){
                List<String> rule = new ArrayList<>();
                for(String r : Arrays.stream(x.split(" ")).toList()){
                    if(!r.isEmpty()) rule.add(r);
                }
                if(rule.size()!=3) throw ex;
                data.addRule(rule.get(0), rule.get(1), rule.get(2));
            }
            i++;
        }
        return data;
    }

    public static RegularExpression readRegularExpressionFile(File f) throws FileNotFoundException, RegexReaderException {
        Scanner sc = new Scanner(f);
        StringBuilder r = new StringBuilder();
        while(sc.hasNextLine()) r.append(sc.nextLine());
        return RegexBuilder.buildRegex(r.toString().replace(" ", ""), f.getName());
    }

    public static RegularExpression readRegularExpressionString(String s) throws RegexReaderException {
        return RegexBuilder.buildRegex(s.replace(" ","").replace("\n",""),null);
    }

    public static List<String> readWordsFile(File f) throws FileNotFoundException {
        Scanner sc = new Scanner(f);
        List<String> list = new ArrayList<>();
        while(sc.hasNextLine()) list.add(sc.nextLine());
        return list;
    }

    public static List<String> readWordsString(String s) {
        return Arrays.stream(s.split("\n")).toList();
    }

    public static Cfg readGrammarFile(File f) throws FileNotFoundException, GrammarReaderException {
        Scanner sc = new Scanner(f);
        GrammarData data = new GrammarData();

        while(!data.hasBasic()){
            String x = sc.next();
            switch (x) {
                case "terminals:" -> data.setTerminals(sc.nextLine().replace(" ", "").split(","));
                case "variables:" -> data.setVariables(sc.nextLine().replace(" ", "").split(","));
                case "start:"     -> data.setStart(sc.nextLine().replace(" ", ""));
                default           -> throw new GrammarReaderException(Printer.grammarCheck(f.getName()));
            }
        }

        while(sc.hasNextLine()){
            data.addRule(sc.nextLine().split(" "));
        }

        if(!data.check()) throw new GrammarReaderException(Printer.grammarCheck(f.getName()));

        return data.getCfg();
    }

    public static Cfg readGrammarString(String s) throws GrammarReaderException{
        GrammarReaderException ex = new GrammarReaderException(Printer.grammarCheck(null));
        GrammarData data = new GrammarData();
        List<String> list = Arrays.stream(s.split("\n")).toList();
        int i=0;
        while(!data.hasBasic()){
            if(i>=list.size()) throw ex;
            String x = list.get(i);
            if(x.startsWith("terminals:")){
                x = x.replace("terminals:","").replace(" ","");
                data.setTerminals(x.split(","));
            }
            else if(x.startsWith("variables:")){
                x = x.replace("variables:","").replace(" ","");
                data.setVariables(x.split(","));
            }
            else if(x.startsWith("start:")){
                x = x.replace("start:","").replace(" ","");
                data.setStart(x);
            }
            else if(!x.isEmpty()){
                throw ex;
            }
            i++;
        }
        while(i<list.size()){
            String x = list.get(i);
            if(!x.isEmpty()) data.addRule(x.split(" "));
            i++;
        }

        if(!data.check()) throw ex;
        return data.getCfg();
    }

}
