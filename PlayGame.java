import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PlayGame extends Application
{
  Game game = new Game();
  Dice[] dice = game.getDice();
  Player[] players = game.getPlayers();
  Label currentScoreText, rollText, roundText;
  Button rollButton, keepScoreButton, restartButton, exitButton;
  int currentPlayerIndex;
 
  public static void main(String[] args)
  {
    launch(args);
  }
 
  @Override
  public void start(Stage primaryStage)
  {
    // Constants for the scene size
    final double SCENE_WIDTH = 1400.0;
    final double SCENE_HEIGHT = 900.0;
    
    // create players and add them to grid pane
    // with scores initialized to 0
    GridPane playerGrid = new GridPane();
    for (int i = 0; i < players.length; i++)
    {
      players[i] = new Player(i + 1);
      
      playerGrid.add(players[i].getPlayerText(), i, 0);
      GridPane.setHalignment(players[i].getPlayerText(), HPos.CENTER);
      playerGrid.add(players[i].getScoreText(), i, 1);
      GridPane.setHalignment(players[i].getScoreText(), HPos.CENTER);
    }

    playerGrid.setHgap(200);
    playerGrid.setVgap(10);
    playerGrid.setAlignment(Pos.CENTER);
    playerGrid.setPadding(new Insets(30, 0, 20, 0));

    currentPlayerIndex = game.getCurrentPlayer() - 1;
    
    // display the round and roll number in VBox
    roundText = new Label("Round " + game.getCurrentRound());
    roundText.getStyleClass().add("round-roll");
    rollText = new Label();
    rollText.getStyleClass().add("round-roll");
    
    VBox roundRollVBox = new VBox(roundText, rollText);
    roundRollVBox.setAlignment(Pos.CENTER);
    roundRollVBox.setPadding(new Insets(0, 0, 10, 0));
    
    // Create the dice objects and
    // the hbox to hold the dice images
    HBox diceHBox = new HBox(60);
    diceHBox.setAlignment(Pos.CENTER);
    diceHBox.setPadding(new Insets(0, 0, 30, 0));
    
    for (int i = 0; i < dice.length; i++)
    {
      dice[i] = new Dice();
      diceHBox.getChildren().add(dice[i].getDiceView());
    }
    
    // display the round and roll number in VBox
    currentScoreText = new Label("Current Round Score: " + players[currentPlayerIndex].getRoundScore());
    currentScoreText.setId("current-score");
    HBox currentScoreHBox = new HBox(currentScoreText);
    currentScoreHBox.setAlignment(Pos.CENTER);
    
    // Add the button to roll
    rollButton = new Button("Roll");
    rollButton.setId("roll");
    rollButton.setOnAction(new RollButtonHandler());
    
    // Add the button to keep score
    keepScoreButton = new Button("Keep Score");
    keepScoreButton.setId("keep-score");
    keepScoreButton.setOnAction(new KeepScoreButtonHandler());
  
    // Create HBox to put roll and keep score buttons in
    HBox buttonHBox1 = new HBox(50, rollButton, keepScoreButton);
    buttonHBox1.setAlignment(Pos.CENTER);
    
    // Add the button to restart game
    restartButton = new Button("Restart");
    restartButton.setOnAction(event -> {
      game.restart(players, dice, currentScoreText, roundText, rollText);
      
      rollButton.setVisible(true);
      keepScoreButton.setVisible(true);
    });
    
    // Add the button to exit the program
    exitButton = new Button("Exit");
    
    exitButton.setOnAction( event -> {
      primaryStage.close();
    });
    
    // Create HBox to put roll and keep score buttons in
    HBox buttonHBox2 = new HBox(50, restartButton, exitButton);
    buttonHBox2.setAlignment(Pos.CENTER);
    
    // Add the nodes to a GridPane
    VBox boardVBox = new VBox(20, playerGrid, roundRollVBox, diceHBox, currentScoreHBox, buttonHBox1, buttonHBox2);
    boardVBox.setId("board");
    
    // temp stage to get user input??
//     Scene sceneTemp = new Scene(new Label("Hello"), 100, 100);
//     Stage stageTemp = new Stage();
//     stageTemp.setScene(sceneTemp);
//     stageTemp.showAndWait();
    
    // Create a Scene with the Pane as the root node
    Scene scene = new Scene(boardVBox, SCENE_WIDTH, SCENE_HEIGHT);
  
    // add css styling to scene
    scene.getStylesheets().add("assets/game.css");
    players[currentPlayerIndex].getPlayerText().setStyle("-fx-font-weight: bold; -fx-text-fill: red");
    players[currentPlayerIndex].getScoreText().setStyle("-fx-font-weight: bold; -fx-text-fill: red");
    
    primaryStage.setScene(scene);
    primaryStage.show();
  }

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
        rollText.setText("");
        
        // make roll button visible again
        rollButton.setVisible(true);
        
        currentPlayerIndex = game.getCurrentPlayer() - 1;
        roundText.setText("Round " + game.getCurrentRound());
        players[currentPlayerIndex].highlight();
      } else {
        game.over(players, rollButton, keepScoreButton, roundText, rollText, currentScoreText);
      }
    }
  }
  
  class RollButtonHandler implements EventHandler<ActionEvent>
  {
    @Override
    public void handle(ActionEvent event)
    {
      currentPlayerIndex = game.getCurrentPlayer() - 1;
      
      // roll the dice
      Dice.rollAll(dice);
      
      // calculate the score from the roll and update the current player field
      int roundScore = Dice.calculateScore(dice, dice.length);
      players[currentPlayerIndex].setRoundScore(roundScore, currentScoreText);
      
      rollText.setText("Roll " + dice[0].getRollNumber());
      
      if (dice[0].getRollNumber() >= 3)
        rollButton.setVisible(false);
    }
  }
}
