package Factory.Builders;

import Exceptions.RegexReaderException;
import RegularExpressions.*;

import java.util.ArrayList;
import java.util.List;

public class RegexBuilder {
    public static RegularExpression buildRegex(String r) throws RegexReaderException {
        BuildRegexList list = buildTokenList(r);
        BuildRegex build = list.buildParenthesis();
        //list, void, empty, char, e_union, e_star, e_plus
        build = build.buildStarAndPlus();
        //list, void, empty, char, e_union, star, plus
        build = build.buildUnionAndConcat();
        //void, empty, char, union, star, plus, concat
        return build.getRegex();
    }

    //PRIVATE

    //+
    private static BuildRegexList buildTokenList(String r) throws RegexReaderException {
        BuildRegexList list = new BuildRegexList();
        int i = 0;
        while(i < r.length()){
            char x = r.charAt(i);
            switch (x) {
                case '(' -> list.add(new BuildRegexElem(BuildRegexType.OPEN));
                case ')' -> list.add(new BuildRegexElem(BuildRegexType.CLOSE));
                case '|' -> list.add(new BuildRegexElem(BuildRegexType.UNION));
                case '*' -> list.add(new BuildRegexElem(BuildRegexType.STAR));
                case '+' -> list.add(new BuildRegexElem(BuildRegexType.PLUS));
                case '#' -> list.add(new BuildRegexVoid());
                case '\\'-> list.add(new BuildRegexEmpty());
                default  -> list.add(new BuildRegexChar(x));
                case ' ' -> {}
                case '$' -> {
                    if (i == r.length() - 1) throw new RegexReaderException();
                    char y = r.charAt(++i);
                    boolean eight = (y=='(' || y==')' || y=='|' || y=='*' || y=='+' || y=='#' || y=='\\' || y=='$');

                    if(y == 's') list.add(new BuildRegexChar(' '));
                    else if(eight) list.add(new BuildRegexChar(y));
                    else throw new RegexReaderException();
                }
            }
            i++;
        }
        return list;
    }

    private enum BuildRegexType {
        CLOSE, OPEN, UNION, STAR, PLUS
    }

    private abstract static class BuildRegex {
        public boolean isOpenElement()  {return false;}
        public boolean isCloseElement() {return false;}
        public boolean isUnionElement() {return false;}
        public boolean isStarElement()  {return false;}
        public boolean isPlusElement()  {return false;}
        public boolean isStarOrPlusElement() {return false;}
        public boolean isStarable() {return false;}

        public abstract BuildRegex buildStarAndPlus() throws RegexReaderException;   //5
        public abstract BuildRegex buildUnionAndConcat() throws RegexReaderException;
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

        public boolean isStarable() {
            return true;
        }

        public BuildRegex buildParenthesis() throws RegexReaderException {
            BuildRegexList build = new BuildRegexList();
            List<BuildRegexList> depth = new ArrayList<>();
            depth.add(build);
            int parenthesis = 0;

            for(BuildRegex r : list){
                if(r.isOpenElement()){
                    BuildRegexList x = new BuildRegexList();
                    depth.get(depth.size()-1).add(x);
                    depth.add(x);
                    parenthesis++;
                }
                else if(r.isCloseElement()){
                    parenthesis--;
                    if(parenthesis < 0) throw new RegexReaderException();
                    depth.remove(depth.size()-1);
                }
                else{
                    depth.get(depth.size()-1).add(r);
                }
            }

            if(parenthesis != 0) throw new RegexReaderException();
            return build;
        }

        public BuildRegex buildStarAndPlus() throws RegexReaderException {
            BuildRegexList aux = new BuildRegexList();

            int i=0;
            while(i < list.size() - 1){
                if(list.get(i).isStarOrPlusElement()){
                    throw new RegexReaderException();
                }
                else if(list.get(i+1).isStarElement()){
                    if(!list.get(i).isStarable()) throw new RegexReaderException();
                    aux.add(new BuildRegexStar(list.get(i).buildStarAndPlus()));
                    i++;
                }
                else if(list.get(i+1).isPlusElement()){
                    if(!list.get(i).isStarable()) throw new RegexReaderException();
                    aux.add(new BuildRegexPlus(list.get(i).buildStarAndPlus()));
                    i++;
                }
                else{
                    aux.add(list.get(i).buildStarAndPlus());
                }
                i++;
            }
            if(i==list.size()-1) aux.add(list.get(list.size()-1).buildStarAndPlus());

            return aux;
        }

        public BuildRegex buildUnionAndConcat() throws RegexReaderException {
            BuildRegexUnion union = new BuildRegexUnion();
            BuildRegexConcat inner = new BuildRegexConcat();
            for (BuildRegex r : list){
                if(!r.isUnionElement()) inner.add(r.buildUnionAndConcat());
                else{
                    union.add(inner);
                    inner = new BuildRegexConcat();
                }
            }
            union.add(inner);
            return union;
        }

        public RegularExpression getRegex() throws RegexReaderException {
            if(list.size() != 1) throw new RegexReaderException();
            return list.get(0).getRegex();
        }
    }

    private static class BuildRegexElem extends BuildRegex {
        private final BuildRegexType element;

        public BuildRegexElem(BuildRegexType e){
            element = e;
        }

        public boolean isOpenElement()  {return element == BuildRegexType.OPEN;}
        public boolean isCloseElement() {return element == BuildRegexType.CLOSE;}
        public boolean isUnionElement() {return element == BuildRegexType.UNION;}
        public boolean isStarElement()  {return element == BuildRegexType.STAR;}
        public boolean isPlusElement()  {return element == BuildRegexType.PLUS;}

        public boolean isStarOrPlusElement() {
            return isPlusElement() || isStarElement();
        }

        public BuildRegex buildStarAndPlus() {
            return this;
        }

        public BuildRegex buildUnionAndConcat() throws RegexReaderException {
            throw new RegexReaderException();
        }

        public RegularExpression getRegex() throws RegexReaderException {
            throw new RegexReaderException();
        }
    }

    private static class BuildRegexChar extends BuildRegex {
        private final char c;

        public BuildRegexChar(char c){
            this.c = c;
        }

        public boolean isStarable() {
            return true;
        }

        public BuildRegex buildStarAndPlus() {
            return this;
        }

        public BuildRegex buildUnionAndConcat() {
            return this;
        }

        public RegularExpression getRegex() {
            return new RegexChar(c);
        }
    }

    private static class BuildRegexVoid extends BuildRegex {
        public BuildRegexVoid(){}

        public boolean isStarable(){
            return true;
        }

        public BuildRegex buildStarAndPlus() {
            return this;
        }

        public BuildRegex buildUnionAndConcat() {
            return this;
        }

        public RegularExpression getRegex() {
            return new RegexVoid();
        }
    }

    private static class BuildRegexEmpty extends BuildRegex {
        public BuildRegexEmpty(){}

        public boolean isStarable() {
            return true;
        }

        public BuildRegex buildStarAndPlus() {
            return this;
        }

        public BuildRegex buildUnionAndConcat() {
            return this;
        }

        public RegularExpression getRegex() {
            return new RegexEmptyChar();
        }
    }

    private static class BuildRegexStar extends BuildRegex {
        private final BuildRegex inner;

        public BuildRegexStar(BuildRegex r){
            inner = r;
        }

        public BuildRegex buildStarAndPlus() throws RegexReaderException {
            return new BuildRegexStar(inner.buildStarAndPlus());
        }

        public BuildRegex buildUnionAndConcat() throws RegexReaderException {
            return new BuildRegexStar(inner.buildUnionAndConcat());
        }

        public RegularExpression getRegex() throws RegexReaderException {
            return new RegexStar(inner.getRegex());
        }
    }

    private static class BuildRegexPlus extends BuildRegex {
        private final BuildRegex inner;

        public BuildRegexPlus(BuildRegex r){
            inner = r;
        }

        public BuildRegex buildStarAndPlus() throws RegexReaderException {
            return new BuildRegexPlus(inner.buildStarAndPlus());
        }

        public BuildRegex buildUnionAndConcat() throws RegexReaderException {
            return new BuildRegexPlus(inner.buildUnionAndConcat());
        }

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

        public BuildRegex buildStarAndPlus() throws RegexReaderException {
            throw new RegexReaderException();
        }

        public BuildRegex buildUnionAndConcat() throws RegexReaderException {
            throw new RegexReaderException();
        }

        public RegularExpression getRegex() throws RegexReaderException {
            if(list.isEmpty()) throw new RegexReaderException();

            List<BuildRegex> aux = new ArrayList<>(list);
            RegularExpression regex = aux.remove(aux.size()-1).getRegex();
            while(!aux.isEmpty()){
                regex = new RegexUnion(aux.remove(aux.size()-1).getRegex(), regex);
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

        public BuildRegex buildStarAndPlus() throws RegexReaderException {
            throw new RegexReaderException();
        }

        public BuildRegex buildUnionAndConcat() throws RegexReaderException {
            throw new RegexReaderException();
        }

        public RegularExpression getRegex() throws RegexReaderException {
            if(list.isEmpty()) throw new RegexReaderException();

            List<BuildRegex> aux = new ArrayList<>(list);
            RegularExpression regex = aux.remove(aux.size()-1).getRegex();
            while(!aux.isEmpty()){
                regex = new RegexConcat(aux.remove(aux.size()-1).getRegex(), regex);
            }
            return regex;
        }
    }
}
