package JavafxClasses;

import Automatons.Dfa;
import Factory.Reader;
import Functionalities.Menu;
import Grammars.Cfg;
import Utils.Utility;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class LayoutTransformation implements Layout{
    private final Label title;
    private final Label description;

    private final ChoiceBox<String> options;
    private final Button loadFile;
    private final Button apply;

    private final Text textLeft;
    private final Text textRight;
    private final TextArea textAreaLeft;
    private final TextArea textAreaRight;

    private CallMenuOne call;
    private boolean done;

    private final String minimize   = "DFA minimize";
    private final String complement = "DFA complement";
    private final String reverse    = "DFA reverse";
    private final String simplify   = "CFG simplify";
    private final String chomsky    = "CFG chomsky";
    private final String greibach   = "CFG greibach";

    public LayoutTransformation(){
        title = new Label();
        description = new Label();
        options = new ChoiceBox<>();
        loadFile = new Button();
        apply = new Button();
        textLeft = new Text();
        textRight = new Text();
        textAreaLeft = new TextArea();
        textAreaRight = new TextArea();
        done = false;
    }

    public Pane build(){
        if(!done){
            buildElements();
            buildInteractions();
            loadMinimizeDfa();
        }

        VBox layout = new VBox();
        Properties.centerVBox(layout);

        HBox buttonBox = new HBox();
        Properties.buttonBox(buttonBox);
        buttonBox.getChildren().addAll(options, loadFile, apply);

        GridPane grid = new GridPane();
        Properties.grid(grid);
        GridPane.setConstraints(textLeft,0,0);
        GridPane.setConstraints(textRight,1,0);
        GridPane.setConstraints(textAreaLeft,0,1);
        GridPane.setConstraints(textAreaRight,1,1);
        grid.getChildren().addAll(textLeft, textRight, textAreaLeft, textAreaRight);

        layout.getChildren().addAll(title,description,buttonBox,grid);
        done = true;
        return layout;
    }

    private void buildElements(){
        Properties.title(title);
        Properties.description(description);

        options.getItems().clear();
        options.getItems().addAll(minimize, complement, reverse, simplify, chomsky, greibach);
        options.setValue(minimize);

        Properties.textOfLoadFile(loadFile);
        Properties.textOfApply(apply);

        textRight.setText(Utility.result);

        Properties.gridCompanion(textLeft);
        Properties.gridCompanion(textRight);
        Properties.textAreaEdit(textAreaLeft);
        Properties.textAreaResult(textAreaRight);
    }

    private void buildInteractions(){
        loadFile.setOnAction(event -> Utility.loadFile(textAreaLeft));
        apply.setOnAction(event -> textAreaRight.setText(call.call(textAreaLeft.getText())));

        options.setOnAction(event -> {
            if(options.getValue() != null){
                switch (options.getValue()){
                    case minimize -> loadMinimizeDfa();
                    case reverse -> loadReverseDfa();
                    case complement -> loadComplementDfa();
                    case simplify -> loadSimplifyCfg();
                    case chomsky -> loadChomskyCfg();
                    case greibach -> loadGreibachCfg();
                }
            }
        });
    }

    //Load

    private void loadMinimizeDfa(){
        title.setText("Minimize dfa");
        description.setText("Minimize a deterministic finite automaton.");
        textLeft.setText(Utility.dfa);
        call = data -> {
            try {
                Dfa dfa = Reader.readAutomatonString(data).toDfa();
                return Menu.minimizeDfa(dfa);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    private void loadReverseDfa(){
        title.setText("Reverse dfa");
        description.setText("Make the reverse of a deterministic finite automaton.\nThe result is a nondeterministic finite automaton that accepts\nthe same words as the original but reversed.");
        textLeft.setText(Utility.dfa);
        call = data -> {
            try {
                Dfa dfa = Reader.readAutomatonString(data).toDfa();
                return Menu.reverseDfa(dfa);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    private void loadComplementDfa(){
        title.setText("Complement dfa");
        description.setText("Make the complement of a deterministic finite automaton.\nThe result is a deterministic finite automaton that accepts\nthe words that the original doesn't accept.");
        textLeft.setText(Utility.dfa);
        call = data -> {
            try {
                Dfa dfa = Reader.readAutomatonString(data).toDfa();
                return Menu.complementDfa(dfa);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    private void loadSimplifyCfg(){
        title.setText("Simplify cfg");
        description.setText("Simplify a context free grammar.\nRemove non derivable rules, non reachable rules, empty rules and unit rules.");
        textLeft.setText(Utility.cfg);
        call = data -> {
            try {
                Cfg cfg = Reader.readGrammarString(data);
                return Menu.simplifyCfg(cfg);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    private void loadChomskyCfg(){
        title.setText("Transform cfg chomsky");
        description.setText("Transform a context free grammar to the chomsky form.\nOn chomsky form, every rule is \"A->BC\" or \"A->a\".");
        textLeft.setText(Utility.cfg);
        call = data -> {
            try {
                Cfg cfg = Reader.readGrammarString(data);
                return Menu.chomskyCfg(cfg);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }

    private void loadGreibachCfg(){
        title.setText("Transform cfg greibach");
        description.setText("Transform a context free grammar to the greibach form.\nOn greibach form, every derivation starts with a terminal.");
        textLeft.setText(Utility.cfg);
        call = data -> {
            try {
                Cfg cfg = Reader.readGrammarString(data);
                return Menu.greibachCfg(cfg);
            }
            catch (Exception ex){
                return ex.getMessage();
            }
        };
    }
}
