import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CalculateEndDateApp extends Application {
    public List<Holiday> holidayList = new ArrayList<>();
    LocalDate endDate;

    @Override
    public void start(Stage stage) {
        //create Date Picker and set default date
        DatePicker datePickerStartEvent = new DatePicker();
        datePickerStartEvent.setValue(LocalDate.now());
        datePickerStartEvent.setShowWeekNumbers(false);

        //create Spinner: min = 1, max = 100, default = 3
        final Spinner<Integer> spinnerDuration = new Spinner<>();
        final int initialValue = 3;

        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, initialValue);
        spinnerDuration.setValueFactory(valueFactory);

        //create obj Font and Label's
        Font font = new Font("Times New Roman", 25);
        Label labelStart = new Label("Enter date of start: ");
        labelStart.setFont(font);
        Label labelDuration = new Label("Enter duration(days): ");
        labelDuration.setFont(font);
        Label labelAddHoliday = new Label("Add holidays: ");
        labelAddHoliday.setFont(font);

        //create Button and evenHandler(appear new window) Holiday's start&end date
        Button buttonAddHoliday = new Button("Add");
        buttonAddHoliday.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Label labelHolidayStartDate = new Label("Holiday start: ");
                Label labelHolidayEndDate = new Label("Holiday end: ");
                DatePicker datePickerStartHoliday = new DatePicker();
                datePickerStartHoliday.setValue(datePickerStartEvent.getValue());
                DatePicker datePickerEndHoliday = new DatePicker();
                datePickerEndHoliday.setValue(datePickerStartHoliday.getValue());
                Button addHoliday = new Button("Add Holiday");

                datePickerStartHoliday.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if (datePickerStartHoliday.getValue().compareTo(datePickerStartEvent.getValue()) < 0){
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Warning alert");
                            alert.setHeaderText(null);
                            alert.setContentText("The holiday can't be earlier than the event!");
                            alert.showAndWait();
                            datePickerStartHoliday.setValue(datePickerStartEvent.getValue());
                        }
                    }
                });
                datePickerEndHoliday.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if (datePickerEndHoliday.getValue().compareTo(datePickerStartHoliday.getValue()) < 0){
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Warning alert");
                            alert.setHeaderText(null);
                            alert.setContentText("The end date can't be earlier than the start date!");
                            alert.showAndWait();
                            datePickerEndHoliday.setValue(datePickerStartHoliday.getValue());
                        }
                    }
                });

                GridPane holidayPane = new GridPane();
                holidayPane.setPadding(new Insets(20));
                holidayPane.setHgap(25);
                holidayPane.setVgap(15);
                holidayPane.add(labelHolidayStartDate, 0, 0);
                holidayPane.add(datePickerStartHoliday, 1, 0);
                holidayPane.add(labelHolidayEndDate, 0, 1);
                holidayPane.add(datePickerEndHoliday, 1, 1);
                holidayPane.add(addHoliday, 0, 2);

                Scene secondScene = new Scene(holidayPane, 450, 160);

                // New window (Stage)
                Stage windowHolidayDate = new Stage();
                windowHolidayDate.setTitle("Set Holiday Date");
                windowHolidayDate.setScene(secondScene);

                // Specifies the modality for new window.
                windowHolidayDate.initModality(Modality.WINDOW_MODAL);

                // Specifies the owner Window (parent) for new window
                windowHolidayDate.initOwner(stage);

                // Set position of second window, related to primary window.
                windowHolidayDate.setX(stage.getX() + 200);
                windowHolidayDate.setY(stage.getY() + 100);

                windowHolidayDate.show();

                addHoliday.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        LocalDate sHoliday = datePickerStartHoliday.getValue();
                        LocalDate eHoliday = datePickerEndHoliday.getValue();
                        holidayList.add(new Holiday(sHoliday, eHoliday));
                        windowHolidayDate.close();
                    }
                });
            }
        });


        Button buttonCalculate = new Button("Calculate");
        buttonCalculate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                Label labelReportStartDate = new Label("Start: ");
                Label labelReportStartDateValue = new Label(datePickerStartEvent.getValue().format(formatter));
                Label labelReportDuration = new Label("Duration:");
                Label labelReportDurationValue = new Label(spinnerDuration.getValue().toString());

                Button buttonClose = new Button("Close");

                //Function for calculate end date
                endDate = datePickerStartEvent.getValue();
                int sum = 0;
                for (Holiday element : holidayList) {
                    sum += element.getHolidayDuration();
                }
                sum += spinnerDuration.getValue();
                endDate = endDate.plusDays(sum - 1);
                //end of Function for calculate the end date

                Label labelReportEndDate = new Label("End: ");
                Label labelReportEndDateValue = new Label(endDate.format(formatter));

                GridPane reportPane = new GridPane();
                reportPane.setPadding(new Insets(20));
                reportPane.setHgap(25);
                reportPane.setVgap(15);
                reportPane.add(labelReportStartDate, 0, 0);
                reportPane.add(labelReportStartDateValue, 1, 0);
                reportPane.add(labelReportDuration, 0, 1);
                reportPane.add(labelReportDurationValue, 1, 1);
                reportPane.add(labelReportEndDate, 0, 2);
                reportPane.add(labelReportEndDateValue, 1, 2);
                reportPane.add(buttonClose, 0, 3);


                Scene reportScene = new Scene(reportPane, 450, 160);
                Stage windowReport = new Stage();
                windowReport.setTitle("End of Event");
                windowReport.setScene(reportScene);
                windowReport.setX(stage.getX() + 200);
                windowReport.setY(stage.getY() + 100);
                windowReport.show();

                buttonClose.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        //holidays.clear();
                        holidayList.clear();
                        windowReport.close();
                    }
                });
            }
        });

        //create Grid Panel
        GridPane root = new GridPane();
        root.setPadding(new Insets(20));
        root.setHgap(25);
        root.setVgap(15);
        root.add(labelStart, 0, 0);
        root.add(datePickerStartEvent, 1, 0);
        root.add(labelDuration, 0, 1 );
        root.add(spinnerDuration, 1, 1);
        root.add(labelAddHoliday, 0, 2);
        root.add(buttonAddHoliday,1, 2);
        root.add(buttonCalculate, 0, 3);

        //create window
        Scene scene = new Scene(root, 550, 200);
        stage.setTitle("Calculate the End Date of the Event");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        Application.launch(args);

    }
}