package Factory;

import AutomatonElements.AutomatonData;
import RegularExpressions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Reader {

    //Read automaton

    public AutomatonData readAutomatonFile(String filename) throws FileNotFoundException, AutomatonReaderException {
        Scanner sc = new Scanner(new File(filename));
        AutomatonData data = new AutomatonData();

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
            throw new AutomatonReaderException();
        }

        return data;
    }

    //Read regular expression

    public RegularExpression readRegularExpressionFile(String filename) throws FileNotFoundException, RegexReaderException {
        Scanner sc = new Scanner(new File(filename));
        StringBuilder r = new StringBuilder();
        while(sc.hasNextLine()) r.append(sc.nextLine());

        BuildRegexList list = buildList(r.toString());
        BuildRegex build = list.buildParenthesis();
        build = build.buildStar();
        build = build.buildPlus();
        build = build.buildUnion();
        build = build.buildConcat();
        return build.getRegex();
    }

    private BuildRegexList buildList(String s) throws RegexReaderException {
        BuildRegexList build = new BuildRegexList();
        int i = 0;
        while (i < s.length()){
            char x = s.charAt(i);
            switch (x) {
                case '(' -> build.add(new BuildRegexElem(RegexTypeElement.OPEN));
                case ')' -> build.add(new BuildRegexElem(RegexTypeElement.CLOSE));
                case '|' -> build.add(new BuildRegexElem(RegexTypeElement.UNION));
                case '*' -> build.add(new BuildRegexElem(RegexTypeElement.STAR));
                case '+' -> build.add(new BuildRegexElem(RegexTypeElement.PLUS));
                case '#' -> build.add(new BuildRegexVoid());
                case '\\'-> build.add(new BuildRegexEmpty());
                default  -> build.add(new BuildRegexChar(x));
                case ' ' -> {}
                case '$' -> {
                    if (i == s.length() - 1) throw new RegexReaderException();
                    char y = s.charAt(++i);
                    boolean eight = (y=='(' || y==')' || y=='|' || y=='*' || y=='+' || y=='#' || y=='\\' || y=='$');

                    if(y == 's') build.add(new BuildRegexChar(' '));
                    else if(eight) build.add(new BuildRegexChar(y));
                    else throw new RegexReaderException();
                }
            }
            i++;
        }
        return build;
    }

    //Private regex builder classes

    enum RegexTypeElement{
        OPEN, CLOSE, UNION, STAR, PLUS
    }

    private abstract static class BuildRegex {
        public boolean isOpenElement() {return false;}
        public boolean isCloseElement(){return false;}
        public boolean isStarElement() {return false;}
        public boolean isPlusElement() {return false;}
        public boolean isUnionElement(){return false;}

        public void addListDepth(int depth) throws RegexReaderException {
            throw new RegexReaderException();
        }

        public void add(BuildRegex r, int depth) throws RegexReaderException {
            throw new RegexReaderException();
        }

        public abstract BuildRegex buildStar();
        public abstract BuildRegex buildPlus();
        public abstract BuildRegex buildUnion();
        public abstract BuildRegex buildConcat();
        public abstract RegularExpression getRegex() throws RegexReaderException;
    }

    private static class BuildRegexList extends BuildRegex {
        private final List<BuildRegex> list;

        public BuildRegexList(){
            list = new ArrayList<>();
        }

        public void add(BuildRegex r){
            list.add(r);
        }

        public BuildRegexList buildParenthesis() throws RegexReaderException {
            BuildRegexList build = new BuildRegexList();
            int parenthesis = 0;
            for(BuildRegex r : list){
                if(r.isOpenElement()){
                    build.addListDepth(parenthesis);
                    parenthesis++;
                }
                else if(r.isCloseElement()){
                    parenthesis--;
                    if(parenthesis < 0) throw new RegexReaderException();
                }
                else build.add(r, parenthesis);
            }
            if(parenthesis != 0) throw new RegexReaderException();
            return build;
        }

        public void addListDepth(int depth) throws RegexReaderException {
            if(depth == 0) list.add(new BuildRegexList());
            else if(list.isEmpty()) throw new RegexReaderException();
            else list.getLast().addListDepth(depth-1);
        }

        public void add(BuildRegex r, int depth) throws RegexReaderException {
            if(depth == 0) list.add(r);
            else if(list.isEmpty()) throw new RegexReaderException();
            else list.getLast().add(r, depth-1);
        }

        public BuildRegex buildStar() {
            BuildRegexList aux = new BuildRegexList();
            int i = 0;
            while(i < list.size()-1){
                if(list.get(i+1).isStarElement()){
                    aux.add(new BuildRegexStar(list.get(i).buildStar()));
                    i++;
                }
                else{
                    aux.add(list.get(i).buildStar());
                }
                i++;
            }
            if(i == list.size()) aux.add(list.getLast().buildStar());
            return aux;
        }

        public BuildRegex buildPlus() {
            BuildRegexList aux = new BuildRegexList();
            int i = 0;
            while(i < list.size()-1){
                if(list.get(i+1).isPlusElement()){
                    aux.add(new BuildRegexPlus(list.get(i).buildPlus()));
                    i++;
                }
                else{
                    aux.add(list.get(i).buildPlus());
                }
                i++;
            }
            if(i == list.size()) aux.add(list.getLast().buildPlus());
            return aux;
        }

        public BuildRegex buildUnion() {
            BuildRegexUnion union = new BuildRegexUnion();
            BuildRegexList inner = new BuildRegexList();
            for (BuildRegex r : list){
                if(!r.isUnionElement()) inner.add(r.buildUnion());
                else{
                    union.add(inner);
                    inner = new BuildRegexList();
                }
            }
            union.add(inner);
            return union;
        }

        public BuildRegex buildConcat() {
            BuildRegexConcat concat = new BuildRegexConcat();
            for(BuildRegex r : list) concat.add(r.buildConcat());
            return concat;
        }

        public RegularExpression getRegex() throws RegexReaderException {
            if(list.size() != 1) throw new RegexReaderException();
            return list.getFirst().getRegex();
        }
    }

    private static class BuildRegexElem extends BuildRegex {
        private final RegexTypeElement element;

        public BuildRegexElem(RegexTypeElement e){
            element = e;
        }

        public boolean isOpenElement() {return element == RegexTypeElement.OPEN;}
        public boolean isCloseElement(){return element == RegexTypeElement.CLOSE;}
        public boolean isStarElement() {return element == RegexTypeElement.STAR;}
        public boolean isPlusElement() {return element == RegexTypeElement.PLUS;}
        public boolean isUnionElement(){return element == RegexTypeElement.UNION;}

        public BuildRegex buildStar()  {return this;}
        public BuildRegex buildPlus()  {return this;}
        public BuildRegex buildUnion() {return this;}
        public BuildRegex buildConcat(){return this;}

        public RegularExpression getRegex() throws RegexReaderException {
            throw new RegexReaderException();
        }
    }

    private static class BuildRegexChar extends BuildRegex {
        private final char c;

        public BuildRegexChar(char c){
            this.c = c;
        }

        public BuildRegex buildStar()  {return this;}
        public BuildRegex buildPlus()  {return this;}
        public BuildRegex buildUnion() {return this;}
        public BuildRegex buildConcat(){return this;}

        public RegularExpression getRegex() {
            return new RegexChar(c);
        }
    }

    private static class BuildRegexVoid extends BuildRegex {
        public BuildRegexVoid(){}

        public BuildRegex buildStar()  {return this;}
        public BuildRegex buildPlus()  {return this;}
        public BuildRegex buildUnion() {return this;}
        public BuildRegex buildConcat(){return this;}

        public RegularExpression getRegex() {
            return new RegexVoid();
        }
    }

    private static class BuildRegexEmpty extends BuildRegex {
        public BuildRegexEmpty(){}

        public BuildRegex buildStar()  {return this;}
        public BuildRegex buildPlus()  {return this;}
        public BuildRegex buildUnion() {return this;}
        public BuildRegex buildConcat(){return this;}

        public RegularExpression getRegex() {
            return new RegexEmptyChar();
        }
    }

    private static class BuildRegexStar extends BuildRegex {
        private final BuildRegex inner;

        public BuildRegexStar(BuildRegex r){
            inner = r;
        }

        public BuildRegex buildStar()  {return new BuildRegexStar(inner.buildStar());}
        public BuildRegex buildPlus()  {return new BuildRegexStar(inner.buildPlus());}
        public BuildRegex buildUnion() {return new BuildRegexStar(inner.buildUnion());}
        public BuildRegex buildConcat(){return new BuildRegexStar(inner.buildConcat());}

        public RegularExpression getRegex() throws RegexReaderException {
            return new RegexStar(inner.getRegex());
        }
    }

    private static class BuildRegexPlus extends BuildRegex {
        private final BuildRegex inner;

        public BuildRegexPlus(BuildRegex r){
            inner = r;
        }

        public BuildRegex buildStar()  {return new BuildRegexPlus(inner.buildStar());}
        public BuildRegex buildPlus()  {return new BuildRegexPlus(inner.buildPlus());}
        public BuildRegex buildUnion() {return new BuildRegexPlus(inner.buildUnion());}
        public BuildRegex buildConcat(){return new BuildRegexPlus(inner.buildConcat());}

        public RegularExpression getRegex() throws RegexReaderException {
            return new RegexConcat(inner.getRegex(), new RegexStar(inner.getRegex()));
        }
    }

    private static class BuildRegexUnion extends BuildRegex {
        private final List<BuildRegex> list;

        public BuildRegexUnion(){
            list = new ArrayList<>();
        }

        public void add(BuildRegex r){
            list.add(r);
        }

        public BuildRegex buildStar(){
            BuildRegexUnion union = new BuildRegexUnion();
            for(BuildRegex r : list) union.add(r.buildStar());
            return union;
        }

        public BuildRegex buildPlus(){
            BuildRegexUnion union = new BuildRegexUnion();
            for(BuildRegex r : list) union.add(r.buildPlus());
            return union;
        }

        public BuildRegex buildUnion(){
            BuildRegexUnion union = new BuildRegexUnion();
            for(BuildRegex r : list) union.add(r.buildUnion());
            return union;
        }

        public BuildRegex buildConcat(){
            BuildRegexUnion union = new BuildRegexUnion();
            for(BuildRegex r : list) union.add(r.buildConcat());
            return union;
        }

        public RegularExpression getRegex() throws RegexReaderException {
            if(list.isEmpty()) throw new RegexReaderException();

            List<BuildRegex> aux = new ArrayList<>(list);
            RegularExpression regex = aux.removeLast().getRegex();
            while(!aux.isEmpty()){
                regex = new RegexUnion(aux.removeLast().getRegex(), regex);
            }
            return regex;
        }
    }

    private static class BuildRegexConcat extends BuildRegex {
        private final List<BuildRegex> list;

        public BuildRegexConcat(){
            list = new ArrayList<>();
        }

        public void add(BuildRegex r){
            list.add(r);
        }

        public BuildRegex buildStar(){
            BuildRegexConcat concat = new BuildRegexConcat();
            for(BuildRegex r : list) concat.add(r.buildStar());
            return concat;
        }

        public BuildRegex buildPlus(){
            BuildRegexConcat concat = new BuildRegexConcat();
            for(BuildRegex r : list) concat.add(r.buildPlus());
            return concat;
        }

        public BuildRegex buildUnion(){
            BuildRegexConcat concat = new BuildRegexConcat();
            for(BuildRegex r : list) concat.add(r.buildUnion());
            return concat;
        }

        public BuildRegex buildConcat(){
            BuildRegexConcat concat = new BuildRegexConcat();
            for(BuildRegex r : list) concat.add(r.buildConcat());
            return concat;
        }

        public RegularExpression getRegex() throws RegexReaderException {
            if(list.isEmpty()) throw new RegexReaderException();

            List<BuildRegex> aux = new ArrayList<>(list);
            RegularExpression regex = aux.removeLast().getRegex();
            while(!aux.isEmpty()){
                regex = new RegexConcat(aux.removeLast().getRegex(), regex);
            }
            return regex;
        }
    }


}
