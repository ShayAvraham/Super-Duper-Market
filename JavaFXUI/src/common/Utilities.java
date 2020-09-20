package common;

import dataContainers.OrderDataContainer;
import dataContainers.ProductDataContainer;
import dataContainers.StoreDataContainer;
import javafx.scene.control.Alert;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.awt.*;
import java.text.NumberFormat;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public final class Utilities
{
//    private final String LOAD_FILE_FAILURE_MESSAGE = "Failed to load the file, cause of failure:\n";
        private static final String POSITION_FORMAT = "(%1$s, %2$s)";
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

    public static StringConverter<StoreDataContainer> getStoreConverterInPlaceOrder()
    {
       return new StringConverter<StoreDataContainer>()
       {
            @Override
            public String toString(StoreDataContainer object)
            {
                return object.getName() + " | " +
                        "id: " + object.getId() + " | " +
                        "location: " +"("+ object.getPosition().x +
                         ","+ object.getPosition().y +")";
            }

            @Override
            public StoreDataContainer fromString(String string) {
                return null;
            }
        };
    }

    public static StringConverter<ProductDataContainer> getProductConverterInPlaceOrder()
    {
        return new StringConverter<ProductDataContainer>()
        {
            @Override
            public String toString(ProductDataContainer object)
            {
                return object.getName() + " | " +
                        "id: " + object.getId();
            }

            @Override
            public ProductDataContainer fromString(String string) {
                return null;
            }
        };
    }

    public static String convertPositionFormat(Point position)
    {
        return String.format(POSITION_FORMAT, (int) position.getX(), (int) position.getY());
    }

    public static StringConverter<OrderDataContainer> getOrderConverterInShowOrdersHistory()
    {
        return new StringConverter<OrderDataContainer>()
        {
            @Override
            public String toString(OrderDataContainer object)
            {
                return "ID: " + object.getId() + " | " +
                        "Date: " + object.getDate();
            }

            @Override
            public OrderDataContainer fromString(String string) {
                return null;
            }
        };
    }

    public static Spinner SetSpinnerToNaturalNumbers(Spinner spinner , Integer maxValue)
    {
        TextFormatter<Integer> amountFormatter = new TextFormatter<Integer>(new IntegerStringConverter(), 1,
                maxValue == null ? getNaturalNumbersFilter(): getMaxNumberFilter());
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,maxValue == null? Integer.MAX_VALUE : maxValue, 1, 1));
        spinner.getEditor().setTextFormatter(amountFormatter);
        spinner.getValueFactory().valueProperty().bindBidirectional(amountFormatter.valueProperty());
        spinner.setEditable(true);
        return spinner;
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

    private static UnaryOperator<TextFormatter.Change> getMaxNumberFilter()
    {
        UnaryOperator<TextFormatter.Change> naturalNumbersFilter = change -> {
            String text = change.getControlNewText();
            if (text.matches("[1-4][0-9]?")|| text.matches("[0-9]") || text.matches("[5][0]"))
            {
                return change;
            }
            return null;
        };

        return naturalNumbersFilter;
    }

    public static Spinner SetSpinnerToPositiveRealNumbers(Spinner spinner)
    {
        TextFormatter<Double> amountFormatter = new TextFormatter<Double>(new DoubleStringConverter(), 1d, getPositiveRealNumbersFilter());
        spinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(1,Integer.MAX_VALUE , 1, 0.1));
        spinner.getEditor().setTextFormatter(amountFormatter);
        spinner.getValueFactory().valueProperty().bindBidirectional(amountFormatter.valueProperty());
        spinner.setEditable(true);
        return spinner;
    }

    public static UnaryOperator<TextFormatter.Change> getPositiveRealNumbersFilter()
    {

        UnaryOperator<TextFormatter.Change> naturalNumbersFilter = change -> {
            String text = change.getControlNewText();
            if (text.matches("[1-9]+[0-9]*[.]?[0-9]?[0-9]?"))
            {
                return change;
            }
            return null;
        };

        return naturalNumbersFilter;
    }
}
