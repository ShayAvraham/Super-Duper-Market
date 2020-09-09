package common;

import javafx.scene.control.Alert;

public final class Utilities
{
//    private final String LOAD_FILE_FAILURE_MESSAGE = "Failed to load the file, cause of failure:\n";

    private Utilities()
    {
    }

    public static void ShowErrorAlert(String alertMsg)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setTitle("Error Dialog");
        alert.setContentText(alertMsg);
        alert.showAndWait();
    }

    public static void ShowAlert(String alertMsg)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Information Dialog");
        alert.setContentText(alertMsg);
        alert.showAndWait();
    }
}
