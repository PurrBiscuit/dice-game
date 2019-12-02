// PlayGame class - the driver class that initializes a new game to play

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PlayGame extends Application
{
  // create fields that will be used throughout the dice game app
  private final double SCENE_WIDTH = 1400.0;
  private final double SCENE_HEIGHT = 900.0;
  private DiceGame game;
  private Dice[] dice;
  private Player[] players;
  private Label currentScoreText, rollText, roundText;
  private Button rollButton, keepScoreButton, restartButton, exitButton;
  private int currentPlayerIndex;
 
  public static void main(String[] args)
  {
    launch(args);
  }
 
  // override the start method that the Application class requires
  @Override
  public void start(Stage primaryStage)
  {
    // stage for user input before starting game
    inputStage();

    // grab the players and dice arrays that were created
    // when the game was initialized during the input stage
    dice = game.getDice();
    players = game.getPlayers();
    
    // add the player text labels to a gridpane for uniform layout
    GridPane playerGrid = new GridPane();
    for (int i = 0; i < players.length; i++)
    {
      playerGrid.add(players[i].getPlayerText(), i, 0);
      GridPane.setHalignment(players[i].getPlayerText(), HPos.CENTER);
      playerGrid.add(players[i].getScoreText(), i, 1);
      GridPane.setHalignment(players[i].getScoreText(), HPos.CENTER);
    }

    playerGrid.setHgap(200);
    playerGrid.setVgap(10);
    playerGrid.setAlignment(Pos.CENTER);
    playerGrid.setPadding(new Insets(30, 0, 20, 0));
    
    // display the current round and roll number in a VBox
    roundText = new Label("Round " + game.getCurrentRound());
    roundText.getStyleClass().add("round-roll");
    rollText = new Label();
    rollText.getStyleClass().add("round-roll");
    
    VBox roundRollVBox = new VBox(roundText, rollText);
    roundRollVBox.setAlignment(Pos.CENTER);
    roundRollVBox.setPadding(new Insets(0, 0, 10, 0));
    
    // Create the dice objects and the hbox to hold the dice images
    HBox diceHBox = new HBox(60);
    diceHBox.setAlignment(Pos.CENTER);
    diceHBox.setPadding(new Insets(0, 0, 30, 0));
    
    for (int i = 0; i < dice.length; i++)
    {
      diceHBox.getChildren().add(dice[i].getDiceView());
    }
    
    // display the current round score for a player in an HBox
    currentPlayerIndex = game.getCurrentPlayer() - 1;
    currentScoreText = new Label("Current Round Score: " + players[currentPlayerIndex].getRoundScore());
    currentScoreText.setId("current-score");
    HBox currentScoreHBox = new HBox(currentScoreText);
    currentScoreHBox.setAlignment(Pos.CENTER);
    
    // Add the button to roll the dice
    rollButton = new Button("Roll");
    rollButton.getStyleClass().add("blue-button");
    rollButton.setOnAction(new RollButtonHandler());
    
    // Add the button to keep the score from the last roll
    keepScoreButton = new Button("Keep Score");
    keepScoreButton.setId("keep-score");
    keepScoreButton.setOnAction(new KeepScoreButtonHandler());
  
    // Create HBox to put roll and keep score buttons in
    HBox buttonHBox1 = new HBox(50, rollButton, keepScoreButton);
    buttonHBox1.setAlignment(Pos.CENTER);
    
    // Add the button to restart game
    restartButton = new Button("Restart");

    // register the restart game event handler
    restartButton.setOnAction(event -> {
      game.restart(players, dice, currentScoreText, roundText, rollText);
      
      rollButton.setVisible(true);
      keepScoreButton.setVisible(true);
    });
    
    // Add the button to exit the program
    exitButton = new Button("Exit");
    
    // register the exit game event handler
    exitButton.setOnAction( event -> {
      primaryStage.close();
    });
    
    // Create HBox to put restart and exit buttons in
    HBox buttonHBox2 = new HBox(50, restartButton, exitButton);
    buttonHBox2.setAlignment(Pos.CENTER);
    
    // Add all the nodes created above to a root VBox node
    VBox boardVBox = new VBox(20, playerGrid, roundRollVBox, diceHBox, currentScoreHBox, buttonHBox1, buttonHBox2);
    boardVBox.getStyleClass().add("board");
    
    // Create a scene object with the VBox as the root node
    Scene scene = new Scene(boardVBox, SCENE_WIDTH, SCENE_HEIGHT);
  
    // add css styling to scene
    scene.getStylesheets().add("assets/game.css");

    // highlight the first player before game starts
    players[currentPlayerIndex].getPlayerText().setStyle("-fx-font-weight: bold; -fx-text-fill: red");
    players[currentPlayerIndex].getScoreText().setStyle("-fx-font-weight: bold; -fx-text-fill: red");
    
    // set the scene and show it on the primary stage
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  // method to create an initial stage which a user can pick the values for their game
  private void inputStage()
  {
    // create sliders to capture user input for player count
    Slider playersSlider = new Slider(1.0, 4.0, 2.0);
    playersSlider.setMinorTickCount(0);
    playersSlider.setShowTickLabels(true);
    playersSlider.setShowTickMarks(true);
    playersSlider.setSnapToTicks(true);
    playersSlider.setMajorTickUnit(1.0);
    playersSlider.setMaxWidth(500.0);

    // create the label for player number input
    Label playersLabel = new Label("Players: ");

    // create hbox to put the player number label and slider into
    HBox playersHBox = new HBox(20, playersLabel, playersSlider);
    playersHBox.setAlignment(Pos.CENTER);
    playersHBox.setHgrow(playersSlider, Priority.ALWAYS);

    // create sliders to capture user input for round count
    Slider roundsSlider = new Slider(1.0, 6.0, 3.0);
    roundsSlider.setMinorTickCount(0);
    roundsSlider.setShowTickLabels(true);
    roundsSlider.setShowTickMarks(true);
    roundsSlider.setSnapToTicks(true);
    roundsSlider.setMajorTickUnit(1.0);
    roundsSlider.setMaxWidth(500.0);

    // create the label for round number input
    Label roundsLabel = new Label("Rounds: ");

    // create hbox to put the round number label and slider into
    HBox roundsHBox = new HBox(20, roundsLabel, roundsSlider);
    roundsHBox.setAlignment(Pos.CENTER);
    roundsHBox.setHgrow(roundsSlider, Priority.ALWAYS);

    // create sliders to capture user input for dice count
    Slider diceSlider = new Slider(1.0, 5.0, 3.0);
    diceSlider.setMinorTickCount(0);
    diceSlider.setShowTickLabels(true);
    diceSlider.setShowTickMarks(true);
    diceSlider.setSnapToTicks(true);
    diceSlider.setMajorTickUnit(1.0);
    diceSlider.setMaxWidth(500.0);

    // create the label for dice number input
    Label diceLabel = new Label("Dice: ");

    // create hbox to put the dice number label and slider into
    HBox diceHBox = new HBox(20, diceLabel, diceSlider);
    diceHBox.setAlignment(Pos.CENTER);
    diceHBox.setHgrow(diceSlider, Priority.ALWAYS);

    // create button to start the game
    Button startGameButton = new Button("Start Game");
    startGameButton.getStyleClass().add("blue-button");

    // add all the nodes created above to a root VBox node
    VBox inputVBox = new VBox(40, playersHBox, roundsHBox, diceHBox, startGameButton);
    inputVBox.getStyleClass().add("board");
    inputVBox.setAlignment(Pos.CENTER);

    // add the root VBox node to the input scene
    Scene inputScene = new Scene(inputVBox, SCENE_WIDTH, SCENE_HEIGHT);

    // add css styling to scene
    inputScene.getStylesheets().add("assets/game.css");

    // create a stage for the input stage
    Stage inputStage = new Stage();

    // add the scene to the input stage
    inputStage.setScene(inputScene);

    // register the start game event handler
    startGameButton.setOnAction( event -> {
      // get all the values from the sliders
      int playersCount = (int)playersSlider.getValue();
      int roundsCount = (int)roundsSlider.getValue();
      int diceCount = (int)diceSlider.getValue();

      // create a new game object with the values grabbed from the sliders
      game = new DiceGame(roundsCount, playersCount, diceCount);

      // close the input stage and proceed to the primary stage
      inputStage.close();
    });

    // register an event handler to exit out of the app altogether
    // if the user closes out the input stage window before starting game
    inputStage.setOnCloseRequest( event ->
    {
      System.exit(0);
    });

    // use the showAndWait method here to halt the app at this stage
    // and wait for user input before proceeding to the primary stage
    inputStage.showAndWait();
  }

  // inner class to handle the keep score button events
  class KeepScoreButtonHandler implements EventHandler<ActionEvent>
  {
    @Override
    public void handle(ActionEvent event)
    {
      currentPlayerIndex = game.getCurrentPlayer() - 1;
      
      // add the score to the current player's total and display it
      int currentScore = players[currentPlayerIndex].getTotalScore();
      int roundScore = players[currentPlayerIndex].getRoundScore();
      players[currentPlayerIndex].setTotalScore(currentScore + roundScore);
      
      players[currentPlayerIndex].resetStyles();
      
      // move on to the next player if we can
      if (game.nextTurn())
      {
        // set the current score to 0 again
        players[currentPlayerIndex].setRoundScore(0, currentScoreText);
        
        // update the roll counter and clear roll number text
        Dice.resetDice(dice);
        players[currentPlayerIndex].setRollNumber(0);
        rollText.setText("");
        
        // make roll button visible again
        rollButton.setVisible(true);
        
        currentPlayerIndex = game.getCurrentPlayer() - 1;
        roundText.setText("Round " + game.getCurrentRound());
        players[currentPlayerIndex].highlight();
      } else {
        // end the game if there are no more turns left
        game.over(players, rollButton, keepScoreButton, roundText, rollText, currentScoreText);
      }
    }
  }
  
  // inner class to handle the roll button events
  class RollButtonHandler implements EventHandler<ActionEvent>
  {
    @Override
    public void handle(ActionEvent event)
    {
      currentPlayerIndex = game.getCurrentPlayer() - 1;
      
      // roll all of the dice
      players[currentPlayerIndex].rollAll(dice);
      
      // calculate the score from the roll and update the current player field
      int roundScore = Dice.calculateScore(dice, dice.length);
      players[currentPlayerIndex].setRoundScore(roundScore, currentScoreText);
      
      // display the current roll number
      rollText.setText("Roll " + players[currentPlayerIndex].getRollNumber());
      
      // hide the roll button if we've reached the last roll for a user
      if (players[currentPlayerIndex].getRollNumber() >= 3)
        rollButton.setVisible(false);
    }
  }
}
