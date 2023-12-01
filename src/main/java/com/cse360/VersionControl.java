package com.cse360;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class VersionControl extends Application {

    // CREATE A LOG FILE TO STORE ALL USER ACTIVITY
    private static final String LOG_FILE_PATH = "button_clicks_log.txt";

    // CREATE BUTTONS
    private Button initRepoButton;
    private Button connectButton;
    private Button addButton;
    private Button commitButton;
    private Button fetchButton;

    private ComboBox<String> fileDropdown;

    private ImageView tick_init;
    private ImageView tick_connect;
    private ImageView tick_add;
    private ImageView tick_commit;
    private ImageView tick_fetch;

    @Override
    public void start(Stage primaryStage) {
        initUI(primaryStage);
        setupEventHandlers();
    }

    // HELPER METHOD TO INITIALIZE OUR UI 
    private void initUI(Stage primaryStage) {
        Label titleLabel = new Label("Version Control");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label instructionsLabel = new Label("Click buttons below to perform actions. Log file created at: " + LOG_FILE_PATH);
        instructionsLabel.setStyle("-fx-font-weight: bold;");

        Label githubActionLabel = new Label("Enter your repository link to remotely connect to GitHub");


        // HELPER METHOD TO CREATE A TICK IMAGE TO INDICATE WHEN TASK IS DONE
        tick_init = createTickImage();
        tick_connect = createTickImage();
        tick_add = createTickImage();
        tick_commit = createTickImage();
        tick_fetch = createTickImage();

        // HELPER BUTTON TO CREATE STYLED BUTTONS 
        initRepoButton = createStyledButton("Initialize Repo");
        connectButton = createStyledButton("Connect");
        fetchButton = createStyledButton("Pull Code");
        commitButton = createStyledButton("Commit Files");
        addButton = createStyledButton("Add");
        
        // WE DISABLE THE ADD AND COMMIT BUTTON UNTIL A FILE HAS BEEN SELECTED
        addButton.setDisable(true);
        commitButton.setDisable(true);

        TextField inputText = new TextField();

        HBox headerBox = new HBox(10);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.getChildren().addAll(initRepoButton, tick_init);

        HBox connectBox = new HBox(20, inputText, connectButton, tick_connect);
        connectBox.setAlignment(Pos.BASELINE_CENTER);

        // HELPER METHOD THAT FETCHES ALL FILES IN THE GIVEN DIRECTORY
        fileDropdown = new ComboBox<>(FXCollections.observableArrayList(getFilesInCurrentDirectory()));
        fileDropdown.setPromptText("Select a file");
        fileDropdown.setDisable(true);

        HBox fileDropdownBox = new HBox(10, fileDropdown);
        fileDropdownBox.setPadding(new Insets(0, 0, 0, 240));

        Label dropBoxDescription = new Label("Choose a file from pwd to add or commit. To push, use the cmd with the command git push <specifier> <branch_name>");
        dropBoxDescription.setStyle("-fx-font-style: italic;");

        HBox buttonBox = new HBox(10, addButton, tick_add, commitButton, tick_commit);
        buttonBox.setPadding(new Insets(20));
        buttonBox.setAlignment(Pos.CENTER);

        Label fetchLabel = new Label("Fetch existing code from the local repository");
        fetchLabel.setStyle("-fx-font-weight: bold;");

        HBox fetchBox = new HBox(10, fetchButton, tick_fetch);
        fetchBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(titleLabel, headerBox, githubActionLabel, connectBox, dropBoxDescription, fileDropdownBox, instructionsLabel, buttonBox, fetchLabel, fetchBox);
        root.setSpacing(10);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Version Control App");
        primaryStage.show();
    }

    private void setupEventHandlers() {
        initRepoButton.setOnAction(event -> handleInitRepoButtonClick());
        connectButton.setOnAction(event -> handleConnectButtonClick());
        addButton.setOnAction(event -> handleAddButtonClick());
        commitButton.setOnAction(event -> handleCommitButtonClick());
        fetchButton.setOnAction(event -> handleFetchButtonClick());

        fileDropdown.setOnAction(e -> handleFileDropdownSelection());
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        return button;
    }

    private ImageView createTickImage() {
        try (InputStream inputStream = getClass().getResourceAsStream("/com/cse360/assets/tick.png")) {
            if (inputStream != null) {
                Image tickImage = new Image(inputStream);
                ImageView tick = new ImageView(tickImage);
                tick.setFitHeight(25);
                tick.setFitWidth(25);
                tick.setVisible(false);
                return tick;
            } else {
                throw new FileNotFoundException("Tick image not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



    private List<String> getFilesInCurrentDirectory() {
        List<String> files = new ArrayList<>();
        File[] fileList = new File(".").listFiles();
        if (fileList != null) {
            for (File file : fileList) {
                if (file.isFile()) {
                    files.add(file.getName());
                }
            }
        }
        return files;
    }

    // WE USE ASYNCHORONOUS JAVA TO RUN ALL GIT COMMANDS IN REALTIME

    private void executeGitCommandAsync(String command, String successMessage) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                try {
                    ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", command);
                    processBuilder.redirectErrorStream(true);

                    Process process = processBuilder.start();

                    process.waitFor();

                    java.util.Scanner s = new java.util.Scanner(process.getInputStream()).useDelimiter("\\A");
                    String output = s.hasNext() ? s.next() : "";
                    System.out.println(output);

                    if (successMessage != null && !successMessage.isEmpty()) {
                        logAction(successMessage);
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        new Thread(task).start();
    }

    private void logAction(String action) {
        System.out.println("Action: " + action);
        updateLogFile(action);
    }

    private void updateLogFile(String action) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE_PATH, true))) {
            LocalDateTime now = LocalDateTime.now();
            String formattedDateTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            writer.write(formattedDateTime + " - " + action);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleInitRepoButtonClick() {

        fileDropdown.setVisible(true);
        executeGitCommandAsync("git init", "Repository initialized");
        executeGitCommandAsync("git config --global user.name YourUserName", null);
        executeGitCommandAsync("git config --global user.email your.email@example.com", null);
        tick_init.setVisible(true);

        connectButton.setDisable(false);
        fileDropdown.setDisable(false);
    }

    private void handleConnectButtonClick() {

        fileDropdown.setVisible(!fileDropdown.isVisible()? true: false);

        String input = getUserInput("Enter your repository link to remotely connect to GitHub");

        if (input != null && !input.isEmpty()) {
            executeGitCommandAsync("git remote add origin " + input, "Connected to repository");
            tick_connect.setVisible(true);

            addButton.setDisable(false);
            commitButton.setDisable(false);
        }
    }

    private void handleAddButtonClick() {
        String selectedFile = fileDropdown.getValue();
        if (selectedFile != null && !selectedFile.isEmpty()) {
            executeGitCommandAsync("git add " + selectedFile, "File added: " + selectedFile);
            tick_add.setVisible(true);
            commitButton.setDisable(false);
        }
    }

    private void handleCommitButtonClick() {
        executeGitCommandAsync("git commit -m \"Committed\"", "Committed changes");
        tick_commit.setVisible(true);
    }

    private void handleFetchButtonClick() {
        executeGitCommandAsync("git fetch origin", "Fetched changes from origin");
        tick_fetch.setVisible(true);
    }

    private void handleFileDropdownSelection() {
        if (fileDropdown.getValue() != null && !fileDropdown.getValue().isEmpty()) {
            addButton.setDisable(false);
        }
    }

    private String getUserInput(String prompt) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText(null);
        dialog.setContentText(prompt);

        return dialog.showAndWait().orElse(null);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
