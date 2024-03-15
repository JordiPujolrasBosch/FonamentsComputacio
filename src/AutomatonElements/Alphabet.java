package AutomatonElements;

import java.util.Set;

public class Alphabet {
    private Set<Character> set;

    public static Character transform(String s) throws Exception {
        if(s.length() == 1) return s.charAt(0);
        else if(s.equals("''")) return Character.MIN_VALUE;
        else if(s.equals("space")) return ' ';
        else throw new Exception();
    }

    public void addElements(String x){
        if(x.length() == 1) set.add(x.charAt(0));
        else{
            switch (x){
                case "lettersLower" -> addLettersLower();
                case "lettersUpper" -> addLettersUpper();
                case "numbers"      -> addNumbers();
                case "space"        -> set.add(' ');
                case "''"           -> set.add(Character.MIN_VALUE);
                case "comma"        -> set.add(',');
            }
        }
    }

    private void addLettersLower(){
        set.add('a');
        set.add('b');
        set.add('c');
        set.add('d');
        set.add('e');
        set.add('f');
        set.add('g');
        set.add('h');
        set.add('i');
        set.add('j');
        set.add('k');
        set.add('l');
        set.add('m');
        set.add('n');
        set.add('o');
        set.add('p');
        set.add('q');
        set.add('r');
        set.add('s');
        set.add('t');
        set.add('u');
        set.add('v');
        set.add('w');
        set.add('x');
        set.add('y');
        set.add('z');
    }

    private void addLettersUpper(){
        set.add('A');
        set.add('B');
        set.add('C');
        set.add('D');
        set.add('E');
        set.add('F');
        set.add('G');
        set.add('H');
        set.add('I');
        set.add('J');
        set.add('K');
        set.add('L');
        set.add('M');
        set.add('N');
        set.add('O');
        set.add('P');
        set.add('Q');
        set.add('R');
        set.add('S');
        set.add('T');
        set.add('U');
        set.add('V');
        set.add('W');
        set.add('X');
        set.add('Y');
        set.add('Z');
    }

    private void addNumbers(){
        set.add('0');
        set.add('1');
        set.add('2');
        set.add('3');
        set.add('4');
        set.add('5');
        set.add('6');
        set.add('7');
        set.add('8');
        set.add('9');
    }


}
