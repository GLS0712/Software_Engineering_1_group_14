package dtu.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class CompanyViewer extends Application {
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {

    }

    public void menuSwitchToEmployees(TabPane pages) {
        if (theModel.getLoggedIn() != null) {
            pages.getSelectionModel().select(3);
        }
    }

    public void menuSwitchToLogin(TabPane pages) {
        if (theModel.getLoggedIn() != null) {
            pages.getSelectionModel().select(0);
        }
    }

    public void menuSwitchToProjects(TabPane pages) {
        if (theModel.getLoggedIn() != null) {
            pages.getSelectionModel().select(4);
        }
    }

    public void menuSwitchToTimeLog(TabPane pages) {
        if (theModel.getLoggedIn() != null) {
            pages.getSelectionModel().select(1);
        }
    }
    public void menuSwitchToCreateProject(TabPane pages) {
        if (theModel.getLoggedIn() != null) {
            pages.getSelectionModel().select(6);
        }
    }

    public void showProjects(FlowPane bounds) {
        // Project project : theModel.getProjects()
        bounds.getChildren().clear();
        for (Project project : theModel.getProjects()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/projectView.fxml"));
                Button projectButton = loader.load();
                Pane graphicPane = (Pane) projectButton.getGraphic();
                bounds.getChildren().add(projectButton);
                
                Label nameLabel =  (Label) graphicPane.getChildren().get(1);  
                Label idLabel = (Label) graphicPane.getChildren().get(2);  
                Label endDateLabel = (Label) graphicPane.getChildren().get(3);

                nameLabel.setText(project.getName());
                idLabel.setText(project.getId());
                endDateLabel.setText(project.getEndDate());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
