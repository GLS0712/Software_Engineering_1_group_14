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
import javafx.scene.text.Text;

public class CompanyController {
    private Company theModel;
    private CompanyViewer theView;

    @FXML
    private TabPane pages;
    @FXML
    private AnchorPane bottomPane;
    @FXML
    private Button ProfileIcon;
    @FXML
    private TextField loginField;
    @FXML
    private Text errorText;
    @FXML
    private FlowPane projectFlowPane;
    @FXML
    private DatePicker projectEndDatePicker;
    @FXML
    private DatePicker projectStartDatePicker;
    @FXML
    private ChoiceBox<String> projectLeaderPicker;
    @FXML
    private TextField projectNameField;
    @FXML
    private TextArea projectDescriptionField;
    @FXML
    private Text projectCreateErrorText;
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
        for (Employee employee : theModel.getEmployees()) {
            projectLeaderPicker.getItems().add(employee.getName());
        }
        projectCreateErrorText.setVisible(false);
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
    void goToProject(ActionEvent event) {

    }
}
