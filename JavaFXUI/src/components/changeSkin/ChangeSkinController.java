package components.changeSkin;


import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class ChangeSkinController
{
    private final String PURPLE = "Purple";
    private final String DARK = "Dark";
    private final String LIGHT = "Light";

    private ScrollPane mainAppPane;
    private ObservableList<String> skinTypeValue = FXCollections.observableArrayList(PURPLE, DARK, LIGHT);

    private SimpleListProperty<String> skinTypesProperty;
    private SimpleStringProperty selectedSkinTypeProperty;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private GridPane selectSkinGridPane;

    @FXML
    private Label selectSkinLabel;

    @FXML
    private ComboBox<String> selectSkinComboBox;

    @FXML
    private Button applySkinButton;

    public void setMainAppPane(ScrollPane mainAppPane)
    {
        this.mainAppPane = mainAppPane;
    }
    public AnchorPane getRootPane()
    {
        return rootPane;
    }

    public ChangeSkinController()
    {
        skinTypesProperty = new SimpleListProperty<>();
        selectedSkinTypeProperty = new SimpleStringProperty();
    }

    @FXML
    void initialize()
    {
        skinTypesProperty.setValue(skinTypeValue);
        selectSkinComboBox.itemsProperty().bind(skinTypesProperty);
        applySkinButton.disableProperty().bind(selectSkinComboBox.getSelectionModel().selectedItemProperty().isNull());
        selectedSkinTypeProperty.bind(selectSkinComboBox.getSelectionModel().selectedItemProperty());
    }

    @FXML
    void applySelectedSkin(ActionEvent event)
    {
        if (selectedSkinTypeProperty.getValue() != null)
        {
            mainAppPane.getStylesheets().clear();
            mainAppPane.getStylesheets().add("/resources/MainStyle" + selectedSkinTypeProperty.getValue() + ".css");
            selectSkinComboBox.getSelectionModel().clearSelection();
        }
    }
}
