package JavafxClasses;

import Utils.Utility;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Layout2Words {
    private final String title;
    private final String description;
    private final String left;
    private final String right;
    private final CallMenuOne call;

    private TextArea textAreaLeft;
    private TextArea textAreaRight;

    public Layout2Words(String title, String description, String left, String right, CallMenuOne call){
        this.title = title;
        this.description = description;
        this.left = left;
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

        Pane d = buildGrid();

        layout.getChildren().addAll(a,b,c,d);
        return layout;
    }

    private HBox buildButtons(){
        HBox layout = new HBox();
        Properties.buttonBox(layout);

        Text size = new Text("Size:");

        TextField inputNumber = new TextField("100");
        inputNumber.setEditable(true);
        inputNumber.setPrefWidth(80);

        Button load = new Button(Utility.loadFile);
        Properties.button(load);
        load.setOnAction(event -> Utility.loadFile(textAreaLeft));

        Button apply = new Button(Utility.apply);
        Properties.button(apply);
        apply.setOnAction(event -> {
            if(!Utility.isNumber(inputNumber.getText())) textAreaRight.setText("Enter a number");
            else{
                Utility.setInt(Integer.parseInt(inputNumber.getText()));
                textAreaRight.setText(call.call(textAreaLeft.getText()));
            }
        });

        layout.getChildren().addAll(size, inputNumber ,load, apply);
        return layout;
    }

    private GridPane buildGrid(){
        GridPane layout = new GridPane();
        Properties.grid(layout);

        Text textLeft = new Text(left);
        Properties.gridCompanion(textLeft);

        Text textRight = new Text(right);
        Properties.gridCompanion(textRight);

        textAreaLeft = new TextArea();
        Properties.textAreaEdit(textAreaLeft);

        textAreaRight = new TextArea();
        Properties.textAreaResult(textAreaRight);

        GridPane.setConstraints(textLeft,0,0);
        GridPane.setConstraints(textRight,1,0);
        GridPane.setConstraints(textAreaLeft,0,1);
        GridPane.setConstraints(textAreaRight,1,1);

        layout.getChildren().addAll(textLeft, textRight, textAreaLeft, textAreaRight);
        return layout;
    }
}
