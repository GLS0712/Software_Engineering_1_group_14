package dtu.app;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

public class CompanyController {
    private Company theModel;
    private CompanyViewer theView;
   
    @FXML
    private TabPane pages;
    @FXML
    private AnchorPane bottomPane;
    @FXML
    private Button ProfileIcon;

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
        theView.menuSwitchToProjects(this.pages);
    }

    @FXML
    void menuSwitchToTimeLog(ActionEvent event) {
        theView.menuSwitchToTimeLog(this.pages);
    }
 
    @FXML
    void employeeLogin(ActionEvent event) {

    }
}
