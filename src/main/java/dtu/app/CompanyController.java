package dtu.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class CompanyController {
    private Company theModel;
    private CompanyViewer theView;

       @FXML
    private Button ProfileIcon;

    @FXML
    private AnchorPane bottomPane;

    @FXML
    private Text errorText;

    @FXML
    private TextField loginField;

    @FXML
    private TabPane pages;

    @FXML
    private Text projectCreateErrorText;

    @FXML
    private TextArea projectDescriptionField;

    @FXML
    private DatePicker projectEndDatePicker;

    @FXML
    private FlowPane projectFlowPane;

    @FXML
    private ChoiceBox<String> projectLeaderPicker;

    @FXML
    private TextField projectNameField;

    @FXML
    private VBox projectShowActivitiesBounds;

    @FXML
    private Pane projectShowActivityShow;

    @FXML
    private Text projectShowEndDate;

    @FXML
    private Text projectShowId;

    @FXML
    private Text projectShowName;

    @FXML
    private Text projectShowPojectLeader;

    @FXML
    private Rectangle projectShowSatusColor;

    @FXML
    private Text projectShowStartDate;

    @FXML
    private DatePicker projectStartDatePicker;

    public void setModelAndView(Company model, CompanyViewer view) {
        this.theModel = model;
        this.theView = view;

    }

    @FXML
    void menuSwitchToEmployees(ActionEvent event) {
        theView.menuSwitchToEmployees(this.pages);

    }

    @FXML
    void menuSwitchToLogin(ActionEvent event) {
        theView.menuSwitchToLogin(this.pages);
    }

    @FXML
    void menuSwitchToProjects(ActionEvent event) {
        theView.showProjects(projectFlowPane);
        theView.menuSwitchToProjects(this.pages);
    }

    @FXML
    void menuSwitchToTimeLog(ActionEvent event) {
        theView.menuSwitchToTimeLog(this.pages);
    }

    @FXML
    void switchToCreateProject(ActionEvent event) {
        projectLeaderPicker.getItems().clear();
        for (Employee employee : theModel.getEmployees()) {
            projectLeaderPicker.getItems().add(employee.getName());
        }
        projectCreateErrorText.setVisible(false);
        projectLeaderPicker.setValue(null);

        projectNameField.setText(null);
        projectEndDatePicker.setValue(null);
        projectStartDatePicker.setValue(null);
        projectDescriptionField.setText(null);

        theView.menuSwitchToCreateProject(this.pages);

    }

    @FXML
    void employeeLogin(ActionEvent event) {
        try {
            theModel.login(loginField.getText());
            ProfileIcon.setText(theModel.getLoggedIn().getName());
            menuSwitchToProjects(event);
        } catch (IllegalAccessError e) {
            errorText.setText(e.getMessage());
            errorText.setVisible(true);
        }

    }

    @FXML
    void createActivity(ActionEvent event) {

    }

    @FXML
    void createProject(ActionEvent event) {
        if (projectNameField.getText() == null || projectStartDatePicker.getValue() == null
                || projectDescriptionField.getText() == null) {
            projectCreateErrorText.setText("Please fill out all non optional fields");
            projectCreateErrorText.setVisible(true);
        } else {

            theModel.createProject(projectNameField.getText());
            Project project = theModel.getProject(projectNameField.getText());
            project.setDescription(projectDescriptionField.getText());
            if (projectLeaderPicker.getValue() != null) {
                project.setProjectLeader(theModel.getEmployeeFromName(projectLeaderPicker.getValue().toString()));
            }
            if (projectStartDatePicker.getValue() != null) {
                System.out.println("IMPLEMENT START DATE");
            }
            if (projectEndDatePicker.getValue() != null) {
                project.setEndDate(projectEndDatePicker.getValue().toString());
            }

            this.menuSwitchToProjects(event);
        }
    }

    @FXML
    void goToProject(ActionEvent event, String projectName) {
        System.out.println("PROJECT NAME FROM RICIEVER: " + projectName);
        Project project = theModel.getProject(projectName);
        
        projectShowActivityShow.setVisible(false);
        projectShowName.setText(projectName);
        projectShowId.setText(project.getId());
        projectShowStartDate.setText("IMPLEMENT START DATE");
        projectShowEndDate.setText("End date: " + project.getEndDate() != null ? project.getEndDate() : "N/A");
        projectShowPojectLeader.setText("Project Leader: " + project.getProjectLeader().getName());
        theView.menuSwitchToProjectView(this.pages, projectName);
    }  
}
