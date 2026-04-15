package dtu.app;
import javafx.application.Application;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class CompanyViewer extends Application{
    private Company theModel;
    private CompanyController theController;
    @Override
    public void start(Stage primaryStage) {

        try {

            theModel = new Company();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/gui.fxml"));
            AnchorPane root = loader.load();
            
            // Exchange information between controller and view objects
            theController = (CompanyController) loader.getController();
            theController.setModelAndView(theModel, this);
            
            Scene scene = new Scene(root);
            primaryStage.setTitle("Softwarehuset");
            scene.getStylesheets().add(getClass().getResource("resources/myStyleSheet.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void update() {
    }

    public static void main(String[] args) {
        launch(args);
    }
}


