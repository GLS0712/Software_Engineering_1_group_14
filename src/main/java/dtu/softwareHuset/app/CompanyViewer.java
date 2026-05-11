package dtu.softwareHuset.app;

import java.time.LocalDate;
import java.util.List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
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

    // JavaFX entry point — loads the FXML layout, creates the model, and wires the controller and view together
    @Override
    // Author: Daniel Hedegaard
    public void start(Stage primaryStage) {
        try {
            theModel = new Company();
            try {
                theModel.loadProjectsFromLogs();
            } catch (Exception e) {
                // No log file yet — nothing to restore
            }
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

    // Placeholder for future UI refresh logic
    // Author: Daniel Hedegaard
    public void update() {

    }

    // Navigate to the single Employee tab (tab index 2) — only if someone is logged in
    // Author: Daniel Hedegaard
    public void menuSwitchToEmployee(TabPane pages) {
        if (theModel.getLoggedIn() != null) {
            pages.getSelectionModel().select(2);
        }
    }

    // Navigate to the Employees list tab (tab index 3)
    // Author: Daniel Hedegaard
    public void menuSwitchToEmployees(TabPane pages) {
        if (theModel.getLoggedIn() != null) {
            pages.getSelectionModel().select(3);
        }
    }

    // Navigate back to the Login tab (tab index 0)
    // Author: Daniel Hedegaard
    public void menuSwitchToLogin(TabPane pages) {
        if (theModel.getLoggedIn() != null) {
            pages.getSelectionModel().select(0);
        }
    }

    // Navigate to the active Projects tab (tab index 4)
    // Author: Daniel Hedegaard
    public void menuSwitchToProjects(TabPane pages) {
        if (theModel.getLoggedIn() != null) {
            pages.getSelectionModel().select(4);
        }
    }

    // Navigate to the Time Log tab (tab index 1)
    // Author: Daniel Hedegaard
    public void menuSwitchToTimeLog(TabPane pages) {
        if (theModel.getLoggedIn() != null) {
            pages.getSelectionModel().select(1);
        }
    }

    // Navigate to the Create Project form tab (tab index 6)
    // Author: Daniel Hedegaard
    public void menuSwitchToCreateProject(TabPane pages) {
        if (theModel.getLoggedIn() != null) {
            pages.getSelectionModel().select(6);
        }
    }

    // Navigate to the project detail/activity view tab (tab index 5)
    // Author: Daniel Hedegaard
    public void menuSwitchToProjectView(TabPane pages, String projectName) {
        if (theModel.getLoggedIn() != null) {
            pages.getSelectionModel().select(5);
        }
    }

    // Navigate to the Create Activity form tab (tab index 7)
    // Author: Daniel Hedegaard
    public void menuSwitchToCreateActivity(TabPane pages) {
        if (theModel.getLoggedIn() != null) {
            pages.getSelectionModel().select(7);
        }
    }

    // Navigate to the Edit Project form tab (tab index 8)
    // Author: Daniel Hedegaard
    public void menuSwitchToEditProject(TabPane pages) {
        if (theModel.getLoggedIn() != null) {
            pages.getSelectionModel().select(8);
        }
    }

    // Navigate to the Edit Activity form tab (tab index 9)
    // Author: Daniel Hedegaard
    public void menuSwitchToEditActivity(TabPane pages) {
        if (theModel.getLoggedIn() != null) {
            pages.getSelectionModel().select(9);
        }
    }

    // Navigate to the Hire Employee form tab (tab index 10)
    // Author: Daniel Hedegaard
    public void menuSwitchToHireEmployee(TabPane pages) {
        if (theModel.getLoggedIn() != null) {
            pages.getSelectionModel().select(10);
        }
    }

    // Navigate to the Archive (Completed Projects) tab (tab index 11)
    // Author: GedeGustav
    public void menuSwitchToCompletedProjects(TabPane pages) {
        if (theModel.getLoggedIn() != null) {
            pages.getSelectionModel().select(11);
        }
    }

    // Populates the archive FlowPane with completed/archived project cards.
    // A project is considered archived when its end date has passed and all activities are also done.
    // The project leader gets full access; non-leaders can only view the description via an overlay.
    // Author: GedeGustav
    public void showCompletedProjects(FlowPane bounds) {
        bounds.getChildren().clear();
        LocalDate today = LocalDate.now();
        for (Project project : theModel.getProjects()) {
            // Skip projects that have no end date or haven't ended yet
            if (project.getEndDate() == null || project.getEndDate().isEmpty()) continue;
            if (!LocalDate.parse(project.getEndDate()).isBefore(today)) continue;
            // Skip if any activity is still ongoing — project isn't fully archived yet
            boolean allActivitiesDone = project.getActivities().stream()
                .allMatch(a -> a.getEndDate() != null && a.getEndDate().isBefore(today));
            if (!allActivitiesDone) continue;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/projectView.fxml"));
                Button projectButton = loader.load();
                // Children in the graphic pane follow the order defined in projectView.fxml:
                // 0=status rectangle, 1=name label, 2=id label, 3=end date label, 4=leader label
                Pane graphicPane = (Pane) projectButton.getGraphic();
                Label nameLabel = (Label) graphicPane.getChildren().get(1);
                Label idLabel = (Label) graphicPane.getChildren().get(2);
                Label endDateLabel = (Label) graphicPane.getChildren().get(3);
                Label leaderLabel = (Label) graphicPane.getChildren().get(4);
                nameLabel.setText(project.getName());
                idLabel.setText(project.getId());
                endDateLabel.setText(project.getEndDate());
                leaderLabel.setText(project.getProjectLeader() != null ? "Leader: " + project.getProjectLeader().getName() : "");
                // Archived projects always show green — they are fully done
                Rectangle statusRect = (Rectangle) graphicPane.getChildren().get(0);
                statusRect.setStyle("-fx-fill: #00c853;");

                // Only the project leader (or projects with no leader) can open the full project view
                Employee loggedIn = theModel.getLoggedIn();
                boolean canView = project.getProjectLeader() == null
                        || project.getProjectLeader().equals(loggedIn);

                if (canView) {
                    projectButton.setOnAction(event -> {
                        if (theController != null) {
                            theController.goToProject(event, project.getName());
                        }
                    });
                } else {
                    // Non-leaders can still click to read the description via the overlay panel
                    projectButton.setOnAction(event -> {
                        if (theController != null) {
                            theController.showArchivedProjectDescription(project.getName(), project.getDescription());
                        }
                    });
                }

                bounds.getChildren().add(projectButton);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Populates the Projects FlowPane with all active (non-archived) project cards.
    // Projects where the end date has passed and all activities are done are excluded — they belong in the archive.
    // Author: Daniel Hedegaard
    public void showProjects(FlowPane bounds) {
        bounds.getChildren().clear();
        LocalDate today = LocalDate.now();
        for (Project project : theModel.getProjects()) {
            // Skip fully archived projects — they are shown in the Archive tab instead
            if (project.getEndDate() != null && !project.getEndDate().isEmpty()
                    && LocalDate.parse(project.getEndDate()).isBefore(today)
                    && project.getActivities().stream()
                        .allMatch(a -> a.getEndDate() != null && a.getEndDate().isBefore(today))) {
                continue;
            }
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/projectView.fxml"));
                Button projectButton = loader.load();
                // Children in the graphic pane follow the order defined in projectView.fxml:
                // 0=status rectangle, 1=name label, 2=id label, 3=end date label, 4=leader label
                Pane graphicPane = (Pane) projectButton.getGraphic();

                // graphicPane.setMouseTransparent(true);

                Label nameLabel = (Label) graphicPane.getChildren().get(1);
                Label idLabel = (Label) graphicPane.getChildren().get(2);
                Label endDateLabel = (Label) graphicPane.getChildren().get(3);
                Label leaderLabel = (Label) graphicPane.getChildren().get(4);

                nameLabel.setText(project.getName());
                idLabel.setText(project.getId());
                endDateLabel.setText(project.getEndDate() != null ? project.getEndDate() : "N/A");
                leaderLabel.setText(project.getProjectLeader() != null ? "Leader: " + project.getProjectLeader().getName() : "");

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

    // Populates the activity list in the project detail view.
    // Index 0 is always the "Add Activity" button, so existing activity rows are appended from index 1 onward.
    // Author: Daniel Hedegaard
    public void showActivities(VBox bounds, String projectName) {
        // Keep index 0 (the "Add Activity" button) and remove all previously loaded activity rows
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

                // Color the status indicator based on the activity's start/end dates relative to today
                if (activity.getStartDate() != null) {
                    if (activity.getEndDate() != null) {
                        // Both dates known — delegate to the controller's shared color logic
                        statusRect.setStyle("-fx-fill : " + theController.getStatusColorForActivity(activity) + " ;");
                    } else {
                        // No end date: yellow if already started, red if not yet started
                        if (activity.getStartDate().isBefore(LocalDate.now()) || activity.getStartDate().isEqual(LocalDate.now())) {
                            statusRect.setStyle("-fx-fill: #fffc00;");
                        } else {
                            statusRect.setStyle("-fx-fill: #ff0000;");
                        }
                    }
                } else {
                    // No start date at all — treat as not started
                    statusRect.setStyle("-fx-fill: #ff0000;");
                }

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

    // Populates the employee list inside the activity detail panel.
    // If viewing an archived project, the remove button is hidden to prevent modifications.
    // Author: GedeGustav
    public void showActivityDetails(Pane activityDetails, Activity activity, VBox bounds, boolean archived) {
        bounds.getChildren().clear();
        for (Employee employee : activity.getEmployees()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/activityEmployee.fxml"));
                HBox employeeHbox = loader.load();
                // Each row is an HBox: first child is the employee info button, last child is the remove button
                Button employeeButton = (Button) employeeHbox.getChildren().getFirst();
                Pane graphicPane = (Pane) employeeButton.getGraphic();

                Label nameLabel = (Label) graphicPane.getChildren().get(0);
                Label initialsLabel = (Label) graphicPane.getChildren().get(1);
                Button removeButton = (Button) employeeHbox.getChildren().getLast();

                nameLabel.setText(employee.getName());
                initialsLabel.setText(employee.getInitials());
                // Hide the remove button for archived projects — changes are not allowed
                removeButton.setVisible(!archived);
                removeButton.setManaged(!archived);
                removeButton.setOnAction(event -> {
                    if (theController != null) {
                        theController.removeEmployee(event, employee);
                    }
                });
                // Clicking the employee row navigates to their availability calendar
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

    // Populates the Employees tab list with a clickable card for each employee in the company
    // Author: Daniel Hedegaard
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

                // Clicking an employee card opens their detail sidebar in the Employees tab
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

    // Shows the week's calendar entries for the given employee and date in the Employee tab.
    // Displays a placeholder message when the employee has no activities that week.
    // Author: GedeGustav
    public void showEmployeeCalendar(VBox bounds, Employee employee, LocalDate date) {
        bounds.getChildren().clear();
        List<Employee_Calendar.CalendarEntry> entries = employee.getCalendar().getEntries(date);
        if (entries.isEmpty()) {
            bounds.getChildren().add(new Label("No activities this week"));
        } else {
            for (Employee_Calendar.CalendarEntry entry : entries) {
                Label label = new Label(entry.getType() + ": " + entry.getDescription());
                label.setStyle("-fx-padding: 8 15; -fx-font-size: 13;");
                bounds.getChildren().add(label);
            }
        }
    }

    // Author: Daniel Hedegaard
    public static void main(String[] args) {
        launch(args);
    }

}
