package org.orbisgis.orbismap.map.renderer.legend;


import java.awt.*;

public class LegendText extends LegendElement  {

    private final String text;
    Font textFont = new Font("Arial", Font.PLAIN, 12);

    Color textColor = Color.BLACK;

    public LegendText(String text) {
        this.text = text;
    }

    public void setTextFont(Font textFont) {
        this.textFont = textFont;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public String getText() {
        return text;
    }

    @Override
    public void draw(Graphics2D graphics2D, int x, int y, int width, int height) {
        // Draw title
        graphics2D.setFont(textFont);
        graphics2D.setColor(textColor);
        Rectangle titleRectangle = new Rectangle(x, y, width, height);
        LegendUtils.drawString(graphics2D, text, titleRectangle, HorizontalAlign.LEFT, VerticalAlign.MIDDLE);
        y += height + getGapBetweenEntries();
    }
}
