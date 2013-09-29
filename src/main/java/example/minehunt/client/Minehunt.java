package example.minehunt.client;

import example.minehunt.MinehuntService;
import example.minehunt.SimpleMinehuntService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 */
public final class Minehunt extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        final Stage stage = new Stage(StageStyle.UTILITY);

        final MinehuntService minehuntService = new SimpleMinehuntService();

        Scene scene = new Scene(new GameNode(minehuntService, "mer-surface.jpg"));
        stage.setScene(scene);


        stage.setOnCloseRequest(windowEvent -> System.exit(0));
        stage.setTitle("Minehunt");
        stage.show();
    }
}
