package org.orbisgis.orbismap.map.renderer.legend;

import java.awt.*;

public class LegendUtils {

    /**
     *
     * @param graphics
     * @param text
     * @param rectangle
     * @param horizontalAlign
     * @param verticalAlign
     */
    public static void drawString(Graphics2D graphics, String text, Rectangle rectangle, HorizontalAlign horizontalAlign, VerticalAlign verticalAlign) {
        FontMetrics fontMetrics = graphics.getFontMetrics(graphics.getFont());
        int x = 0;
        if (horizontalAlign == HorizontalAlign.LEFT) {
            x = rectangle.x;
        } else if (horizontalAlign == HorizontalAlign.CENTER) {
            x = rectangle.x + (rectangle.width - fontMetrics.stringWidth(text)) / 2;
        } else if (horizontalAlign == HorizontalAlign.RIGHT) {
            x = rectangle.x +  rectangle.width - fontMetrics.stringWidth(text);
        }
        int y=0;
        if (verticalAlign == VerticalAlign.TOP) {
            y = rectangle.y + fontMetrics.getHeight() - fontMetrics.getDescent();
        } else if (verticalAlign == VerticalAlign.MIDDLE) {
            y = rectangle.y + ((rectangle.height - fontMetrics.getHeight()) / 2) + fontMetrics.getHeight() - fontMetrics.getDescent();
        } else if (verticalAlign == VerticalAlign.BOTTOM) {
            y = rectangle.y + rectangle.height - fontMetrics.getDescent();
        }
        graphics.drawString(text, x, y);
    }

}
