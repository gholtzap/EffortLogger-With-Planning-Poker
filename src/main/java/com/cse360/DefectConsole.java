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

public class DefectConsole extends Application{
    
    public String project;
    public String defect;
    public String defectName;
    public int defectNum;
    public boolean status;
    public String injected;
    public String removed;
    public String category;

    String parent_name;

    public DefectConsole(String name){
        parent_name = name;
    }

    public void start(Stage primaryStage) {

        primaryStage.setTitle("EffortLoggerV2");

        // Sets the grid pane
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER); //TOP_LEFT
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Sets the title of the scene
        Text consoleTitle = new Text("Defect Console");
        consoleTitle.setFont(Font.font("Arial", 32));
        grid.add(consoleTitle, 3, 0, 2, 1);
        
        Button Button0 = createButton("Clear this Defect Log");
        Button Button1 = createButton("Create a New Defect");
        Button Button2 = createButton("Close");
        Button Button3 = createButton("Reopen");
        Button Button4 = createButton("Update the Current Defect");
        Button Button5 = createButton("Delete the Current Defect");
        Button Button6 = createButton("Proceed to the Effort Log Console");

        Text projectText = new Text("1. Select the Project");
        projectText.setFont(Font.font("Ariel", 15));
        grid.add(projectText, 0, 3, 2, 1);

        Text clearDefectText = new Text("2.a. Clear this Project's Defect Log.");
        clearDefectText.setFont(Font.font("Ariel", 15));
        grid.add(clearDefectText, 10, 3, 2, 1);

        Text currentDefect = new Text("2.b. Select one of the following defects to make it the Current Defect.");
        currentDefect.setFont(Font.font("Ariel", 15));
        grid.add(currentDefect, 0, 10, 2, 1);

        Text newDefect = new Text("2.c. Press to \"Create a New Defect\".");
        newDefect.setFont(Font.font("Ariel", 15));
        grid.add(newDefect, 10, 10, 2, 1);

        Text attributes = new Text("3. Defect or update the following Current Defect attribute:");
        attributes.setFont(Font.font("Ariel", 15));
        grid.add(attributes, 0, 18, 2, 1);

        Text update = new Text("4. Press the \"Update the Current Defect\" to save the changes made above.");
        update.setFont(Font.font("Ariel", 15));
        grid.add(update, 0, 40, 2, 1);

        Text injectionText = new Text("Step when injected");
        injectionText.setFont(Font.font("Ariel", 15));
        grid.add(injectionText, 0, 33, 2, 1);

        Text removedText = new Text("Step when removed");
        removedText.setFont(Font.font("Ariel", 15));
        grid.add(removedText, 5, 33, 2, 1);

        Text categoryText = new Text("Defect Category");
        categoryText.setFont(Font.font("Ariel", 15));
        grid.add(categoryText, 10, 33, 2, 1);

        Text statusText = new Text("Status: ");
        statusText.setFont(Font.font("Ariel", 15));
        grid.add(statusText, 5, 19, 2, 1);

        Text currentStatusText = new Text("Closed");
        currentStatusText.setFont(Font.font("Ariel", 15));
        grid.add(currentStatusText, 10, 19, 2, 1);

        Text nameText = new Text("Defect Name");
        nameText.setFont(Font.font("Ariel", 15));
        grid.add(nameText, 0, 19, 2, 1);

        Text detailsText = new Text("Defect Details");
        detailsText.setFont(Font.font("Ariel", 15));
        grid.add(detailsText, 0, 24, 2, 1);

        final ComboBox<String> projectComboBox = new ComboBox<String>();
        projectComboBox.getItems().addAll("Business Project", "Development Project");

        final ComboBox<String> defectComboBox = new ComboBox<String>();
        defectComboBox.getItems().addAll("--no defect selected--");

        final ComboBox<String> injected = new ComboBox<String>();
        injected.getItems().addAll("Problem Understanding", "Conceptual Design Plan", "Requirements", "Conceptual Design", "Conceptual Design Review", "Detailed Design Review", "Implementation Plan", "Test Case Generation", "Solution Specification", "Solution Review", "Solution Implementation", "Unit/System Update", "Repository Update", "");

        final ComboBox<String> removed = new ComboBox<String>();
        removed.getItems().addAll("Problem Understanding", "Conceptual Design Plan", "Requirements", "Conceptual Design", "Conceptual Design Review", "Detailed Design Review", "Implementation Plan", "Test Case Generation", "Solution Specification", "Solution Review", "Solution Implementation", "Unit/System Update", "Repository Update", "");

        final ComboBox<String> category = new ComboBox<String>();
        category.getItems().addAll("Not Specified", "10 Documentation", "20 Syntax", "30 Build, Package", "40 Assignment", "50 Interface", "60 Checking", "70 Data", "80 Function", "90 System", "100 Environment", "");

        TextField name = new TextField();
        TextField symptoms = new TextField();

        grid.add(projectComboBox, 0, 5, 2, 1);
        grid.add(Button0, 10, 5, 2, 1);

        grid.add(defectComboBox, 0, 13, 2, 1);
        grid.add(Button1, 10, 13, 2, 1);

        grid.add(name, 0, 20, 2, 1);
        grid.add(Button2, 5, 20, 2, 1);
        grid.add(Button3, 10, 20, 2, 1);
        grid.add(symptoms, 0, 25, 2, 1);

        grid.add(injected, 0, 35, 2, 1);
        grid.add(removed, 5, 35, 2, 1);
        grid.add(category, 10, 35, 2, 1);

        grid.add(Button4, 0, 45, 2, 1);
        grid.add(Button5, 5, 45, 2, 1);
        grid.add(Button6, 10, 45, 2, 1);
        
        // Sets initial values
        projectComboBox.getSelectionModel().select("Development Project");
        defectComboBox.getSelectionModel().select("--no defect selected--");

        // Adds a listener to the project combo box which gives the user all of the current information
        projectComboBox.valueProperty().addListener((observable, oldValue, newValue) -> { 

            if (newValue != null) {
                // Sets defect combo box to include all existing defects
                try {
                    FileInputStream inputStream = new FileInputStream("DefectLogs.xlsx");
                    Workbook workbook = new XSSFWorkbook(inputStream);
                    Sheet sheet = workbook.getSheet("DefectLogs");

                    defectComboBox.getItems().removeAll(defectComboBox.getItems());
                    defectComboBox.getItems().add("--no defect selected--");
                    defectComboBox.getSelectionModel().select("--no defect selected--");
                    name.setText("");
                    symptoms.setText("");
                    injected.setValue("");
                    removed.setValue("");
                    category.setValue("");

                    // Iterate through the rows to find and display all defect log names
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Start from 1 to skip headers
                        Row row = sheet.getRow(i);
                        if (row.getCell(0).toString().equals(newValue)) {
                            defectComboBox.getItems().add(row.getCell(1).toString());
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

        // Adds a listener to the defect combo box which gives the user all of the current information
        defectComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    FileInputStream inputStream = new FileInputStream("DefectLogs.xlsx");
                    Workbook workbook = new XSSFWorkbook(inputStream);
                    Sheet sheet = workbook.getSheet("DefectLogs");

                    // Iterate through the rows to find and display all defect log names
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Start from 1 to skip headers
                        Row row = sheet.getRow(i);
                        
                        // Updates the data once the name matches the desired defect log
                        if (row.getCell(1).toString().equals(newValue)) {
                            name.setText(row.getCell(1).toString());
                            symptoms.setText(row.getCell(2).toString());
                            injected.setValue(row.getCell(3).toString());
                            removed.setValue(row.getCell(4).toString());
                            category.setValue(row.getCell(5).toString());
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
            else {
                newValue = oldValue;
            }
        });

        // Clear Defect Logs
        Button0.setOnAction(new EventHandler<ActionEvent>() {

            //@Override
            public void handle(ActionEvent e) {
                try {
                    String defectsToDelete = projectComboBox.getValue() != null ? projectComboBox.getValue().toString() : "";
        
                    FileInputStream inputStream = new FileInputStream("DefectLogs.xlsx");
                    Workbook workbook = new XSSFWorkbook(inputStream);
                    Sheet sheet = workbook.getSheet("DefectLogs");

                    System.out.println("The number of rows is: " + sheet.getLastRowNum());

                    // Iterate through the rows to find and delete the row with the selected defect name
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Start from 1 to skip headers
                        Row row = sheet.getRow(i);
                        if (row.getCell(0).getStringCellValue().equals(defectsToDelete)) {
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
                    FileOutputStream outputStream = new FileOutputStream("DefectLogs.xlsx");
                    workbook.write(outputStream);
                    workbook.close();
                    outputStream.close();
        
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Create New Defect Log
        Button1.setOnAction(new EventHandler<ActionEvent>() {

            //@Override
            public void handle(ActionEvent e) {

                defectComboBox.getItems().add("--New Defect Log--");
                defectComboBox.setValue("--New Defect Log--");
            }
        });

        // Close Log
        Button2.setOnAction(new EventHandler<ActionEvent>() {

            //@Override
            public void handle(ActionEvent e) {
                currentStatusText.setText("Closed");
            }
        });

        // Reopen Log
        Button3.setOnAction(new EventHandler<ActionEvent>() {

            //@Override
            public void handle(ActionEvent e) {
                currentStatusText.setText("Open");
            }
        });

        // Update Defect Log
        Button4.setOnAction(new EventHandler<ActionEvent>() {

            //@Override
            public void handle(ActionEvent e) {
                
                String projectValue = projectComboBox.getValue() != null ? projectComboBox.getValue().toString() : "";
                String defectName = name.getText() != null ? name.getText().toString() : "";
                String DefectDetails = symptoms.getText() != null ? symptoms.getText().toString() : "";
                String injectedValue = injected.getValue() != null ? injected.getValue().toString() : "";
                String removedValue = removed.getValue() != null ? removed.getValue().toString() : "";
                String categoryValue = category.getValue() != null ? category.getValue().toString() : "";
                
                try {

                    Workbook workbook = null;
                    Sheet sheet = null;
                    File file = new File("DefectLogs.xlsx");

                    if (!file.exists()) {
                        workbook = new XSSFWorkbook();
                        sheet = workbook.createSheet("DefectLogs");

                        // Add headers in the first row
                        Row headerRow = sheet.createRow(0);
                        String[] headers = {"Project", "Defect Name", "Defect Information", "Step when Injected", "Step when Removed", "Defect Category"};
                        for (int i = 0; i < headers.length; i++) {
                            Cell cell = headerRow.createCell(i);
                            cell.setCellValue(headers[i]);
                        }
                    } else {
                        FileInputStream inputStream = new FileInputStream(file);
                        workbook = new XSSFWorkbook(inputStream);
                        sheet = workbook.getSheet("DefectLogs");
                        inputStream.close();
                    }

                    boolean exists = false;

                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        Row row = sheet.getRow(i);
                        
                        if (row.getCell(1).getStringCellValue().equals(defectComboBox.getValue())) {
                            row.createCell(0).setCellValue(projectValue);
                            row.createCell(1).setCellValue(defectName);
                            row.createCell(2).setCellValue(DefectDetails);
                            row.createCell(3).setCellValue(injectedValue);
                            row.createCell(4).setCellValue(removedValue);
                            row.createCell(5).setCellValue(categoryValue);
                            exists = true;
                        }
                    }

                    if (exists == false) {
                        int rowNum = sheet.getLastRowNum() + 1; // Find the next available row after existing data
                        Row row = sheet.createRow(rowNum); // Create the new row at the next available row

                        // Set cell values in the row
                        row.createCell(0).setCellValue(projectValue);
                        row.createCell(1).setCellValue(defectName);
                        row.createCell(2).setCellValue(DefectDetails);
                        row.createCell(3).setCellValue(injectedValue);
                        row.createCell(4).setCellValue(removedValue);
                        row.createCell(5).setCellValue(categoryValue);
                    }

                    // Write the workbook content to a file
                    FileOutputStream outputStream = new FileOutputStream("DefectLogs.xlsx");
                    workbook.write(outputStream);
                    workbook.close();
                    outputStream.close();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Delete Current Defect Log  
        Button5.setOnAction(new EventHandler<ActionEvent>() {

            //@Override
            public void handle(ActionEvent e) {
                try {
                    String defectToDelete = name.getText() != null ? name.getText().toString() : "";
        
                    FileInputStream inputStream = new FileInputStream("DefectLogs.xlsx");
                    Workbook workbook = new XSSFWorkbook(inputStream);
                    Sheet sheet = workbook.getSheet("DefectLogs");

                    System.out.println("The number of rows is: " + sheet.getLastRowNum());

                    // Iterate through the rows to find and delete the row with the selected defect name
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Start from 1 to skip headers
                        Row row = sheet.getRow(i);
                        if (row.getCell(1).getStringCellValue().equals(defectToDelete)) {
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
                    FileOutputStream outputStream = new FileOutputStream("DefectLogs.xlsx");
                    workbook.write(outputStream);
                    workbook.close();
                    outputStream.close();
        
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Main Console
        Button6.setOnAction(new EventHandler<ActionEvent>() {

            //@Override
            public void handle(ActionEvent e) {

                // Sends the user to the EffortLogger console
                EffortLoggerV2Console console = new EffortLoggerV2Console(parent_name);
                console.start(primaryStage);
            }
        });

        // Setting the scene
        primaryStage.setScene(new Scene(grid, 900, 900));
        primaryStage.setFullScreen(true);
        grid.setStyle("-fx-background-color: linear-gradient(to bottom, #f0f0f0, #d6e0f0);");
        primaryStage.show();
    }


    // Function for creating beutiful buttons
    private static Button createButton(String text) {
        Button button = new Button(text);
        button.setMinSize(200, 40);
        button.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white; -fx-font-size: 16px;");
        return button;
    }

    public static void main(String[] args) {
        launch(args);
    }

};
