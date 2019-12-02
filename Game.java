// Game class - used by the DiceGame class

public class Game
{
  // create fields for game objects
  private int maxPlayers;
  private Player[] players;

  // no arg constructor to create a new game with default values
  public Game()
  {
    maxPlayers = 4;
    players = new Player[maxPlayers];

    for (int i = 0; i < players.length; i++)
      players[i] = new Player(i + 1);
  }
  
  // constructor to create a new game with custom values passed in
  public Game(int p)
  {
    maxPlayers = p;
    players = new Player[p];

    for (int i = 0; i < players.length; i++)
      players[i] = new Player(i + 1);
  }

  // method to return the player objects used for a game
  public Player[] getPlayers()
  {
    return players;
  }

  // method to return the max number of players for a game
  public int getMaxPlayers()
  {
    return maxPlayers;
  }
}