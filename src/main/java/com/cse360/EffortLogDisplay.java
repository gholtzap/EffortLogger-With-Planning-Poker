package com.cse360;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class EffortLogDisplay extends Application {

    @Override
    public void start(Stage primaryStage) {

        // Sets the grid pane
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER); //TOP_LEFT
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Creates the table
        TableView table = new TableView<TableInformation>();

        // Creates the columns for the table
        TableColumn<TableInformation, String> projectColumn = new TableColumn<TableInformation, String>("Project");
        projectColumn.setCellValueFactory(new PropertyValueFactory<TableInformation, String>("Project"));

        TableColumn<TableInformation, String> timeColumn = new TableColumn<TableInformation, String>("Elapsed Time");
        timeColumn.setCellValueFactory(new PropertyValueFactory<TableInformation, String>("elapsedTime"));

        TableColumn<TableInformation, String> lifeCycleColumn = new TableColumn<TableInformation, String>("Life Cycle Step");
        lifeCycleColumn.setCellValueFactory(new PropertyValueFactory<TableInformation, String>("lifeCycleStep"));

        TableColumn<TableInformation, String> effortCategoryColumn = new TableColumn<TableInformation, String>("Effort Category");
        effortCategoryColumn.setCellValueFactory(new PropertyValueFactory<TableInformation, String>("effortCategory"));

        TableColumn<TableInformation, String> planColumn = new TableColumn<TableInformation, String>("Plan");
        planColumn.setCellValueFactory(new PropertyValueFactory<TableInformation, String>("Plan"));

        // Checks for the if the columns already exist
        if (!table.getColumns().contains(projectColumn)) {
            table.getColumns().add(projectColumn);
        }
        
        if (!table.getColumns().contains(timeColumn)) {
            table.getColumns().add(timeColumn);
        }
        
        if (!table.getColumns().contains(lifeCycleColumn)) {
            table.getColumns().add(lifeCycleColumn);
        }
        
        if (!table.getColumns().contains(effortCategoryColumn)) {
            table.getColumns().add(effortCategoryColumn);
        }
        
        if (!table.getColumns().contains(planColumn)) {
            table.getColumns().add(planColumn);
        }
        

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        ObservableList<String> list = FXCollections.observableArrayList();
        table.setItems(list);
        //table.getColumns().addAll(projectColumn, timeColumn, lifeCycleColumn, effortCategoryColumn, planColumn);

        
        try {

            Workbook workbook = null;
            Sheet sheet = null;
            File file = new File("EffortLogs.xlsx");

            FileInputStream inputStream = new FileInputStream(file);
            workbook = new XSSFWorkbook(inputStream);
            sheet = workbook.getSheet("EffortLogs");
            inputStream.close();

            ObservableList<TableInformation> tableData = FXCollections.observableArrayList();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                String project = row.getCell(0) != null ? row.getCell(0).getStringCellValue() : "";
                String elapsedTime = row.getCell(1) != null ? row.getCell(1).getStringCellValue() : "";
                String lifeCycleStep = row.getCell(2) != null ? row.getCell(2).getStringCellValue() : "";
                String effortCategory = row.getCell(3) != null ? row.getCell(3).getStringCellValue() : "";
                String plan = row.getCell(4) != null ? row.getCell(4).getStringCellValue() : "";

                TableInformation info = new TableInformation(project, elapsedTime, lifeCycleStep, effortCategory, plan);
            
                tableData.add(info);
            }
            
            table.setItems(tableData);

            // Write the workbook content to a file
            FileOutputStream outputStream = new FileOutputStream("EffortLogs.xlsx");
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        
        grid.add(table, 0, 0);

        // Setting the scene
        primaryStage.setScene(new Scene(grid, 900, 900));
        primaryStage.setFullScreen(true);
        grid.setStyle("-fx-background-color: linear-gradient(to bottom, #f0f0f0, #d6e0f0);");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
