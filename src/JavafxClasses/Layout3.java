package JavafxClasses;

import Utils.Utility;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Layout3 implements Layout{
    private final String title;
    private final String description;
    private final String left;
    private final String right;
    private final String under;
    private final CallMenuTwo call;

    private TextArea textAreaLeft;
    private TextArea textAreaRight;
    private TextArea textAreaUnder;

    public Layout3(String title, String description, String left, String right, String under, CallMenuTwo call){
        this.title = title;
        this.description = description;
        this.left = left;
        this.under = under;
        this.right = right;
        this.call = call;
    }

    public Pane build(){
        VBox layout = new VBox();
        Properties.centerVBox(layout);

        Label a = new Label(title);
        Properties.title(a);

        Label b = new Label(description);
        Properties.description(b);

        Pane c = buildButtons();

        Pane d = buildGridUp();

        Pane e = buildGridDown();

        layout.getChildren().addAll(a,b,c,d,e);
        return layout;
    }

    private HBox buildButtons(){
        HBox layout = new HBox();
        Properties.buttonBox(layout);

        Button loadA = new Button(Utility.loadFileA);
        Properties.button(loadA);
        loadA.setOnAction(event -> Utility.loadFile(textAreaLeft));

        Button loadB = new Button(Utility.loadFileB);
        Properties.button(loadB);
        loadB.setOnAction(actionEvent -> Utility.loadFile(textAreaRight));

        Button apply = new Button(Utility.apply);
        Properties.button(apply);
        apply.setOnAction(event -> textAreaUnder.setText(call.call(textAreaLeft.getText(),textAreaRight.getText())));

        layout.getChildren().addAll(loadA, loadB, apply);
        return layout;
    }

    private GridPane buildGridUp(){
        GridPane layout = new GridPane();
        Properties.grid(layout);

        Text textLeft = new Text(left);
        Properties.gridCompanion(textLeft);

        Text textRight = new Text(right);
        Properties.gridCompanion(textRight);

        textAreaLeft = new TextArea();
        Properties.textAreaEdit(textAreaLeft);

        textAreaRight = new TextArea();
        Properties.textAreaEdit(textAreaRight);

        GridPane.setConstraints(textLeft,0,0);
        GridPane.setConstraints(textRight,1,0);
        GridPane.setConstraints(textAreaLeft,0,1);
        GridPane.setConstraints(textAreaRight,1,1);

        layout.getChildren().addAll(textLeft, textRight, textAreaLeft, textAreaRight);
        return layout;
    }

    private GridPane buildGridDown(){
        GridPane layout = new GridPane();
        Properties.grid(layout);

        Text text = new Text(under);
        Properties.gridCompanion(text);

        textAreaUnder = new TextArea();
        Properties.textAreaResult(textAreaUnder);

        GridPane.setConstraints(text,0,0);
        GridPane.setConstraints(textAreaUnder,0,1);

        layout.getChildren().addAll(text, textAreaUnder);
        return layout;
    }
}
