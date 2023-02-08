package htw;

/**
 * Minimal representation of a single location in a grid maze.
 */
public interface Cell {

  int getX();

  int getY();

  boolean sameLocation(Cell c);

}
