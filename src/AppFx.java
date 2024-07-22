import JavafxClasses.Layouts;
import JavafxClasses.UtilFx;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;


public class AppFx extends Application {
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("RL&CFLTools");
        UtilFx.setStage(stage);

        double width = Screen.getPrimary().getBounds().getWidth()*0.6;
        double height = Screen.getPrimary().getBounds().getHeight()*0.6;
        //Scene scene = new Scene(Layouts.dfaToNfaAct(), width, height);
        //stage.setScene(scene);
        stage.setMaximized(true);

        stage.show();
    }
}
