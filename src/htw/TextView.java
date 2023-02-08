package htw;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Container for function for rendering a grid of maze cells as ASCII art.
 */
public class TextView {

  public static final char PLAYER = '@';
  public static final char WUMPUS = 'W';
  public static final char BAT = 'B';
  public static final int CELL_LEFT_PADDING = 4;
  public static final int MIN_CELL_WIDTH = 4; // best to leave this at 4 or larger

  /**
   * Renders a grid of maze cells as ASCII art. The grid is represented as an array of array of
   * Cell. Uses Cell equals method to determine player and goal location within the grid.
   *
   * @param grid   the grid to render
   * @param player a cell representing player's current location, can be null if there is no player
   * @return the ASCII art representation of the maze
   */
  public static String render(RoomI[][] grid, Cell player) {
    if (grid == null || grid.length == 0 || grid[0].length == 0) {
      throw new IllegalArgumentException("Invalid grid");
    }
    int maxCellWidth = MIN_CELL_WIDTH;
    for (Cell[] row : grid) {
      for (Cell c : row) {
        maxCellWidth = Integer.max(maxCellWidth, c.toString().length());
      }
    }
    int numRows = grid.length;
    int numCols = grid[0].length;
    StringBuilder sb = new StringBuilder();
    int cellWidth = maxCellWidth + CELL_LEFT_PADDING;
    // gap for opening in horizontal wall is 4 for even width, 3 for odd width
    int midLen = 4 - cellWidth % 2;
    int sideLen = (cellWidth - midLen) / 2;
    for (RoomI[] cells : grid) {
      sb.append("+");
      sb.append(Arrays.stream(cells).map(c ->
              "-".repeat(sideLen)
                      + (c.hasNorth() ? " " : "-").repeat(midLen)
                      + "-".repeat(sideLen)
      ).collect(Collectors.joining("+")));
      sb.append("+\n");

      for (int c = 0; c < cells.length; c++) {
        RoomI cell = cells[c];
        sb.append(cell.hasWest() ? " " : "|").append(" ".repeat(CELL_LEFT_PADDING));
        String paddedCellString = " ".repeat(maxCellWidth).substring(cell.toString().length())
                + cell.toString();
        StringBuilder cellTemp = new StringBuilder(paddedCellString);
        if (cell.isTunnel()) {
          cellTemp.setCharAt(3, 'T');
        }
        if (cell.hasPit()) {
          cellTemp.setCharAt(3, 'P');
        }
        if (cell.hasBat()) {
          cellTemp.setCharAt(2, BAT);
        }
        if (cell.hasWumpus()) {
          cellTemp.setCharAt(1, WUMPUS);
        }
        if (cell.sameLocation(player)) {
          cellTemp.setCharAt(0, PLAYER);
        }
        sb.append(cellTemp);
        if (c == numCols - 1 && !cell.hasEast()) {
          sb.append("|");
        }
      }
      sb.append("\n");
    }
    sb.append("+");
    sb.append(Arrays.stream(grid[numRows - 1]).map(c ->
            "-".repeat(sideLen)
                    + (c.hasSouth() ? " " : "-").repeat(midLen)
                    + "-".repeat(sideLen)
    ).collect(Collectors.joining("+"))).append("+");
    return sb.toString();
  }
}