package org.orbisgis.orbismap.map.renderer.legend;

import java.awt.*;

public abstract class LegendElement implements ILegendEntry {

    private Integer spacingAfter;

    int gapBetweenEntries = 10;


    /**
     * Spacing after image
     * @return null if default else number
     * of pixel to draw between current cell and next
     * cell
     */
    public Integer getSpacingAfter(){
        return this.spacingAfter;
    }

    /**
     * Sets the spacing after image.  Set
     * to null to use default
     * @param spacing
     */
    public void setSpacingAfter(Integer spacing){
        this.spacingAfter = spacing;
    }

    public int getGapBetweenEntries() {
        return gapBetweenEntries;
    }

    public void setGapBetweenEntries(int gapBetweenEntries) {
        this.gapBetweenEntries = gapBetweenEntries;
    }
}
