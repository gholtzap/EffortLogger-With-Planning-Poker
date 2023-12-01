package com.cse360;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class PlanningPoker extends Application {


    // *********************************************************************************
    // *********************************************************************************
    // DEFINE ALL VARIABLES HERE
    // *********************************************************************************
    // *********************************************************************************

    String current_user;

    static final String URL = "jdbc:mysql://localhost:3306/PLANNINGPOKER";
    static final String USERNAME = "root";
    static final String PASSWORD = "hello@123";
    private String currProject;
    
    List<String> project1 = new ArrayList<>();
    List<String> project2 = new ArrayList<>();

    private Connection connection;
    private Statement statement;

    private Stage stage;

    public PlanningPoker(String name) {
        current_user = name;
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        // *********************************************************************************
        // FIRST, WE CONNECT TO OUR DATABASE
        // *********************************************************************************
        
        try 
        {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            statement = connection.createStatement();
            System.out.println("<------Connected to Database successfully------>");
        
        } catch (SQLException e){
            
            System.out.println(e);
        }

        // *********************************************************************************
        // SECOND, WE GENERATE A RANDOM USER NAME.
        // *********************************************************************************

        // current_user = "User" + Integer.toString(new Random().nextInt(100)); 
        primaryStage.setTitle("Planning Poker");

        // *********************************************************************************
        // THIRD, WE ADD THIS USER TO OUR DATABASE.
        // *********************************************************************************
        
        addUserToDatabase(connection, statement);

        // *********************************************************************************
        // FOURTH, WE BUILD OUR UI AND CONNECT ALL ELEMENTS TO THEIR HANDLERS.
        // *********************************************************************************
        
        Label welcomeLabel = new Label("Welcome to Planning Poker session: " + current_user);
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/com/cse360/assets/planning_poker_icon.png")));
         // Replace with the actual image path
        imageView.setFitHeight(50);
        imageView.setFitWidth(120);
        welcomeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 25; -fx-text-fill: #4CAF50;");

        HBox welcome = new HBox(20);
        welcome.getChildren().addAll(imageView, welcomeLabel);
        welcome.setAlignment(Pos.CENTER);


        Label welcomeLabel2 = new Label("Choose a project OR Start as Manager");
        welcomeLabel2.setStyle("-fx-font-weight: bold; -fx-font-size: 19; -fx-text-fill: #A78F50;");

        HBox welcomeBox2 = new HBox(10,welcomeLabel2);
        welcomeBox2.setAlignment(Pos.CENTER);

        
        ChoiceBox<String> projectChoiceBox = new ChoiceBox<>();
        projectChoiceBox.getItems().addAll("Project 1", "Project 2");

        ListView<String> projectListView = new ListView<>();
        projectListView.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        
        Button startGameButton = new Button("Start as an Employee");
        startGameButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        startGameButton.setDisable(true);
        startGameButton.setOnAction(e -> {
            openServerToClientConnection(current_user, connection, statement);
        });

        Button adminGameButton = new Button("Start as a Manager");
        adminGameButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        adminGameButton.setOnAction(e -> openManagerSession());

        projectChoiceBox.setOnAction(e -> {

            if(projectChoiceBox.getValue() != null){
                startGameButton.setDisable(false);
            }
            displayProjectBacklog(projectChoiceBox.getValue(), projectListView);
        });

        HBox buttonBox = new HBox(50);
        buttonBox.setPadding(new Insets(20, 20, 20, 0));
        Label orLabel = new Label("OR");

        buttonBox.getChildren().addAll(projectChoiceBox, orLabel, adminGameButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10);
        layout.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(welcome, welcomeBox2, buttonBox, projectListView, startGameButton);

        
        Scene scene = new Scene(layout);
        primaryStage.setScene(scene);

        primaryStage.show();

        stage = primaryStage;
    }

    // *********************************************************************************
    // *********************************************************************************
    // METHOD TO ADD USER TO DATABASE SO WE CAN USE THE USERNAME FOR FUTURE PURPOSES
    // *********************************************************************************
    // *********************************************************************************

     private void addUserToDatabase(Connection connection, Statement statement){

        try {

            System.out.println("LOG ------ " + current_user);
            
            String query = "INSERT INTO users VALUES (?)";
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, current_user);
                preparedStatement.executeUpdate();
                System.out.println("LOG ------ User inserted successfully!");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        
    }

    // *********************************************************************************
    // *********************************************************************************
    // METHOD TO DISPLAY PROJECT BACKLOG OF A SPECIFIC CHOSEN BACKLOG ON JAVAFX GUI.
    // WE CALL A UTILITY FUNCTION TO RETRIEVE ALL ITEMS FROM THAT PARTICULAR PROJECT. 
    // *********************************************************************************
    // *********************************************************************************

    private void displayProjectBacklog(String project, ListView<String> projectListView) {
  
        projectListView.getItems().clear();

        for (String backlog : getBackLogItems(project)) {
            projectListView.getItems().add(backlog);
        }
        System.out.println("LOG ------ Backlog fetched successfully successfully!");
    }

    // *********************************************************************************
    // *********************************************************************************
    // METHOD TO RETREIVE ALL ITEMS FROM THAT PARTICULAR PROJECT. 
    // *********************************************************************************
    // *********************************************************************************

    private List<String> getBackLogItems(String project) {

        currProject = project.equals("Project 1") ?  "1" : "2";

        project1.add("Backlogs for " + project);
        project1.add("P1.1: API Querying Errors not resolved. Possible endpoint down.");
        project1.add("P2.2: Write JUnit Tests.");

        project2.add("Backlogs for " + project);
        project2.add("P2.1: Pending Code Reviews.");
        project2.add("P2.2: Push to Working Tree by 21st November.");

        return project.equals("Project 1") ? project1 : project2;
    }

    // *********************************************************************************
    // *********************************************************************************
    // METHOD TO HANDLE A NEW STAGE AND START A CLIENT SERVER CONNECTION.
    // *********************************************************************************
    // *********************************************************************************

    private void openServerToClientConnection(String currentUser, Connection connection, Statement statement) {
        Platform.runLater(() -> {
            Stage newStage = new Stage();
            ServerToClientConnection connectionApp = new ServerToClientConnection(currentUser, connection, statement, currProject.equals("Project 1") ? project1 : project2, currProject);
            System.out.println("LOG ------ User Session started successfully!");
            connectionApp.start(newStage);
            stage.close();
        });
    }

    private void openManagerSession(){
        
        Platform.runLater(() -> {
            Stage newStage = new Stage();
            ManagerDashboard connectionApp = new ManagerDashboard(current_user, connection, statement, 2);
            System.out.println("LOG ------ Manager Session started successfully!");
            connectionApp.start(newStage);
            stage.close();
            System.out.println("LOG ------ Closed current Thread!");
        });
    }
}
