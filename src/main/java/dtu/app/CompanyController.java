package dtu.app;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class CompanyController {
    private Company theModel;
    private CompanyViewer theView;
   
    @FXML
    private AnchorPane bottomPane;
    @FXML
    private Button ProfileIcon;

    public void setModelAndView(Company model, CompanyViewer view) {
        this.theModel = model;
        this.theView = view;
        
    }
 
}
