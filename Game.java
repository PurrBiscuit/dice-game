import java.util.Scanner;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class Game
{
  private final static int maxPlayers = 4;
  private final static int maxDice = 4;
  private static int maxRounds;
  private int currentPlayer = 1;
  private int currentRound = 1;
  private Player[] players;
  private Dice[] dice;
  
  public Game()
  {
    maxRounds = 3;
    players = new Player[4];
    dice = new Dice[5];
  }
  
  public Game(int r, int p, int d)
  {
    maxRounds = r;
    players = new Player[p];
    dice = new Dice[5];
  }
  
  public int getCurrentPlayer()
  {
    return currentPlayer;
  }
  
  public int getCurrentRound()
  {
    return currentRound;
  }
  
  public Dice[] getDice()
  {
    return dice;
  }
  
  public int getMaxRounds()
  {
    return maxRounds;
  }
  
  public Player[] getPlayers()
  {
    return players;
  }
  
  public boolean nextTurn()
  {
    if (currentPlayer < maxPlayers)
    {
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
    
    return false;
  }
  
  public static void getWinner(Player[] players, Label rollText)
  {
    int playerNumber = highScore(players);
    rollText.setText("Player " + playerNumber + " Wins!");
    rollText.setStyle("-fx-font-size: 40pt; -fx-text-fill: white");
    players[playerNumber - 1].getPlayerText().setStyle("-fx-font-weight: bold; -fx-text-fill: white");
    players[playerNumber - 1].getScoreText().setStyle("-fx-font-weight: bold; -fx-text-fill: white");
  }
  
  public static int highScore(Player[] players)
  {
    int highestScore = 0;
    
    for (int i = 1; i < players.length; i++)
    {
      if (players[i].getTotalScore() > players[highestScore].getTotalScore())
        highestScore = i;
    }
    
    return highestScore + 1;
  }
    
  public void over(Player[] players, Button roll, Button keep, 
                   Label roundText, Label rollText, Label roundScoreText)
  {    
    roll.setVisible(false);
    keep.setVisible(false);
    roundText.setText("GAME OVER");
    roundText.setStyle("-fx-font-size: 50pt; -fx-text-fill: white; -fx-underline: true");
    
    roundScoreText.setVisible(false);
    
    Game.getWinner(players, rollText);
  }
  
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
    
    Dice.resetDice(dice);
    
    roundScoreText.setVisible(true);
    players[currentPlayer - 1].highlight();
    roundText.setText("Round " + currentRound);
    roundText.setStyle("");
    rollText.setText("");
    rollText.setStyle("");
  }
}