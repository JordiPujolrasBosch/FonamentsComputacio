package Functionalities;

public class RunCommandLine {
    public static void run(String[] args){
        if(equalCfgCfg(args)) CommandLineMenu.equalCfgCfgArticle(args[3], args[4]);
    }

    private static boolean equalCfgCfg(String[] args){
        boolean ok = args.length == 5;
        ok = ok && args[0].equals(compare);
        ok = ok && args[1].equals(cfg);
        ok = ok && args[2].equals(cfg);
        return ok;
    }

    private final static String cfg = "cfg";
    private final static String dfa = "dfa";
    private final static String nfa = "nfa";
    private final static String regex = "regex";

    private final static String compare = "--compare";
}
