package htw;

import java.util.Scanner;

/**
 * Driver class for the Maze that takes in CL args.
 */
public class Controller {

  Model m;
  IView v;

  /**
   * Controller constructor.
   *
   * @param m model
   */
  public Controller(Model m) {
    this.m = m;
  }

  /**
   * Play takes input from standard input, renders the view, and provides output for what happened
   * with each move. Once the game state is WIN or LOSE, game comes to an end.
   */
  public void play() {
    Scanner in = new Scanner(System.in);
    String line;
    String msg;
    while (m.getState() == IModel.State.IN_PROGRESS) {
      System.out.println(m.playerStatus(m.getPlayers().get(0)));
      //System.out.println(MazeView.render(m.getRooms(), m.getPlayer()));
      System.out.println("Enter move: ");
      line = in.nextLine();

      try {
        msg = m.inputMove(line);
      } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
        System.out.println(e.getMessage());
        continue;
      }
      System.out.println(msg);
    }
    System.out.println(m.getState() == IModel.State.WIN ? "YOU WIN!!! 😁🏆" : "YOU LOSE 😭⚰️");
  }

}
