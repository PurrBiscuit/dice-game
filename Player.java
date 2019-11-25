import javafx.scene.control.Label;

public class Player
{
  private int rollNumber, roundScore, totalScore;
  private Label playerText, scoreText;
  
  public Player(int i)
  {
    totalScore = 0;
    rollNumber = 0;
    roundScore = 0;
    playerText = new Label("Player " + i);
    playerText.getStyleClass().add("player-score");
    scoreText = new Label(Integer.toString(totalScore));
    scoreText.getStyleClass().add("player-score");
  }
  
  public Label getPlayerText()
  {
    return playerText;
  }
  
    public int getRollNumber()
  {
    return rollNumber;
  }
  
  public int getRoundScore()
  {
    return roundScore;
  }
  
  public Label getScoreText()
  {
    return scoreText;
  }
  
  public int getTotalScore()
  {
    return totalScore;
  }
  
  public void highlight()
  {
    playerText.setStyle("-fx-font-weight: bold; -fx-text-fill: red");
    scoreText.setStyle("-fx-font-weight: bold; -fx-text-fill: red");
  }
  
  public void resetStyles()
  {
    playerText.setStyle("");
    scoreText.setStyle("");
  }
  
  public void setRollNumber(int s)
  {
    rollNumber = s;
  }
  
  public void setRoundScore(int s, Label roundScoreText)
  {
    roundScore = s;
    roundScoreText.setText("Current Round Score: " + Integer.toString(s));
  }
  
  public void setTotalScore(int s)
  {
    totalScore = s;
    scoreText.setText(Integer.toString(s));
  }
}