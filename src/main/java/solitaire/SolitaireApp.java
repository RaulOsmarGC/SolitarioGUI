package solitaire;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SolitaireApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        SolitaireGame game = new SolitaireGame();

        SolitaireBoard root = new SolitaireBoard(game);
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("Solitario ROGC");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}