import JavafxClasses.AppFx;
import Functionalities.RunCommandLine;

public class Main {
    public static void main(String[] args) {
        if(args.length == 0) AppFx.main(args);
        else RunCommandLine.run(args);
    }
}
