package tasks;

import common.Utilities;
import engineLogic.SystemManager;
import javafx.application.Platform;
import javafx.concurrent.Task;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public class LoadXmlTask extends Task<Boolean>
{
    private final int NUM_OF_LOADING_STEPS = 50;
    private final String LOAD_FILE_FAILURE_MESSAGE = "Failed to load the file, cause of failure:\n";

    private SystemManager systemManager;
    private File file;
    private Runnable setIsFileLoadedProperty;

    public LoadXmlTask(SystemManager systemManager, File file , Runnable setIsFileLoadedProperty)
    {
        this.systemManager = systemManager;
        this.file = file;
        this.setIsFileLoadedProperty = setIsFileLoadedProperty;
    }

    @Override
    protected Boolean call() throws Exception
    {
        try
        {
            final int[] loadingStepsCount = {0};
            updateMessage("Opening file");
            updateProgress(++loadingStepsCount[0], NUM_OF_LOADING_STEPS);
            Thread.sleep(1000L);
            this.updateMessage("Loading file");
            systemManager.loadDataFromXmlFile(file.getAbsolutePath());
            for (int i = loadingStepsCount[0]; i < 50; i++) {
                updateProgress(++loadingStepsCount[0], NUM_OF_LOADING_STEPS);
                Thread.sleep(100L);
            }
            this.updateMessage("File loaded successfully");
            updateProgress(++loadingStepsCount[0], NUM_OF_LOADING_STEPS);
            setIsFileLoadedProperty.run();
            return true;
        }
        catch (Exception e)
        {
            this.updateMessage("XML file is invalid");
            Platform.runLater(
                    () -> Utilities.ShowErrorAlert(LOAD_FILE_FAILURE_MESSAGE + e.getMessage()));

            return false;
        }
    }
}
