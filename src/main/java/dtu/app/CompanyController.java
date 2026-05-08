package dtu.app;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TitledPane;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class CompanyController {
    private Company theModel;
    private CompanyViewer theView;
    private Employee currentShownEmployee;
    private Employee pendingEmployeeToAdd;
    @FXML
    private Button ProfileIcon;

    @FXML
    private Text activityCreateErrorText;

    @FXML
    private Text activityDetailAlottedTime;

    @FXML
    private Text activityDetailDesctiption;

    @FXML
    private Text activityDetailEndDate;

    @FXML
    private Text activityDetailName;

    @FXML
    private Text activityDetailStartDate;

    @FXML
    private Rectangle activityDetailStatusColor;

    @FXML
    private Label activityDetailStatusLabel;

    @FXML
    private Pane activityDetails;

    @FXML
    private VBox activityDetailsEmployeeBounds;

    @FXML
    private TextField activityDetailsEmployeeInitalsField;

    @FXML
    private Text activityEditErrorText;

    @FXML
    private Text hireEmployeeErrorText;

    @FXML
    private AnchorPane bottomPane;

    @FXML
    private TextArea createActivityDescription;

    @FXML
    private DatePicker createActivityEndDate;

    @FXML
    private TextField createActivityHours;

    @FXML
    private TextField createActivityName;

    @FXML
    private DatePicker createActivityStartDate;

    @FXML
    private TextArea editActivityDescription;

    @FXML
    private DatePicker editActivityEndDate;

    @FXML
    private Text editActivityHeader;

    @FXML
    private TextField editActivityHours;

    @FXML
    private TextField editActivityName;

    @FXML
    private Label editActivityProjectName;

    @FXML
    private DatePicker editActivityStartDate;

    @FXML
    private TextArea editProjectDescription;

    @FXML
    private DatePicker editProjectEndDate;

    @FXML
    private Text editProjectHeader;

    @FXML
    private TextField editProjectName;

    @FXML
    private ChoiceBox<String> editProjectProjectLeader;

    @FXML
    private DatePicker editProjectStartDate;

    @FXML
    private Pane employeDetails;

    @FXML
    private Text employeDetailsName;

    @FXML
    private Text employeeAddActivityErrorText;

    @FXML
    private Text employeeDetailsInitials;

    @FXML
    private DatePicker employeeShowDatePicker;

    @FXML
    private Text employeeShowInitials;

    @FXML
    private Text employeeShowName;

    @FXML
    private Text employeeShowNumberOfTasks;

    @FXML
    private VBox employeeCalendarBounds;

    @FXML
    private VBox employeesBounds;

    @FXML
    private Text errorText;

    @FXML
    private Button confirmAddEmployeeButton;

    @FXML
    private Button hireEmployeeButton;

    @FXML
    private TextField hireEmployeeInitials;

    @FXML
    private TextField hireEmployeeName;

    @FXML
    private TextField loginField;

    @FXML
    private TabPane pages;

    @FXML
    private Text projectCreateErrorText;

    @FXML
    private TextArea projectDescriptionField;

    @FXML
    private Text projectEditErrorText;

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
    private Text projectShowEndDate;

    @FXML
    private Text projectShowId;

    @FXML
    private Text projectShowName;

    @FXML
    private Text projectShowPojectLeader;

    @FXML
    private Text projectViewErrorText;

    @FXML
    private Rectangle projectShowSatusColor;

    @FXML
    private Text projectShowStartDate;

    @FXML
    private Pane timeOffPane;

    @FXML
    private DatePicker projectStartDatePicker;
    public void setModelAndView(Company model, CompanyViewer view) {
        this.theModel = model;
        this.theView = view;

    }

    public void initialize() {
        createActivityHours.setTextFormatter(
                new TextFormatter<>(change -> change.getControlNewText().matches("\\d*") ? change : null));
    }

    @FXML
    void menuSwitchToEmployees(ActionEvent event) {
        if (theModel.getLoggedIn().getInitials() == "huba") {
            hireEmployeeButton.setVisible(true);
            hireEmployeeButton.setDisable(false);
        } else {
            hireEmployeeButton.setVisible(false);
            hireEmployeeButton.setDisable(true);
        }
        employeDetails.setVisible(false);
        theView.addEmployees(employeesBounds);
        theView.menuSwitchToEmployees(this.pages);
    }
    @FXML
    void menuSwitchToHireEmployee(ActionEvent event) {
        hireEmployeeErrorText.setVisible(false);
        theView.menuSwitchToHireEmployee(this.pages);
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
    void switchToCreateActivity(ActionEvent event) {
        activityCreateErrorText.setVisible(false);
        createActivityDescription.setText(null);
        createActivityHours.setText(null);
        createActivityName.setText(null);
        createActivityStartDate.setValue(null);
        createActivityEndDate.setValue(null);
        theView.menuSwitchToCreateActivity(this.pages);
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
        if (createActivityName.getText().isEmpty() || createActivityDescription.getText().isEmpty()
                || createActivityStartDate.getValue() == null) {
            activityCreateErrorText.setText("Please fill out all non optional fields");
            activityCreateErrorText.setVisible(true);
        } else {
            try {
                theModel.getProject(projectShowName.getText()).createActivity(theModel.getLoggedIn(),
                        createActivityName.getText(), createActivityDescription.getText());
                Activity activity = theModel.getProject(projectShowName.getText())
                        .getActivityFromName(createActivityName.getText());
                activity.setStartDate(createActivityStartDate.getValue());
                if (!createActivityHours.getText().isEmpty()) {
                    activity.setAlottedTime(createActivityHours.getText());
                }
                if (createActivityEndDate.getValue() != null) {
                    activity.setEndDate(createActivityEndDate.getValue());
                }
                this.goToProject(event, projectShowName.getText());
            } catch (Exception e) {
                activityCreateErrorText.setText(e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName());
                activityCreateErrorText.setVisible(true);
            }
        }
    }

    @FXML
    void editActivity(ActionEvent event) {
        if (editActivityName.getText() == null || editActivityDescription.getText() == null
                || editActivityStartDate.getValue() == null || editActivityHours == null) {
            activityEditErrorText.setText("Please fill out all non optional fields");
            activityEditErrorText.setVisible(true);
        } else {

            Activity activity = theModel.getProject(projectShowName.getText())
                    .getActivityFromName(editActivityHeader.getText());

            LocalDate oldStartDate = activity.getStartDate();
            LocalDate oldEndDate = activity.getEndDate();
            String oldName = activity.getName();

            activity.setName(editActivityName.getText());
            activity.setDescription(editActivityDescription.getText());
            activity.setStartDate(editActivityStartDate.getValue());
            activity.setAlottedTime(editActivityHours.getText());
            if (editActivityEndDate.getValue() != null) {
                activity.setEndDate(editActivityEndDate.getValue());
            } else {
                Project project = theModel.getProject(projectShowName.getText());
                activity.setEndDate(project.getEndDate() != null ? LocalDate.parse(project.getEndDate()) : null);
            }

            activity.updateEmployeeCalendars(oldStartDate, oldEndDate, oldName);

            this.goToProject(event, projectShowName.getText());
            showActivityDetails(event, editActivityName.getText());
        }

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
    void editProject(ActionEvent event) {
        if (editProjectName.getText() == null || editProjectStartDate.getValue() == null
                || editProjectDescription.getText() == null) {
            projectEditErrorText.setText("Please fill out all non optional fields");
            projectEditErrorText.setVisible(true);
        } else {

            Project project = theModel.getProject(editProjectHeader.getText());
            project.setName(editProjectName.getText());
            project.setDescription(editProjectDescription.getText());
            if (editProjectProjectLeader.getValue() != null) {
                project.setProjectLeader(theModel.getEmployeeFromName(editProjectProjectLeader.getValue().toString()));
            }
            if (editProjectStartDate.getValue() != null) {
                System.out.println("IMPLEMENT START DATE");
            }
            if (editProjectEndDate.getValue() != null) {
                project.setEndDate(editProjectEndDate.getValue().toString());
            }

            this.goToProject(event, editProjectName.getText());

        }
    }

    @FXML
    void goToProject(ActionEvent event, String projectName) {
        Project project = theModel.getProject(projectName);
        activityDetails.setVisible(false);
        projectViewErrorText.setVisible(false);
        projectShowName.setText(projectName);
        projectShowId.setText(project.getId());
        projectShowStartDate.setText("IMPLEMENT START DATE");
        projectShowEndDate
                .setText(project.getEndDate() != null ? "End date: " + project.getEndDate() : "End date: N/A");
        if (project.getProjectLeader() != null) {
            projectShowPojectLeader.setText("Project Leader: " + project.getProjectLeader().getName());
        } else {
            projectShowPojectLeader.setText("Project Leader: N/A");
        }
        theView.showActivities(projectShowActivitiesBounds, projectName);

        theView.menuSwitchToProjectView(this.pages, projectName);
    }

    @FXML
    void gotToEditActivity(ActionEvent event) {
        Project project = theModel.getProject(projectShowName.getText());
        if (project.getProjectLeader() != null &&
                !project.getProjectLeader().getName().equals(theModel.getLoggedIn().getName())) {
            employeeAddActivityErrorText.setText("Only the project leader can edit this activity");
            employeeAddActivityErrorText.setVisible(true);
            return;
        }
        Activity activity = project.getActivityFromName(activityDetailName.getText());
        editActivityHeader.setText(activity.getName());
        editActivityProjectName.setText("From project: " + project.getName());
        editActivityName.setText(activity.getName());
        editActivityDescription.setText(activity.getDescription());
        editActivityStartDate.setValue(activity.getStartDate());
        editActivityEndDate.setValue(activity.getEndDate());
        editActivityHours.setText(activity.getAlottedTime());
        theView.menuSwitchToEditActivity(this.pages);
    }

    @FXML
    void gotToEditProject(ActionEvent event) {
        Project project = theModel.getProject(projectShowName.getText());
        if (project.getProjectLeader() != null &&
                !project.getProjectLeader().getName().equals(theModel.getLoggedIn().getName())) {
            projectViewErrorText.setText("Only the project leader can edit this project");
            projectViewErrorText.setVisible(true);
            return;
        }
        editProjectHeader.setText(project.getName());
        editProjectName.setText(project.getName());
        editProjectDescription.setText(project.getDescription());
        editProjectProjectLeader.getItems().clear();
        for (Employee employee : theModel.getEmployees()) {
            editProjectProjectLeader.getItems().add(employee.getName());
        }
        if (project.getProjectLeader() != null) {
            editProjectProjectLeader.setValue(project.getProjectLeader().getName());
        }

        theView.menuSwitchToEditProject(pages);
    }

    @FXML
    void showActivityDetails(ActionEvent event, String activityName) {
        Activity activity = theModel.getProject(projectShowName.getText()).getActivityFromName(activityName);
        activityDetailName.setText(activity.getName());

        activityDetailDesctiption.setText(activity.getDescription());
        if (activity.getAlottedTime() != null) {
            activityDetailAlottedTime.setText("Alotted time: " + activity.getAlottedTime());
        } else {
            activityDetailAlottedTime.setText("Alotted time: N/A");
        }
        if (activity.getStartDate() != null) {
            activityDetailStartDate.setText("Start Date :" + activity.getStartDate().toString());
        } else {
            activityDetailStartDate.setText("Start Date: N/A");
        }
        if (activity.getEndDate() != null) {
            activityDetailEndDate.setText("End Date: " + activity.getEndDate().toString());
        } else {
            activityDetailEndDate.setText("End Date: N/A");
        }
        employeeAddActivityErrorText.setVisible(false);
        activityDetailsEmployeeInitalsField.setText(null);
        theView.showActivityDetails(this.activityDetails, activity, this.activityDetailsEmployeeBounds);

    }

    @FXML
    void removeEmployee(ActionEvent event, Employee employee) {
        Project project = theModel.getProject(projectShowName.getText());
        if (project.getProjectLeader() != null &&
                !project.getProjectLeader().getName().equals(theModel.getLoggedIn().getName())) {
            employeeAddActivityErrorText.setText("Only the project leader can modify employees");
            employeeAddActivityErrorText.setVisible(true);
            return;
        }
        Activity activity = project.getActivityFromName(activityDetailName.getText());
        activity.removeEmployee(employee);
        showActivityDetails(event, activity.getName());
    }

    @FXML
    void goToEmployee(ActionEvent event, Employee employee) {
        currentShownEmployee = employee;
        employeeShowName.setText(employee.getName());
        employeeShowInitials.setText(employee.getInitials());
        employeeShowDatePicker.setValue(LocalDate.now());
        employeeShowNumberOfTasks.setText("Activities this week: " + employee.getCalendar().getEntries(LocalDate.now()).size());
        if (theModel.getLoggedIn().getName().equals(employee.getName())) {
            timeOffPane.setVisible(true);
        } else {
            timeOffPane.setVisible(false);
        }
        theView.showEmployeeCalendar(employeeCalendarBounds, employee, LocalDate.now());
        theView.menuSwitchToEmployee(pages);
    }

    @FXML
    void refreshEmployeeCalendar(ActionEvent event) {
        if (currentShownEmployee == null || employeeShowDatePicker.getValue() == null) return;
        LocalDate date = employeeShowDatePicker.getValue();
        employeeShowNumberOfTasks.setText("Activities this week: " + currentShownEmployee.getCalendar().getEntries(date).size());
        theView.showEmployeeCalendar(employeeCalendarBounds, currentShownEmployee, date);
    }

    @FXML
    void addEmployeeToActivity(ActionEvent event) {
        Project project = theModel.getProject(projectShowName.getText());
        if (project.getProjectLeader() != null &&
                !project.getProjectLeader().getName().equals(theModel.getLoggedIn().getName())) {
            employeeAddActivityErrorText.setText("Only the project leader can modify employees");
            employeeAddActivityErrorText.setVisible(true);
            return;
        }
        try {
            Employee employee = theModel.getEmployeeFromInitials(activityDetailsEmployeeInitalsField.getText());
            Activity activity = theModel.getProject(projectShowName.getText())
                    .getActivityFromName(activityDetailName.getText());

            if (employee == null) {
                employeeAddActivityErrorText.setText("Employee not found");
                employeeAddActivityErrorText.setVisible(true);

            } else if (!activity.getEmployees().contains(employee)) {
                try {
                    activity.addEmployee(employee);
                    activityDetailsEmployeeInitalsField.setText(null);
                    confirmAddEmployeeButton.setVisible(false);
                    showActivityDetails(event, activity.getName());
                } catch (IllegalArgumentException e) {
                    pendingEmployeeToAdd = employee;
                    employeeAddActivityErrorText.setText(e.getMessage());
                    employeeAddActivityErrorText.setVisible(true);
                    confirmAddEmployeeButton.setVisible(true);
                }
            } else {
                employeeAddActivityErrorText.setText("Employee already on project");
                employeeAddActivityErrorText.setVisible(true);
            }

        } catch (Exception e) {
            employeeAddActivityErrorText.setText("Employee not found");
            employeeAddActivityErrorText.setVisible(true);
        }

    }

    @FXML
    void confirmAddEmployeeToActivity(ActionEvent event) {
        if (pendingEmployeeToAdd == null) return;
        Project project = theModel.getProject(projectShowName.getText());
        if (project.getProjectLeader() != null &&
                !project.getProjectLeader().getName().equals(theModel.getLoggedIn().getName())) {
            employeeAddActivityErrorText.setText("Only the project leader can modify employees");
            employeeAddActivityErrorText.setVisible(true);
            return;
        }
        Activity activity = theModel.getProject(projectShowName.getText())
                .getActivityFromName(activityDetailName.getText());
        activity.forceAddEmployee(pendingEmployeeToAdd);
        pendingEmployeeToAdd = null;
        activityDetailsEmployeeInitalsField.setText(null);
        confirmAddEmployeeButton.setVisible(false);
        employeeAddActivityErrorText.setVisible(false);
        showActivityDetails(event, activity.getName());
    }

    @FXML
    void showEmployeeDetails(ActionEvent event, Employee employee) {
        employeDetailsName.setText(employee.getName());
        employeeDetailsInitials.setText(employee.getInitials());
        employeDetails.setVisible(true);
    }

    @FXML
    void viewAvailability(ActionEvent event) {
        goToEmployee(event, theModel.getEmployeeFromInitials(employeeDetailsInitials.getText()));
    }

    @FXML
    void hireEmployee(ActionEvent event) {
        if(theModel.getEmployeeFromInitials(hireEmployeeInitials.getText()) == null){
            theModel.hireEmployee(new Employee(hireEmployeeName.getText(), hireEmployeeInitials.getText()));
            menuSwitchToEmployees(event);
        }else{
            hireEmployeeErrorText.setText("Employee with initials already exists");
            hireEmployeeErrorText.setVisible(true);
        }
    }

}
