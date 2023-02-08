package htw;

/**
 * Player class keeps track of player's location and gold count.
 */
public class Player implements PlayerI {

  private int x;
  private int y;
  private int arrows;
  private String name;
  private Boolean alive;

  /**
   * Player has a location, number of arrows, and ability to shoot.
   *
   * @param x      x
   * @param y      y
   * @param arrows number of arrows
   */
  public Player(int x, int y, int arrows, String name) {
    this.x = x;
    this.y = y;
    this.arrows = arrows;
    this.name = name;
    alive = true;
  }

  @Override
  public void shootArrow() {
    arrows--;
  }

  @Override
  public int getArrows() {
    return arrows;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean isAlive() {
    return alive;
  }

  @Override
  public void setDead() {
    alive = false;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public void setX(int x) {
    this.x = x;
  }

  public void setY(int y) {
    this.y = y;
  }

  public void decX() {
    x--;
  }

  public void incX() {
    x++;
  }

  public void decY() {
    y--;
  }

  public void incY() {
    y++;
  }

  @Override
  public boolean sameLocation(Cell c) {
    return (this.x == c.getX() && this.y == c.getY());
  }
}
