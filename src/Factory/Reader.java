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
        AutomatonData data = new AutomatonData();
        Printer.filename = f.getName();

        while (!data.hasBasic()) {
            String x = sc.next();
            switch (x) {
                case "states:"   -> data.setStates(sc.next());
                case "start:"    -> data.setStart(sc.next());
                case "final:"    -> data.setFinal(sc.nextLine().replace(" ", "").split(","));
                case "alphabet:" -> data.setAlphabet(sc.nextLine().replace(" ", "").split(","));
                default          -> {
                    Printer.automatonCheckBasic();
                    throw new AutomatonReaderException(Printer.exceptionMessage);
                }
            }
        }

        while (sc.hasNextLine()) {
            data.addRule(sc.next(), sc.next(), sc.next());
        }

        return data;
    }

    public static AutomatonData readAutomatonString(String s) throws AutomatonReaderException {
        Printer.filename = null;
        AutomatonData data = new AutomatonData();

        List<String> list = Arrays.stream(s.split("\n")).toList();
        int i = 0;
        while(!data.hasBasic()){
            if(i>=list.size()) {
                Printer.automatonCheckBasic();
                throw new AutomatonReaderException(Printer.exceptionMessage);
            }
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
                Printer.automatonCheckBasic();
                throw new AutomatonReaderException(Printer.exceptionMessage);
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
                if(rule.size()!=3){
                    Printer.automatonCheckRuleFormat(x);
                    throw new AutomatonReaderException(Printer.exceptionMessage);
                }
                data.addRule(rule.get(0), rule.get(1), rule.get(2));
            }
            i++;
        }
        return data;
    }

    public static RegularExpression readRegularExpressionFile(File f) throws FileNotFoundException, RegexReaderException {
        Printer.filename = f.getName();
        Scanner sc = new Scanner(f);
        StringBuilder r = new StringBuilder();
        while(sc.hasNextLine()) r.append(sc.nextLine());
        return RegexBuilder.buildRegex(r.toString().replace(" ", ""));
    }

    public static RegularExpression readRegularExpressionString(String s) throws RegexReaderException {
        Printer.filename = null;
        return RegexBuilder.buildRegex(s.replace(" ","").replace("\n",""));
    }

    public static List<String> readWordsFile(File f) throws FileNotFoundException {
        Scanner sc = new Scanner(f);
        List<String> list = new ArrayList<>();
        while(sc.hasNextLine()) list.add(sc.nextLine());
        return list;
    }

    public static List<String> readWordsString(String s) {
        List<String> out = new ArrayList<>(List.of(s.split("\n")));
        if(s.endsWith("\n")) out.add("");
        return out;
    }

    public static Cfg readGrammarFile(File f) throws FileNotFoundException, GrammarReaderException {
        Printer.filename = f.getName();
        Scanner sc = new Scanner(f);
        GrammarData data = new GrammarData();

        while(!data.hasBasic()){
            String x = sc.next();
            switch (x) {
                case "terminals:" -> data.setTerminals(sc.nextLine().replace(" ", "").split(","));
                case "variables:" -> data.setVariables(sc.nextLine().replace(" ", "").split(","));
                case "start:"     -> data.setStart(sc.nextLine().replace(" ", ""));
                default           -> {
                    Printer.grammarCheckBasic();
                    throw new GrammarReaderException(Printer.exceptionMessage);
                }
            }
        }

        while(sc.hasNextLine()){
            data.addRule(sc.nextLine().split(" "));
        }

        if(!data.check()) throw new GrammarReaderException(Printer.exceptionMessage);

        return data.getCfg();
    }

    public static Cfg readGrammarString(String s) throws GrammarReaderException{
        Printer.filename = null;
        GrammarData data = new GrammarData();
        List<String> list = Arrays.stream(s.split("\n")).toList();
        int i=0;
        while(!data.hasBasic()){
            if(i>=list.size()) {
                Printer.grammarCheckBasic();
                throw new GrammarReaderException(Printer.exceptionMessage);
            }
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
                Printer.grammarCheckBasic();
                throw new GrammarReaderException(Printer.exceptionMessage);
            }
            i++;
        }
        while(i<list.size()){
            String x = list.get(i);
            if(!x.isEmpty()) data.addRule(x.split(" "));
            i++;
        }

        if(!data.check()) throw new GrammarReaderException(Printer.exceptionMessage);
        return data.getCfg();
    }

}
