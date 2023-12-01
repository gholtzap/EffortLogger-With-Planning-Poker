package com.cse360;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

// this is where we import all the aws imports

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;

public class AWSBackup extends Application {

    private Label backupStatusLabel;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Backup Center");

        AnchorPane root = new AnchorPane();

        // Creating the labels
        Label titleLabel = new Label("Backup Center");
        titleLabel.setFont(Font.font("Bold", 20.0));
        AnchorPane.setTopAnchor(titleLabel, 20.0);
        AnchorPane.setLeftAnchor(titleLabel, 180.0);

        Label instructionLabel = new Label("Perform Data Backup");
        instructionLabel.setFont(Font.font("System Bold", 13.0));
        AnchorPane.setTopAnchor(instructionLabel, 115.0);
        AnchorPane.setLeftAnchor(instructionLabel, 185.0);

        // label label to show backup status
        backupStatusLabel = new Label();
        backupStatusLabel.setFont(Font.font("System Bold", 13.0));
        AnchorPane.setTopAnchor(backupStatusLabel, 220.0);
        AnchorPane.setLeftAnchor(backupStatusLabel, 170.0);
        backupStatusLabel.setVisible(false); // Initially hide this label

        // Create backup button
        Button backupButton = new Button("Start Backup");
        backupButton.setOnAction(event -> performBackup());
        AnchorPane.setTopAnchor(backupButton, 140.0);
        AnchorPane.setLeftAnchor(backupButton, 200.0);

        // Add components to the AnchorPane
        root.getChildren().addAll(titleLabel, instructionLabel, backupButton, backupStatusLabel);

        Scene scene = new Scene(root, 500, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void performBackup() {
        System.out.println("Backup in progress...");
        // System.out.println("Backup complete.");

        uploadFilesToS3Bucket();

        // Show "Backup created" message
        backupStatusLabel.setText("Backup Created Successfully...");
        backupStatusLabel.setVisible(true);

        System.out.println("Backup complete.");
    }

    // Example method for AWS S3 backup logic
    private void uploadFilesToS3Bucket() {
        // Hardcoded AWS credentials (not recommended for production)
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY);

        // Creating an S3 client
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withRegion(Regions.YOUR_REGION) // Choose the appropriate region
                .build();

        // Define the bucket name and file to upload
        String bucketName = BUCKET_NAME;
        String fileObjKeyName = OBJECT_NAME;
        String fileName = FILE_PATH;

        try {
            // Uploading the file to S3
            s3Client.putObject(new PutObjectRequest(bucketName, fileObjKeyName, new File(fileName)));
            System.out.println("Data backup created successfully.");
        } catch (Exception e) {
            // Handle exceptions
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void openWindow() {
        Stage stage = new Stage();
        start(stage);
    }
}
