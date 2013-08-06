package example.minehunt.client;

import example.minehunt.Cell;
import example.minehunt.DisplayCellResult;
import example.minehunt.Grid;
import example.minehunt.Position;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Collections;

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

        Scene scene = new Scene(new GridNode(new Grid() {
            @Override
            public int getLines() {
                return 10;
            }

            @Override
            public int getColumns() {
                return 10;
            }

            @Override
            public DisplayCellResult displayCell(Position position) {
                return new DisplayCellResult(new Cell(position, 0), Collections.emptySet());
            }
        }));
        stage.setScene(scene);

        stage.setOnCloseRequest(windowEvent -> System.exit(0));
        stage.setTitle("Minehunt");
        stage.show();
    }
}
