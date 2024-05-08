package Factory.Builders;

import Exceptions.RegexReaderException;
import Factory.TokenFactory;
import RegularExpressions.*;

import java.util.ArrayList;
import java.util.List;

public class RegexBuilder {
    public static RegularExpression buildRegex(String r) throws RegexReaderException {
        checkParenthesis(r);
        List<BuildRegex> list = buildList(r);
        while(list.size() != 1) step(list);
        return list.get(0).toRegex();
    }

    private static List<BuildRegex> buildList(String r) throws RegexReaderException {
        List<BuildRegex> list = new ArrayList<>();

        int i = 0;
        while(i < r.length()-1){
            char x = r.charAt(i);
            if(TokenFactory.isROperator(x)) list.add(new BuildRegexOperator(TokenFactory.getROperator(x)));
            else if(x == TokenFactory.getSpecialChar()){
                String xy = "" + x + r.charAt(++i);
                if(TokenFactory.isRChar(xy)) list.add(new BuildRegexChar(TokenFactory.getRChar(xy)));
                else if(TokenFactory.isRGroup(xy)) list.add(new BuildRegexUnion(TokenFactory.getRGroup(xy)));
                else throw new RegexReaderException();
            }
            else if(x != ' ') list.add(new BuildRegexChar(x));
            i++;
        }
        if(i == r.length()-1){
            char x = r.charAt(i);
            if(TokenFactory.isROperator(x)) list.add(new BuildRegexOperator(TokenFactory.getROperator(x)));
            else if(x == TokenFactory.getSpecialChar()) throw new RegexReaderException();
            else if (x != ' ') list.add(new BuildRegexChar(x));
        }

        return list;
    }

    private static void checkParenthesis(String r) throws RegexReaderException {
        int depth = 0;
        int i = 0;
        while(i < r.length()){
            if(r.charAt(i) == '(') depth++;
            else if(r.charAt(i) == ')'){
                depth--;
                if(depth<0) throw new RegexReaderException();
            }
            i++;
        }
        if(depth != 0) throw new RegexReaderException();
    }

    private static void step(List<BuildRegex> list) throws RegexReaderException {
        int n = list.size();
        if(n == 0) throw new RegexReaderException();

        BuildRegex last = list.get(n-1);
        BuildRegex preLast = null;
        if(n>1) preLast = list.get(n-2);

        if(n == 2 && isPattern_xc(list)) throw new RegexReaderException();  // x)
        else if(n == 3 && isPattern_xux(list)) stepUnion(list);             // x|x
        else if(n>3 && isPattern_xux(list)){
            if(list.get(n-4).isOpen()) stepUnion(list);                     //.(x|x
            else if(list.get(n-4).isUnion()) stepUnion(list);               //.|x|x
            else stepLastTwo(list);                                         //.?x|x
        }
        else if(n>2 && isPattern_aux(list)) stepLastTwo(list);              //.?|x
        else if(n>2 && isPattern_xc(list)){
            if(list.get(n-3).isOpen()) step_oxc(list);                      //.(x)
            else stepLastOne(list);                                         //.?x)
        }
        else if(n>1 && last.isStar()){
            if(preLast.isStar()) throw new RegexReaderException();          //.**
            else if(preLast.isPlus()) throw new RegexReaderException();     //.+*
            else if(preLast.isNotOperator()) stepStar(list);                //.x*
            else stepLastOne(list);                                         //.?*
        }
        else if(n>1 && last.isPlus()){
            if(preLast.isStar()) throw new RegexReaderException();          //.*+
            else if(preLast.isPlus()) throw new RegexReaderException();     //.++
            else if(preLast.isNotOperator()) stepPlus(list);                //.x+
            else stepLastOne(list);                                         //.?+
        }
        else if(n>1 && last.isNotOperator()){
            if(preLast.isOpen()) throw new RegexReaderException();          //.(x
            else if(preLast.isNotOperator()) stepConcat(list);              //.xx
            else stepLastOne(list);                                         //.?x
        }
        else if(n>1 && last.isClose()) stepLastOne(list);                   //.?)
        else if(last.isVoid()) stepVoid(list);                              //.#
        else if(last.isEmpty()) stepEmpty(list);                            //./
        else if(last.isOpen()) throw new RegexReaderException();            //.(
        else if(last.isUnion()) throw new RegexReaderException();           //.|
        else throw new RegexReaderException();
    }

    //Private step

    private static boolean isPattern_xc(List<BuildRegex> list){
        int n = list.size();
        return list.get(n-2).isNotOperator() && list.get(n-1).isClose();
    }

    private static boolean isPattern_xux(List<BuildRegex> list){
        int n = list.size();
        return list.get(n-3).isNotOperator() && list.get(n-2).isUnion() && list.get(n-1).isNotOperator();
    }

    private static boolean isPattern_aux(List<BuildRegex> list){
        int n = list.size();
        return list.get(n-2).isUnion() && list.get(n-1).isNotOperator();
    }

    private static void step_oxc(List<BuildRegex> list){
        list.remove(list.size()-1);
        BuildRegex x = list.remove(list.size()-1);
        list.remove(list.size()-1);
        list.add(x);
    }

    private static void stepLastTwo(List<BuildRegex> list) throws RegexReaderException {
        BuildRegex a = list.remove(list.size()-1);
        BuildRegex b = list.remove(list.size()-1);
        step(list);
        list.add(b);
        list.add(a);
    }

    private static void stepLastOne(List<BuildRegex> list) throws RegexReaderException {
        BuildRegex a = list.remove(list.size()-1);
        step(list);
        list.add(a);
    }

    private static void stepStar(List<BuildRegex> list){
        list.remove(list.size()-1);
        BuildRegex x = list.remove(list.size()-1);
        list.add(new BuildRegexStar(x));
    }

    private static void stepPlus(List<BuildRegex> list){
        list.remove(list.size()-1);
        BuildRegex x = list.remove(list.size()-1);
        list.add(new BuildRegexPlus(x));
    }

    private static void stepUnion(List<BuildRegex> list) {
        BuildRegex b = list.remove(list.size()-1);
        list.remove(list.size()-1);
        BuildRegex a = list.remove(list.size()-1);
        list.add(new BuildRegexUnion(a,b));
    }

    private static void stepConcat(List<BuildRegex> list){
        BuildRegex b = list.remove(list.size()-1);
        BuildRegex a = list.remove(list.size()-1);
        list.add(new BuildRegexConcat(a,b));
    }

    private static void stepVoid(List<BuildRegex> list){
        list.remove(list.size()-1);
        list.add(new BuildRegexVoid());
    }

    private static void stepEmpty(List<BuildRegex> list){
        list.remove(list.size()-1);
        list.add(new BuildRegexEmpty());
    }

    //Private classes build

    private static abstract class BuildRegex {
        boolean isNotOperator(){return true;}
        boolean isOpen()  {return false;}
        boolean isClose() {return false;}
        boolean isUnion() {return false;}
        boolean isStar()  {return false;}
        boolean isPlus()  {return false;}
        boolean isEmpty() {return false;}
        boolean isVoid()  {return false;}
        abstract RegularExpression toRegex() throws RegexReaderException;
    }
    private static class BuildRegexOperator extends BuildRegex {
        private final RegexOperator op;
        public BuildRegexOperator(RegexOperator r){
            op = r;
        }

        boolean isNotOperator(){return false;}
        boolean isOpen()  {return op == RegexOperator.OPEN;}
        boolean isClose() {return op == RegexOperator.CLOSE;}
        boolean isUnion() {return op == RegexOperator.UNION;}
        boolean isStar()  {return op == RegexOperator.STAR;}
        boolean isPlus()  {return op == RegexOperator.PLUS;}
        boolean isEmpty() {return op == RegexOperator.EMPTY;}
        boolean isVoid()  {return op == RegexOperator.VOID;}

        public RegularExpression toRegex() throws RegexReaderException {
            if(isEmpty()) return new RegexEmptyChar();
            if(isVoid()) return new RegexVoid();
            throw new RegexReaderException();
        }
    }
    private static class BuildRegexChar extends BuildRegex {
        private final char c;
        public BuildRegexChar(char c){this.c = c;}
        public RegularExpression toRegex(){return new RegexChar(c);}
    }
    private static class BuildRegexConcat extends BuildRegex {
        private final BuildRegex a;
        private final BuildRegex b;
        public BuildRegexConcat(BuildRegex a, BuildRegex b){
            this.a = a;
            this.b = b;
        }
        public RegularExpression toRegex() throws RegexReaderException {return new RegexConcat(a.toRegex(), b.toRegex());}
    }
    private static class BuildRegexUnion extends BuildRegex {
        private final RegularExpression r;
        private final boolean rgroup;
        private final BuildRegex a;
        private final BuildRegex b;
        public BuildRegexUnion(RegularExpression regex) {
            r = regex;
            rgroup = true;
            a = new BuildRegexVoid();
            b = new BuildRegexVoid();
        }
        public BuildRegexUnion(BuildRegex a, BuildRegex b){
            r = new RegexVoid();
            rgroup = false;
            this.a = a;
            this.b = b;
        }
        public RegularExpression toRegex() throws RegexReaderException {
            if(rgroup) return r;
            else return new RegexUnion(a.toRegex(), b.toRegex());
        }
    }
    private static class BuildRegexStar extends BuildRegex {
        private final BuildRegex x;
        public BuildRegexStar(BuildRegex x){this.x = x;}
        public RegularExpression toRegex() throws RegexReaderException {return new RegexStar(x.toRegex());}
    }
    private static class BuildRegexPlus extends BuildRegex {
        private final BuildRegex x;
        public BuildRegexPlus(BuildRegex x){this.x = x;}
        public RegularExpression toRegex() throws RegexReaderException {return new RegexConcat(x.toRegex(), new RegexStar(x.toRegex()));}
    }
    private static class BuildRegexVoid extends BuildRegex {
        public BuildRegexVoid(){}
        public RegularExpression toRegex(){return new RegexVoid();}
    }
    private static class BuildRegexEmpty extends BuildRegex {
        public BuildRegexEmpty(){}
        public RegularExpression toRegex(){return new RegexEmptyChar();}
    }

}
