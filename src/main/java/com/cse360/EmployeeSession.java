package com.cse360;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
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

public class EmployeeSession extends Application {

    private static List<String> AGENDA_ITEMS;
    private List<String> userRatings = new ArrayList<>();

    private Label headerLabel;
    private Label agendaLabel;
    private Label nameLabel;
    private Label instructionLabel;
    private ChoiceBox<Integer> ratingChoiceBox;
    private Button nextButton;
    private Button submitButton;
    private ListView<String> ratingListView;
    private StackPane pokerCardPane;
    private TextArea chatTextArea; 
    private TextField inputTextField;

    private int currentItemIndex = 1;
    private String name;
    private String currentProject;

    private Connection connection;
    private Statement statement;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private Stage stage;

    public EmployeeSession(String name, List<String> items, Connection connection, Statement statement, String currentProject){
        this.name = name;
        AGENDA_ITEMS = items;
        this.connection = connection;
        this.statement = statement;
        this.currentProject = currentProject;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Planning Poker");

        ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/com/cse360/assets/planning_poker_icon.png")));
        logo.setFitHeight(50);
        logo.setFitWidth(120);
        headerLabel = new Label("Game in Progress...");

        nextButton = new Button("Submit and Proceed");
        nextButton.setStyle("-fx-background-color: #4361EE; -fx-text-fill: white;");
        nextButton.setDisable(true);
        nextButton.setOnAction(event -> handleNextButtonClick());

        submitButton = new Button("Close Session");
        submitButton.setDisable(true);
        submitButton.setOnAction(e -> closeSession());
        submitButton.setStyle("-fx-background-color: #DB4035; -fx-text-fill: white;" );


        HBox headerBox = new HBox(10);
        headerBox.getChildren().addAll(logo, headerLabel);
        headerBox.setAlignment(Pos.CENTER);
        nameLabel = new Label("Welcome: " + name);

        headerLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 30; -fx-text-fill: #4CAF50;");
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 35; -fx-text-fill: #355C7D;");

        ImageView profile = new ImageView(new Image(getClass().getResourceAsStream("/com/cse360/assets/user_icon.png")));
        profile.setFitHeight(50);
        profile.setFitWidth(50);
        
        HBox welcomeBox = new HBox(10);
        welcomeBox.getChildren().addAll(nameLabel, profile);
        welcomeBox.setAlignment(Pos.CENTER);

        ratingChoiceBox = new ChoiceBox<>();
        ratingChoiceBox.getItems().addAll(1, 2, 3, 5, 8, 13);
        ratingChoiceBox.setOnAction(e -> {
            if (ratingChoiceBox.getValue() != null){
                nextButton.setDisable(false);
            } else{
                nextButton.setDisable(true);
            }
        });

        Label tempLabel = new Label("Select a value from the dropdown");

        HBox choiceBox = new HBox(10);
        choiceBox.getChildren().addAll(tempLabel, ratingChoiceBox);
        choiceBox.setAlignment(Pos.CENTER);

        instructionLabel = new Label(" Choose an appropriate score for the Backlog item you see on the top.");
        instructionLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 15; -fx-text-fill: #DB4035;");
        agendaLabel = new Label("Agenda: " + AGENDA_ITEMS.get(currentItemIndex));
        agendaLabel.setStyle("-fx-font-weight: bold;");

        chatTextArea = new TextArea();
        chatTextArea.setPrefWidth(300);
        chatTextArea.setPrefHeight(200);
        chatTextArea.setEditable(false);
        chatTextArea.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        ratingListView = new ListView<>();
        ratingListView.setPrefHeight(80);
        ratingListView.setPrefWidth(170);
        ratingListView.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        pokerCardPane = new StackPane();
        pokerCardPane.setAlignment(Pos.CENTER);

        inputTextField = new TextField();
        Button sendButton = new Button("Send");
        sendButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        sendButton.setOnAction(e -> handleSendButtonClick());

        HBox inputBox = new HBox(5, inputTextField, sendButton);
        inputBox.setAlignment(Pos.CENTER);

        VBox chatArea = new VBox(10);
        chatArea.getChildren().addAll(chatTextArea, inputBox);

        HBox boxes = new HBox(50);
        boxes.setAlignment(Pos.CENTER);
        boxes.getChildren().addAll(chatArea, pokerCardPane, ratingListView);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(nextButton, submitButton);

        VBox mainLayout = new VBox(20);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(20));
        mainLayout.getChildren().addAll(headerBox, welcomeBox, agendaLabel, instructionLabel, choiceBox, boxes, buttonBox);

        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(scene);

        primaryStage.show();

        scheduler.scheduleAtFixedRate(() -> {
            refreshChatTextArea(chatTextArea);
        }, 0, 5, TimeUnit.SECONDS);

        stage = primaryStage;
    }

    private void handleSendButtonClick() {
        String message = inputTextField.getText().trim();
        if (!message.isEmpty()) {
            String formattedMessage = name + ": " + message;
            chatTextArea.appendText(formattedMessage + "\n");

            addToDatabase(name, message);

            System.out.println("LOG ------ Message sent successfully!");

            inputTextField.clear();
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

    private void handleNextButtonClick(){
        int selectedRating = ratingChoiceBox.getValue();
        userRatings.add(name + "(You) selected " + selectedRating);
        String currAgenda = AGENDA_ITEMS.get(currentItemIndex);
        showPokerCard(selectedRating);

        currentItemIndex++;

        String insertQuery = "INSERT INTO RATINGS" + currentProject +  " (user, rating) VALUES (?, ?)";
        
        try(PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, currAgenda + " - " + Integer.toString(selectedRating));
            preparedStatement.executeUpdate();
            System.out.println("LOG ------ Inserted into RATINGS successfully!");
        } catch (SQLException e){
            System.out.println(e);
        }

        if (currentItemIndex < AGENDA_ITEMS.size()) {
            agendaLabel.setText("Agenda: " + AGENDA_ITEMS.get(currentItemIndex));
        } else {
            nextButton.setDisable(true);
            submitButton.setDisable(false);
            ratingChoiceBox.setDisable(true);
            agendaLabel.setText("You have finished the session. Please exit.");
            agendaLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 15; -fx-text-fill: #DB4035;");
        }

        // Update the ListView
        updateRatingListView();
    }

    private void showPokerCard(int rating) {
        ImageView pokerCardImageView = createPokerCardImageView(rating);

        pokerCardPane.getChildren().clear();
        pokerCardPane.getChildren().add(pokerCardImageView);

        // Float up, enlarge, and fade out animation
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(pokerCardImageView.translateYProperty(), 0)),
                new KeyFrame(Duration.seconds(1), new KeyValue(pokerCardImageView.translateYProperty(), -25)),
                new KeyFrame(Duration.seconds(1), new KeyValue(pokerCardImageView.scaleXProperty(), 1.5)),
                new KeyFrame(Duration.seconds(1), new KeyValue(pokerCardImageView.scaleYProperty(), 1.5)),
                new KeyFrame(Duration.seconds(2), new KeyValue(pokerCardImageView.opacityProperty(), 1)),
                new KeyFrame(Duration.seconds(3), new KeyValue(pokerCardImageView.opacityProperty(), 0))
        );

        timeline.setOnFinished(event -> Platform.runLater(() -> pokerCardPane.getChildren().clear()));
        timeline.play();

        System.out.println("LOG ------ Rendered Animation successfully!");
    }

    private ImageView createPokerCardImageView(int rating) {
        ImageView imageView;

        switch (rating) {
            case 1:
                imageView = new ImageView(new Image(getClass().getResourceAsStream("/com/cse360/assets/poker_1.png")));
                break;
            case 2:
                imageView = new ImageView(new Image(getClass().getResourceAsStream("/com/cse360/assets/poker_2.png")));
                break;
            case 3:
                imageView = new ImageView(new Image(getClass().getResourceAsStream("/com/cse360/assets/poker_3.png")));
                break;
            case 5:
                imageView = new ImageView(new Image(getClass().getResourceAsStream("/com/cse360/assets/poker_5.png")));
                break;
            case 8:
                imageView = new ImageView(new Image(getClass().getResourceAsStream("/com/cse360/assets/poker_8.png")));
                break;
            case 13:
                imageView = new ImageView(new Image(getClass().getResourceAsStream("/com/cse360/assets/poker_13.png")));
                break;
            default:
                imageView = new ImageView();
                break;
        }


        imageView.setFitWidth(90);
        imageView.setFitHeight(140);
        imageView.setPreserveRatio(true);

        return imageView;
    }

    private void updateRatingListView() {
        ratingListView.getItems().clear();
        ratingListView.getItems().addAll(userRatings);

        System.out.println("LOG ------ Ratings Column updated successfully!");
    }

    private void closeSession(){

        System.out.println("LOG ------ Closing Existing Threads......!");
        scheduler.shutdown();
        stage.close();
        System.out.println("LOG ------ This thread is closed!");
    }
}
