package com.cse360;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ManagerDashboard extends Application {

    private TextArea ratingListView;
    private TextArea chatTextArea;
    private TextField inputTextField;
    private Button fetchButton;
    private ChoiceBox<String> projectChoiceBox;
    private Stage stage;

    private Connection connection;
    private Statement statement;

    private int count;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private String name;

    ArrayList<String> options;

    public ManagerDashboard(String name, Connection connection, Statement statement, int count){
        this.name = name;
        this.connection = connection;
        this.statement = statement;
        this.count = count;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Manager Dashboard");


        options = new ArrayList<>();

        for (Integer i = 1; i <= count; i++) {
            options.add(Integer.toString(i));
        }

        Label headerLabel = new Label("Manager Dashboard");

        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/com/cse360/assets/planning_poker_icon.png"))); // Replace with the actual image path
        imageView.setFitHeight(50);
        imageView.setFitWidth(120);
        headerLabel.setGraphic(imageView);
        headerLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 30; -fx-text-fill: #4CAF50;");

        Label welcomeLabel = new Label("Welcome " + name);
        welcomeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 35; -fx-text-fill: #355C7D;");
        welcomeLabel.setAlignment(Pos.CENTER);

        Label instructionLabel = new Label("Please choose a project and Click fetch to see responses: ");
        instructionLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 15; -fx-text-fill: #C06C84;");


        projectChoiceBox = new ChoiceBox<>();
        projectChoiceBox.getItems().addAll(options);
        projectChoiceBox.setOnAction(e -> {
            if (projectChoiceBox.getValue() == null){
                fetchButton.setDisable(true);
            } else {
                fetchButton.setDisable(false);
            }
        });

        HBox dropDownBox = new HBox(10);
        dropDownBox.getChildren().addAll(instructionLabel, projectChoiceBox);
        dropDownBox.setAlignment(Pos.BASELINE_LEFT);


        ratingListView = new TextArea();
        ratingListView.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        Label heading1 = new Label("Messages Visible to all Participants.");
        Label heading2 = new Label("Participant Responses");

        heading1.setStyle("-fx-font-weight: bold; -fx-font-size: 15; -fx-text-fill: #FF9A98;");
        heading2.setStyle("-fx-font-weight: bold; -fx-font-size: 15; -fx-text-fill: #FF9A98;");

        HBox headingsBox = new HBox(150);
        headingsBox.setAlignment(Pos.BASELINE_LEFT);
        headingsBox.getChildren().addAll(heading1, heading2);

        chatTextArea = new TextArea();
        chatTextArea.setPrefWidth(300);
        chatTextArea.setPrefHeight(200);
        chatTextArea.setEditable(false);
        chatTextArea.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        inputTextField = new TextField();
        Button sendButton = new Button("Send");
        sendButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        HBox inputBox = new HBox(10, inputTextField, sendButton);
        inputBox.setAlignment(Pos.CENTER);

        sendButton.setOnAction(e -> handleSendButtonClick());

        VBox chatArea = new VBox(10);
        chatArea.getChildren().addAll(chatTextArea, inputBox);

        // Fetch and Close Session Buttons
       
        Button closeSessionButton = new Button("Close Session");
        closeSessionButton.setStyle("-fx-background-color: #4361EE; -fx-text-fill: white;");
        closeSessionButton.setOnAction(e -> closeSession());

        fetchButton = new Button("Fetch Ratings");
        fetchButton.setStyle("-fx-background-color: #4361EE; -fx-text-fill: white;");
        fetchButton.setDisable(true);
        fetchButton.setOnAction(e -> fetchRatings(projectChoiceBox.getValue()));

        // Layout Setup
        VBox root = new VBox(20);
        root.setPadding(new Insets(20, 20, 20, 20));
        root.getChildren().addAll(
                headerLabel,
                welcomeLabel,
                dropDownBox,
                headingsBox,
                new HBox(20, chatArea, ratingListView),
                new HBox(20, fetchButton, closeSessionButton)
        );

        // Scene Setup
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        scheduler.scheduleAtFixedRate(() -> {
            refreshChatTextArea(chatTextArea);
        }, 0, 5, TimeUnit.SECONDS);

        primaryStage.show();

        stage = primaryStage;
    }

    private void fetchRatings(String currentProject){

        ratingListView.clear();
        String[] messages = getRatingsFromDatabase(currentProject);

        for (String m: messages){
            ratingListView.appendText(m + "\n");
            ratingListView.setStyle("-fx-font-weight: bold; -fx-font-size: 15;");
        }
    }

    private String[] getRatingsFromDatabase(String currentProject) {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM RATINGS" + currentProject + " ORDER BY rating DESC");
            List<String> messageList = new ArrayList<>();

            while (resultSet.next()) {
                String user = resultSet.getString("user");
                String rating = resultSet.getString("rating");
                messageList.add(user + " : " + rating);
            }

            return messageList.toArray(new String[0]);

        } catch (SQLException e) {
            System.out.println(e);
        }

        return new String[0];
    }

    private void handleSendButtonClick() {
        String message = inputTextField.getText().trim();
        if (!message.isEmpty()) {
            String formattedMessage = name + ": " + message;
            chatTextArea.appendText(formattedMessage + "\n");

            // Add the message to the database
            addToDatabase(name, message);

            inputTextField.clear();
        }
    }

    private void addToDatabase(String name, String message) {
        
        String insertQuery = "INSERT INTO GAMECHAT (user, message) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, message);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refreshChatTextArea(TextArea chatTextArea){
        chatTextArea.clear();
        String[] messages = getMessagesFromDatabase();

        for (String m: messages){
            chatTextArea.appendText(m + "\n");
        }
    }

    private String[] getMessagesFromDatabase() {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM GAMECHAT");
            List<String> messageList = new ArrayList<>();

            while (resultSet.next()) {
                String user = resultSet.getString("user");
                String message = resultSet.getString("message");
                messageList.add(user + ": " + message);
            }

            return messageList.toArray(new String[0]);

        } catch (SQLException e) {
            System.out.println(e);
        }

        return new String[0];
    }

    private void closeSession(){
        scheduler.shutdown();
        stage.close();
    }
    
}
