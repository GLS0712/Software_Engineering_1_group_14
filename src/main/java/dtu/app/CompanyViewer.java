package dtu.app;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
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
    public void menuSwitchToEmployees(TabPane pages){
        pages.getSelectionModel().select(3);
    }
    public void menuSwitchToLogin(TabPane pages){
        pages.getSelectionModel().select(0);
    }
    public void menuSwitchToProjects(TabPane pages){
        pages.getSelectionModel().select(4);
    }
    public void menuSwitchToTimeLog(TabPane pages){
        pages.getSelectionModel().select(1);
    }

    public static void main(String[] args) {
        launch(args);
    }
}


