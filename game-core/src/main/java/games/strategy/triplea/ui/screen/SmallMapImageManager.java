package games.strategy.triplea.ui.screen;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import games.strategy.engine.data.GameData;
import games.strategy.engine.data.Territory;
import games.strategy.triplea.ui.mapdata.MapData;
import games.strategy.triplea.ui.screen.drawable.LandTerritoryDrawable;
import games.strategy.ui.ImageScrollerSmallView;
import games.strategy.ui.Util;

public class SmallMapImageManager {
  private final ImageScrollerSmallView view;
  private Image offscreen;
  private final TileManager tileManager;

  public SmallMapImageManager(final ImageScrollerSmallView view, final BufferedImage offscreen,
      final TileManager tileManager) {
    this.view = view;
    this.offscreen = Util.copyImage(offscreen);
    this.tileManager = tileManager;
  }

  public void updateOffscreenImage(final BufferedImage offscreen) {
    this.offscreen.flush();
    this.offscreen = Util.copyImage(offscreen);
  }

  public void update(final MapData mapData) {
    final Graphics onScreenGraphics = view.getOffScreenImage().getGraphics();
    onScreenGraphics.drawImage(offscreen, 0, 0, null);
    for (final UnitsDrawer drawer : new ArrayList<>(tileManager.getUnitDrawables())) {
      final int x = (int) (drawer.getPlacementPoint().x * view.getRatioX());
      final int y = (int) (drawer.getPlacementPoint().y * view.getRatioY());
      onScreenGraphics.setColor(mapData.getPlayerColor(drawer.getPlayer()).darker());
      onScreenGraphics.fillRect(x, y, mapData.getSmallMapUnitSize(), mapData.getSmallMapUnitSize());
    }
    onScreenGraphics.dispose();
  }

  public void updateTerritoryOwner(final Territory t, final GameData data, final MapData mapData) {
    if (t.isWater()) {
      return;
    }
    final Rectangle bounds = new Rectangle(mapData.getBoundingRect(t.getName()));
    // create a large image for the territory
    final Image largeImage = Util.createImage(bounds.width, bounds.height, true);
    // make it transparent
    // http://www-106.ibm.com/developerworks/library/j-begjava/
    {
      final Graphics2D g = (Graphics2D) largeImage.getGraphics();
      g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
      g.setColor(new Color(0));
      g.fillRect(0, 0, bounds.width, bounds.height);
      g.dispose();
    }
    // draw the territory
    {
      final Graphics2D g = (Graphics2D) largeImage.getGraphics();
      g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
      g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
      g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
      final LandTerritoryDrawable drawable = new LandTerritoryDrawable(t.getName());
      drawable.draw(bounds, data, g, mapData, mapData.getSmallMapTerritorySaturation());
      g.dispose();
    }
    // scale it down
    int thumbWidth = (int) (bounds.width * view.getRatioX());
    int thumbHeight = (int) (bounds.height * view.getRatioY());
    // make the image a little bigger
    // the images wont overlap perfectly after being scaled, make them a little bigger to rebalance that
    thumbWidth += 3;
    thumbHeight += 3;
    final int thumbsX = (int) (bounds.x * view.getRatioX()) - 1;
    final int thumbsY = (int) (bounds.y * view.getRatioY()) - 1;
    // create the thumb image
    final Image thumbImage = Util.createImage(thumbWidth, thumbHeight, true);
    {
      final Graphics g = thumbImage.getGraphics();
      g.drawImage(largeImage, 0, 0, thumbImage.getWidth(null), thumbImage.getHeight(null), null);
      g.dispose();
    }
    {
      final Graphics g = offscreen.getGraphics();
      // draw it on our offscreen
      g.drawImage(thumbImage, thumbsX, thumbsY, thumbImage.getWidth(null), thumbImage.getHeight(null), null);
      g.dispose();
    }
  }
}
