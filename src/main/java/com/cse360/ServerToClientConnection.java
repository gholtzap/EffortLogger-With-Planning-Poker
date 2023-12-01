package com.cse360;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
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


public class ServerToClientConnection extends Application{

    // *********************************************************************************
    // *********************************************************************************
    // DEFINE ALL VARIABLES HERE
    // TO BE ABLE TO RUN MULTIPLE INSTANCES, WE WILL USE THREADS.
    // *********************************************************************************
    // *********************************************************************************

    private ListView<HBox> membersListView;

    private Connection connection;
    private Statement statement;

    private List<String> sendArray;

    private String currentUser;
    private String currentProject;
    private int noOfUsers;
    Label totalCountLabel;

    private Stage stage;
    
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    // *********************************************************************************
    // *********************************************************************************
    // CONSTRUCTOR TO GET ALL OBJECTS FROM PREVIOUS CLASS.
    // *********************************************************************************
    // *********************************************************************************

    public ServerToClientConnection(String currentUser, Connection connection, Statement statement, List<String> p1, String currentProject){
        this.currentUser = currentUser;
        this.connection = connection;
        this.statement = statement;
        sendArray = p1;
        this.currentProject = currentProject;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        // *********************************************************************************
        // FIRST, WE CREATE A LISTVIEW WHERE WE CAN ADD ALL MEMBERS CURRENTLY IN THE ROOM
        // *********************************************************************************

        membersListView = createMembersListView();

        // *********************************************************************************
        // SECOND, WE MAKE A CHAT AREA, WHERE ALL MESSAGES ARE DISPLAYED, AND THE USERS CAN 
        // COMMUNICATE WHEN TO START THE GAME. WE NEED TO REFRESH THE CHAT TEXT AREA TO GET 
        // PREVIOUS CHAT HISTORY.
        // *********************************************************************************

        TextArea chatTextArea = new TextArea();
        chatTextArea.setPrefWidth(300);
        chatTextArea.setPrefHeight(200);
        chatTextArea.setEditable(false);

        refreshChatTextArea(chatTextArea);

        primaryStage.setTitle("Team Chat App");

        Label headerLabel = new Label("Planning Poker Session for Project " + currentProject);
        headerLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16");

        Label memberDemographicLabel = new Label("Member Demographic: Waiting for members to join...");
        memberDemographicLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16");
        memberDemographicLabel.setTextFill(Color.DARKBLUE);
        
        TextField inputTextField = new TextField();
        Button sendButton = new Button("Send");

        // *********************************************************************************
        // SECOND, WE ADD A HANDLER TO THE SEND BUTTON, WHERE IT ADDS THE MESSAGE TO THE DB
        // AND THEN REFRESHES IT FOR ALL INSTANCES RUNNING.
        // *********************************************************************************

        sendButton.setOnAction(event -> {
            String message = inputTextField.getText();
            if (!message.isEmpty()){
                addMessageToDatabase(currentUser, message);
                refreshChatTextArea(chatTextArea);
                inputTextField.clear();
            }
        });

        // *********************************************************************************
        // THIRD, WE ADD SOME MORE UI ELEMENTS.
        // *********************************************************************************

        HBox inputBox = new HBox(5, inputTextField, sendButton);
        inputBox.setAlignment(Pos.CENTER);

        totalCountLabel = new Label("Total Members: " + noOfUsers);

        Button startSessionButton = new Button("Start Session");
        startSessionButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        startSessionButton.setAlignment(Pos.CENTER);
        startSessionButton.setOnAction(e -> openEmployeeSession(currentUser));
        
        chatTextArea.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        membersListView.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(10));
        layout.setTop(memberDemographicLabel);
        layout.setCenter(chatTextArea);
        layout.setBottom(totalCountLabel);

        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(event -> refreshMembersList());

        VBox rightLayout = new VBox(10, headerLabel, membersListView, inputBox, startSessionButton, refreshButton);
        rightLayout.setPadding(new Insets(10));
        layout.setRight(rightLayout);

        Scene scene = new Scene(layout, 800, 400);
        primaryStage.setScene(scene);

        primaryStage.show();

        // *********************************************************************************
        // LASTLY, WE KEEP ON THE REFRESHING EACH THREAD AFTER 5 SECONDS TO UPDATE THE CHATS
        // IN ALL INSTANCES.
        // *********************************************************************************

        scheduler.scheduleAtFixedRate(() -> {
            refreshChatTextArea(chatTextArea);
        }, 0, 5, TimeUnit.SECONDS);

        stage = primaryStage;
        
    }

    // *********************************************************************************
    // *********************************************************************************
    // THIS METHOD CREATE A LISTVIEW AND APPENDS ALL CURRENT MEMBERS IN IT. IT RETURNS
    // A LISTVIEW CONTAINING SEVERAL HBOXES. IT USES A HELPER METHOD TO FETCH ALL USERS
    // FROM A TABLE FROM THE DATABASE.
    // *********************************************************************************
    // *********************************************************************************

    private ListView<HBox> createMembersListView() {
        ListView<HBox> listView = new ListView<>();
        String[] members = getUsersFromDatabase();
        noOfUsers = members.length;

        for (String member : members) {
            Label nameLabel = new Label(member + " joined");
            HBox memberBox = new HBox(10, nameLabel);
            memberBox.setAlignment(Pos.CENTER_LEFT);
            listView.getItems().add(memberBox);
        }

        
        return listView;
    }

    // *********************************************************************************
    // *********************************************************************************
    // THIS METHOD IS THE HELPER METHOD FOR THE ABOVE FUNCTION.
    // *********************************************************************************
    // *********************************************************************************

    private String[] getUsersFromDatabase() {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
            List<String> userList = new ArrayList<>();

            while (resultSet.next()) {
                String username = resultSet.getString("name");
                userList.add(username);
            }

            return userList.toArray(new String[0]);

        } catch (SQLException e) {
            System.out.println(e);
        }

        return new String[0];
    }

    // *********************************************************************************
    // *********************************************************************************
    // THIS METHOD REFRESHES THE LIST OF USERS AND IS CALLED AFTER THE CREATION OF THE
    // LISTVIEW.
    // *********************************************************************************
    // *********************************************************************************

    private void refreshMembersList() {
        membersListView.getItems().clear();
        String[] users = getUsersFromDatabase();

        noOfUsers = users.length;
        totalCountLabel.setText("Total Members: " + Integer.toString(noOfUsers));

        for (String user : users) {
            Label nameLabel = new Label(user + " joined");
            HBox memberBox = new HBox(10, nameLabel);
            memberBox.setAlignment(Pos.CENTER_LEFT);
            membersListView.getItems().add(memberBox);
        }
    }

    // *********************************************************************************
    // *********************************************************************************
    // THIS METHOD ADDS EACH UNSER-TYPED MESSAGE TO A TABLE USING PREPAREDSTATEMENTS.
    // *********************************************************************************
    // *********************************************************************************

    private void addMessageToDatabase(String user, String message){
        try {
            String insertQuery = "INSERT INTO messages (user, message) VALUES (? , ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)){
                preparedStatement.setString(1, user);
                preparedStatement.setString(2, message);
                preparedStatement.executeUpdate();
            }
            System.out.println("LOG ------ Message communicated successfully!");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    // *********************************************************************************
    // *********************************************************************************
    // THIS METHOD REFRESHES CHAT MESSAGES TO FETCH RECENT MESSAGES FROM DATABASE. IT USES
    // A HELPER METHOD TO FETCH RECENT MESSAGES FROM DATABASE.
    // *********************************************************************************
    // *********************************************************************************

    private void refreshChatTextArea(TextArea chatTextArea){
        chatTextArea.clear();
        String[] messages = getMessagesFromDatabase();

        for (String m: messages){
            chatTextArea.appendText(m + "\n");
        }
    }

    // *********************************************************************************
    // *********************************************************************************
    // THIS METHOD IS THE HELPER METHOD FOR THE ABOVE FUNCTION.
    // *********************************************************************************
    // *********************************************************************************
    
    private String[] getMessagesFromDatabase() {
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM messages");
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

    private void openEmployeeSession(String name){

        Platform.runLater(() -> {
            EmployeeSession session = new EmployeeSession(name, sendArray, connection, statement, currentProject);
            Stage newStage = new Stage();
            System.out.println("LOG ------ Starting Employee Session on a new Thread!");
            session.start(newStage);
            // scheduler.shutdown();
            stage.close();
            System.out.println("LOG ------ Closed current Thread!");
        });
    }
}
