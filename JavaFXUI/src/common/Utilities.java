package common;

import javafx.scene.control.Alert;
import javafx.scene.control.TextFormatter;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

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

    public static void ShowInformationAlert(String alertMsg)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Information Dialog");
        alert.setContentText(alertMsg);
        alert.showAndWait();
    }

    public static UnaryOperator<TextFormatter.Change> getNaturalNumbersFilter()
    {
        UnaryOperator<TextFormatter.Change> naturalNumbersFilter = change -> {
            String text = change.getControlNewText();

            if (text.matches("[1-9]+[0-9]*")) {
                return change;
            }
            return null;
        };

        return naturalNumbersFilter;
    }
}
