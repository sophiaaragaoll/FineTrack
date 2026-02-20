package ufrn.finetrack;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/ufrn/finetrack/view/HomeView.fxml"));
        Scene scene = new Scene(loader.load(), 1280, 720);

        scene.getStylesheets().add(
            getClass().getResource("/ufrn/finetrack/view/style.css").toExternalForm()
        );

        stage.setTitle("FineTrack");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
