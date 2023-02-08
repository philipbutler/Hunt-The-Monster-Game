package htw;

/**
 * Features the controller offers to the view.
 */
public interface Features {

  /**
   * Provides view to controller and controller to view.
   *
   * @param v view
   */
  void setView(IView v);

  /**
   * Generates the model.
   *
   * @param numPlayers num players
   * @param rows num rows
   * @param cols num cols
   * @param wrapping wrapping
   * @param perfect perfect
   * @param wallsRemaining num walls remaining
   * @param percentBats percent bats
   * @param percentPits percent pits
   * @param numArrows num arrows
   * @param newMaze new maze
   */
  void generateModel(int numPlayers, int rows, int cols, boolean wrapping, boolean perfect,
                     int wallsRemaining, int percentBats, int percentPits, int numArrows,
                     boolean newMaze);

  /**
   * Move player or shoot north.
   *
   * @param p player
   */
  void moveNorth(JFrameView.PlayerMoving p);

  /**
   * Move player or shoot west.
   *
   * @param p player
   */
  void moveWest(JFrameView.PlayerMoving p);

  /**
   * Move player or shoot south.
   *
   * @param p player
   */
  void moveSouth(JFrameView.PlayerMoving p);

  /**
   * Move player or shoot east.
   *
   * @param p player
   */
  void moveEast(JFrameView.PlayerMoving p);

  /**
   * Execute shot north.
   *
   * @param p player
   */
  void shootArrowNorth(JFrameView.PlayerMoving p);

  /**
   * Execute shot south.
   *
   * @param p player
   */
  void shootArrowSouth(JFrameView.PlayerMoving p);

  /**
   * Execute shot east.
   *
   * @param p player
   */
  void shootArrowEast(JFrameView.PlayerMoving p);

  /**
   * Execute shot west.
   *
   * @param p player
   */
  void shootArrowWest(JFrameView.PlayerMoving p);

  /**
   * Process click move.
   *
   * @param finalI x location
   * @param finalJ y location
   */
  void processCellClick(int finalI, int finalJ);
}
