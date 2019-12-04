// Dice class - manages the dice objects created by a new game

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;
import javafx.animation.RotateTransition;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class Dice implements Rollable
{
  // create fields for dice objects
  private int faceValue, rollNumber;
  private Image diceImage;
  private ImageView diceView;
  private boolean selected = false;

  // no arg constructor to create a dice object with default values
  public Dice()
  {
    faceValue = 1;
    rollNumber = 0;
    diceView = new ImageView();
    setDiceImage(1, selected);

    // add an event handler for mouse clicks to the diceView image
    // that allow a die to be selected to not be rolled again
    diceView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent event) {
        toggleSelected();
      }
    });
  }
  
  // recursive method to add the dice up and produce a score
  public static int calculateScore(Dice[] dice, int end)
  {
    if (end <= 0)
      return 0;
    else
      return (calculateScore(dice, end - 1) + dice[end - 1].faceValue);
  }
  
  // method which sets all the dice values back to 0
  // and set the image to the 1 value facing up again
  public static void resetDice(Dice[] dice)
  {
    for (int i = 0; i < dice.length; i++)
    {
      dice[i].selected = false;
      dice[i].rollNumber = 0;
      dice[i].faceValue = 1;
      dice[i].setDiceImage(dice[i].faceValue, dice[i].selected);
    }
  }
  
  // method to return the current diceView for a die
  public ImageView getDiceView()
  {
    return diceView;
  }
  
  // method to return the current faceValue for a die
  public int getFaceValue()
  {
    return faceValue;
  }
  
  // method to return the current rollNumber for a die
  public int getRollNumber()
  {
    return rollNumber;
  }
  
  // method to return the current selection status of a die
  public boolean getSelected()
  {
    return selected;
  }
  
  // override the roll method from the Rollable interface
  @Override
  public void roll()
  {
    // only roll the die if it isn't selected to be held
    if (!selected)
    {
      // adds a spinning effect to the dice to simulate dice roll
      RotateTransition diceTrans = new RotateTransition(new Duration(1000), diceView);
      diceTrans.setFromAngle(0.0);
      diceTrans.setToAngle(360.0);
      diceTrans.play();

      // sets the faceValue field to a random number between 1 and 6
      faceValue = new Random().nextInt(6) + 1;
      setDiceImage(faceValue, selected);

      // increment the rollNumber field for the die
      rollNumber++;
    }
  }

  private void setDiceImage(int f, boolean s)
  {
    try {
      Image diceImage;

      if (s)
        diceImage = new Image(new FileInputStream("assets/die_" + f + "_selected.png"));
      else
        diceImage = new Image(new FileInputStream("assets/die_" + f + ".png"));

      diceView.setImage(diceImage);
    } catch (FileNotFoundException err) {
      System.out.println("ERROR: " + err.getMessage());
    }
  }

  // method to toggle whether a die is selected or not
  private void toggleSelected()
  {
    // toggle the selected field based on mouse clicks to true and false
    selected = selected ? false : true;

    setDiceImage(faceValue, selected);
  }
}