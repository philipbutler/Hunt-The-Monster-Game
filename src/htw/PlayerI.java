package htw;

/**
 * Interface for Player.
 */
public interface PlayerI extends Cell {
  void shootArrow();

  int getArrows();

  String getName();

  boolean isAlive();

  void setDead();

  void setX(int x);

  void setY(int y);

  void decX();

  void incX();

  void decY();

  void incY();
}
