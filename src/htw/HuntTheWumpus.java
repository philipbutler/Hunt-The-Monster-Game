package htw;

/**
 * Driver class for Hunt The Wumpus.
 */
public class HuntTheWumpus {

  /**
   * Main function creates MVC and kicks off game.
   *
   * @param args CL args
   */
  public static void main(String[] args) {
    if (args[0].equals("--text")) {
      Model m = new Model(1, Integer.parseInt(args[1]), Integer.parseInt(args[2]),
              Boolean.parseBoolean(args[3]), Boolean.parseBoolean(args[4]),
              Integer.parseInt(args[5]), Integer.parseInt(args[6]), Integer.parseInt(args[7]),
              Integer.parseInt(args[8]), 0);
      Controller c = new Controller(m);
      c.play();
    }
    else {
      GUIController c = new GUIController();
      IView v = new JFrameView(c);
      c.setView(v);
    }
  }
}
