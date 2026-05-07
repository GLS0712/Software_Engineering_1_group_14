package dtu.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
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

    public void menuSwitchToEmployee(TabPane pages) {
         if (theModel.getLoggedIn() != null) {
            pages.getSelectionModel().select(2);
        }
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

    public void menuSwitchToProjectView(TabPane pages, String projectName) {
        if (theModel.getLoggedIn() != null) {

            pages.getSelectionModel().select(5);
        }
    }

    public void menuSwitchToCreateActivity(TabPane pages) {
        if (theModel.getLoggedIn() != null) {

            pages.getSelectionModel().select(7);
        }
    }

    public void menuSwitchToEditProject(TabPane pages) {
        if (theModel.getLoggedIn() != null) {

            pages.getSelectionModel().select(8);
        }
    }

    public void menuSwitchToEditActivity(TabPane pages) {
        if (theModel.getLoggedIn() != null) {

            pages.getSelectionModel().select(9);
        }
    }
    public void menuSwitchToHireEmployee(TabPane pages) {
         if (theModel.getLoggedIn() != null) {

            pages.getSelectionModel().select(10);
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

                // graphicPane.setMouseTransparent(true);

                Label nameLabel = (Label) graphicPane.getChildren().get(1);
                Label idLabel = (Label) graphicPane.getChildren().get(2);
                Label endDateLabel = (Label) graphicPane.getChildren().get(3);

                nameLabel.setText(project.getName());
                idLabel.setText(project.getId());
                endDateLabel.setText(project.getEndDate() != null ? project.getEndDate() : "N/A");

                projectButton.setOnAction(event -> {
                    if (theController != null) {
                        theController.goToProject(event, project.getName());
                    }
                });

                bounds.getChildren().add(projectButton);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void showActivities(VBox bounds, String projectName) {
        // Project project : theModel.getProjects()
        bounds.getChildren().remove(1, bounds.getChildren().size());
        for (Activity activity : theModel.getProject(projectName).getActivities()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/activityView.fxml"));
                Button activityButton = loader.load();
                Pane graphicPane = (Pane) activityButton.getGraphic();

                // graphicPane.setMouseTransparent(true);

                Label nameLabel = (Label) graphicPane.getChildren().get(0);
                Label idLabel = (Label) graphicPane.getChildren().get(1);
                Label statusLabel = (Label) graphicPane.getChildren().get(2);
                Rectangle statusRect = (Rectangle) graphicPane.getChildren().get(3);

                nameLabel.setText(activity.getName());
                idLabel.setText("DATELABELS");

                activityButton.setOnAction(event -> {
                    if (theController != null) {
                        theController.showActivityDetails(event, activity.getName());
                    }
                });

                bounds.getChildren().add(activityButton);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void showActivityDetails(Pane activityDetails, Activity activity, VBox bounds) {
        bounds.getChildren().clear();
        for (Employee employee : activity.getEmployees()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/activityEmployee.fxml"));
                HBox employeeHbox = loader.load();
                Button employeeButton = (Button) employeeHbox.getChildren().getFirst();
                Pane graphicPane = (Pane) employeeButton.getGraphic();

                Label nameLabel = (Label) graphicPane.getChildren().get(0);
                Label initialsLabel = (Label) graphicPane.getChildren().get(1);
                Button removeButton = (Button) employeeHbox.getChildren().getLast();

                nameLabel.setText(employee.getName());
                initialsLabel.setText(employee.getInitials());
                removeButton.setOnAction(event -> {
                    if (theController != null) {
                        theController.removeEmployee(event, employee);
                    }

                });
                employeeButton.setOnAction(event -> {
                    if (theController != null) {
                        theController.goToEmployee(event, employee);
                    }
                });

                bounds.getChildren().add(employeeHbox);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        activityDetails.setVisible(true);
    }

    public void addEmployees(VBox bounds) {
        bounds.getChildren().clear();
        for (Employee employee : theModel.getEmployees()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/employeeShow.fxml"));
                Button employeeButton = loader.load();
                Pane graphicPane = (Pane) employeeButton.getGraphic();

                // graphicPane.setMouseTransparent(true);

                Label nameLabel = (Label) graphicPane.getChildren().get(0);
                Label idLabel = (Label) graphicPane.getChildren().get(1);
                Label statusLabel = (Label) graphicPane.getChildren().get(2);
                ImageView statusIcon = (ImageView) graphicPane.getChildren().get(3);
                nameLabel.setText(employee.getName());
                idLabel.setText(employee.getInitials());
                statusLabel.setText("");
                statusIcon.setImage(null);

                employeeButton.setOnAction(event -> {
                    if (theController != null) {
                        theController.showEmployeeDetails(event, employee);
                    }
                });

                bounds.getChildren().add(employeeButton);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        launch(args);
    }

    
    

}
