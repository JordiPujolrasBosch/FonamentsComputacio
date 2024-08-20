# Regular Languages & Context Free Languages Tools

## Command line

Manual to use the application in shell: [Command line manual](CommandLineManual.md)

## Input files format

Format of the input data (dfa, nfa, regex, cfg): [Input data format manual](ReaderManual.md)

## Graphic User Interface

If you want to use the Graphic User Interface choose the jar file that is build for your OS.

* The file `app.jar` is build with Windows JavaFX SDK.
* The file `app_linux.jar` is build with Linux JavaFX SDK.
* The file `app_macos.jar` is build with MacOS JavaFX SDK.

Another option is to install JavaFX yourself.

* Go to te official webpage: [https://openjfx.io/index.html](https://openjfx.io/index.html)
* Download the SDK file and unzip it.
* Open the `app.jar` directory in terminal and run:

```
java --module-path [path_to_javafx_sdk]/lib --add-modules=javafx.controls -jar app.jar
```