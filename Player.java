// Player class - manages the player objects created by a new game

import javafx.scene.control.Label;

public class Player
{
  // create fields for player objects
  private int rollNumber, roundScore, totalScore, playerNumber;
  private Label playerText, scoreText;
  
  // construct that accepts an integer to create a player with
  public Player(int i)
  {
    totalScore = 0;
    rollNumber = 0;
    roundScore = 0;
    playerNumber = i;
    playerText = new Label("Player " + playerNumber);
    playerText.getStyleClass().add("player-score");
    scoreText = new Label(Integer.toString(totalScore));
    scoreText.getStyleClass().add("player-score");
  }
  
  // method to return the current playerNumber for a player
  public int getPlayerNumber()
  {
    return playerNumber;
  }
  
  // method to return the current playerText label for a player
  public Label getPlayerText()
  {
    return playerText;
  }
  
  // method to return the current rollNumber for a player
  public int getRollNumber()
  {
    return rollNumber;
  }
  
  // method to return the current roundScore for a player
  public int getRoundScore()
  {
    return roundScore;
  }
  
  // method to return the current scoreText for a player
  public Label getScoreText()
  {
    return scoreText;
  }
  
  // method to return the current totalScore for a player
  public int getTotalScore()
  {
    return totalScore;
  }
  
  // method used to highlight the currently selected player
  public void highlight()
  {
    playerText.setStyle("-fx-font-weight: bold; -fx-text-fill: red");
    scoreText.setStyle("-fx-font-weight: bold; -fx-text-fill: red");
  }
  
  // method to highlight the winner of a game
  public void highlightWinner()
  {
    playerText.setStyle("-fx-font-weight: bold; -fx-text-fill: white");
    scoreText.setStyle("-fx-font-weight: bold; -fx-text-fill: white");
  }
  
  // method which removes all styling that was previously added to a player
  public void resetStyles()
  {
    playerText.setStyle("");
    scoreText.setStyle("");
  }
  
  // method to roll all the dice at one time
  public void rollAll(Dice[] dice)
  {
    for (int i = 0; i < dice.length; i++)
      if (!dice[i].getSelected())
        dice[i].roll();

    rollNumber++;
  }
  
  // method to set the rollNumber field for a player
  public void setRollNumber(int s)
  {
    rollNumber = s;
  }
  
  // method to set the roundScore field for a player
  public void setRoundScore(int s, Label roundScoreText)
  {
    roundScore = s;
    roundScoreText.setText("Current Round Score: " + Integer.toString(s));
  }
  
  // method to set the totalScore field for a player
  public void setTotalScore(int s)
  {
    totalScore = s;
    scoreText.setText(Integer.toString(s));
  }
}