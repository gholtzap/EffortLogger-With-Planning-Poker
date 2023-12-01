package com.cse360;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;

public class Main extends Application {

    VBox vbox;
    TextField userTextField;
    PasswordField passwordField;

    public void start(Stage primaryStage) {

        primaryStage.setTitle("EffortLoggerV2 Login");

        vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(25, 25, 25, 25));

        Text headerText = new Text("Welcome to Effort Logger V2");
        headerText.setFont(Font.font("Arial", 20));

        Text projectInfo = new Text("This is part of our 360 final project.\nDeveloped in JavaFX, MySQL, and Maven.");
        projectInfo.setFont(Font.font("Arial", 12));

        Label userName = new Label("Username:");
        userName.setStyle("-fx-font-size: 14px;");

        userTextField = new TextField();

        Label password = new Label("Password:");
        password.setStyle("-fx-font-size: 14px;");

        passwordField = new PasswordField();

        Button btnLogin = new Button("Login");
        btnLogin.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        HBox hbBtnLogin = new HBox(10);
        hbBtnLogin.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtnLogin.getChildren().add(btnLogin);

        Button btnSignUp = new Button("Sign Up");
        btnSignUp.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px;");
        HBox hbBtnSignUp = new HBox(10);
        hbBtnSignUp.setAlignment(Pos.BOTTOM_LEFT);
        hbBtnSignUp.getChildren().add(btnSignUp);

        final Text actiontarget = new Text();

        btnLogin.setOnAction(e -> handleLogin(userTextField.getText(), passwordField.getText(), actiontarget, primaryStage));

        btnSignUp.setOnAction(e -> handleSignUp(userTextField.getText(), passwordField.getText(), actiontarget));

        vbox.getChildren().addAll(headerText, projectInfo, userName, userTextField, password, passwordField, hbBtnLogin, hbBtnSignUp, actiontarget);

        Scene scene = new Scene(vbox, 400, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleLogin(String username, String password, Text actiontarget, Stage primaryStage) {
        try {
            File employeeFile = new File("EmployeeInfo.txt");
            FileReader reader = new FileReader(employeeFile);
            BufferedReader in = new BufferedReader(reader);

            String line;
            boolean userFound = false;

            while ((line = in.readLine()) != null) {
                if (line.equals(username)) {
                    userFound = true;
                    break;
                }
            }

            if (userFound) {
                in.close();
                reader = new FileReader(employeeFile);
                in = new BufferedReader(reader);

                boolean passwordCorrect = false;

                while ((line = in.readLine()) != null) {
                    if (line.equals(EncryptionDecryption.EncryptPassword(password))) {
                        passwordCorrect = true;
                        break;
                    }
                }

                if (passwordCorrect) {
                    actiontarget.setText("Login successful!");
                    actiontarget.setStyle("-fx-text-fill: green;");

                    // Redirect to EffortLogger console or another screen
                    EffortLoggerV2Console console = new EffortLoggerV2Console(username);
                    console.start(primaryStage);
                } else {
                    actiontarget.setText("Incorrect password!");
                    actiontarget.setStyle("-fx-text-fill: red;");
                }
            } else {
                actiontarget.setText("User not found!");
                actiontarget.setStyle("-fx-text-fill: red;");
            }

            in.close();

        } catch (FileNotFoundException e) {
            actiontarget.setText("Employee Information file could not be found.");
            actiontarget.setStyle("-fx-text-fill: red;");
        } catch (IOException e) {
            actiontarget.setText("An error has occurred.");
            actiontarget.setStyle("-fx-text-fill: red;");
        }

        userTextField.clear();
        passwordField.clear();
    }

    private void handleSignUp(String username, String password, Text actiontarget) {
        if (PasswordValidation.ValidatePassword(password)) {
            try {
                File employeeFile = new File("EmployeeInfo.txt");
                FileWriter writer = new FileWriter(employeeFile, true);

                password = EncryptionDecryption.EncryptPassword(password);

                writer.write(username + "\n" + password + "\n");
                writer.close();

                actiontarget.setText("Sign up successful!");
                actiontarget.setStyle("-fx-text-fill: green;");

            } catch (IOException e) {
                actiontarget.setText("An error has occurred.");
                actiontarget.setStyle("-fx-text-fill: red;");
            }
        } else {
            actiontarget.setText("Please input a valid password");
            actiontarget.setStyle("-fx-text-fill: red;");

            // Display password rules
            Text rules = new Text("Rules for passwords:\n" +
                    "   Must contain at least one:\n" +
                    "   - Lowercase letter.\n" +
                    "   - Uppercase letter.\n" +
                    "   - Digit (1-9).\n" +
                    "   - Special Character.\n" +
                    "   Must contain none:\n" +
                    "   - Spaces.\n" +
                    "   Must be between 7-18 characters long.");
            rules.setStyle("-fx-font-size: 12px;");
            vbox.getChildren().add(rules);
        }

        userTextField.clear();
        passwordField.clear();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
