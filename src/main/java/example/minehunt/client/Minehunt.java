package example.minehunt.client;

import example.minehunt.Grid;
import example.minehunt.MinehuntService;
import example.minehunt.SimpleMinehuntService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 */
public final class Minehunt extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        final Stage stage = new Stage();

        final MinehuntService minehuntService = new SimpleMinehuntService();
        final Grid grid = minehuntService.createGrid(10, 10, 8);
        Scene scene = new Scene(new GridNode(grid));
        stage.setScene(scene);

        stage.setOnCloseRequest(windowEvent -> System.exit(0));
        stage.setTitle("Minehunt");
        stage.show();
    }
}
