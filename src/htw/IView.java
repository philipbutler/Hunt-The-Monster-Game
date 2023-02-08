package htw;

/**
 * Interface for view class.
 */
public interface IView {

  /**
   * Populates grid with images.
   *
   * @param icons images
   */
  void setCells(ImagePanel[][] icons);

  /**
   * Make cells clickable for movement.
   *
   * @param f feature controller
   */
  void setCellActions(Features f);

  /**
   * Set output to be displayed at each turn.
   *
   * @param msg output
   */
  void setOutput(String msg);

  /**
   * Set actionlisters.
   *
   * @param f feature controller
   */
  void setFeatures(Features f);

  /**
   * Displays big green output when wumpus is slain.
   */
  void win();

  /**
   * Displays sad red output when all players die.
   */
  void lose();

  /**
   * Clears the output from either game result.
   */
  void clearState();
}
