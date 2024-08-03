package JavafxClasses;

import Utils.Utility;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.List;

public class MainLayout {
    private static final List<ItemMenu> menu = buildMenu();
    private static int pos = 1;

    public static Pane build(){
        BorderPane layout = new BorderPane();
        Utility.setBorderPane(layout);
        layout.setLeft(verticalMenu());
        layout.setCenter(menu.get(pos).action.call());
        return layout;
    }

    private static Pane verticalMenu(){
        VBox layout = new VBox();
        Properties.verticalMenu(layout);

        for(ItemMenu i : menu){
            Node node;
            if(!i.space){
                node = new Text(i.text);
                VBox.setMargin(node, new Insets(0,0,0,4));
            }
            else if(!i.active){
                Hyperlink t = new Hyperlink(i.text);
                t.setTextFill(Color.BLACK);
                t.setUnderline(false);
                VBox.setMargin(t, new Insets(0,0,0,10));
                node = t;
            }
            else{
                Hyperlink h = new Hyperlink(i.text);
                h.setOnAction(actionEvent -> move(i));
                VBox.setMargin(h, new Insets(0,0,0,10));
                node = h;
            }
            layout.getChildren().add(node);
        }

        return layout;
    }

    private static void move(ItemMenu item){
        item.active = false;
        menu.get(pos).active = true;
        pos = menu.indexOf(item);
        Utility.getBorderPane().setLeft(verticalMenu());
        Utility.getBorderPane().setCenter(item.action.call());
    }

    private static List<ItemMenu> buildMenu(){
        List<ItemMenu> list = new ArrayList<>();
        list.add(new ItemMenu("Starting", null, false, false));
        list.add(new ItemMenu("Welcome to the app", startPane(), true, false));
        list.add(new ItemMenu("Information",        infoPane(), true, true));
        list.add(new ItemMenu("Application Tools", null, false, false));
        list.add(new ItemMenu("Compare cfg & cfg",            layoutPane(new LayoutCompareCfg()), true, true));
        list.add(new ItemMenu("Counterexample cfg & cfg",     layoutPane(new LayoutCounterExample()), true, true));
        list.add(new ItemMenu("Ambiguity cfg",                layoutPane(new LayoutAmbiguity()), true, true));
        list.add(new ItemMenu("Regular languages comparison", layoutPane(new LayoutComparison()), true, true));
        list.add(new ItemMenu("Regular languages conversion", layoutPane(new LayoutConversion()), true, true));
        list.add(new ItemMenu("Check words",                  layoutPane(new LayoutCheckWords()), true, true));
        list.add(new ItemMenu("Generate words",               layoutPane(new LayoutGenerateWords()), true, true));
        list.add(new ItemMenu("Transformations",              layoutPane(new LayoutTransformation()), true, true));
        return list;
    }

    private static class ItemMenu{
        public String text;
        public boolean space;
        public boolean active;
        public CallPane action;
        public ItemMenu(String text, CallPane action, boolean space, boolean active){
            this.text = text;
            this.action = action;
            this.active = active;
            this.space = space;
        }
    }

    private static CallPane startPane(){
        return () -> {
            VBox layout = new VBox();
            layout.setAlignment(Pos.CENTER);
            layout.setPadding(new Insets(20,20,20,20));

            Label title = new Label("Regular Languages &\nContext Free Languages\nTools");
            title.setFont(Font.font("Source Code Pro", FontWeight.BOLD, 40));
            title.setPadding(new Insets(20,20,20,20));
            title.setTextAlignment(TextAlignment.CENTER);

            layout.getChildren().addAll(title);
            return layout;
        };
    }

    private static CallPane infoPane(){
        return () -> {
            VBox layout = new VBox();
            layout.setAlignment(Pos.TOP_LEFT);
            layout.setPadding(new Insets(20,20,20,20));

            Label titleManual1 = new Label("Manual");
            titleManual1.setFont(Font.font("Source Code Pro", FontWeight.BOLD, 20));
            titleManual1.setPadding(new Insets(20,0,20,10));

            TextField manual1 = new TextField("https://github.com/JordiPujolrasBosch/FonamentsComputacio/blob/master/Manual.md");
            manual1.setPadding(new Insets(0,0,0,10));
            manual1.setEditable(false);
            manual1.setMaxWidth(600);
            manual1.setPrefHeight(30);

            Label title2 = new Label("Author");
            title2.setFont(Font.font("Source Code Pro", FontWeight.BOLD, 20));
            title2.setPadding(new Insets(20,0,20,10));

            Label author = new Label("Made by: Jordi Pujolras Bosch");
            author.setPadding(new Insets(0,0,0,10));

            layout.getChildren().addAll(titleManual1, manual1, title2, author);
            return layout;
        };
    }

    private static CallPane layoutPane(Layout l){
        return l::build;
    }
}
