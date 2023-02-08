package htw;

import java.awt.Image;

/**
 * Interface for ImagePanel.
 */
public interface IImagePanel {

  /**
   * Remove all images.
   */
  void clearImages();

  /**
   * Remove a given image.
   *
   * @param image image
   */
  void removeImage(Image image);

  /**
   * Add an image.
   *
   * @param image image
   */
  void addImage(Image image);
}
