/*
 * Class: CSE 360
 * Author: Caiden James McGregor
 * Group: Th13
 * Description:
 *      This file contains the code for the main EffortLoggerV2 console. This console will
 *  allow users to generate effort logs, as well as traverse to consoles such as the defect console,
 *  definitions console, effort and defect logs console, definitions console, and the new Planning Poker application.
 */

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
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class EffortLoggerV2Console extends Application {
    
    public static String formattedTime = "";
    private static long startTime = 0;
    public static long elapsedTime = 0;
    private static boolean running = false;
    String username;

    public EffortLoggerV2Console(String username){
        this.username = username;
    }

    public void start(Stage primaryStage) {

        // Sets the title
        primaryStage.setTitle("EffortLoggerV2");

        // Sets the grid pane
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Sets the title of the scene
        Text consoleTitle = new Text("EffortLoggerV2 Console");
        consoleTitle.setFont(Font.font("Arial", 20));
        grid.add(consoleTitle, 0, 0, 2, 1);

        // Creates the buttons to travers EffortLoggerV2
        Button Button0 = createButton("Start an Activity");
        Button Button1 = createButton("Effort Log Editor");
        Button Button2 = createButton("Defect Log Console");
        Button Button3 = createButton("Planning Poker");
        Button Button4 = createButton("Effort and Defect Logs");
        Button Button5 = createButton("Stop this Activity");
        Button Button6 = createButton("Version Control");
        Button Button7 = createButton("AWS Backup");


        // Clock:
        //---------------------------------------
        // Backround of the clock
        Rectangle backround = new Rectangle(150, 50, 150, 50);
        backround.setFill(Color.rgb(255, 0, 0));
        backround.setStrokeWidth(3);
        backround.setStroke(Color.DARKRED);
        grid.add(backround, 0, 5, 2, 1);

        // Text for the clock
        Text clock = new Text(" Clock is stopped");
        clock.setFont(Font.font("Ariel", 20));
        grid.add(clock, 0, 5, 2, 1);
        //---------------------------------------

        // Text Instructions:
        //---------------------------------------
        // Text for the first step.
        Text one = new Text("1. When you start a new activity, press the \"Start an Activity\" button.");
        one.setFont(Font.font("Ariel", 15));
        grid.add(one, 0, 8, 2, 1);

        // Text for the second step.
        Text two = new Text("2. Select the project, life cycle step, effort catagory, and deliverable from the following lists:");
        two.setFont(Font.font("Ariel", 15));
        grid.add(two, 0, 15, 2, 1);

        // Text for the Project selection.
        Text projectTitle = new Text("Project:");
        projectTitle.setFont(Font.font("Ariel", 15));
        grid.add(projectTitle, 0, 18, 2, 1);

        // Text for the Life Cycle Step selection.
        Text lifeCycle = new Text("Life Cycle Step:");
        lifeCycle.setFont(Font.font("Ariel", 15));
        grid.add(lifeCycle, 2, 18, 2, 1);

        // Text for the Effort Catagory selection.
        Text effortCatagory = new Text("Effort Catagory:");
        effortCatagory.setFont(Font.font("Ariel", 15));
        grid.add(effortCatagory, 0, 25, 2, 1);

        // Text for the project Plan selection.
        Text projectPlan = new Text("Project Plan:");
        projectPlan.setFont(Font.font("Ariel", 15));
        grid.add(projectPlan, 2, 25, 2, 1);

        // Text for the deliverables selection.
        Text deliverables = new Text("Deliverables:");
        deliverables.setFont(Font.font("Ariel", 15));
        grid.add(deliverables, 2, 25, 2, 1);

        // Text for the interruptions selection.
        Text Interruptions = new Text("Interruptions:");
        Interruptions.setFont(Font.font("Ariel", 15));
        grid.add(Interruptions, 2, 25, 2, 1);

        // Text for the defects selection.
        Text Defects = new Text("Defects:");
        Defects.setFont(Font.font("Ariel", 15));
        grid.add(Defects, 2, 25, 2, 1);

        // Text for the third step.
        Text three = new Text("3. Press the \"Stop Activity\" to generate an effort log entry using the attributes above.");
        three.setFont(Font.font("Ariel", 15));
        grid.add(three, 0, 35, 2, 1);

        // Text for the fourth step.
        Text four = new Text("4. Unless you are done for the day, it is best to perform steps 1 and 2 above before resuming work.");
        four.setFont(Font.font("Ariel", 15));
        grid.add(four, 0, 45, 2, 1);
        //---------------------------------------

        // Combo Boxes:
        //---------------------------------------
        //  Combo Box for Project
        final ComboBox<String> projectComboBox = new ComboBox<String>();
        projectComboBox.getItems().addAll("Business Project", "Development Project");

        // Combo Box for Life Cycle Step
        final ComboBox<String> lifeCycleStepComboBox = new ComboBox<String>();
        lifeCycleStepComboBox.getItems().addAll("Planning", "Information Gathering", "Information Understanding", "Verifying", "Outlining", "Drafting", "Finalizing", "Team Meeting", "Coach Meeting", "Stakeholder Meeting");

        // Combo Box for Effort Catagory
        final ComboBox<String> effortCatagoryComboBox = new ComboBox<String>();
        effortCatagoryComboBox.getItems().addAll("Plans", "Deliverables", "Interruptions", "Defects", "Others");

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
        //---------------------------------------

        //Details Text Section
        //---------------------------------------
        // Text Field for Details
        Text detailsTitle = new Text("Details:");
        detailsTitle.setFont(Font.font("Ariel", 15));
        grid.add(detailsTitle, 0, 30, 2, 1);
        // Text Field for the details
        TextField details = new TextField();
        grid.add(details, 0, 32, 2, 1);
        //---------------------------------------

        // Adding the combo boxes
        grid.add(projectComboBox, 0, 20, 2, 1);
        grid.add(lifeCycleStepComboBox, 2, 20, 2, 1);
        grid.add(effortCatagoryComboBox, 0, 27, 2, 1);
        grid.add(projectPlanComboBox, 2, 27, 2, 1);
        grid.add(projectDeliverablesComboBox, 2, 27, 2, 1);
        grid.add(projectInterruptionsComboBox, 2, 27, 2, 1);
        grid.add(projectDefectsComboBox, 2, 27, 2, 1);

        // Adding the buttons to the scene.
        grid.add(Button0, 0, 10);   //Start an Activity
        grid.add(Button1, 0, 50);   //Effort Log Editor
        grid.add(Button2, 1, 50);   //Defect Log Console
        grid.add(Button3, 2, 50);   //Definitions
        grid.add(Button4, 3, 50);   //Effort and Defect Logs
        grid.add(Button5, 0, 40);   //Stop this Activity
        grid.add(Button6, 4, 50);   // Version Control
        grid.add(Button7, 5, 50);

        // Setting the scene
        primaryStage.setScene(new Scene(grid, 900, 900));
        primaryStage.setFullScreen(true);
        grid.setStyle("-fx-background-color: linear-gradient(to bottom, #f0f0f0, #d6e0f0);");
        primaryStage.show();
        // Combo Box Correlation:
        //---------------------------------------
        // Sets the Plans combo box as default  
        grid.getChildren().remove(deliverables);
        grid.getChildren().remove(Interruptions);
        grid.getChildren().remove(Defects);
        grid.getChildren().remove(projectDeliverablesComboBox);
        grid.getChildren().remove(projectInterruptionsComboBox);
        grid.getChildren().remove(projectDefectsComboBox);
        grid.getChildren().remove(detailsTitle);
        grid.getChildren().remove(details); 
        //---------------------------------------

        // Adds a listener to the Life Cycle Step combo box
        lifeCycleStepComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.equals("Planning")) {
                    // Sets Effort Catagory as Plans
                    effortCatagoryComboBox.getSelectionModel().select("Plans");
                    projectPlanComboBox.getSelectionModel().select("Project Plan");
                }
                else {
                    // Sets Effort Catagory as Deliverables
                    if (newValue.equals("Information Gathering")) {
                        effortCatagoryComboBox.getSelectionModel().select("Deliverables");
                        projectDeliverablesComboBox.getSelectionModel().select("Conceptual Design");
                    }
                    else if (newValue.equals("Information Understanding")) {
                        effortCatagoryComboBox.getSelectionModel().select("Deliverables");
                        projectDeliverablesComboBox.getSelectionModel().select("Conceptual Design");
                    }
                    else if (newValue.equals("Verifying")) {
                        effortCatagoryComboBox.getSelectionModel().select("Deliverables");
                        projectDeliverablesComboBox.getSelectionModel().select("Conceptual Design");
                    }
                    else if (newValue.equals("Outlining")) {
                        effortCatagoryComboBox.getSelectionModel().select("Deliverables");
                        projectDeliverablesComboBox.getSelectionModel().select("Outline");
                    }
                    else if (newValue.equals("Drafting")) {
                        effortCatagoryComboBox.getSelectionModel().select("Deliverables");
                        projectDeliverablesComboBox.getSelectionModel().select("Draft");
                    }
                    else if (newValue.equals("Finalizing")) {
                        effortCatagoryComboBox.getSelectionModel().select("Deliverables");
                        projectDeliverablesComboBox.getSelectionModel().select("Report");
                    }
                    else if (newValue.equals("Team Meeting")) {
                        effortCatagoryComboBox.getSelectionModel().select("Deliverables");
                        projectDeliverablesComboBox.getSelectionModel().select("Conceptual Design");
                    }
                    else if (newValue.equals("Coach Meeting")) {
                        effortCatagoryComboBox.getSelectionModel().select("Deliverables");
                        projectDeliverablesComboBox.getSelectionModel().select("Conceptual Design");
                    }
                    else if (newValue.equals("Stakeholder Meeting")) {
                        effortCatagoryComboBox.getSelectionModel().select("Deliverables");
                        projectDeliverablesComboBox.getSelectionModel().select("Conceptual Design");
                    }
                }
            }
        });

        // Adds a listener to the Effort Catagory Combo Box
        //---------------------------------------
        effortCatagoryComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {

                // Removes all of the text.
                grid.getChildren().remove(projectPlan);
                grid.getChildren().remove(deliverables);
                grid.getChildren().remove(Interruptions);
                grid.getChildren().remove(Defects);

                // Removes all of the cobo and text boxes.
                grid.getChildren().remove(projectPlanComboBox);
                grid.getChildren().remove(projectDeliverablesComboBox);
                grid.getChildren().remove(projectInterruptionsComboBox);
                grid.getChildren().remove(projectDefectsComboBox);
                grid.getChildren().remove(detailsTitle);
                grid.getChildren().remove(details);

                if (newValue.equals("Plans")) {
                    // Enable only the Plans combo box
                    grid.getChildren().add(projectPlan);
                    grid.getChildren().add(projectPlanComboBox);

                } else if (newValue.equals("Deliverables")) {
                    // Enable only the Deliverables combo box
                    grid.getChildren().add(deliverables);
                    grid.getChildren().add(projectDeliverablesComboBox);

                } else if (newValue.equals("Interruptions")) {
                    // Enable only the Interruptions combo box
                    grid.getChildren().add(Interruptions);
                    grid.getChildren().add(projectInterruptionsComboBox);

                } else if (newValue.equals("Defects")) {
                    // Enable only the Defects combo box
                    grid.getChildren().add(Defects);
                    grid.getChildren().add(projectDefectsComboBox);

                } else if (newValue.equals("Others")) {
                    // Enable only the Others text box
                    grid.getChildren().add(detailsTitle);
                    grid.getChildren().add(details);

                    try {
                        FileInputStream inputStream = new FileInputStream("DefectLogs.xlsx");
                        Workbook workbook = new XSSFWorkbook(inputStream);
                        Sheet sheet = workbook.getSheet("DefectLogs");

                        projectDefectsComboBox.getItems().removeAll(projectDefectsComboBox.getItems());
                        projectDefectsComboBox.getItems().add("--no defect selected--");
                        projectDefectsComboBox.getSelectionModel().select("--no defect selected--");

                        // Iterate through the rows to find and display all defect log names
                        for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Start from 1 to skip headers
                            Row row = sheet.getRow(i);
                            if (row.getCell(0).toString().equals(newValue)) {
                                projectDefectsComboBox.getItems().add(row.getCell(1).toString());
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
            }
        });
        //---------------------------------------
        
        // Starts the Clock
        Button0.setOnAction(new EventHandler<ActionEvent>() {

            //@Override
            public void handle(ActionEvent e) {
                        
                clock.setText(" Clock is running");

                backround.setFill(Color.rgb(0, 255, 0));
                backround.setStrokeWidth(3);
                backround.setStroke(Color.DARKGREEN);
                
                startTime = System.currentTimeMillis();
                running = true;
                updateTimer();
            }
        });

        // Sends the user to the Effort Log Editor
        Button1.setOnAction(new EventHandler<ActionEvent>() {

            //@Override
            public void handle(ActionEvent e) {
                EffortLogEditor console = new EffortLogEditor(username);
                console.start(primaryStage);
            }
        });

        // Sends the user to the Defect Log Console
        Button2.setOnAction(new EventHandler<ActionEvent>() {

            //@Override
            public void handle(ActionEvent e) {

                DefectConsole console = new DefectConsole(username);
                console.start(primaryStage);
            }
        });

        Button3.setOnAction(e -> {
            Stage newStage = new Stage();
            PlanningPoker pokerApp = new PlanningPoker(username);
            pokerApp.start(newStage);
        });

        // Sends the user to the Effort and Defect Logs
        Button4.setOnAction(new EventHandler<ActionEvent>() {

            //@Override
            public void handle(ActionEvent e) {
                
                EffortLogDisplay console = new EffortLogDisplay();
                console.start(primaryStage);
            }
        });
        
        // Ends the activity thereby stopping the clock and saving the effort log
        Button5.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent e) {

                clock.setText(" Clock is stopped");

                backround.setFill(Color.rgb(255, 0, 0));
                backround.setStrokeWidth(3);
                backround.setStroke(Color.DARKRED);

                String project = projectComboBox.getValue() != null ? projectComboBox.getValue().toString() : "";
                String lifeCycleStep = lifeCycleStepComboBox.getValue() != null ? lifeCycleStepComboBox.getValue().toString() : "";
                String effortCategory = effortCatagoryComboBox.getValue() != null ? effortCatagoryComboBox.getValue().toString() : "";

                try {

                    Workbook workbook = null;
                    Sheet sheet = null;
                    File file = new File("EffortLogs.xlsx");

                    if (!file.exists()) {
                        workbook = new XSSFWorkbook();
                        sheet = workbook.createSheet("EffortLogs");

                        // Add headers in the first row
                        Row headerRow = sheet.createRow(0);
                        String[] headers = {"Project", "Formatted Time", "Life Cycle Step", "Effort Category", "Specifics"};
                        for (int i = 0; i < headers.length; i++) {
                            Cell cell = headerRow.createCell(i);
                            cell.setCellValue(headers[i]);
                        }
                    } else {
                        FileInputStream inputStream = new FileInputStream(file);
                        workbook = new XSSFWorkbook(inputStream);
                        sheet = workbook.getSheet("EffortLogs");
                        inputStream.close();
                    }

                    int rowNum = sheet.getLastRowNum() + 1; // Find the next available row after existing data
                    Row row = sheet.createRow(rowNum); // Create the new row at the next available row

                    // Set cell values in the row
                    row.createCell(0).setCellValue(project);
                    row.createCell(1).setCellValue(formattedTime);
                    row.createCell(2).setCellValue(lifeCycleStep);
                    row.createCell(3).setCellValue(effortCategory);
                    if (effortCatagoryComboBox.getValue().equals("Plans")) {
                        // Enable only the Plans combo box
                        String plan = projectPlanComboBox.getValue() != null ? projectPlanComboBox.getValue().toString() : "";
                        row.createCell(4).setCellValue(plan);
                    } 
                    else if (effortCatagoryComboBox.getValue().equals("Deliverables")) {
                        // Enable only the Deliverables combo box
                        String deliverables = projectDeliverablesComboBox.getValue() != null ? projectDeliverablesComboBox.getValue().toString() : "";
                        row.createCell(4).setCellValue(deliverables);

                    } 
                    else if (effortCatagoryComboBox.getValue().equals("Interruptions")) {
                        // Enable only the Interruptions combo box
                        String Interruptions = projectInterruptionsComboBox.getValue() != null ? projectInterruptionsComboBox.getValue().toString() : "";
                        row.createCell(4).setCellValue(Interruptions);

                    } 
                    else if (effortCatagoryComboBox.getValue().equals("Defects")) {
                        // Enable only the Defects combo box
                        String Defects = projectDefectsComboBox.getValue() != null ? projectDefectsComboBox.getValue().toString() : "";
                        row.createCell(4).setCellValue(Defects);

                    } 
                    else if (effortCatagoryComboBox.getValue().equals("Others")) {
                        // Enable only the Others text box
                        String Others = details.getText() != null ? details.getText().toString() : "";
                        row.createCell(4).setCellValue(Others);
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

        Button6.setOnAction(e -> {

            Stage newStage = new Stage();
            VersionControl pokerApp = new VersionControl();
            pokerApp.start(newStage);
        });

        Button7.setOnAction(e -> {

            Stage newStage = new Stage();
            AWSBackup pokerApp = new AWSBackup();
            pokerApp.start(newStage);
        });
    }



    public static long startClock(boolean running) {
        
        if (!running) {
            startTime = System.currentTimeMillis();
            running = true;
            updateTimer();
        } else {
            running = false;
            elapsedTime += System.currentTimeMillis() - startTime;
        }
        return elapsedTime;
    }

    //Originalyy non-static
    private static void updateTimer() {
        Thread timerThread = new Thread(() -> {
            while (running) {
                long currentTime = System.currentTimeMillis();
                long totalTime = elapsedTime + (currentTime - startTime);
                long seconds = (totalTime / 1000) % 60;
                long minutes = (totalTime / (1000 * 60)) % 60;
                long hours = (totalTime / (1000 * 60 * 60)) % 24;

                String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                formattedTime = timeString;

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
        });
        timerThread.setDaemon(true);
        timerThread.start();
    }

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