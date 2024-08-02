package JavafxClasses;

import Automatons.Dfa;
import Automatons.Nfa;
import Factory.Reader;
import Functionalities.Menu;
import Grammars.Cfg;
import RegularExpressions.RegularExpression;
import Utils.Pair;
import Utils.Utility;

import java.util.ArrayList;
import java.util.List;

public class GuiMenu {
    public static List<Pair<String,List<GuiMenuIn>>> getList(){return list;}

    private static final List<Pair<String,List<GuiMenuIn>>> list = buildList();

    private static List<Pair<String,List<GuiMenuIn>>> buildList(){
        List<Pair<String,List<GuiMenuIn>>> list = new ArrayList<>();
        List<GuiMenuIn> aux;

        //CONTEXT FREE LANGUAGES TESTS
        aux = new ArrayList<>();
        aux.add(buildEqualCfgCfg());
        aux.add(buildFindCounterExampleCfg());
        //aux.add(buildFindManyCounterExamplesCfg());
        aux.add(buildCheckAmbiguity());
        list.add(new Pair<>("Context free languages tests", aux));

        //REGULAR LANGUAGES COMPARISONS
        aux = new ArrayList<>();
        aux.add(buildEqualDfaDfa());
        aux.add(buildEqualDfaNfa());
        aux.add(buildEqualDfaRegex());
        aux.add(buildEqualNfaDfa());
        aux.add(buildEqualNfaNfa());
        aux.add(buildEqualNfaRegex());
        aux.add(buildEqualRegexDfa());
        aux.add(buildEqualRegexNfa());
        aux.add(buildEqualRegexRegex());
        list.add(new Pair<>("Regular languages comparisons", aux));

        //REGULAR LANGUAGES TRANSFORMATIONS
        aux = new ArrayList<>();
        aux.add(buildTransformDfaNfa());
        aux.add(buildTransformDfaRegex());
        aux.add(buildTransformNfaDfa());
        aux.add(buildTransformNfaDfaMinim());
        aux.add(buildTransformNfaRegex());
        aux.add(buildTransformRegexDfa());
        aux.add(buildTransformRegexDfaMinim());
        aux.add(buildTransformRegexNfa());
        aux.add(buildTransformRegexNfaMinim());
        aux.add(buildTransformDfaCfg());
        aux.add(buildTransformNfaCfg());
        aux.add(buildTransformRegexCfg());
        list.add(new Pair<>("Regular languages transformations", aux));

        //CHECK WORDS
        aux = new ArrayList<>();
        aux.add(buildCheckWordsDfa());
        aux.add(buildCheckWordsNfa());
        aux.add(buildCheckWordsRegex());
        aux.add(buildCheckWordsCfg());
        list.add(new Pair<>("Check words", aux));

        //GENERATE WORDS
        aux = new ArrayList<>();
        aux.add(buildGenerateWordsDfa());
        aux.add(buildGenerateWordsNfa());
        aux.add(buildGenerateWordsRegex());
        aux.add(buildGenerateWordsCfg());
        list.add(new Pair<>("Generate words", aux));

        //DFA TRANSFORMATIONS
        aux = new ArrayList<>();
        aux.add(buildMinimizeDfa());
        aux.add(buildReverseDfa());
        aux.add(buildComplementDfa());
        list.add(new Pair<>("Dfa transformations", aux));

        //CFG TRANSFORMATIONS
        aux = new ArrayList<>();
        aux.add(buildSimplifyCfg());
        aux.add(buildChomskyCfg());
        aux.add(buildGreibachCfg());
        list.add(new Pair<>("Cfg transformations", aux));

        return list;
    }

    private enum LayoutTypes{
        L1X1L, L2, L2W, L2X1, L2X1L, L3, L3W
    }

    static class GuiMenuIn {
        private final String title;
        private final String description;
        private final CallMenuOne callMenuOne;
        private final CallMenuTwo callMenuTwo;
        private final String a;
        private final String b;
        private final String c;
        private final LayoutTypes type;

        public GuiMenuIn(String title, String description, CallMenuOne one, String a, String b, LayoutTypes type) {
            this.title = title;
            this.description = description;
            this.callMenuOne = one;
            this.callMenuTwo = null;
            this.a = a;
            this.b = b;
            this.c = null;
            this.type = type;
        }

        public GuiMenuIn(String title, String description, CallMenuTwo two, String a, String b, String c, LayoutTypes type) {
            this.title = title;
            this.description = description;
            callMenuOne = null;
            callMenuTwo = two;
            this.a = a;
            this.b = b;
            this.c = c;
            this.type = type;
        }

        public CallPane getCallPane() {
            Layout lay = null;
            switch (type){
                case L1X1L -> lay = new Layout1x1Len(title, description, a, b, callMenuOne);
                case L2    -> lay = new Layout2(title, description, a, b, callMenuOne);
                case L2W   -> lay = new Layout2Words(title, description, a, b, callMenuOne);
                case L2X1  -> lay = new Layout2x1(title, description, a, b, c, callMenuTwo);
                case L2X1L -> lay = new Layout2x1Len(title, description, a, b, c, callMenuTwo);
                case L3    -> lay = new Layout3(title, description, a, b, c, callMenuTwo);
                case L3W   -> lay = new Layout3Words(title, description, a, b, c, callMenuTwo);
            }
            Layout finalLay = lay;
            return () -> finalLay.build();
        }

        public String getTitle(){
            return title;
        }
    }

    //CONTEXT FREE LANGUAGES TESTS

    private static GuiMenuIn buildEqualCfgCfg(){
        String title = "Compare cfg & cfg";
        String description = "Check if two context free grammars are equivalent.";
        CallMenuTwo call = (a, b) -> {
            try{
                Cfg aa = Reader.readGrammarString(a);
                Cfg bb = Reader.readGrammarString(b);
                return Menu.equalCfgCfg(aa,bb,null,null);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };

        return new GuiMenuIn(title, description, call, Utility.cfgA, Utility.cfgB, Utility.result, LayoutTypes.L2X1);
    }

    private static GuiMenuIn buildFindCounterExampleCfg(){
        String title = "Counterexample cfg & cfg";
        String description = "Check if two context free grammars are equivalent.\nIf are different it searches a counterexample,\na word that can be generated by only one of them.";
        CallMenuTwo call = (a, b) -> {
            try{
                Cfg aa = Reader.readGrammarString(a);
                Cfg bb = Reader.readGrammarString(b);
                return Menu.findCounterExampleCfg(aa,bb,null,null, Utility.getInt());
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };

        return new GuiMenuIn(title, description, call, Utility.cfgA, Utility.cfgB, Utility.result, LayoutTypes.L2X1L);
    }

    /*private static GuiMenuIn buildFindManyCounterExamplesCfg(){
        String title = "Some counterexamples cfg & cfg";
        String description = "Check if two context free grammars are equivalent.\nIf are different it searches some counterexamples,\nwords that can be generated by only one of them.";
        CallMenuTwo call = (a, b) -> {
            try{
                Cfg aa = Reader.readGrammarString(a);
                Cfg bb = Reader.readGrammarString(b);
                return Menu.findManyCounterExamplesCfg(aa,bb);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };

        return new GuiMenuIn(title, description, call, Utility.cfgA, Utility.cfgB, Utility.result, LayoutTypes.L3);
    }*/

    private static GuiMenuIn buildCheckAmbiguity(){
        String title = "Ambiguity cfg";
        String description = "Check if a context free grammar is ambiguous.\nA grammar is ambiguous if it can generate al least\none word in two or more different ways.";
        CallMenuOne call = data -> {
            try {
                Cfg cfg = Reader.readGrammarString(data);
                return Menu.checkAmbiguity(cfg, null, Utility.getInt());
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };

        return new GuiMenuIn(title, description, call, Utility.cfg, Utility.result, LayoutTypes.L1X1L);
    }

    //REGULAR LANGUAGES COMPARISONS

    private static GuiMenuIn buildEqualDfaDfa(){
        String title = "Compare dfa & dfa";
        String description = "Check if two deterministic finite automatons are equivalent.";
        CallMenuTwo call = (a, b) -> {
            try{
                Dfa aa = Reader.readAutomatonString(a).toDfa();
                Dfa bb = Reader.readAutomatonString(b).toDfa();
                return Menu.equalDfaDfa(aa,bb,null,null);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };

        return new GuiMenuIn(title, description, call, Utility.dfaA, Utility.dfaB, Utility.result, LayoutTypes.L2X1);
    }

    private static GuiMenuIn buildEqualDfaNfa(){
        String title = "Compare dfa & nfa";
        String description = "Check if a deterministic finite automaton and a nondeterministic finite automaton are equivalent.";
        CallMenuTwo call = (a, b) -> {
            try{
                Dfa aa = Reader.readAutomatonString(a).toDfa();
                Nfa bb = Reader.readAutomatonString(b).toNfa();
                return Menu.equalDfaNfa(aa,bb,null,null);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };


        return new GuiMenuIn(title, description, call, Utility.dfa, Utility.nfa, Utility.result, LayoutTypes.L2X1);
    }

    private static GuiMenuIn buildEqualDfaRegex(){
        String title = "Compare dfa & regex";
        String description = "Check if a deterministic finite automaton and a regular expression are equivalent.";
        CallMenuTwo call = (a, b) -> {
            try{
                Dfa aa = Reader.readAutomatonString(a).toDfa();
                RegularExpression bb = Reader.readRegularExpressionString(b);
                return Menu.equalDfaRegex(aa,bb,null,null);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };


        return new GuiMenuIn(title, description, call, Utility.dfa, Utility.regex, Utility.result, LayoutTypes.L2X1);
    }

    private static GuiMenuIn buildEqualNfaDfa(){
        String title = "Compare nfa & dfa";
        String description = "Check if a nondeterministic finite automaton and a deterministic finite automaton are equivalent.";
        CallMenuTwo call = (a, b) -> {
            try{
                Nfa aa = Reader.readAutomatonString(a).toNfa();
                Dfa bb = Reader.readAutomatonString(b).toDfa();
                return Menu.equalNfaDfa(aa,bb,null,null);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };

        return new GuiMenuIn(title, description, call, Utility.nfa, Utility.dfa, Utility.result, LayoutTypes.L2X1);
    }

    private static GuiMenuIn buildEqualNfaNfa(){
        String title = "Compare nfa & nfa";
        String description = "Check if two nondeterministic finite automatons are equivalent.";
        CallMenuTwo call = (a, b) -> {
            try{
                Nfa aa = Reader.readAutomatonString(a).toNfa();
                Nfa bb = Reader.readAutomatonString(b).toNfa();
                return Menu.equalNfaNfa(aa,bb,null,null);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };


        return new GuiMenuIn(title, description, call, Utility.nfaA, Utility.nfaB, Utility.result, LayoutTypes.L2X1);
    }

    private static GuiMenuIn buildEqualNfaRegex(){
        String title = "Compare nfa & regex";
        String description = "Check if a nondeterministic finite automaton and a regular expression are equivalent.";
        CallMenuTwo call = (a, b) -> {
            try{
                Nfa aa = Reader.readAutomatonString(a).toNfa();
                RegularExpression bb = Reader.readRegularExpressionString(b);
                return Menu.equalNfaRegex(aa,bb,null,null);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };

        return new GuiMenuIn(title, description, call, Utility.nfa, Utility.regex, Utility.result, LayoutTypes.L2X1);
    }

    private static GuiMenuIn buildEqualRegexDfa(){
        String title = "Compare regex & dfa";
        String description = "Check if a regular expression and a deterministic finite automaton are equivalent.";
        CallMenuTwo call = (a, b) -> {
            try{
                RegularExpression aa = Reader.readRegularExpressionString(a);
                Dfa bb = Reader.readAutomatonString(b).toDfa();
                return Menu.equalRegexDfa(aa,bb,null,null);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };

        return new GuiMenuIn(title, description, call, Utility.regex, Utility.dfa, Utility.result, LayoutTypes.L2X1);
    }

    private static GuiMenuIn buildEqualRegexNfa(){
        String title = "Compare regex & nfa";
        String description = "Check if a regular expression and a nondeterministic finite automaton are equivalent.";
        CallMenuTwo call = (a, b) -> {
            try{
                RegularExpression aa = Reader.readRegularExpressionString(a);
                Nfa bb = Reader.readAutomatonString(b).toNfa();
                return Menu.equalRegexNfa(aa,bb,null,null);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };

        return new GuiMenuIn(title, description, call, Utility.regex, Utility.nfa, Utility.result, LayoutTypes.L2X1);
    }

    private static GuiMenuIn buildEqualRegexRegex(){
        String title = "Compare regex & regex";
        String description = "Check if two regular expressions are equivalent.";
        CallMenuTwo call = (a, b) -> {
            try{
                RegularExpression aa = Reader.readRegularExpressionString(a);
                RegularExpression bb = Reader.readRegularExpressionString(b);
                return Menu.equalRegexRegex(aa,bb,null,null);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };

        return new GuiMenuIn(title, description, call, Utility.regexA, Utility.regexB, Utility.result, LayoutTypes.L2X1);
    }

    //REGULAR LANGUAGES TRANSFORMATIONS

    private static GuiMenuIn buildTransformDfaNfa(){
        String title = "Transform dfa to nfa";
        String description = "Transform a deterministic finite automaton to a nondeterministic finite automaton.";
        CallMenuOne call = data -> {
            try {
                Dfa dfa = Reader.readAutomatonString(data).toDfa();
                return Menu.transformDfaNfa(dfa);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };

        return new GuiMenuIn(title, description, call, Utility.dfa, Utility.nfa, LayoutTypes.L2);
    }

    private static GuiMenuIn buildTransformDfaRegex(){
        String title = "Transform dfa to regex";
        String description = "Transform a deterministic finite automaton to a regular expression.";
        CallMenuOne call = data -> {
            try {
                Dfa dfa = Reader.readAutomatonString(data).toDfa();
                return Menu.transformDfaRegex(dfa);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };

        return new GuiMenuIn(title, description, call, Utility.dfa, Utility.regex, LayoutTypes.L2);
    }

    private static GuiMenuIn buildTransformNfaDfa(){
        String title = "Transform nfa to dfa";
        String description = "Transform a nondeterministic finite automaton to a deterministic finite automaton.";
        CallMenuOne call = data -> {
            try {
                Nfa nfa = Reader.readAutomatonString(data).toNfa();
                return Menu.transformNfaDfa(nfa);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };

        return new GuiMenuIn(title, description, call, Utility.nfa, Utility.dfa, LayoutTypes.L2);
    }

    private static GuiMenuIn buildTransformNfaDfaMinim(){
        String title = "Transform nfa to dfa minimized";
        String description = "Transform a nondeterministic finite automaton to a deterministic finite automaton minimized.";
        CallMenuOne call = data -> {
            try {
                Nfa nfa = Reader.readAutomatonString(data).toNfa();
                return Menu.transformNfaDfaMinim(nfa);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };

        return new GuiMenuIn(title, description, call, Utility.nfa, Utility.dfa, LayoutTypes.L2);
    }

    private static GuiMenuIn buildTransformNfaRegex(){
        String title = "Transform nfa to regex";
        String description = "Transform a nondeterministic finite automaton to a regular expression.";
        CallMenuOne call = data -> {
            try {
                Nfa nfa = Reader.readAutomatonString(data).toNfa();
                return Menu.transformNfaRegex(nfa);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };

        return new GuiMenuIn(title, description, call, Utility.nfa, Utility.regex, LayoutTypes.L2);
    }

    private static GuiMenuIn buildTransformRegexDfa(){
        String title = "Transform regex to dfa";
        String description = "Transform a regular expression to a deterministic finite automaton.";
        CallMenuOne call = data -> {
            try {
                RegularExpression regex = Reader.readRegularExpressionString(data);
                return Menu.transformRegexDfa(regex);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };

        return new GuiMenuIn(title, description, call, Utility.regex, Utility.dfa, LayoutTypes.L2);
    }

    private static GuiMenuIn buildTransformRegexDfaMinim(){
        String title = "Transform regex to dfa minimized";
        String description = "Transform a regular expression to a deterministic finite automaton minimized.";
        CallMenuOne call = data -> {
            try {
                RegularExpression regex = Reader.readRegularExpressionString(data);
                return Menu.transformRegexDfaMinim(regex);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };

        return new GuiMenuIn(title, description, call, Utility.regex, Utility.dfa, LayoutTypes.L2);
    }

    private static GuiMenuIn buildTransformRegexNfa(){
        String title = "Transform regex to nfa";
        String description = "Transform a regular expression to a nondeterministic finite automaton.";
        CallMenuOne call = data -> {
            try {
                RegularExpression regex = Reader.readRegularExpressionString(data);
                return Menu.transformRegexNfa(regex);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };

        return new GuiMenuIn(title, description, call, Utility.regex, Utility.nfa, LayoutTypes.L2);
    }

    private static GuiMenuIn buildTransformRegexNfaMinim(){
        String title = "Transform regex to nfa minimized";
        String description = "Transform a regular expression to a nondeterministic finite automaton minimized.";
        CallMenuOne call = data -> {
            try {
                RegularExpression regex = Reader.readRegularExpressionString(data);
                return Menu.transformRegexNfaMinim(regex);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };

        return new GuiMenuIn(title, description, call, Utility.regex, Utility.nfa, LayoutTypes.L2);
    }

    private static GuiMenuIn buildTransformDfaCfg(){
        String title = "Transform dfa to cfg";
        String description = "Transform a deterministic finite automaton to a context free grammar.";
        CallMenuOne call = data -> {
            try {
                Dfa dfa = Reader.readAutomatonString(data).toDfa();
                return Menu.transformDfaCfg(dfa);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };

        return new GuiMenuIn(title, description, call, Utility.dfa, Utility.cfg, LayoutTypes.L2);
    }

    private static GuiMenuIn buildTransformNfaCfg(){
        String title = "Transform nfa to cfg";
        String description = "Transform a nondeterministic finite automaton to a context free grammar.";
        CallMenuOne call = data -> {
            try {
                Nfa nfa = Reader.readAutomatonString(data).toNfa();
                return Menu.transformNfaCfg(nfa);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };

        return new GuiMenuIn(title, description, call, Utility.nfa, Utility.cfg, LayoutTypes.L2);
    }

    private static GuiMenuIn buildTransformRegexCfg(){
        String title = "Transform regex to cfg";
        String description = "Transform a regular expression to a context free grammar.";
        CallMenuOne call = data -> {
            try {
                RegularExpression regex = Reader.readRegularExpressionString(data);
                return Menu.transformRegexCfg(regex);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };

        return new GuiMenuIn(title, description, call, Utility.regex, Utility.cfg, LayoutTypes.L2);
    }

    //CHECK WORDS

    private static GuiMenuIn buildCheckWordsDfa(){
        String title = "Check words dfa";
        String description = "Test if a list of words are accepted by a deterministic finite automaton.";
        CallMenuTwo call = (a, b) -> {
            try{
                Dfa aa = Reader.readAutomatonString(a).toDfa();
                List<String> bb = Reader.readWordsString(b);
                if(Utility.getInt() == 0) return Menu.checkWordsDfa(aa,bb);
                else if(Utility.getInt() == 1) return Menu.checkWordsDfaYes(aa,bb);
                return Menu.checkWordsDfaNo(aa,bb);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };

        return new GuiMenuIn(title, description, call, Utility.dfa, Utility.words, Utility.result, LayoutTypes.L3W);
    }

    private static GuiMenuIn buildCheckWordsNfa(){
        String title = "Check words nfa";
        String description = "Test if a list of words are accepted by a nondeterministic finite automaton.";
        CallMenuTwo call = (a, b) -> {
            try{
                Nfa aa = Reader.readAutomatonString(a).toNfa();
                List<String> bb = Reader.readWordsString(b);
                if(Utility.getInt() == 0) return Menu.checkWordsNfa(aa,bb);
                else if(Utility.getInt() == 1) return Menu.checkWordsNfaYes(aa,bb);
                return Menu.checkWordsNfaNo(aa,bb);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };

        return new GuiMenuIn(title, description, call, Utility.nfa, Utility.words, Utility.result, LayoutTypes.L3W);
    }

    private static GuiMenuIn buildCheckWordsRegex(){
        String title = "Check words regex";
        String description = "Test if a list of words can be generated by a regular expression.";
        CallMenuTwo call = (a, b) -> {
            try{
                RegularExpression aa = Reader.readRegularExpressionString(a);
                List<String> bb = Reader.readWordsString(b);
                if(Utility.getInt() == 0) return Menu.checkWordsRegex(aa,bb);
                else if(Utility.getInt() == 1) return Menu.checkWordsRegexYes(aa,bb);
                return Menu.checkWordsRegexNo(aa,bb);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };

        return new GuiMenuIn(title, description, call, Utility.regex, Utility.words, Utility.result, LayoutTypes.L3W);
    }

    private static GuiMenuIn buildCheckWordsCfg(){
        String title = "Check words cfg";
        String description = "Test if a list of words can be generated by a context free grammar.";
        CallMenuTwo call = (a, b) -> {
            try{
                Cfg aa = Reader.readGrammarString(a);
                List<String> bb = Reader.readWordsString(b);
                if(Utility.getInt() == 0) return Menu.checkWordsCfg(aa,bb);
                else if(Utility.getInt() == 1) return Menu.checkWordsCfgYes(aa,bb);
                return Menu.checkWordsCfgNo(aa,bb);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };

        return new GuiMenuIn(title, description, call, Utility.cfg, Utility.words, Utility.result, LayoutTypes.L3W);
    }

    //GENERATE WORDS

    private static GuiMenuIn buildGenerateWordsDfa(){
        String title = "Generate words dfa";
        String description = "Generate a list of words that are accepted by a deterministic finite automaton.";
        CallMenuOne call = data -> {
            try {
                Dfa dfa = Reader.readAutomatonString(data).toDfa();
                return Menu.generateWordsDfa(dfa, Utility.getInt());
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };

        return new GuiMenuIn(title, description, call, Utility.dfa, Utility.result, LayoutTypes.L2W);
    }

    private static GuiMenuIn buildGenerateWordsNfa(){
        String title = "Generate words nfa";
        String description = "Generate a list of words that are accepted by a nondeterministic finite automaton.";
        CallMenuOne call = data -> {
            try {
                Nfa nfa = Reader.readAutomatonString(data).toNfa();
                return Menu.generateWordsNfa(nfa, Utility.getInt());
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };

        return new GuiMenuIn(title, description, call, Utility.nfa, Utility.result, LayoutTypes.L2W);
    }

    private static GuiMenuIn buildGenerateWordsRegex(){
        String title = "Generate words regex";
        String description = "Generate a list of words from a regular expression.";
        CallMenuOne call = data -> {
            try {
                RegularExpression regex = Reader.readRegularExpressionString(data);
                return Menu.generateWordsRegex(regex, Utility.getInt());
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };

        return new GuiMenuIn(title, description, call, Utility.regex, Utility.result, LayoutTypes.L2W);
    }

    private static GuiMenuIn buildGenerateWordsCfg(){
        String title = "Generate words cfg";
        String description = "Generate a list of words from a context free grammar.";
        CallMenuOne call = data -> {
            try {
                Cfg cfg = Reader.readGrammarString(data);
                return Menu.generateWordsCfg(cfg, Utility.getInt());
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };

        return new GuiMenuIn(title, description, call, Utility.cfg, Utility.result, LayoutTypes.L2W);
    }

    //DFA TRANSFORMATIONS

    private static GuiMenuIn buildMinimizeDfa(){
        String title = "Minimize dfa";
        String description = "Minimize a deterministic finite automaton.";
        CallMenuOne call = data -> {
            try {
                Dfa dfa = Reader.readAutomatonString(data).toDfa();
                return Menu.minimizeDfa(dfa);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };

        return new GuiMenuIn(title, description, call, Utility.dfa, Utility.result, LayoutTypes.L2);
    }

    private static GuiMenuIn buildReverseDfa(){
        String title = "Reverse dfa";
        String description = "Make the reverse of a deterministic finite automaton.\nThe result is a nondeterministic finite automaton that accepts\nthe same words as the original but reversed.";
        CallMenuOne call = data -> {
            try {
                Dfa dfa = Reader.readAutomatonString(data).toDfa();
                return Menu.reverseDfa(dfa);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };

        return new GuiMenuIn(title, description, call, Utility.dfa, Utility.result, LayoutTypes.L2);
    }

    private static GuiMenuIn buildComplementDfa(){
        String title = "Complement dfa";
        String description = "Make the complement of a deterministic finite automaton.\nThe result is a deterministic finite automaton that accepts\nthe words that the original doesn't accept.";
        CallMenuOne call = data -> {
            try {
                Dfa dfa = Reader.readAutomatonString(data).toDfa();
                return Menu.complementDfa(dfa);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };

        return new GuiMenuIn(title, description, call, Utility.dfa, Utility.result, LayoutTypes.L2);
    }

    //CFG TRANSFORMATIONS

    private static GuiMenuIn buildSimplifyCfg(){
        String title = "Simplify cfg";
        String description = "Simplify a context free grammar.\nRemove non derivable rules, non reachable rules, empty rules and unit rules.";
        CallMenuOne call = data -> {
            try {
                Cfg cfg = Reader.readGrammarString(data);
                return Menu.simplifyCfg(cfg);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };

        return new GuiMenuIn(title, description, call, Utility.cfg, Utility.result, LayoutTypes.L2);
    }

    private static GuiMenuIn buildChomskyCfg(){
        String title = "Transform cfg chomsky";
        String description = "Transform a context free grammar to the chomsky form.\nOn chomsky form, every rule is \"A->BC\" or \"A->a\".";
        CallMenuOne call = data -> {
            try {
                Cfg cfg = Reader.readGrammarString(data);
                return Menu.chomskyCfg(cfg);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };

        return new GuiMenuIn(title, description, call, Utility.cfg, Utility.result, LayoutTypes.L2);
    }

    private static GuiMenuIn buildGreibachCfg(){
        String title = "Transform cfg greibach";
        String description = "Transform a context free grammar to the greibach form.\nOn greibach form, every derivation starts with a terminal.";
        CallMenuOne call = data -> {
            try {
                Cfg cfg = Reader.readGrammarString(data);
                return Menu.greibachCfg(cfg);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };

        return new GuiMenuIn(title, description, call, Utility.cfg, Utility.result, LayoutTypes.L2);
    }
}
