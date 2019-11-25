import java.util.Random;
import javafx.animation.RotateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Dice implements Rollable
{
  private int faceValue, rollNumber;
  private Image diceImage;
  private ImageView diceView;
    
  public Dice()
  {
    faceValue = 0;
    rollNumber = 0;
    diceImage = new Image("file:assets/die_" + (faceValue + 1) + ".png");
    diceView = new ImageView(diceImage);
  }
  
  public static int calculateScore(Dice[] dice, int end)
  {
    if (end <= 0)
      return 0;
    else
      return (calculateScore(dice, end - 1) + dice[end - 1].faceValue);
  }
  
  public static void resetDice(Dice[] dice)
  {
    for (int i = 0; i < dice.length; i++)
    {
      dice[i].rollNumber = 0;
      dice[i].faceValue = 0;
      dice[i].diceImage = new Image("file:assets/die_" + (dice[i].faceValue + 1) + ".png");
      dice[i].diceView.setImage(dice[i].diceImage);
    }
    
    
  }

  public static void rollAll(Dice[] dice)
  {
    for (int i = 0; i < dice.length; i++)
      dice[i].roll();
  }
  
  public int getRollNumber()
  {
    return rollNumber;
  }
  
  public ImageView getDiceView()
  {
    return diceView;
  }
  
  public int getFaceValue()
  {
    return faceValue;
  }
  
  @Override
  public void roll()
  {
    RotateTransition diceTrans = new RotateTransition(new Duration(1000), diceView);
    diceTrans.setFromAngle(0.0);
    diceTrans.setToAngle(360.0);
    diceTrans.play();
    
    faceValue = new Random().nextInt(6) + 1;
    diceImage = new Image("file:assets/die_" + (faceValue) + ".png");
    diceView.setImage(diceImage);
    
    rollNumber++;
  }
}