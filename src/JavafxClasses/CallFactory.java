package JavafxClasses;

import Automatons.Dfa;
import Automatons.Nfa;
import Factory.Reader;
import Functionalities.Menu;
import Grammars.Cfg;
import RegularExpressions.RegularExpression;

import java.util.List;

public class CallFactory {
    //ARTICLE

    public static CallTwo callEqualCfgCfg(){
        return (a, b) -> {
            try{
                Cfg aa = Reader.readGrammarString(a);
                Cfg bb = Reader.readGrammarString(b);
                return Menu.equalCfgCfgArticle(aa,bb,null,null);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    public static CallTwo callFindCounterExampleCfgs(){
        return (a, b) -> {
            try{
                Cfg aa = Reader.readGrammarString(a);
                Cfg bb = Reader.readGrammarString(b);
                return Menu.findCounterExampleCfgs(aa,bb,null,null);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    public static CallOne callCheckAmbiguity(){
        return data -> {
            try {
                Cfg cfg = Reader.readGrammarString(data);
                return Menu.checkAmbiguity(cfg, null);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    //COMPARE

    public static CallTwo callEqualDfaDfa(){
        return (a, b) -> {
            try{
                Dfa aa = Reader.readAutomatonString(a).toDfa();
                Dfa bb = Reader.readAutomatonString(b).toDfa();
                return Menu.equalDfaDfa(aa,bb,null,null);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    public static CallTwo callEqualDfaNfa(){
        return (a, b) -> {
            try{
                Dfa aa = Reader.readAutomatonString(a).toDfa();
                Nfa bb = Reader.readAutomatonString(b).toNfa();
                return Menu.equalDfaNfa(aa,bb,null,null);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    public static CallTwo callEqualDfaRegex(){
        return (a, b) -> {
            try{
                Dfa aa = Reader.readAutomatonString(a).toDfa();
                RegularExpression bb = Reader.readRegularExpressionString(b);
                return Menu.equalDfaRegex(aa,bb,null,null);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    public static CallTwo callEqualNfaNfa(){
        return (a, b) -> {
            try{
                Nfa aa = Reader.readAutomatonString(a).toNfa();
                Nfa bb = Reader.readAutomatonString(b).toNfa();
                return Menu.equalNfaNfa(aa,bb,null,null);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    public static CallTwo callEqualNfaRegex(){
        return (a, b) -> {
            try{
                Nfa aa = Reader.readAutomatonString(a).toNfa();
                RegularExpression bb = Reader.readRegularExpressionString(b);
                return Menu.equalNfaRegex(aa,bb,null,null);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    public static CallTwo callEqualRegexRegex(){
        return (a, b) -> {
            try{
                RegularExpression aa = Reader.readRegularExpressionString(a);
                RegularExpression bb = Reader.readRegularExpressionString(b);
                return Menu.equalRegexRegex(aa,bb,null,null);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    //DFA transformations

    public static CallOne callMinimizeDfa(){
        return data -> {
            try {
                Dfa dfa = Reader.readAutomatonString(data).toDfa();
                return Menu.minimizeDfa(dfa);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    public static CallOne callReverseDfa(){
        return data -> {
            try {
                Dfa dfa = Reader.readAutomatonString(data).toDfa();
                return Menu.reverseDfa(dfa);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    public static CallOne callComplementDfa(){
        return data -> {
            try {
                Dfa dfa = Reader.readAutomatonString(data).toDfa();
                return Menu.complementDfa(dfa);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    //TRANSFORM

    public static CallOne callTransformDfaNfa(){
        return data -> {
            try {
                Dfa dfa = Reader.readAutomatonString(data).toDfa();
                return Menu.transformDfaNfa(dfa);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    public static CallOne callTransformDfaRegex(){
        return data -> {
            try {
                Dfa dfa = Reader.readAutomatonString(data).toDfa();
                return Menu.transformDfaRegex(dfa);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    public static CallOne callTransformNfaDfa(){
        return data -> {
            try {
                Nfa nfa = Reader.readAutomatonString(data).toNfa();
                return Menu.transformNfaDfa(nfa);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    public static CallOne callTransformNfaRegex(){
        return data -> {
            try {
                Nfa nfa = Reader.readAutomatonString(data).toNfa();
                return Menu.transformNfaRegex(nfa);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    public static CallOne callTransformRegexDfaMinim(){
        return data -> {
            try {
                RegularExpression regex = Reader.readRegularExpressionString(data);
                return Menu.transformRegexDfaMinim(regex);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    public static CallOne callTransformRegexDfaNotMinim(){
        return data -> {
            try {
                RegularExpression regex = Reader.readRegularExpressionString(data);
                return Menu.transformRegexDfaNotMinim(regex);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    public static CallOne callTransformRegexNfaMinim(){
        return data -> {
            try {
                RegularExpression regex = Reader.readRegularExpressionString(data);
                return Menu.transformRegexNfaMinim(regex);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    public static CallOne callTransformRegexNfaNotMinim(){
        return data -> {
            try {
                RegularExpression regex = Reader.readRegularExpressionString(data);
                return Menu.transformRegexNfaNotMinim(regex);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    public static CallOne callTransformDfaCfg(){
        return data -> {
            try {
                Dfa dfa = Reader.readAutomatonString(data).toDfa();
                return Menu.transformDfaCfg(dfa);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    public static CallOne callTransformNfaCfg(){
        return data -> {
            try {
                Nfa nfa = Reader.readAutomatonString(data).toNfa();
                return Menu.transformNfaCfg(nfa);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    public static CallOne callTransformRegexCfg(){
        return data -> {
            try {
                RegularExpression regex = Reader.readRegularExpressionString(data);
                return Menu.transformRegexCfg(regex);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    //CHECK WORDS

    public static CallTwo callCheckWordsDfa(){
        return (a, b) -> {
            try{
                Dfa aa = Reader.readAutomatonString(a).toDfa();
                List<String> bb = Reader.readWordsString(b);
                return Menu.checkWordsDfa(aa,bb);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    public static CallTwo callCheckWordsNfa(){
        return (a, b) -> {
            try{
                Nfa aa = Reader.readAutomatonString(a).toNfa();
                List<String> bb = Reader.readWordsString(b);
                return Menu.checkWordsNfa(aa,bb);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    public static CallTwo callCheckWordsRegex(){
        return (a, b) -> {
            try{
                RegularExpression aa = Reader.readRegularExpressionString(a);
                List<String> bb = Reader.readWordsString(b);
                return Menu.checkWordsRegex(aa,bb);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    public static CallTwo callCheckWordsCfg(){
        return (a, b) -> {
            try{
                Cfg aa = Reader.readGrammarString(a);
                List<String> bb = Reader.readWordsString(b);
                return Menu.checkWordsCfg(aa,bb);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    //GENERATE WORDS

    public static CallOne callGenerateWordsDfa(){
        return data -> {
            try {
                Dfa dfa = Reader.readAutomatonString(data).toDfa();
                return Menu.generateWordsDfa(dfa, UtilFx.getInteger());
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    public static CallOne callGenerateWordsNfa(){
        return data -> {
            try {
                Nfa nfa = Reader.readAutomatonString(data).toNfa();
                return Menu.generateWordsNfa(nfa, UtilFx.getInteger());
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    public static CallOne callGenerateWordsRegex(){
        return data -> {
            try {
                RegularExpression regex = Reader.readRegularExpressionString(data);
                return Menu.generateWordsRegex(regex, UtilFx.getInteger());
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    public static CallOne callGenerateWordsCfg(){
        return data -> {
            try {
                Cfg cfg = Reader.readGrammarString(data);
                return Menu.generateWordsCfg(cfg, UtilFx.getInteger());
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    //CFG transformations

    public static CallOne callSimplifyGrammar(){
        return data -> {
            try {
                Cfg cfg = Reader.readGrammarString(data);
                return Menu.simplifyGrammar(cfg);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    public static CallOne callTransformChomsky(){
        return data -> {
            try {
                Cfg cfg = Reader.readGrammarString(data);
                return Menu.transformChomsky(cfg);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    public static CallOne callTransformGriebach(){
        return data -> {
            try {
                Cfg cfg = Reader.readGrammarString(data);
                return Menu.transformGriebach(cfg);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }
}
