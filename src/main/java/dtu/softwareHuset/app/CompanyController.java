package dtu.softwareHuset.app;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class CompanyController {
    private Company theModel;
    private CompanyViewer theView;
    private Employee currentShownEmployee;
    // Holds an employee pending a force-add after an availability warning was shown
    private Employee pendingEmployeeToAdd;
    // Tracks whether the currently viewed project is archived, to gate all write
    // operations
    private boolean viewingArchivedProject = false;
    private String editingEntryId = null;

    @FXML
    private Button ProfileIcon;

    @FXML
    private Text activityCreateErrorText;

    @FXML
    private Text activityDetailAlottedTime;

    @FXML
    private TextArea activityDetailDesctiption;

    @FXML
    private Text activityDetailId;

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
    private Button addActivityButton;

    @FXML
    private Button editActivityButton;

    @FXML
    private Button addEmployeeButton;

    @FXML
    private Text editProjectArchivedNote;

    @FXML
    private Pane projectStatsPane;

    @FXML
    private Text statsActivitiesInProgress;

    @FXML
    private Text statsActivitiesCompleted;

    @FXML
    private Text statsTotalEmployees;

    @FXML
    private Text statsTotalAllottedTime;

    @FXML
    private Text statsTotalLoggedTime;

    @FXML
    private Button closeActivityDetailsButton;

    @FXML
    private Button deleteProjectButton;

    @FXML
    private Button deleteActivityButton;

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
    private FlowPane completedProjectsFlowPane;

    @FXML
    private Pane archivedProjectDescPane;

    @FXML
    private Text archivedProjectDescName;

    @FXML
    private TextArea archivedProjectDescText;

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
    private TextArea projectShowDescription;

    @FXML
    private Text projectViewErrorText;

    @FXML
    private Rectangle projectShowSatusColor;

    @FXML
    private Text projectShowStartDate;

    @FXML
    private Pane timeOffPane;

    @FXML
    private Text sickStatusText;

    @FXML
    private CheckBox sickCheckBox;

    @FXML
    private Text sicknessErrorText;

    @FXML
    private TextField timeOffReasonField;

    @FXML
    private DatePicker timeOffStartDatePicker;

    @FXML
    private DatePicker timeOffEndDatePicker;

    @FXML
    private Text timeOffErrorText;

    @FXML
    private DatePicker projectStartDatePicker;

    @FXML
    private TableView<List<String>> timeLogTable;
    @FXML
    private TableColumn<List<String>, String> logDateColumn;
    @FXML
    private TableColumn<List<String>, String> logProjectColumn;
    @FXML
    private TableColumn<List<String>, String> logActivityColumn;
    @FXML
    private TableColumn<List<String>, String> logHoursColumn;
    @FXML
    private ChoiceBox<String> logProjectChoiceBox;
    @FXML
    private ChoiceBox<String> logActivityChoiceBox;
    @FXML
    private DatePicker logDatePicker;
    @FXML
    private Spinner<Double> logHoursSpinner;
    @FXML
    private Text logTimeErrorText;
    @FXML
    private Text timeLogTableErrorText;

    // Stores references to the model and view so the controller can communicate
    // with both
    // Author: Daniel Hedegaard
    public void setModelAndView(Company model, CompanyViewer view) {
        this.theModel = model;
        this.theView = view;

    }

    // Called automatically by JavaFX after the FXML is loaded — sets up input
    // constraints on fields
    // Author: Daniel Hedegaard
    public void initialize() {
        // Restrict the allotted hours field to digits only so invalid input is blocked
        // at the source
        createActivityHours.setTextFormatter(
                new TextFormatter<>(change -> change.getControlNewText().matches("\\d*") ? change : null));

        // Set up the time-log table columns
        // Each row is a List<String>: [entryId, employeeId, projectId, activityName, date, hours]
        logDateColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().get(4)));
        logProjectColumn.setCellValueFactory(cell -> {
            String projectId = cell.getValue().size() > 2 ? cell.getValue().get(2) : "";
            Project p = theModel.getProjectById(projectId);
            return new SimpleStringProperty(p != null ? p.getName() : projectId);
        });
        logActivityColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().get(3)));
        logHoursColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().get(5)));

        // Hours spinner: 0.5 to 24.0, in 0.5 increments, default 1.0
        logHoursSpinner.setValueFactory(
                new SpinnerValueFactory.DoubleSpinnerValueFactory(0.5, 24.0, 1.0, 0.5));

        // Repopulate activities when a project is selected
        logProjectChoiceBox.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> refreshActivityChoiceBoxForProject(newVal));
    }

    // Navigates to the Employees tab and refreshes the employee list
    @FXML
    // Author: Daniel Hedegaard
    void menuSwitchToEmployees(ActionEvent event) {
        // if (theModel.getLoggedIn().getInitials() == "huba") {
        // hireEmployeeButton.setVisible(true);
        // hireEmployeeButton.setDisable(false);
        // } else {
        // hireEmployeeButton.setVisible(false);
        // hireEmployeeButton.setDisable(true);
        // }
        hireEmployeeButton.setVisible(true);
        hireEmployeeButton.setDisable(false);

        employeDetails.setVisible(false);
        theView.addEmployees(employeesBounds);
        theView.menuSwitchToEmployees(this.pages);
    }

    // Navigates to the Hire Employee form and clears any previous error message
    @FXML
    // Author: Daniel Hedegaard
    void menuSwitchToHireEmployee(ActionEvent event) {
        hireEmployeeErrorText.setVisible(false);
        theView.menuSwitchToHireEmployee(this.pages);
    }

    // Navigates back to the Login tab
    @FXML
    // Author: Daniel Hedegaard
    void menuSwitchToLogin(ActionEvent event) {
        theView.menuSwitchToLogin(this.pages);
    }

    // Refreshes the project list and navigates to the Projects tab
    @FXML
    // Author: Daniel Hedegaard
    void menuSwitchToProjects(ActionEvent event) {
        theView.showProjects(projectFlowPane);
        theView.menuSwitchToProjects(this.pages);
    }

    // Resets the description overlay, refreshes the archive list, and navigates to
    // the Archive tab
    @FXML
    // Author: GedeGustav
    void menuSwitchToCompletedProjects(ActionEvent event) {
        archivedProjectDescPane.setVisible(false);
        theView.showCompletedProjects(completedProjectsFlowPane);
        theView.menuSwitchToCompletedProjects(this.pages);
    }

    // Shows the description overlay panel for a non-leader viewing an archived
    // project
    // Author: GedeGustav
    public void showArchivedProjectDescription(String name, String description) {
        archivedProjectDescName.setText(name);
        archivedProjectDescText.setText(description != null ? description : "");
        archivedProjectDescPane.setVisible(true);
    }

    // Hides the archived project description overlay panel
    @FXML
    // Author: GedeGustav
    void closeArchivedProjectDesc(ActionEvent event) {
        archivedProjectDescPane.setVisible(false);
    }

    // Navigates to the Time Log tab
    @FXML
    // Author: Daniel Hedegaard
    void menuSwitchToTimeLog(ActionEvent event) {
        editingEntryId = null; // clear any in-progress edit
        refreshTimeLogTable();
        populateProjectChoiceBox();
        logTimeErrorText.setVisible(false);
        timeLogTableErrorText.setVisible(false);
        logDatePicker.setValue(LocalDate.now());
        theView.menuSwitchToTimeLog(this.pages);
    }

    // Clears the Create Project form and navigates to it, pre-populating the leader
    // picker with all employees
    @FXML
    // Author: Daniel Hedegaard
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

    // Clears the Create Activity form and navigates to it — blocked if viewing an
    // archived project
    @FXML
    // Author: Daniel Hedegaard
    void switchToCreateActivity(ActionEvent event) {
        if (viewingArchivedProject)
            return;
        activityCreateErrorText.setVisible(false);
        createActivityDescription.setText(null);
        createActivityHours.setText(null);
        createActivityName.setText(null);
        createActivityStartDate.setValue(null);
        createActivityEndDate.setValue(null);
        theView.menuSwitchToCreateActivity(this.pages);
    }

    // Attempts to log in using the entered initials, then navigates to the Projects
    // tab on success
    @FXML
    // Author: Daniel Hedegaard
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

    // Validates the form inputs and creates a new activity on the current project.
    // Generates a unique ID by counting all activities across all projects at time
    // of creation.
    @FXML
    // Author: Daniel Hedegaard
    void createActivity(ActionEvent event) {
        if (createActivityName.getText().isEmpty() || createActivityDescription.getText().isEmpty()
                || createActivityStartDate.getValue() == null) {
            activityCreateErrorText.setText("Please fill out all non optional fields");
            activityCreateErrorText.setVisible(true);
        } else if (createActivityEndDate.getValue() != null
                && createActivityEndDate.getValue().isBefore(createActivityStartDate.getValue())) {
            activityCreateErrorText.setText("End date cannot be before start date");
            activityCreateErrorText.setVisible(true);
        } else {
            Project proj = theModel.getProject(projectShowName.getText());
            // Prevent the activity end date from exceeding the project's own end date
            if (createActivityEndDate.getValue() != null && proj.getEndDate() != null && !proj.getEndDate().isEmpty()
                    && createActivityEndDate.getValue().isAfter(LocalDate.parse(proj.getEndDate()))) {
                activityCreateErrorText
                        .setText("End date cannot be after the project end date (" + proj.getEndDate() + ")");
                activityCreateErrorText.setVisible(true);
            } else {
                try {
                    proj.createActivity(theModel.getLoggedIn(),
                            createActivityName.getText(), createActivityDescription.getText());
                    Activity activity = proj.getActivityFromName(createActivityName.getText());
                    // ID is based on the total number of activities across all projects at time of
                    // creation
                    int totalActivities = theModel.getProjects().stream()
                            .mapToInt(p -> p.getActivities().size())
                            .sum();
                    activity.setId(totalActivities);
                    activity.setStartDate(createActivityStartDate.getValue());
                    if (!createActivityHours.getText().isEmpty()) {
                        activity.setAlottedTime(createActivityHours.getText());
                    }
                    if (createActivityEndDate.getValue() != null) {
                        activity.setEndDate(createActivityEndDate.getValue());
                    }
                    try { theModel.writeActivityStub(proj, activity); } catch (Exception ignored) {}
                    this.goToProject(event, projectShowName.getText());
                } catch (Exception e) {
                    activityCreateErrorText
                            .setText(e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName());
                    activityCreateErrorText.setVisible(true);
                }
            }
        }
    }

    // Validates the Edit Activity form and saves changes to the activity.
    // Also updates all assigned employee calendars to reflect the new dates and
    // name.
    @FXML
    // Author: Daniel Hedegaard
    void editActivity(ActionEvent event) {
        if (editActivityName.getText() == null || editActivityDescription.getText() == null
                || editActivityStartDate.getValue() == null || editActivityHours.getText() == null) {
            activityEditErrorText.setText("Please fill out all non optional fields");
            activityEditErrorText.setVisible(true);
        } else {
            Project proj = theModel.getProject(projectShowName.getText());
            LocalDate effectiveEndDate = editActivityEndDate.getValue() != null
                    ? editActivityEndDate.getValue()
                    : (proj.getEndDate() != null && !proj.getEndDate().isEmpty()
                            ? LocalDate.parse(proj.getEndDate()) : null);
            if (effectiveEndDate != null && effectiveEndDate.isBefore(editActivityStartDate.getValue())) {
                activityEditErrorText.setText("End date cannot be before start date");
                activityEditErrorText.setVisible(true);
            } else if (effectiveEndDate != null && proj.getEndDate() != null && !proj.getEndDate().isEmpty()
                    && effectiveEndDate.isAfter(LocalDate.parse(proj.getEndDate()))) {
                activityEditErrorText
                        .setText("End date cannot be after the project end date (" + proj.getEndDate() + ")");
                activityEditErrorText.setVisible(true);
            } else {
                Activity activity = proj.getActivityFromName(editActivityHeader.getText());

                // Save the old values before applying changes so employee calendars can be
                // updated correctly
                LocalDate oldStartDate = activity.getStartDate();
                LocalDate oldEndDate = activity.getEndDate();
                String oldName = activity.getName();

                activity.setName(editActivityName.getText());
                activity.setDescription(editActivityDescription.getText());
                activity.setStartDate(editActivityStartDate.getValue());
                activity.setAlottedTime(editActivityHours.getText());
                activity.setEndDate(effectiveEndDate);

                // Synchronise all employee calendars with the updated activity dates and name
                activity.updateEmployeeCalendars(oldStartDate, oldEndDate, oldName);

                this.goToProject(event, projectShowName.getText());
                showActivityDetails(event, editActivityName.getText());
                try { theModel.syncLogs(); } catch (Exception ignored) {}
            }
        }

    }

    // Validates the Create Project form and creates the project in the model, then
    // returns to the Projects tab
    @FXML
    // Author: Daniel Hedegaard
    void createProject(ActionEvent event) {
        if (projectNameField.getText() == null || projectStartDatePicker.getValue() == null
                || projectDescriptionField.getText() == null) {
            projectCreateErrorText.setText("Please fill out all non optional fields");
            projectCreateErrorText.setVisible(true);
        } else if (projectEndDatePicker.getValue() != null
                && projectEndDatePicker.getValue().isBefore(projectStartDatePicker.getValue())) {
            projectCreateErrorText.setText("End date cannot be before start date");
            projectCreateErrorText.setVisible(true);
        } else {

            theModel.createProject(projectNameField.getText());
            Project project = theModel.getProject(projectNameField.getText());
            project.setId(theModel);
            project.setDescription(projectDescriptionField.getText());
            if (projectLeaderPicker.getValue() != null) {
                project.setProjectLeader(theModel.getEmployeeFromName(projectLeaderPicker.getValue().toString()));
            }
            if (projectStartDatePicker.getValue() != null) {
                project.setStartDate(projectStartDatePicker.getValue());
            }
            if (projectEndDatePicker.getValue() != null) {
                project.setEndDate(projectEndDatePicker.getValue().toString());
            }

            try { theModel.writeProjectStub(project); } catch (Exception ignored) {}
            this.menuSwitchToProjects(event);
        }
    }

    // Validates the Edit Project form and saves changes to the existing project
    @FXML
    // Author: Daniel Hedegaard
    void editProject(ActionEvent event) {
        if (editProjectName.getText() == null || editProjectStartDate.getValue() == null
                || editProjectDescription.getText() == null) {
            projectEditErrorText.setText("Please fill out all non optional fields");
            projectEditErrorText.setVisible(true);
        } else if (editProjectEndDate.getValue() != null
                && editProjectEndDate.getValue().isBefore(editProjectStartDate.getValue())) {
            projectEditErrorText.setText("End date cannot be before start date");
            projectEditErrorText.setVisible(true);
        } else {

            Project project = theModel.getProject(editProjectHeader.getText());
            project.setName(editProjectName.getText());
            project.setDescription(editProjectDescription.getText());
            if (editProjectProjectLeader.getValue() != null) {
                project.setProjectLeader(theModel.getEmployeeFromName(editProjectProjectLeader.getValue().toString()));
            }
            if (editProjectStartDate.getValue() != null) {
                project.setStartDate(editProjectStartDate.getValue());
            }
            if (editProjectEndDate.getValue() != null) {
                project.setEndDate(editProjectEndDate.getValue().toString());
            } else {
                project.setEndDate(null);
            }

            this.goToProject(event, editProjectName.getText());
            try { theModel.syncLogs(); } catch (Exception ignored) {}

        }
    }

    // Loads the project detail view: populates all labels, description, statistics
    // (leaders only),
    // and the activity list, then navigates to the Activity tab
    @FXML
    // Author: Daniel Hedegaard
    void goToProject(ActionEvent event, String projectName) {
        Project project = theModel.getProject(projectName);
        viewingArchivedProject = isProjectArchived(project);
        // Hide the "Add Activity" button for archived projects — no modifications
        // allowed
        addActivityButton.setVisible(!viewingArchivedProject);
        addActivityButton.setManaged(!viewingArchivedProject);
        activityDetails.setVisible(false);
        projectViewErrorText.setVisible(false);
        projectShowName.setText(projectName);

        // Only the project leader (or projects with no leader) see the statistics panel
        boolean isLeader = project.getProjectLeader() == null
                || project.getProjectLeader().equals(theModel.getLoggedIn());
        if (isLeader) {
            LocalDate today = LocalDate.now();
            long completed = project.getActivities().stream()
                    .filter(a -> a.getEndDate() != null && !a.getEndDate().isAfter(today))
                    .count();
            long inProgress = project.getActivities().size() - completed;
            long totalEmployees = project.getActivities().stream()
                    .flatMap(a -> a.getEmployees().stream())
                    .distinct()
                    .count();
            double totalAllottedTime = project.getActivities().stream()
                    .filter(a -> a.getAlottedTime() != null && !a.getAlottedTime().isEmpty())
                    .mapToDouble(a -> {
                        try {
                            return Double.parseDouble(a.getAlottedTime());
                        } catch (NumberFormatException e) {
                            return 0;
                        }
                    })
                    .sum();
            double totalLoggedTime = 0;
            try {
                String projectId = project.getId();
                totalLoggedTime = theModel.loadAllLogs().stream()
                        .filter(log -> log.size() > 5
                                && log.get(2).equals(projectId)
                                && !log.get(1).isEmpty()
                                && !log.get(5).isEmpty())
                        .mapToDouble(log -> {
                            try { return Double.parseDouble(log.get(5)); }
                            catch (NumberFormatException e) { return 0; }
                        })
                        .sum();
            } catch (Exception ignored) {}
            statsActivitiesInProgress.setText("Activities in progress: " + inProgress);
            statsActivitiesCompleted.setText("Activities completed: " + completed);
            statsTotalEmployees.setText("Total employees: " + totalEmployees);
            statsTotalAllottedTime.setText("Total allotted time: " + totalAllottedTime + " hours");
            statsTotalLoggedTime.setText("Total logged time: " + totalLoggedTime + " hours");
            projectStatsPane.setVisible(true);
        } else {
            projectStatsPane.setVisible(false);
        }
        projectShowId.setText(project.getId());
        projectShowStartDate
                .setText(project.getStartDate() != null ? "Start date: " + project.getStartDate() : "Start date: N/A");
        projectShowEndDate
                .setText(project.getEndDate() != null ? "End date: " + project.getEndDate() : "End date: N/A");
        projectShowSatusColor.setStyle("-fx-fill: " + getStatusColorForProject(project) + ";");
        if (project.getProjectLeader() != null) {
            projectShowPojectLeader.setText("Project Leader: " + project.getProjectLeader().getName());
        } else {
            projectShowPojectLeader.setText("Project Leader: N/A");
        }
        projectShowDescription.setText(project.getDescription() != null ? project.getDescription() : "");
        theView.showActivities(projectShowActivitiesBounds, projectName);

        theView.menuSwitchToProjectView(this.pages, projectName);
    }

    // Pre-fills the Edit Activity form with the selected activity's current data,
    // then navigates to it.
    // Blocked if the project is archived or the logged-in user is not the project
    // leader.
    @FXML
    // Author: Daniel Hedegaard
    void gotToEditActivity(ActionEvent event) {
        if (viewingArchivedProject)
            return;
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

    // Pre-fills the Edit Project form with the current project's data, then
    // navigates to it.
    // Archived projects can only have their end date changed; all other fields are
    // disabled.
    @FXML
    // Author: Daniel Hedegaard
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
        editProjectStartDate.setValue(project.getStartDate());
        if (project.getEndDate() != null && !project.getEndDate().isEmpty()) {
            editProjectEndDate.setValue(LocalDate.parse(project.getEndDate()));
        } else {
            editProjectEndDate.setValue(null);
        }
        editProjectProjectLeader.getItems().clear();
        for (Employee employee : theModel.getEmployees()) {
            editProjectProjectLeader.getItems().add(employee.getName());
        }
        if (project.getProjectLeader() != null) {
            editProjectProjectLeader.setValue(project.getProjectLeader().getName());
        }

        // Lock most fields for archived projects — only the end date adjustment is
        // permitted
        editProjectName.setDisable(viewingArchivedProject);
        editProjectDescription.setDisable(viewingArchivedProject);
        editProjectStartDate.setDisable(viewingArchivedProject);
        editProjectProjectLeader.setDisable(viewingArchivedProject);
        projectEditErrorText.setVisible(false);
        editProjectArchivedNote.setVisible(viewingArchivedProject);

        theView.menuSwitchToEditProject(pages);
    }

    // Populates and shows the activity detail panel on the right side of the
    // project view.
    // Hides edit and add-employee controls when viewing an archived project.
    @FXML
    // Author: Daniel Hedegaard
    void showActivityDetails(ActionEvent event, String activityName) {
        Activity activity = theModel.getProject(projectShowName.getText()).getActivityFromName(activityName);
        activityDetailName.setText(activity.getName());
        activityDetailId.setText("ID: " + activity.getId());
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

        // Determine the status indicator color from the activity's date range
        if (activity.getStartDate() != null) {
            if (activity.getEndDate() != null) {
                // Both dates present — delegate to the shared color helper
                activityDetailStatusColor
                        .setStyle("-fx-fill : " + this.getStatusColorForActivity(activity) + " ;");
            } else {
                // No end date: yellow if already started, red if not yet started
                if (activity.getStartDate().isBefore(LocalDate.now())
                        || activity.getStartDate().isEqual(LocalDate.now())) {
                    activityDetailStatusColor.setStyle("-fx-fill: #fffc00;");
                } else {
                    activityDetailStatusColor.setStyle("-fx-fill: #ff0000;");
                }
            }
        } else {
            // No start date at all — treat as not started
            activityDetailStatusColor.setStyle("-fx-fill: #ff0000;");
        }
        employeeAddActivityErrorText.setVisible(false);
        activityDetailsEmployeeInitalsField.setText(null);
        // Hide all write controls for archived projects
        editActivityButton.setVisible(!viewingArchivedProject);
        editActivityButton.setManaged(!viewingArchivedProject);
        deleteActivityButton.setVisible(!viewingArchivedProject);
        deleteActivityButton.setManaged(!viewingArchivedProject);
        addEmployeeButton.setVisible(!viewingArchivedProject);
        addEmployeeButton.setManaged(!viewingArchivedProject);
        activityDetailsEmployeeInitalsField.setVisible(!viewingArchivedProject);
        activityDetailsEmployeeInitalsField.setManaged(!viewingArchivedProject);
        confirmAddEmployeeButton.setVisible(false);
        // Hide the project stats pane while an activity detail panel is open
        projectStatsPane.setVisible(false);
        theView.showActivityDetails(this.activityDetails, activity, this.activityDetailsEmployeeBounds,
                viewingArchivedProject);

    }

    // Hides the activity detail panel and restores the stats pane if the user is
    // the project leader
    @FXML
    // Author: GedeGustav
    void closeActivityDetails(ActionEvent event) {
        activityDetails.setVisible(false);
        Project project = theModel.getProject(projectShowName.getText());
        boolean isLeader = project.getProjectLeader() == null
                || project.getProjectLeader().equals(theModel.getLoggedIn());
        projectStatsPane.setVisible(isLeader);
    }

    // Removes the given employee from the currently viewed activity.
    // Blocked for archived projects and non-leaders.
    @FXML
    // Author: Daniel Hedegaard
    void removeEmployee(ActionEvent event, Employee employee) {
        if (viewingArchivedProject)
            return;
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
        try { theModel.syncLogs(); } catch (Exception ignored) {}
    }

    // Loads the Employee detail tab for the given employee, showing their calendar
    // for the current week.
    // The sick/time-off panel is only shown when viewing your own profile.
    @FXML
    // Author: Daniel Hedegaard
    void goToEmployee(ActionEvent event, Employee employee) {
        currentShownEmployee = employee;
        employeeShowName.setText(employee.getName());
        employeeShowInitials.setText(employee.getInitials());
        employeeShowDatePicker.setValue(LocalDate.now());
        employeeShowNumberOfTasks
                .setText("Activities this week: " + employee.getCalendar().getEntries(LocalDate.now()).size());
        if (theModel.getLoggedIn().getName().equals(employee.getName())) {
            timeOffPane.setVisible(true);
            sicknessErrorText.setVisible(false);
            timeOffErrorText.setVisible(false);
            // Show a status message reflecting the employee's current sick/time-off state
            if (employee.getCalendar().hasTimeOffInPeriod(LocalDate.now(), LocalDate.now().plusDays(1))) {
                sickStatusText.setText("You have sick leave or time off this week");
                sickStatusText.setStyle("-fx-fill: #cc0000;");
            } else {
                sickStatusText.setText("No current sick leave or time off");
                sickStatusText.setStyle("-fx-fill: #008800;");
            }
        } else {
            timeOffPane.setVisible(false);
        }
        theView.showEmployeeCalendar(employeeCalendarBounds, employee, LocalDate.now());
        theView.menuSwitchToEmployee(pages);
    }

    // Refreshes the employee calendar display when the user picks a different date
    // from the date picker
    @FXML
    // Author: GedeGustav
    void refreshEmployeeCalendar(ActionEvent event) {
        if (currentShownEmployee == null || employeeShowDatePicker.getValue() == null)
            return;
        LocalDate date = employeeShowDatePicker.getValue();
        employeeShowNumberOfTasks
                .setText("Activities this week: " + currentShownEmployee.getCalendar().getEntries(date).size());
        theView.showEmployeeCalendar(employeeCalendarBounds, currentShownEmployee, date);
    }

    // Attempts to add the entered employee to the current activity.
    // If the employee is fully booked during the activity period, a warning is
    // shown
    // with an "Add anyway" button rather than silently rejecting the request.
    @FXML
    // Author: Daniel Hedegaard
    void addEmployeeToActivity(ActionEvent event) {
        if (viewingArchivedProject)
            return;
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
                    try { theModel.syncLogs(); } catch (Exception ignored) {}
                } catch (IllegalStateException e) {
                    // Hard block — employee has a direct conflict that cannot be overridden
                    employeeAddActivityErrorText.setText(e.getMessage());
                    employeeAddActivityErrorText.setVisible(true);
                    confirmAddEmployeeButton.setVisible(false);
                } catch (IllegalArgumentException e) {
                    // Soft warning — employee exceeds 10 activities/day limit; offer an override
                    // option
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

    // Force-adds the previously warned employee to the activity, bypassing the
    // availability limit check
    @FXML
    // Author: GedeGustav
    void confirmAddEmployeeToActivity(ActionEvent event) {
        if (viewingArchivedProject)
            return;
        if (pendingEmployeeToAdd == null)
            return;
        Project project = theModel.getProject(projectShowName.getText());
        if (project.getProjectLeader() != null &&
                !project.getProjectLeader().getName().equals(theModel.getLoggedIn().getName())) {
            employeeAddActivityErrorText.setText("Only the project leader can modify employees");
            employeeAddActivityErrorText.setVisible(true);
            return;
        }
        Activity activity = theModel.getProject(projectShowName.getText())
                .getActivityFromName(activityDetailName.getText());
        try {
            activity.forceAddEmployee(pendingEmployeeToAdd);
        } catch (IllegalStateException e) {
            employeeAddActivityErrorText.setText(e.getMessage());
            employeeAddActivityErrorText.setVisible(true);
            confirmAddEmployeeButton.setVisible(false);
            pendingEmployeeToAdd = null;
            return;
        }
        pendingEmployeeToAdd = null;
        activityDetailsEmployeeInitalsField.setText(null);
        confirmAddEmployeeButton.setVisible(false);
        employeeAddActivityErrorText.setVisible(false);
        showActivityDetails(event, activity.getName());
        try { theModel.syncLogs(); } catch (Exception ignored) {}
    }

    // Shows the employee detail sidebar for the selected employee in the Employees
    // list tab
    @FXML
    // Author: Daniel Hedegaard
    void showEmployeeDetails(ActionEvent event, Employee employee) {
        employeDetailsName.setText(employee.getName());
        employeeDetailsInitials.setText(employee.getInitials());
        employeDetails.setVisible(true);
    }

    // Shows the currently logged-in employee's own details in the sidebar
    @FXML
    // Author: Daniel Hedegaard
    void showSelf(ActionEvent event) {
        employeDetailsName.setText(theModel.getLoggedIn().getName());
        employeeDetailsInitials.setText(theModel.getLoggedIn().getInitials());
        employeDetails.setVisible(true);
    }

    // Navigates to the full Employee tab for the employee currently shown in the
    // sidebar
    @FXML
    // Author: Daniel Hedegaard
    void viewAvailability(ActionEvent event) {
        goToEmployee(event, theModel.getEmployeeFromInitials(employeeDetailsInitials.getText()));
    }

    // Creates and hires a new employee if the given initials are not already taken
    @FXML
    // Author: Daniel Hedegaard
    void hireEmployee(ActionEvent event) {
        if (theModel.getEmployeeFromInitials(hireEmployeeInitials.getText()) == null) {
            theModel.hireEmployee(new Employee(hireEmployeeName.getText(), hireEmployeeInitials.getText()));
            menuSwitchToEmployees(event);
        } else {
            hireEmployeeErrorText.setText("Employee with initials already exists");
            hireEmployeeErrorText.setVisible(true);
        }
    }

    // Registers sick leave for the logged-in employee for today after the checkbox
    // is confirmed
    @FXML
    // Author: GedeGustav
    void reportSickness(ActionEvent event) {
        sicknessErrorText.setVisible(false);
        if (!sickCheckBox.isSelected()) {
            sicknessErrorText.setText("Please check 'I am sick' to confirm");
            sicknessErrorText.setVisible(true);
            return;
        }
        Employee employee = theModel.getLoggedIn();
        employee.getCalendar().registerTimeOff(LocalDate.now(), "Sick");
        sickCheckBox.setSelected(false);
        sickStatusText.setText("You are sick this week");
        sickStatusText.setStyle("-fx-fill: #cc0000;");
        theView.showEmployeeCalendar(employeeCalendarBounds, employee, LocalDate.now());
        employeeShowNumberOfTasks
                .setText("Activities this week: " + employee.getCalendar().getEntries(LocalDate.now()).size());
    }

    // Registers a time-off period for the logged-in employee.
    // The end date from the picker is inclusive; one day is added internally so the
    // full end week is covered.
    @FXML
    // Author: GedeGustav
    void requestTimeOff(ActionEvent event) {
        timeOffErrorText.setVisible(false);
        if (timeOffStartDatePicker.getValue() == null || timeOffEndDatePicker.getValue() == null) {
            timeOffErrorText.setText("Please select start and end dates");
            timeOffErrorText.setVisible(true);
            return;
        }
        LocalDate start = timeOffStartDatePicker.getValue();
        LocalDate end = timeOffEndDatePicker.getValue();
        if (end.isBefore(start)) {
            timeOffErrorText.setText("End date must be after start date");
            timeOffErrorText.setVisible(true);
            return;
        }
        String reason = (timeOffReasonField.getText() != null && !timeOffReasonField.getText().isEmpty())
                ? timeOffReasonField.getText()
                : "Time off";
        Employee employee = theModel.getLoggedIn();
        // endDate from picker is inclusive; add 1 day so distinctWeeks covers the end
        // week
        employee.getCalendar().registerTimeOff(start, end.plusDays(1), reason);
        timeOffReasonField.setText(null);
        timeOffStartDatePicker.setValue(null);
        timeOffEndDatePicker.setValue(null);
        sickStatusText.setText("Time off registered: " + start + " – " + end);
        sickStatusText.setStyle("-fx-fill: #008800;");
        theView.showEmployeeCalendar(employeeCalendarBounds, employee, LocalDate.now());
        employeeShowNumberOfTasks
                .setText("Activities this week: " + employee.getCalendar().getEntries(LocalDate.now()).size());
    }

    // Returns true if the project's end date has passed and all its activities are
    // also finished
    // Author: GedeGustav
    private boolean isProjectArchived(Project project) {
        LocalDate today = LocalDate.now();
        return project.getEndDate() != null && !project.getEndDate().isEmpty()
                && LocalDate.parse(project.getEndDate()).isBefore(today)
                && project.getActivities().stream()
                        .allMatch(a -> a.getEndDate() != null && a.getEndDate().isBefore(today));
    }

    // Returns a hex color string for the activity's status indicator:
    // red = not yet started, yellow = in progress, green = completed
    // Author: Daniel Hedegaard
    public String getStatusColorForActivity(Activity activity) {
        if (activity.getStartDate().isAfter(LocalDate.now())) {
            return "#ff0000";
        } else if (activity.getEndDate().isAfter(LocalDate.now())) {
            return "#fffc00";
        } else {
            return "#41ff00";
        }
    }

    @FXML
    // Author: GedeGustav
    void deleteProject(ActionEvent event) {
        Project project = theModel.getProject(projectShowName.getText());
        if (project.getProjectLeader() != null &&
                !project.getProjectLeader().getName().equals(theModel.getLoggedIn().getName())) {
            projectViewErrorText.setText("Only the project leader can delete this project");
            projectViewErrorText.setVisible(true);
            return;
        }
        try {
            theModel.deleteProject(project);
            menuSwitchToProjects(event);
        } catch (Exception e) {
            projectViewErrorText.setText("Could not delete project: " + e.getMessage());
            projectViewErrorText.setVisible(true);
        }
    }

    @FXML
    // Author: GedeGustav
    void deleteActivity(ActionEvent event) {
        if (viewingArchivedProject) return;
        Project project = theModel.getProject(projectShowName.getText());
        if (project.getProjectLeader() != null &&
                !project.getProjectLeader().getName().equals(theModel.getLoggedIn().getName())) {
            employeeAddActivityErrorText.setText("Only the project leader can delete this activity");
            employeeAddActivityErrorText.setVisible(true);
            return;
        }
        Activity activity = project.getActivityFromName(activityDetailName.getText());
        try {
            theModel.deleteActivity(project, activity);
            activityDetails.setVisible(false);
            projectStatsPane.setVisible(true);
            goToProject(event, project.getName());
        } catch (Exception e) {
            employeeAddActivityErrorText.setText("Could not delete activity: " + e.getMessage());
            employeeAddActivityErrorText.setVisible(true);
        }
    }

    @FXML
    // Author: GedeGustav
    void deleteSelectedLog(ActionEvent event) {
        timeLogTableErrorText.setVisible(false);
        List<String> selected = timeLogTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            timeLogTableErrorText.setText("Please select a log entry first");
            timeLogTableErrorText.setVisible(true);
            return;
        }
        try {
            theModel.deleteLogEntry(selected.get(0));
            refreshTimeLogTable();
            resetForm();
        } catch (Exception e) {
            timeLogTableErrorText.setText("Could not delete entry: " + e.getMessage());
            timeLogTableErrorText.setVisible(true);
        }
    }

    // --------------------------- TIME LOG --------------------------------
    // Returns a hex color string for the project's status indicator:
    // red = not yet started, yellow = in progress, green = completed
    // Author: GedeGustav
    public String getStatusColorForProject(Project project) {
        if (project.getStartDate() != null && project.getStartDate().isAfter(LocalDate.now())) {
            return "#ff0000";
        } else if (project.getEndDate() == null || LocalDate.parse(project.getEndDate()).isAfter(LocalDate.now())) {
            return "#fffc00";
        } else {
            return "#41ff00";
        }
    }

    // Author: GubbeMK
    private void refreshTimeLogTable() {
        try {
            String myInitials = theModel.getLoggedIn().getInitials();
            List<List<String>> myLogs = theModel.loadAllLogs().stream()
                    .filter(log -> log.size() > 5
                            && log.get(1).equals(myInitials)
                            && !log.get(5).isEmpty())
                    .collect(Collectors.toList());
            timeLogTable.setItems(FXCollections.observableArrayList(myLogs));
        } catch (Exception e) {
            e.printStackTrace();
            logTimeErrorText.setText("Could not load logs: " + e.getMessage());
            logTimeErrorText.setVisible(true);
        }
    }

    // Author: GedeGustav
    private void populateProjectChoiceBox() {
        logProjectChoiceBox.getItems().clear();
        logActivityChoiceBox.getItems().clear();
        for (Project project : theModel.getProjects()) {
            logProjectChoiceBox.getItems().add(project.getName());
        }
    }

    // Author: GedeGustav
    private void refreshActivityChoiceBoxForProject(String projectName) {
        logActivityChoiceBox.getItems().clear();
        if (projectName == null) return;
        Project project = theModel.getProject(projectName);
        if (project == null) return;
        for (Activity activity : project.getActivities()) {
            logActivityChoiceBox.getItems().add(activity.getName());
        }
    }

    @FXML
    // Author: GubbeMK
    void logTime(ActionEvent event) throws IOException {
        logTimeErrorText.setVisible(false);
        timeLogTableErrorText.setVisible(false);

        List<String> originalEntires = timeLogTable.getSelectionModel().getSelectedItem();
        String selectedProject = logProjectChoiceBox.getValue();
        String selectedActivity = logActivityChoiceBox.getValue();
        LocalDate date = logDatePicker.getValue();
        Double dHours = logHoursSpinner.getValue();

        // Validate inputs based on whether we're editing or creating
        if (originalEntires == null) {
            // Creating a new entry — all form fields must be filled
            if (selectedProject == null || selectedActivity == null || date == null || dHours == null || dHours <= 0) {
                logTimeErrorText.setText("Please fill out all fields");
                logTimeErrorText.setVisible(true);
                return;
            }
        } else {
            // Editing — only validate the spinner if a value is present
            if (dHours != null && dHours <= 0) {
                logTimeErrorText.setText("Hours must be positive");
                logTimeErrorText.setVisible(true);
                return;
            }
        }

        // Resolve project and activity
        Project project;
        Activity activity;
        if (selectedProject != null && selectedActivity != null) {
            project = theModel.getProject(selectedProject);
            activity = project.getActivityFromName(selectedActivity);
        } else {
            project = theModel.getProject(originalEntires.get(6));
            activity = project.getActivityFromName(originalEntires.get(8));
        }

        // Resolve date
        if (date == null) {
            date = LocalDate.parse(originalEntires.get(3));
        }

        // Resolve hours
        double hours = (dHours != null) ? dHours : Double.parseDouble(originalEntires.get(2));

        try {
            if (originalEntires == null) {
                // No selection → create new entry
                theModel.registerLog(theModel.getLoggedIn(), project, activity, date, hours);
            } else {
                // Selection exists → update that entry
                theModel.updateLogEntry(originalEntires.get(0), theModel.getLoggedIn(), project, activity, date, hours);
            }
            refreshTimeLogTable();
            resetForm();
        } catch (IllegalArgumentException e) {
            logTimeErrorText.setText(e.getMessage());
            logTimeErrorText.setVisible(true);
        } catch (Exception e) {
            logTimeErrorText.setText("Error: " + e.getMessage());
            logTimeErrorText.setVisible(true);
        }
    }

    // Author: GubbeMK
    private void resetForm() {
        logHoursSpinner.getValueFactory().setValue(1.0);
        logProjectChoiceBox.setValue(null);
        logActivityChoiceBox.setValue(null);
    }

    @FXML
    // Author: GubbeMK
    void editSelectedLog(ActionEvent event) {
        timeLogTableErrorText.setVisible(false);

        List<String> selected = timeLogTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            timeLogTableErrorText.setText("Please select a log entry first");
            timeLogTableErrorText.setVisible(true);
            return;
        }

        // Pre-fill the existing form with the selected entry's values
        String projectName = selected.get(6);
        String activityName = selected.get(8);
        String dateStr = selected.get(3);
        String hoursStr = selected.get(2);

        logProjectChoiceBox.setValue(projectName);   // triggers listener → populates activities
        logActivityChoiceBox.setValue(activityName);
        logDatePicker.setValue(LocalDate.parse(dateStr));
        logHoursSpinner.getValueFactory().setValue(Double.parseDouble(hoursStr));

        // Remember which entry we're editing so we can delete the old one when "Log
        // time" is clicked
        editingEntryId = selected.get(0);
        timeLogTableErrorText.setText("Editing entry — modify and click 'Log time' to save");
        timeLogTableErrorText.setStyle("-fx-fill: #008800;");
        timeLogTableErrorText.setVisible(true);
    }
}
