package JavafxClasses;

import Automatons.Dfa;
import Automatons.Nfa;
import Factory.Reader;

import Functionalities.Menu;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class Layouts {
    public static StackPane testOne(){
        Button button = new Button();
        button.setText("Click");
        StackPane layout = new StackPane();
        layout.getChildren().add(button);
        return layout;
    }

    public static BorderPane testTwo(){
        BorderPane border = new BorderPane();
        VBox vbox = new VBox();
        //vbox.setPadding(new Insets(10));
        //vbox.setSpacing(8);

        Text title = new Text("Data");
        //title.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        vbox.getChildren().add(title);

        Hyperlink options[] = new Hyperlink[] {
                new Hyperlink("Sales"),
                new Hyperlink("Marketing"),
                new Hyperlink("Distribution"),
                new Hyperlink("Costs")};

        for (int i=0; i<4; i++) {
            //VBox.setMargin(options[i], new Insets(0, 0, 0, 8));
            vbox.getChildren().add(options[i]);
        }
        border.setLeft(vbox);
        return border;
    }

    //CFG MAIN

    public static Pane equalCfgCfg(){
        String title = TextFactory.titleEqualCfgCfg();
        String description = TextFactory.desEqualCfgCfg();
        String left = TextFactory.miniCfgA();
        String middle = TextFactory.miniCfgB();
        String right = TextFactory.miniResult();
        CallTwo call = CallFactory.callEqualCfgCfg();

        ThreeTextAreas main = new ThreeTextAreas(title, description, left, middle, right, call);
        return main.build();
    }

    public static Pane counterExapleCfgs(){
        String title = TextFactory.titleCounterExampleCfgs();
        String description = TextFactory.desCounterExampleCfgs();
        String left = TextFactory.miniCfgA();
        String middle = TextFactory.miniCfgB();
        String right = TextFactory.miniResult();
        CallTwo call = CallFactory.callFindCounterExampleCfgs();

        ThreeTextAreas main = new ThreeTextAreas(title, description, left, middle, right, call);
        return main.build();
    }

    public static Pane checkAmbiguity(){
        String title = TextFactory.titleCheckAmbiguity();
        String description = TextFactory.desCheckAmbiguity();
        String left = TextFactory.miniCfg();
        String right = TextFactory.miniResult();
        CallOne call = CallFactory.callCheckAmbiguity();

        TwoTextAreas main = new TwoTextAreas(title, description, left, right, call);
        return main.build();
    }

    //COMPARE

    public static Pane equalDfaDfa(){
        String title = TextFactory.titleEqualDfaDfa();
        String description = TextFactory.desEqualDfaDfa();
        String left = TextFactory.miniDfaA();
        String middle = TextFactory.miniDfaB();
        String right = TextFactory.miniResult();
        CallTwo call = CallFactory.callEqualDfaDfa();

        ThreeTextAreas main = new ThreeTextAreas(title, description, left, middle, right, call);
        return main.build();
    }

    public static Pane equalDfaNfa(){
        String title = TextFactory.titleEqualDfaNfa();
        String description = TextFactory.desEqualDfaNfa();
        String left = TextFactory.miniDfa();
        String middle = TextFactory.miniNfa();
        String right = TextFactory.miniResult();
        CallTwo call = CallFactory.callEqualDfaNfa();

        ThreeTextAreas main = new ThreeTextAreas(title, description, left, middle, right, call);
        return main.build();
    }

    public static Pane equalDfaRegex(){
        String title = TextFactory.titleEqualDfaRegex();
        String description = TextFactory.desEqualDfaRegex();
        String left = TextFactory.miniDfa();
        String middle = TextFactory.miniRegex();
        String right = TextFactory.miniResult();
        CallTwo call = CallFactory.callEqualDfaRegex();

        ThreeTextAreas main = new ThreeTextAreas(title, description, left, middle, right, call);
        return main.build();
    }

    public static Pane equalNfaNfa(){
        String title = TextFactory.titleEqualNfaNfa();
        String description = TextFactory.desEqualNfaNfa();
        String left = TextFactory.miniNfaA();
        String middle = TextFactory.miniNfaB();
        String right = TextFactory.miniResult();
        CallTwo call = CallFactory.callEqualNfaNfa();

        ThreeTextAreas main = new ThreeTextAreas(title, description, left, middle, right, call);
        return main.build();
    }

    public static Pane equalNfaRegex(){
        String title = TextFactory.titleEqualNfaRegex();
        String description = TextFactory.desEqualNfaRegex();
        String left = TextFactory.miniNfa();
        String middle = TextFactory.miniRegex();
        String right = TextFactory.miniResult();
        CallTwo call = CallFactory.callEqualNfaRegex();

        ThreeTextAreas main = new ThreeTextAreas(title, description, left, middle, right, call);
        return main.build();
    }

    public static Pane equalRegexRegex(){
        String title = TextFactory.titleEqualRegexRegex();
        String description = TextFactory.desEqualRegexRegex();
        String left = TextFactory.miniRegexA();
        String middle = TextFactory.miniRegexB();
        String right = TextFactory.miniResult();
        CallTwo call = CallFactory.callEqualRegexRegex();

        ThreeTextAreas main = new ThreeTextAreas(title, description, left, middle, right, call);
        return main.build();
    }

    //DFA transformations

    public static Pane minimizeDfa(){
        String title = TextFactory.titleMinimizeDfa();
        String description = TextFactory.desMinimizeDfa();
        String left = TextFactory.miniDfa();
        String right = TextFactory.miniResult();
        CallOne call = CallFactory.callMinimizeDfa();

        TwoTextAreas main = new TwoTextAreas(title, description, left, right, call);
        return main.build();
    }

    public static Pane reverseDfa(){
        String title = TextFactory.titleReverseDfa();
        String description = TextFactory.desReverseDfa();
        String left = TextFactory.miniDfa();
        String right = TextFactory.miniResult();
        CallOne call = CallFactory.callReverseDfa();

        TwoTextAreas main = new TwoTextAreas(title, description, left, right, call);
        return main.build();
    }

    public static Pane complementDfa(){
        String title = TextFactory.titleComplementDfa();
        String description = TextFactory.desComplementDfa();
        String left = TextFactory.miniDfa();
        String right = TextFactory.miniResult();
        CallOne call = CallFactory.callComplementDfa();

        TwoTextAreas main = new TwoTextAreas(title, description, left, right, call);
        return main.build();
    }

    //TRANSFORM

    public static Pane transformDfaNfa(){
        String title = TextFactory.titleTransformDfaNfa();
        String description = TextFactory.desTransformDfaNfa();
        String left = TextFactory.miniDfa();
        String right = TextFactory.miniNfa();
        CallOne call = CallFactory.callTransformDfaNfa();

        TwoTextAreas main = new TwoTextAreas(title, description, left, right, call);
        return main.build();
    }

    public static Pane transformDfaRegex(){
        String title = TextFactory.titleTransformDfaRegex();
        String description = TextFactory.desTransformDfaRegex();
        String left = TextFactory.miniDfa();
        String right = TextFactory.miniRegex();
        CallOne call = CallFactory.callTransformDfaRegex();

        TwoTextAreas main = new TwoTextAreas(title, description, left, right, call);
        return main.build();
    }

    public static Pane transformNfaDfa(){
        String title = TextFactory.titleTransformNfaDfa();
        String description = TextFactory.desTransformNfaDfa();
        String left = TextFactory.miniNfa();
        String right = TextFactory.miniDfa();
        CallOne call = CallFactory.callTransformNfaDfa();

        TwoTextAreas main = new TwoTextAreas(title, description, left, right, call);
        return main.build();
    }

    public static Pane transformNfaRegex(){
        String title = TextFactory.titleTransformNfaRegex();
        String description = TextFactory.desTransformNfaRegex();
        String left = TextFactory.miniNfa();
        String right = TextFactory.miniRegex();
        CallOne call = CallFactory.callTransformNfaRegex();

        TwoTextAreas main = new TwoTextAreas(title, description, left, right, call);
        return main.build();
    }

    public static Pane transformRegexDfaMinim(){
        String title = TextFactory.titleTransformRegexDfaMinim();
        String description = TextFactory.desTransformRegexDfaMinim();
        String left = TextFactory.miniRegex();
        String right = TextFactory.miniDfa();
        CallOne call = CallFactory.callTransformRegexDfaMinim();

        TwoTextAreas main = new TwoTextAreas(title, description, left, right, call);
        return main.build();
    }

    public static Pane transformRegexDfaNotMinim(){
        String title = TextFactory.titleTransformRegexDfaNotMinim();
        String description = TextFactory.desTransformRegexDfaNotMinim();
        String left = TextFactory.miniRegex();
        String right = TextFactory.miniDfa();
        CallOne call = CallFactory.callTransformRegexDfaNotMinim();

        TwoTextAreas main = new TwoTextAreas(title, description, left, right, call);
        return main.build();
    }

    public static Pane transformRegexNfaMinim(){
        String title = TextFactory.titleTransformRegexNfaMinim();
        String description = TextFactory.desTransformRegexNfaMinim();
        String left = TextFactory.miniRegex();
        String right = TextFactory.miniNfa();
        CallOne call = CallFactory.callTransformRegexNfaMinim();

        TwoTextAreas main = new TwoTextAreas(title, description, left, right, call);
        return main.build();
    }

    public static Pane transformRegexNfaNotMinim(){
        String title = TextFactory.titleTransformRegexNfaNotMinim();
        String description = TextFactory.desTransformRegexNfaNotMinim();
        String left = TextFactory.miniRegex();
        String right = TextFactory.miniNfa();
        CallOne call = CallFactory.callTransformRegexNfaNotMinim();

        TwoTextAreas main = new TwoTextAreas(title, description, left, right, call);
        return main.build();
    }

    public static Pane transformDfaCfg(){
        String title = TextFactory.titleTransformDfaCfg();
        String description = TextFactory.desTransformDfaCfg();
        String left = TextFactory.miniDfa();
        String right = TextFactory.miniCfg();
        CallOne call = CallFactory.callTransformDfaCfg();

        TwoTextAreas main = new TwoTextAreas(title, description, left, right, call);
        return main.build();
    }

    public static Pane transformNfaCfg(){
        String title = TextFactory.titleTransformNfaCfg();
        String description = TextFactory.desTransformNfaCfg();
        String left = TextFactory.miniNfa();
        String right = TextFactory.miniCfg();
        CallOne call = CallFactory.callTransformNfaCfg();

        TwoTextAreas main = new TwoTextAreas(title, description, left, right, call);
        return main.build();
    }

    public static Pane transformRegexCfg(){
        String title = TextFactory.titleTransformRegexCfg();
        String description = TextFactory.desTransformRegexCfg();
        String left = TextFactory.miniRegex();
        String right = TextFactory.miniCfg();
        CallOne call = CallFactory.callTransformRegexCfg();

        TwoTextAreas main = new TwoTextAreas(title, description, left, right, call);
        return main.build();
    }

    //CHECK WORDS

    public static Pane checkWordsDfa(){
        String title = TextFactory.titleCheckWordsDfa();
        String description = TextFactory.desCheckWordsDfa();
        String left = TextFactory.miniDfa();
        String middle = TextFactory.miniWords();
        String right = TextFactory.miniResult();
        CallTwo call = CallFactory.callCheckWordsDfa();

        ThreeTextAreas main = new ThreeTextAreas(title, description, left, middle, right, call);
        return main.build();
    }

    public static Pane checkWordsNfa(){
        String title = TextFactory.titleCheckWordsNfa();
        String description = TextFactory.desCheckWordsNfa();
        String left = TextFactory.miniNfa();
        String middle = TextFactory.miniWords();
        String right = TextFactory.miniResult();
        CallTwo call = CallFactory.callCheckWordsNfa();

        ThreeTextAreas main = new ThreeTextAreas(title, description, left, middle, right, call);
        return main.build();
    }

    public static Pane checkWordsRegex(){
        String title = TextFactory.titleCheckWordsRegex();
        String description = TextFactory.desCheckWordsRegex();
        String left = TextFactory.miniRegex();
        String middle = TextFactory.miniWords();
        String right = TextFactory.miniResult();
        CallTwo call = CallFactory.callCheckWordsRegex();

        ThreeTextAreas main = new ThreeTextAreas(title, description, left, middle, right, call);
        return main.build();
    }

    public static Pane checkWordsCfg(){
        String title = TextFactory.titleCheckWordsCfg();
        String description = TextFactory.desCheckWordsCfg();
        String left = TextFactory.miniCfg();
        String middle = TextFactory.miniWords();
        String right = TextFactory.miniResult();
        CallTwo call = CallFactory.callCheckWordsCfg();

        ThreeTextAreas main = new ThreeTextAreas(title, description, left, middle, right, call);
        return main.build();
    }

    //GENERATE WORDS

    public static Pane generateWordsDfa(){
        String title = TextFactory.titleGenerateWordsDfa();
        String description = TextFactory.desGenerateWordsDfa();
        String left = TextFactory.miniDfa();
        String right = TextFactory.miniResult();
        CallOne call = CallFactory.callGenerateWordsDfa();

        TwoTextAreas main = new TwoTextAreas(title, description, left, right, call);
        return main.build();
    }

    public static Pane generateWordsNfa(){
        String title = TextFactory.titleGenerateWordsNfa();
        String description = TextFactory.desGenerateWordsNfa();
        String left = TextFactory.miniNfa();
        String right = TextFactory.miniResult();
        CallOne call = CallFactory.callGenerateWordsNfa();

        TwoTextAreas main = new TwoTextAreas(title, description, left, right, call);
        return main.build();
    }

    public static Pane generateWordsRegex(){
        String title = TextFactory.titleGenerateWordsRegex();
        String description = TextFactory.desGenerateWordsRegex();
        String left = TextFactory.miniRegex();
        String right = TextFactory.miniResult();
        CallOne call = CallFactory.callGenerateWordsRegex();

        TwoTextAreas main = new TwoTextAreas(title, description, left, right, call);
        return main.build();
    }

    public static Pane generateWordsCfg(){
        String title = TextFactory.titleGenerateWordsCfg();
        String description = TextFactory.desGenerateWordsCfg();
        String left = TextFactory.miniCfg();
        String right = TextFactory.miniResult();
        CallOne call = CallFactory.callGenerateWordsCfg();

        TwoTextAreas main = new TwoTextAreas(title, description, left, right, call);
        return main.build();
    }

    //CFG transformations

    public static Pane simplifyGrammar(){
        String title = TextFactory.titleSimplifyGrammar();
        String description = TextFactory.desSimplifyGrammar();
        String left = TextFactory.miniCfg();
        String right = TextFactory.miniResult();
        CallOne call = CallFactory.callSimplifyGrammar();

        TwoTextAreas main = new TwoTextAreas(title, description, left, right, call);
        return main.build();
    }

    public static Pane transformChomsky(){
        String title = TextFactory.titleTransformChomsky();
        String description = TextFactory.desTransformChomsky();
        String left = TextFactory.miniCfg();
        String right = TextFactory.miniResult();
        CallOne call = CallFactory.callTransformChomsky();

        TwoTextAreas main = new TwoTextAreas(title, description, left, right, call);
        return main.build();
    }

    public static Pane transformGriebach(){
        String title = TextFactory.titleTransformGriebach();
        String description = TextFactory.desTransformGriebach();
        String left = TextFactory.miniCfg();
        String right = TextFactory.miniResult();
        CallOne call = CallFactory.callTransformGriebach();

        TwoTextAreas main = new TwoTextAreas(title, description, left, right, call);
        return main.build();
    }

}
