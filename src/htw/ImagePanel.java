package htw;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

/**
 * Modified Image for overlaying multiple images.
 */
public class ImagePanel extends JPanel implements IImagePanel {
  private List<Image> images = new ArrayList<>();

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    for (Image image : images) {
      g.drawImage(image, 0, 0, this);
    }
  }

  @Override
  public void clearImages() {
    images.clear();
    repaint();
  }

  @Override
  public void removeImage(Image image) {
    images.remove(image);
    repaint();
  }

  @Override
  public void addImage(Image image) {
    images.add(image);
    repaint();
  }
}
