// DiceGame class - manages the dice game object created by the PlayGame driver class

import java.util.ArrayList;
import java.util.Scanner;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class DiceGame extends Game
{
  // create fields for game objects
  private int maxRounds;
  private int currentPlayer = 1;
  private int currentRound = 1;
  private Dice[] dice;
  
  // no arg constructor to create a new dice game with default values
  public DiceGame()
  {
    maxRounds = 3;
    dice = new Dice[5];

    for (int i = 0; i < dice.length; i++)
      dice[i] = new Dice();
  }
  
  // constructor to create a new dice game with custom values passed in
  public DiceGame(int r, int p, int d)
  {
    super(p);
    dice = new Dice[d];

    for (int i = 0; i < dice.length; i++)
      dice[i] = new Dice();
  }
  
  // method to return the currentPlayer for a game
  public int getCurrentPlayer()
  {
    return currentPlayer;
  }
  
  // method to return the currentRound for a game
  public int getCurrentRound()
  {
    return currentRound;
  }
  
  // method to return the dice objects used for a game
  public Dice[] getDice()
  {
    return dice;
  }
  
  // method to return the maxRounds for a game
  public int getMaxRounds()
  {
    return maxRounds;
  }
  
  // method which determines if there is another turn left in a game
  public boolean nextTurn()
  {
    // check if the current player is not the last player
    if (currentPlayer < getMaxPlayers())
    {
      // move to the next player if it is
      currentPlayer++;
      return true;
    }
    else if (currentRound < maxRounds)
    {
      // otherwise bump the round number since we've reached last player
      currentRound++;
      // start back at player 1
      currentPlayer = 1;
      
      return true;
    }
    
    // return false to indicate that there are no more turns left in the game
    return false;
  }
  
  // method to determine the result of a game (ie. show winner or ties)
  private static void getResult(Player[] players, Label rollText)
  {
    // check what the high score is
    int highScore = highScore(players);
    // find all players with this score
    ArrayList<Player> highScorePlayers = playersWithHighScore(players, highScore);
    
    // if only one player has the score then call the showWinner method
    // else call the showTie method to correctly display tie info
    if (highScorePlayers.size() == 1)
      showWinner(highScorePlayers.get(0), rollText);
    else
      showTie(highScorePlayers, rollText);

    rollText.setStyle("-fx-font-size: 40pt; -fx-text-fill: white");
  }
  
  // find the highscore for a game and return the result
  private static int highScore(Player[] players)
  {
    int highestScore = players[0].getTotalScore();
    
    for (int i = 1; i < players.length; i++)
    {
      if (players[i].getTotalScore() > highestScore)
        highestScore = players[i].getTotalScore();
    }
    
    return highestScore;
  }
  
  // return a list of all the players with the highscore for a game
  private static ArrayList<Player> playersWithHighScore(Player[] players, int highScore)
  {
    ArrayList<Player> highScoreList = new ArrayList<Player>();

    for (Player player : players)
    {
      if (player.getTotalScore() == highScore)
        highScoreList.add(player);
    }

    return highScoreList;
  }

  // method to end a game and call other methods to determine the result of the game
  public void over(Player[] players, Button roll, Button keep, 
                   Label roundText, Label rollText, Label roundScoreText)
  {    
    roll.setVisible(false);
    keep.setVisible(false);
    roundText.setText("GAME OVER");
    roundText.setStyle("-fx-font-size: 50pt; -fx-text-fill: white; -fx-underline: true");
    
    roundScoreText.setVisible(false);
    
    DiceGame.getResult(players, rollText);
  }
  
  // method used to restart a game by setting all the values back to
  // the original values that are set when a new game is created
  public void restart(Player[] players, Dice[] dice, Label roundScoreText,
                      Label roundText, Label rollText)
  {
    // reset current round and player to 1
    currentPlayer = 1;
    currentRound = 1;
    
    // reset all the players total scores to 0
    for (Player player : players)
    {
      player.resetStyles();
      player.setRoundScore(0, roundScoreText);
      player.setTotalScore(0);
      player.setRollNumber(0);
    }
    
    // call the static dice method to reset all dice
    Dice.resetDice(dice);
    
    roundScoreText.setVisible(true);
    players[currentPlayer - 1].highlight();
    roundText.setText("Round " + currentRound);
    roundText.setStyle("");
    rollText.setText("");
    rollText.setStyle("");
  }
  
  // logic used to correctly show the results of a tie after a game ends
  private static void showTie(ArrayList<Player> players, Label rollText)
  {
    // initialize empty string to concatonate values to below
    String playersString = "";
    
    for (int i = 0; i < players.size(); i++)
    {
      Player player = players.get(i);
      
      // if it's the last player in the list add "and" before to player number
      if ((i + 1) == players.size())
        playersString += (" and " + player.getPlayerNumber());
      // leave off the comma if it's the second to last player
      else if ((i + 1) == (players.size() - 1))
        playersString += (" " + player.getPlayerNumber());
      // add a comma to end of string concatonation otherwise after player number
      else
        playersString += (" " + player.getPlayerNumber() + ",");
        
      player.highlightWinner();
    }
    
    rollText.setText("Players " + playersString + " Tied!");
  }

  // logic used to correctly show the winner if no ties exists
  private static void showWinner(Player player, Label rollText)
  {
    rollText.setText("Player " + player.getPlayerNumber() + " Wins!");
    player.highlightWinner();
  }
}