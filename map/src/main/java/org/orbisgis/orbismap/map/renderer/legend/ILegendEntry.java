package org.orbisgis.orbismap.map.renderer.legend;

import java.awt.*;

public interface ILegendEntry {

    void draw(Graphics2D graphics, int x, int y, int width, int height);

    int getGapBetweenEntries();
}
