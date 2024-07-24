package Functionalities;

import Factory.Printer;

public class RunCommandLine {
    public static void run(String[] args){
        if(equalCfgCfg(args))                      CommandLineMenu.equalCfgCfgArticle(args[3], args[4]);
        else if(findCounterExampleCfg(args))       CommandLineMenu.findCounterExampleCfg(args[1],args[2]);
        else if(findManyCounterExamplesCfg1(args)) CommandLineMenu.findManyCounterExamplesCfg(args[1],args[2]);
        else if(findManyCounterExamplesCfg2(args)) CommandLineMenu.findManyCounterExamplesCfg(args[2],args[3]);
        else if(checkAmbiguity(args))              CommandLineMenu.checkAmbiguity(args[1]);

        else if(equalDfaDfa(args))     CommandLineMenu.equalDfaDfa(args[3],args[4]);
        else if(equalDfaNfa(args))     CommandLineMenu.equalDfaNfa(args[3],args[4]);
        else if(equalDfaRegex(args))   CommandLineMenu.equalDfaRegex(args[3],args[4]);
        else if(equalNfaDfa(args))     CommandLineMenu.equalNfaDfa(args[3],args[4]);
        else if(equalNfaNfa(args))     CommandLineMenu.equalNfaNfa(args[3],args[4]);
        else if(equalNfaRegex(args))   CommandLineMenu.equalNfaRegex(args[3],args[4]);
        else if(equalRegexDfa(args))   CommandLineMenu.equalRegexDfa(args[3],args[4]);
        else if(equalRegexNfa(args))   CommandLineMenu.equalRegexNfa(args[3],args[4]);
        else if(equalRegexRegex(args)) CommandLineMenu.equalRegexRegex(args[3],args[4]);

        else if(transformDfaNfa(args))         CommandLineMenu.transformDfaNfa(args[3]);
        else if(transformDfaRegex(args))       CommandLineMenu.transformDfaRegex(args[3]);
        else if(transformNfaDfa(args))         CommandLineMenu.transformNfaDfa(args[3]);
        else if(transformNfaDfaMinim1(args))   CommandLineMenu.transformNfaDfaMinim(args[3]);
        else if(transformNfaDfaMinim2(args))   CommandLineMenu.transformNfaDfaMinim(args[4]);
        else if(transformNfaRegex(args))       CommandLineMenu.transformNfaRegex(args[3]);
        else if(transformRegexDfa(args))       CommandLineMenu.transformRegexDfa(args[3]);
        else if(transformRegexDfaMinim1(args)) CommandLineMenu.transformRegexDfaMinim(args[3]);
        else if(transformRegexDfaMinim2(args)) CommandLineMenu.transformRegexDfaMinim(args[4]);
        else if(transformRegexNfa(args))       CommandLineMenu.transformRegexNfa(args[3]);
        else if(transformRegexNfaMinim1(args)) CommandLineMenu.transformRegexNfaMinim(args[3]);
        else if(transformRegexNfaMinim2(args)) CommandLineMenu.transformRegexNfaMinim(args[4]);
        else if(transformDfaCfg(args))         CommandLineMenu.transformDfaCfg(args[3]);
        else if(transformNfaCfg(args))         CommandLineMenu.transformNfaCfg(args[3]);
        else if(transformRegexCfg(args))       CommandLineMenu.transformRegexCfg(args[3]);

        else if(checkWordsDfa(args))       CommandLineMenu.checkWordsDfa(args[2],args[3]);
        else if(checkWordsDfaYes1(args))   CommandLineMenu.checkWordsDfaYes(args[2],args[3]);
        else if(checkWordsDfaYes2(args))   CommandLineMenu.checkWordsDfaYes(args[3],args[4]);
        else if(checkWordsDfaNo1(args))    CommandLineMenu.checkWordsDfaNo(args[2],args[3]);
        else if(checkWordsDfaNo2(args))    CommandLineMenu.checkWordsDfaNo(args[3],args[4]);
        else if(checkWordsNfa(args))       CommandLineMenu.checkWordsNfa(args[2],args[3]);
        else if(checkWordsNfaYes1(args))   CommandLineMenu.checkWordsNfaYes(args[2],args[3]);
        else if(checkWordsNfaYes2(args))   CommandLineMenu.checkWordsNfaYes(args[3],args[4]);
        else if(checkWordsNfaNo1(args))    CommandLineMenu.checkWordsNfaNo(args[2],args[3]);
        else if(checkWordsNfaNo2(args))    CommandLineMenu.checkWordsNfaNo(args[3],args[4]);
        else if(checkWordsRegex(args))     CommandLineMenu.checkWordsRegex(args[2],args[3]);
        else if(checkWordsRegexYes1(args)) CommandLineMenu.checkWordsRegexYes(args[2],args[3]);
        else if(checkWordsRegexYes2(args)) CommandLineMenu.checkWordsRegexYes(args[3],args[4]);
        else if(checkWordsRegexNo1(args))  CommandLineMenu.checkWordsRegexNo(args[2],args[3]);
        else if(checkWordsRegexNo2(args))  CommandLineMenu.checkWordsRegexNo(args[3],args[4]);
        else if(checkWordsCfg(args))       CommandLineMenu.checkWordsCfg(args[2],args[3]);
        else if(checkWordsCfgYes1(args))   CommandLineMenu.checkWordsCfgYes(args[2],args[3]);
        else if(checkWordsCfgYes2(args))   CommandLineMenu.checkWordsCfgYes(args[3],args[4]);
        else if(checkWordsCfgNo1(args))    CommandLineMenu.checkWordsCfgNo(args[2],args[3]);
        else if(checkWordsCfgNo2(args))    CommandLineMenu.checkWordsCfgNo(args[3],args[4]);

        else if(generateWordsDfa(args))    CommandLineMenu.generateWordsDfa(args[2]);
        else if(generateWordsDfaN(args))   CommandLineMenu.generateWordsDfa(args[2],args[3]);
        else if(generateWordsNfa(args))    CommandLineMenu.generateWordsNfa(args[2]);
        else if(generateWordsNfaN(args))   CommandLineMenu.generateWordsNfa(args[2],args[3]);
        else if(generateWordsRegex(args))  CommandLineMenu.generateWordsRegex(args[2]);
        else if(generateWordsRegexN(args)) CommandLineMenu.generateWordsRegex(args[2],args[3]);
        else if(generateWordsCfg(args))    CommandLineMenu.generateWordsCfg(args[2]);
        else if(generateWordsCfgN(args))   CommandLineMenu.generateWordsCfg(args[2],args[3]);

        else if(minimizeDfa(args))   CommandLineMenu.minimizeDfa(args[3]);
        else if(reverseDfa(args))    CommandLineMenu.reverseDfa(args[3]);
        else if(complementDfa(args)) CommandLineMenu.complementDfa(args[3]);
        else if(simplifyCfg(args))   CommandLineMenu.simplifyCfg(args[3]);
        else if(chomskyCfg(args))    CommandLineMenu.chomskyCfg(args[3]);
        else if(greibachCfg(args))   CommandLineMenu.greibachCfg(args[3]);

        else if(help(args)) System.out.println(helpMessage());
        else System.out.println(Printer.incorrectArguments());
    }

    //CONTEXT FREE LANGUAGES TESTS

    private static boolean equalCfgCfg(String[] args){
        boolean ok = args.length == 5;
        ok = ok && args[0].equals(compare);
        ok = ok && args[1].equals(cfg);
        ok = ok && args[2].equals(cfg);
        return ok;
    }

    private static boolean findCounterExampleCfg(String[] args){
        boolean ok = args.length == 3;
        ok = ok && args[0].equals(counterexample);
        return ok;
    }

    private static boolean findManyCounterExamplesCfg1(String[] args){
        boolean ok = args.length == 4;
        ok = ok && args[0].equals(counterexample);
        ok = ok && args[3].equals(many);
        return ok;
    }

    private static boolean findManyCounterExamplesCfg2(String[] args){
        boolean ok = args.length == 4;
        ok = ok && args[0].equals(counterexample);
        ok = ok && args[1].equals(many);
        return ok;
    }

    private static boolean checkAmbiguity(String[] args){
        boolean ok = args.length == 2;
        ok = ok && args[0].equals(ambiguity);
        return ok;
    }

    //REGULAR LANGUAGES COMPARISONS

    private static boolean equalDfaDfa(String[] args){
        boolean ok = args.length == 5;
        ok = ok && args[0].equals(compare);
        ok = ok && args[1].equals(dfa);
        ok = ok && args[2].equals(dfa);
        return ok;
    }

    private static boolean equalDfaNfa(String[] args){
        boolean ok = args.length == 5;
        ok = ok && args[0].equals(compare);
        ok = ok && args[1].equals(dfa);
        ok = ok && args[2].equals(nfa);
        return ok;
    }

    private static boolean equalDfaRegex(String[] args){
        boolean ok = args.length == 5;
        ok = ok && args[0].equals(compare);
        ok = ok && args[1].equals(dfa);
        ok = ok && args[2].equals(regex);
        return ok;
    }

    private static boolean equalNfaDfa(String[] args){
        boolean ok = args.length == 5;
        ok = ok && args[0].equals(compare);
        ok = ok && args[1].equals(nfa);
        ok = ok && args[2].equals(dfa);
        return ok;
    }

    private static boolean equalNfaNfa(String[] args){
        boolean ok = args.length == 5;
        ok = ok && args[0].equals(compare);
        ok = ok && args[1].equals(nfa);
        ok = ok && args[2].equals(nfa);
        return ok;
    }

    private static boolean equalNfaRegex(String[] args){
        boolean ok = args.length == 5;
        ok = ok && args[0].equals(compare);
        ok = ok && args[1].equals(nfa);
        ok = ok && args[2].equals(regex);
        return ok;
    }

    private static boolean equalRegexDfa(String[] args){
        boolean ok = args.length == 5;
        ok = ok && args[0].equals(compare);
        ok = ok && args[1].equals(regex);
        ok = ok && args[2].equals(dfa);
        return ok;
    }

    private static boolean equalRegexNfa(String[] args){
        boolean ok = args.length == 5;
        ok = ok && args[0].equals(compare);
        ok = ok && args[1].equals(regex);
        ok = ok && args[2].equals(nfa);
        return ok;
    }

    private static boolean equalRegexRegex(String[] args){
        boolean ok = args.length == 5;
        ok = ok && args[0].equals(compare);
        ok = ok && args[1].equals(regex);
        ok = ok && args[2].equals(regex);
        return ok;
    }

    //REGULAR LANGUAGES TRANSFORMATIONS

    private static boolean transformDfaNfa(String[] args){
        boolean ok = args.length == 4;
        ok = ok && args[0].equals(transform);
        ok = ok && args[1].equals(dfa);
        ok = ok && args[2].equals(nfa);
        return ok;
    }

    private static boolean transformDfaRegex(String[] args){
        boolean ok = args.length == 4;
        ok = ok && args[0].equals(transform);
        ok = ok && args[1].equals(dfa);
        ok = ok && args[2].equals(regex);
        return ok;
    }

    private static boolean transformNfaDfa(String[] args){
        boolean ok = args.length == 4;
        ok = ok && args[0].equals(transform);
        ok = ok && args[1].equals(nfa);
        ok = ok && args[2].equals(dfa);
        return ok;
    }

    private static boolean transformNfaDfaMinim1(String[] args){
        boolean ok = args.length == 5;
        ok = ok && args[0].equals(transform);
        ok = ok && args[1].equals(nfa);
        ok = ok && args[2].equals(dfa);
        ok = ok && args[4].equals(minimized);
        return ok;
    }

    private static boolean transformNfaDfaMinim2(String[] args){
        boolean ok = args.length == 5;
        ok = ok && args[0].equals(transform);
        ok = ok && args[1].equals(minimized);
        ok = ok && args[2].equals(nfa);
        ok = ok && args[3].equals(dfa);
        return ok;
    }

    private static boolean transformNfaRegex(String[] args){
        boolean ok = args.length == 4;
        ok = ok && args[0].equals(transform);
        ok = ok && args[1].equals(nfa);
        ok = ok && args[2].equals(regex);
        return ok;
    }

    private static boolean transformRegexDfa(String[] args){
        boolean ok = args.length == 4;
        ok = ok && args[0].equals(transform);
        ok = ok && args[1].equals(regex);
        ok = ok && args[2].equals(dfa);
        return ok;
    }

    private static boolean transformRegexDfaMinim1(String[] args){
        boolean ok = args.length == 5;
        ok = ok && args[0].equals(transform);
        ok = ok && args[1].equals(regex);
        ok = ok && args[2].equals(dfa);
        ok = ok && args[4].equals(minimized);
        return ok;
    }

    private static boolean transformRegexDfaMinim2(String[] args){
        boolean ok = args.length == 5;
        ok = ok && args[0].equals(transform);
        ok = ok && args[1].equals(minimized);
        ok = ok && args[2].equals(regex);
        ok = ok && args[3].equals(dfa);
        return ok;
    }

    private static boolean transformRegexNfa(String[] args){
        boolean ok = args.length == 4;
        ok = ok && args[0].equals(transform);
        ok = ok && args[1].equals(regex);
        ok = ok && args[2].equals(nfa);
        return ok;
    }

    private static boolean transformRegexNfaMinim1(String[] args){
        boolean ok = args.length == 5;
        ok = ok && args[0].equals(transform);
        ok = ok && args[1].equals(regex);
        ok = ok && args[2].equals(nfa);
        ok = ok && args[4].equals(minimized);
        return ok;
    }

    private static boolean transformRegexNfaMinim2(String[] args){
        boolean ok = args.length == 5;
        ok = ok && args[0].equals(transform);
        ok = ok && args[1].equals(minimized);
        ok = ok && args[2].equals(regex);
        ok = ok && args[3].equals(nfa);
        return ok;
    }

    private static boolean transformDfaCfg(String[] args){
        boolean ok = args.length == 4;
        ok = ok && args[0].equals(transform);
        ok = ok && args[1].equals(dfa);
        ok = ok && args[2].equals(cfg);
        return ok;
    }

    private static boolean transformNfaCfg(String[] args){
        boolean ok = args.length == 4;
        ok = ok && args[0].equals(transform);
        ok = ok && args[1].equals(nfa);
        ok = ok && args[2].equals(cfg);
        return ok;
    }

    private static boolean transformRegexCfg(String[] args){
        boolean ok = args.length == 4;
        ok = ok && args[0].equals(transform);
        ok = ok && args[1].equals(regex);
        ok = ok && args[2].equals(cfg);
        return ok;
    }

    //CHECK WORDS

    private static boolean checkWordsDfa(String[] args){
        boolean ok = args.length == 4;
        ok = ok && args[0].equals(checkwords);
        ok = ok && args[1].equals(dfa);
        return ok;
    }

    private static boolean checkWordsDfaYes1(String[] args){
        boolean ok = args.length == 5;
        ok = ok && args[0].equals(checkwords);
        ok = ok && args[1].equals(dfa);
        ok = ok && args[4].equals(y);
        return ok;
    }

    private static boolean checkWordsDfaYes2(String[] args){
        boolean ok = args.length == 5;
        ok = ok && args[0].equals(checkwords);
        ok = ok && args[1].equals(y);
        ok = ok && args[2].equals(dfa);
        return ok;
    }

    private static boolean checkWordsDfaNo1(String[] args){
        boolean ok = args.length == 5;
        ok = ok && args[0].equals(checkwords);
        ok = ok && args[1].equals(dfa);
        ok = ok && args[4].equals(n);
        return ok;
    }

    private static boolean checkWordsDfaNo2(String[] args){
        boolean ok = args.length == 5;
        ok = ok && args[0].equals(checkwords);
        ok = ok && args[1].equals(n);
        ok = ok && args[2].equals(dfa);
        return ok;
    }

    private static boolean checkWordsNfa(String[] args){
        boolean ok = args.length == 4;
        ok = ok && args[0].equals(checkwords);
        ok = ok && args[1].equals(nfa);
        return ok;
    }

    private static boolean checkWordsNfaYes1(String[] args){
        boolean ok = args.length == 5;
        ok = ok && args[0].equals(checkwords);
        ok = ok && args[1].equals(nfa);
        ok = ok && args[4].equals(y);
        return ok;
    }

    private static boolean checkWordsNfaYes2(String[] args){
        boolean ok = args.length == 5;
        ok = ok && args[0].equals(checkwords);
        ok = ok && args[1].equals(y);
        ok = ok && args[2].equals(nfa);
        return ok;
    }

    private static boolean checkWordsNfaNo1(String[] args){
        boolean ok = args.length == 5;
        ok = ok && args[0].equals(checkwords);
        ok = ok && args[1].equals(nfa);
        ok = ok && args[4].equals(n);
        return ok;
    }

    private static boolean checkWordsNfaNo2(String[] args){
        boolean ok = args.length == 5;
        ok = ok && args[0].equals(checkwords);
        ok = ok && args[1].equals(n);
        ok = ok && args[2].equals(nfa);
        return ok;
    }

    private static boolean checkWordsRegex(String[] args){
        boolean ok = args.length == 4;
        ok = ok && args[0].equals(checkwords);
        ok = ok && args[1].equals(regex);
        return ok;
    }

    private static boolean checkWordsRegexYes1(String[] args){
        boolean ok = args.length == 5;
        ok = ok && args[0].equals(checkwords);
        ok = ok && args[1].equals(regex);
        ok = ok && args[4].equals(y);
        return ok;
    }

    private static boolean checkWordsRegexYes2(String[] args){
        boolean ok = args.length == 5;
        ok = ok && args[0].equals(checkwords);
        ok = ok && args[1].equals(y);
        ok = ok && args[2].equals(regex);
        return ok;
    }

    private static boolean checkWordsRegexNo1(String[] args){
        boolean ok = args.length == 5;
        ok = ok && args[0].equals(checkwords);
        ok = ok && args[1].equals(regex);
        ok = ok && args[4].equals(n);
        return ok;
    }

    private static boolean checkWordsRegexNo2(String[] args){
        boolean ok = args.length == 5;
        ok = ok && args[0].equals(checkwords);
        ok = ok && args[1].equals(n);
        ok = ok && args[2].equals(regex);
        return ok;
    }

    private static boolean checkWordsCfg(String[] args){
        boolean ok = args.length == 4;
        ok = ok && args[0].equals(checkwords);
        ok = ok && args[1].equals(cfg);
        return ok;
    }

    private static boolean checkWordsCfgYes1(String[] args){
        boolean ok = args.length == 5;
        ok = ok && args[0].equals(checkwords);
        ok = ok && args[1].equals(cfg);
        ok = ok && args[4].equals(y);
        return ok;
    }

    private static boolean checkWordsCfgYes2(String[] args){
        boolean ok = args.length == 5;
        ok = ok && args[0].equals(checkwords);
        ok = ok && args[1].equals(y);
        ok = ok && args[2].equals(cfg);
        return ok;
    }

    private static boolean checkWordsCfgNo1(String[] args){
        boolean ok = args.length == 5;
        ok = ok && args[0].equals(checkwords);
        ok = ok && args[1].equals(cfg);
        ok = ok && args[4].equals(n);
        return ok;
    }

    private static boolean checkWordsCfgNo2(String[] args){
        boolean ok = args.length == 5;
        ok = ok && args[0].equals(checkwords);
        ok = ok && args[1].equals(n);
        ok = ok && args[2].equals(cfg);
        return ok;
    }

    //GENERATE WORDS

    private static boolean generateWordsDfa(String[] args){
        boolean ok = args.length == 3;
        ok = ok && args[0].equals(generatewords);
        ok = ok && args[1].equals(dfa);
        return ok;
    }

    private static boolean generateWordsDfaN(String[] args){
        boolean ok = args.length == 4;
        ok = ok && args[0].equals(generatewords);
        ok = ok && args[1].equals(dfa);
        return ok;
    }

    private static boolean generateWordsNfa(String[] args){
        boolean ok = args.length == 3;
        ok = ok && args[0].equals(generatewords);
        ok = ok && args[1].equals(nfa);
        return ok;
    }

    private static boolean generateWordsNfaN(String[] args){
        boolean ok = args.length == 4;
        ok = ok && args[0].equals(generatewords);
        ok = ok && args[1].equals(nfa);
        return ok;
    }

    private static boolean generateWordsRegex(String[] args){
        boolean ok = args.length == 3;
        ok = ok && args[0].equals(generatewords);
        ok = ok && args[1].equals(regex);
        return ok;
    }

    private static boolean generateWordsRegexN(String[] args){
        boolean ok = args.length == 4;
        ok = ok && args[0].equals(generatewords);
        ok = ok && args[1].equals(regex);
        return ok;
    }

    private static boolean generateWordsCfg(String[] args){
        boolean ok = args.length == 3;
        ok = ok && args[0].equals(generatewords);
        ok = ok && args[1].equals(cfg);
        return ok;
    }

    private static boolean generateWordsCfgN(String[] args){
        boolean ok = args.length == 4;
        ok = ok && args[0].equals(generatewords);
        ok = ok && args[1].equals(cfg);
        return ok;
    }

    //DFA TRANSFORMATIONS

    private static boolean minimizeDfa(String[] args){
        boolean ok = args.length == 4;
        ok = ok && args[0].equals(build);
        ok = ok && args[1].equals(dfa);
        ok = ok && args[2].equals(minimize);
        return ok;
    }

    private static boolean reverseDfa(String[] args){
        boolean ok = args.length == 4;
        ok = ok && args[0].equals(build);
        ok = ok && args[1].equals(dfa);
        ok = ok && args[2].equals(reverse);
        return ok;
    }

    private static boolean complementDfa(String[] args){
        boolean ok = args.length == 4;
        ok = ok && args[0].equals(build);
        ok = ok && args[1].equals(dfa);
        ok = ok && args[2].equals(complement);
        return ok;
    }

    //CFG TRANSFORMATIONS

    private static boolean simplifyCfg(String[] args){
        boolean ok = args.length == 4;
        ok = ok && args[0].equals(build);
        ok = ok && args[1].equals(cfg);
        ok = ok && args[2].equals(simplify);
        return ok;
    }

    private static boolean chomskyCfg(String[] args){
        boolean ok = args.length == 4;
        ok = ok && args[0].equals(build);
        ok = ok && args[1].equals(cfg);
        ok = ok && args[2].equals(chomsky);
        return ok;
    }

    private static boolean greibachCfg(String[] args){
        boolean ok = args.length == 4;
        ok = ok && args[0].equals(build);
        ok = ok && args[1].equals(cfg);
        ok = ok && args[2].equals(greibach);
        return ok;
    }

    //HELP

    private static boolean help(String[] args){
        return args.length == 1 && args[0].equals(help);
    }

    private static String helpMessage(){
        return "Check this webpage: https://github.com/JordiPujolrasBosch/FonamentsComputacio/blob/master/Manual.md";
    }

    private final static String cfg = "cfg";
    private final static String dfa = "dfa";
    private final static String nfa = "nfa";
    private final static String regex = "regex";

    private final static String compare = "--compare";
    private final static String counterexample = "--counterexample";
    private final static String ambiguity = "--ambiguity";
    private final static String transform = "--transform";
    private final static String checkwords = "--checkwords";
    private final static String generatewords = "--generatewords";
    private final static String build = "--build";
    private final static String help = "--help";

    private final static String many = "-many";
    private final static String minimized = "-minimized";
    private final static String y = "-y";
    private final static String n = "-n";

    private final static String minimize = "minimize";
    private final static String reverse = "reverse";
    private final static String complement = "complement";
    private final static String simplify = "simplify";
    private final static String chomsky = "chomsky";
    private final static String greibach = "greibach";
}
