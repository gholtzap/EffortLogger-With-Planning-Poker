package com.cse360;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.io.FileOutputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EffortLogEditor extends Application {

    public String project;
    public String defect;
    public String defectName;
    public int defectNum;
    public boolean status;
    String name;

    public EffortLogEditor(String name) {
        this.name = name;
    }


    public void start(Stage primaryStage) {

        primaryStage.setTitle("EffortLoggerV2");

        // Sets the grid pane
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER); //Top_left
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Sets the title of the scene
        Text consoleTitle = new Text("Effort Log Editor");
        consoleTitle.setFont(Font.font("Arial", 20));
        grid.add(consoleTitle, 1, 0, 2, 1);

        Button Button0 = createButton("Clear This Effort Log");
        Button Button1 = createButton("Update This Entry");
        Button Button2 = createButton("Delete This Entry");
        Button Button3 = createButton("Split This Entry into Two Entries");
        Button Button4 = createButton("Proceed to the Effort Log Console");

        Text projectText = new Text("1. Select the Project in order to edit its Effort Log.");
        projectText.setFont(Font.font("Ariel", 15));
        grid.add(projectText, 0, 5, 2, 1);

        Text clearDefectText = new Text("2.a. Clear this Project's Effort Log.");
        clearDefectText.setFont(Font.font("Ariel", 15));
        grid.add(clearDefectText, 5, 5, 2, 1);

        Text currentDefect = new Text("2.b. Select Effort Log Entry to be modified and make it the Current Effort Log Entry");
        currentDefect.setFont(Font.font("Ariel", 15));
        grid.add(currentDefect, 0, 10, 2, 1);

        Text attributes = new Text("3.a. Modify the Current Effort Log Entry's attributes and press \"Update This Entry\" when finished.");
        attributes.setFont(Font.font("Ariel", 15));
        grid.add(attributes, 0, 18, 2, 1);

        Text delete = new Text("3.b. Delete the Current Effort Log Entry.");
        delete.setFont(Font.font("Ariel", 15));
        grid.add(delete, 0, 30, 2, 1);

        Text split = new Text("3.c. Split the Current Effort Log Entry into two entries.");
        split.setFont(Font.font("Ariel", 15));
        grid.add(split, 0, 40, 2, 1);

        Text update = new Text("4. Proceed to the Effort Log Console.");
        update.setFont(Font.font("Ariel", 15));
        grid.add(update, 5, 40, 2, 1);

        Text lifeCycleText = new Text("Life Cycle Step: ");
        lifeCycleText.setFont(Font.font("Ariel", 15));
        grid.add(lifeCycleText, 0, 19, 2, 1);

        Text effortCategoryText = new Text("Effort Category: ");
        effortCategoryText.setFont(Font.font("Ariel", 15));
        grid.add(effortCategoryText, 5, 19, 2, 1);

        Text Plans = new Text("Specifics: ");
        Plans.setFont(Font.font("Ariel", 15));
        grid.add(Plans, 5, 24, 2, 1);

        Text timeElaspedText = new Text("Elapsed Time: ");
        timeElaspedText.setFont(Font.font("Ariel", 15));
        grid.add(timeElaspedText, 0, 23, 2, 1);

        final ComboBox<String> projectComboBox = new ComboBox<String>();
        projectComboBox.getItems().addAll("Business Project", "Development Project");

        final ComboBox<String> effortLogComboBox = new ComboBox<String>();
        effortLogComboBox.getItems().addAll("--no effort log selected--");

        final ComboBox<String> lifeCycleBox = new ComboBox<String>();
        lifeCycleBox.getItems().addAll("Planning", "Information Gathering", "Information Understanding", "Verifying", "Outlining", "Drafting", "Finalizing", "Team Meeting", "Coach Meeting", "Stakeholder Meeting");

        final ComboBox<String> categoryBox= new ComboBox<String>();
        categoryBox.getItems().addAll("Plans", "Deliverables", "Conceptual Design", "Interruptions", "Defects", "Others");

        // Combo Box for Plans
        final ComboBox<String> projectPlanComboBox = new ComboBox<String>();
        projectPlanComboBox.getItems().addAll("Project Plan", "Risk Management Plan", "Conceptual Design Plan", "Detailed Design Plan", "Implementation Plan");

        // Combo Box for Deliverables
        final ComboBox<String> projectDeliverablesComboBox = new ComboBox<String>();
        projectDeliverablesComboBox.getItems().addAll("Conceptual Design", "Detailed Design", "Test Cases", "Solution", "Reflection", "Outline", "Draft", "Report", "User Defined", "Other");

        // Combo Box for Interruptions
        final ComboBox<String> projectInterruptionsComboBox = new ComboBox<String>();
        projectInterruptionsComboBox.getItems().addAll("Break", "Phone", "Teammate", "Visitor", "Other");

        // Combo Box for Defects
        final ComboBox<String> projectDefectsComboBox = new ComboBox<String>();
        projectDefectsComboBox.getItems().addAll("- no defect selected -");

        // Text Field for Details
        Text detailsTitle = new Text("Details:");
        detailsTitle.setFont(Font.font("Ariel", 15));
        grid.add(detailsTitle, 0, 28, 2, 1);
        
        // Text Field for the details
        TextField details = new TextField();
        grid.add(details, 0, 30, 2, 1);

        TextField timeElapsed = new TextField();

        grid.add(projectComboBox, 0, 8, 2, 1);
        grid.add(Button0, 5, 8, 2, 1);

        grid.add(effortLogComboBox, 0, 13, 2, 1);
        grid.add(Button1, 5, 35, 2, 1);

        grid.add(timeElapsed, 0, 25, 2, 1);
        grid.add(Button2, 0, 35, 2, 1);
        grid.add(Button3, 0, 45, 2, 1);

        grid.add(lifeCycleBox, 0, 20, 2, 1);
        grid.add(categoryBox, 5, 20, 2, 1);
        grid.add(projectPlanComboBox, 5, 25, 2, 1);
        grid.add(projectDeliverablesComboBox, 5, 25, 2, 1);
        grid.add(projectInterruptionsComboBox, 5, 25, 2, 1);
        grid.add(projectDefectsComboBox, 5, 25, 2, 1);

        grid.add(Button4, 5, 45, 2, 1);

        projectComboBox.valueProperty().addListener((observable, oldValue, newValue) -> { 

            if (newValue != null) {
                // Sets defect combo box to include all existing defects
                try {
                    FileInputStream inputStream = new FileInputStream("EffortLogs.xlsx");
                    Workbook workbook = new XSSFWorkbook(inputStream);
                    Sheet sheet = workbook.getSheet("EffortLogs");

                    effortLogComboBox.getItems().removeAll(effortLogComboBox.getItems());
                    effortLogComboBox.getItems().add("--no defect selected--");
                    effortLogComboBox.getSelectionModel().select("--no defect selected--");
                    details.setText("");
                    timeElapsed.setText("");
                    lifeCycleBox.setValue("");
                    categoryBox.setValue("");
                    projectPlanComboBox.setValue("");

                    // Iterate through the rows to find and display all defect log names
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Start from 1 to skip headers
                        Row row = sheet.getRow(i);
                        if (row.getCell(0).toString().equals(newValue)) {
                            //effortLogComboBox.getItems().add(row.getCell(0).toString());
                            effortLogComboBox.getItems().add(row.getCell(0).toString() + ", " + row.getCell(1).toString() + ", " + row.getCell(2).toString());
                        }
                    }

                    inputStream.close();

                    // Write the modified workbook content back to the file
                    FileOutputStream outputStream = new FileOutputStream("DefectLogs.xlsx");
                    workbook.write(outputStream);
                    workbook.close();
                    outputStream.close();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Adds a listener to the effort log combo box which gives the user all of the current information
        effortLogComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    FileInputStream inputStream = new FileInputStream("EffortLogs.xlsx");
                    Workbook workbook = new XSSFWorkbook(inputStream);
                    Sheet sheet = workbook.getSheet("EffortLogs");

                    // Iterate through the rows to find and display all effort log names
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Start from 1 to skip headers
                        Row row = sheet.getRow(i);
                        
                        // Updates the data once the name matches the desired effort log
                        if ((row.getCell(0).toString() + ", " + row.getCell(1).toString() + ", " + row.getCell(2).toString()).equals(newValue)) {
                            timeElapsed.setText(row.getCell(1).toString());
                            lifeCycleBox.setValue(row.getCell(2).toString());
                            categoryBox.setValue(row.getCell(3).toString());
                            projectPlanComboBox.setValue(row.getCell(4).toString());
                        }
                    }

                    inputStream.close();

                    // Write the modified workbook content back to the file
                    FileOutputStream outputStream = new FileOutputStream("EffortLogs.xlsx");
                    workbook.write(outputStream);
                    workbook.close();
                    outputStream.close();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            else {
                newValue = oldValue;
            }
        });

        categoryBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {

                // Removes all of the cobo and text boxes.
                grid.getChildren().remove(projectPlanComboBox);
                grid.getChildren().remove(projectDeliverablesComboBox);
                grid.getChildren().remove(projectInterruptionsComboBox);
                grid.getChildren().remove(projectDefectsComboBox);

                if (newValue.equals("Plans")) {
                    // Enable only the Plans combo box
                    grid.getChildren().add(projectPlanComboBox);

                } else if (newValue.equals("Deliverables")) {
                    // Enable only the Deliverables combo box
                    grid.getChildren().add(projectDeliverablesComboBox);

                } else if (newValue.equals("Interruptions")) {
                    // Enable only the Interruptions combo box
                    grid.getChildren().add(projectInterruptionsComboBox);

                } else if (newValue.equals("Defects")) {
                    // Enable only the Defects combo box
                    grid.getChildren().add(projectDefectsComboBox);

                } else if (newValue.equals("Others")) {
                    // Enable only the Others text box
                    grid.getChildren().add(detailsTitle);
                    grid.getChildren().add(details);
                }
            }
            else {
                newValue = oldValue;
            }
        });

        // Clear This Effort Log
        Button0.setOnAction(new EventHandler<ActionEvent>() {

            //@Override
            public void handle(ActionEvent e) {
                try {
                    String effortLogToDelete = projectComboBox.getValue() != null ? projectComboBox.getValue().toString() : "";
        
                    FileInputStream inputStream = new FileInputStream("EffortLogs.xlsx");
                    Workbook workbook = new XSSFWorkbook(inputStream);
                    Sheet sheet = workbook.getSheet("EffortLogs");

                    System.out.println("The number of rows is: " + sheet.getLastRowNum());

                    // Iterate through the rows to find and delete the row with the selected effort name
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Start from 1 to skip headers
                        Row row = sheet.getRow(i);
                        if (row.getCell(0).getStringCellValue().equals(effortLogToDelete)) {
                            sheet.removeRow(row);
                            for (int j = i+1; j <= sheet.getLastRowNum(); j++) {
                                // Shift rows up to remove the gap created by deleting the row
                                sheet.shiftRows(j, sheet.getLastRowNum(), -1);
                            }
                            i--;
                        }
                    }
        
                    inputStream.close();
        
                    // Write the modified workbook content back to the file
                    FileOutputStream outputStream = new FileOutputStream("EffortLogs.xlsx");
                    workbook.write(outputStream);
                    workbook.close();
                    outputStream.close();
        
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Update This Entry
        Button1.setOnAction(new EventHandler<ActionEvent>() {

            //@Override
            public void handle(ActionEvent e) {

                try {

                    Workbook workbook = null;
                    Sheet sheet = null;
                    File file = new File("EffortLogs.xlsx");

                    FileInputStream inputStream = new FileInputStream(file);
                    workbook = new XSSFWorkbook(inputStream);
                    sheet = workbook.getSheet("EffortLogs");
                    inputStream.close();

                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        Row row = sheet.getRow(i);
                        
                        if ((row.getCell(0).toString() + ", " + row.getCell(1).toString() + ", " + row.getCell(2).toString()).equals(effortLogComboBox.getValue())) {
                            row.createCell(0).setCellValue(projectComboBox.getValue());
                            row.createCell(1).setCellValue(timeElapsed.getText());
                            row.createCell(2).setCellValue(lifeCycleBox.getValue());
                            row.createCell(3).setCellValue(categoryBox.getValue());
                            row.createCell(4).setCellValue(projectPlanComboBox.getValue());
                        }
                    }

                    // Write the workbook content to a file
                    FileOutputStream outputStream = new FileOutputStream("EffortLogs.xlsx");
                    workbook.write(outputStream);
                    workbook.close();
                    outputStream.close();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Delete This Entry
        Button2.setOnAction(new EventHandler<ActionEvent>() {

            //@Override
            public void handle(ActionEvent e) {
                try {
                    String effortLogToDelete = effortLogComboBox.getValue() != null ? effortLogComboBox.getValue().toString() : "";
        
                    FileInputStream inputStream = new FileInputStream("EffortLogs.xlsx");
                    Workbook workbook = new XSSFWorkbook(inputStream);
                    Sheet sheet = workbook.getSheet("EffortLogs");

                    // Iterate through the rows to find and delete the row with the selected effort name
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Start from 1 to skip headers
                        Row row = sheet.getRow(i);
                        if ((row.getCell(0).toString() + ", " + row.getCell(1).toString() + ", " + row.getCell(2).toString()).equals(effortLogToDelete)) {
                            sheet.removeRow(row);
                            for (int j = i+1; j <= sheet.getLastRowNum(); j++) {
                                // Shift rows up to remove the gap created by deleting the row
                                sheet.shiftRows(j, sheet.getLastRowNum(), -1);
                            }
                            break; // Exit loop once the row is deleted
                        }
                    }
        
                    inputStream.close();
        
                    // Write the modified workbook content back to the file
                    FileOutputStream outputStream = new FileOutputStream("EffortLogs.xlsx");
                    workbook.write(outputStream);
                    workbook.close();
                    outputStream.close();
        
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Split This Entry into Two Entries
        Button3.setOnAction(new EventHandler<ActionEvent>() {

            //@Override
            public void handle(ActionEvent e) {

                try {

                    FileInputStream inputStream = new FileInputStream("EffortLogs.xlsx");
                    Workbook workbook = new XSSFWorkbook(inputStream);
                    Sheet sheet = workbook.getSheet("EffortLogs");

                    int rowNum = sheet.getLastRowNum() + 1; // Find the next available row after existing data
                    Row row = sheet.createRow(rowNum); // Create the new row at the next available row

                    // Set cell values in the row
                    row.createCell(0).setCellValue(projectComboBox.getValue());
                    row.createCell(1).setCellValue(timeElapsed.getText());
                    row.createCell(2).setCellValue(lifeCycleBox.getValue());
                    row.createCell(3).setCellValue(categoryBox.getValue());
                    row.createCell(4).setCellValue(projectPlanComboBox.getValue());

                    inputStream.close();
        
                    // Write the modified workbook content back to the file
                    FileOutputStream outputStream = new FileOutputStream("EffortLogs.xlsx");
                    workbook.write(outputStream);
                    workbook.close();
                    outputStream.close();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Proceed to the Effort Log Console
        Button4.setOnAction(new EventHandler<ActionEvent>() {

            //@Override
            public void handle(ActionEvent e) {

                // Sends the user to the EffortLogger console
                EffortLoggerV2Console console = new EffortLoggerV2Console(name);
                console.start(primaryStage);
            }
        });

        // Setting the scene
        primaryStage.setScene(new Scene(grid, 900, 900));
        primaryStage.setFullScreen(true);
        grid.setStyle("-fx-background-color: linear-gradient(to bottom, #f0f0f0, #d6e0f0);");
        primaryStage.show();
    }

    private static Button createButton(String text) {
        Button button = new Button(text);
        button.setMinSize(200, 40);
        button.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; -fx-font-size: 16px;");
        return button;
    }

    public void main(String[] args) {
        launch(args);
    }
}
