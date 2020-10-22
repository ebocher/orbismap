/**
 * Map is part of the OrbisGIS platform
 * 
 * OrbisGIS is a java GIS application dedicated to research in GIScience.
 * OrbisGIS is developed by the GIS group of the DECIDE team of the
 * Lab-STICC CNRS laboratory, see <http://www.lab-sticc.fr/>.
 *
 * The GIS group of the DECIDE team is located at :
 *
 * Laboratoire Lab-STICC – CNRS UMR 6285 Equipe DECIDE UNIVERSITÉ DE
 * BRETAGNE-SUD Institut Universitaire de Technologie de Vannes 8, Rue Montaigne
 * - BP 561 56017 Vannes Cedex
 *
 * Map is distributed under LGPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2020 CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Map is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Map is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * Map. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.orbismap.map.renderer.legend;

import org.orbisgis.orbismap.map.renderer.featureStyle.symbolizer.AreaSymbolizerDrawer;
import org.orbisgis.orbismap.style.Feature2DRule;
import org.orbisgis.orbismap.style.IFeatureSymbolizer;
import org.orbisgis.orbismap.style.symbolizer.AreaSymbolizer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Erwan Bocher, CNRS (2020)
 */
public class LegendItem extends MapItem{

    Color backgroundColor = Color.WHITE;

    String title = "Legend";

    Font titleFont = new Font("Arial", Font.BOLD, 18);

    Color titleColor = Color.BLACK;

    Font textFont = new Font("Arial", Font.PLAIN, 12);

    Color textColor = Color.BLACK;

    List<ILegendEntry> entries = new ArrayList<>();

    int legendEntryWidth = 50;

    int legendEntryHeight = 30;

    int gapBetweenEntries = 10;

    String numberFormat = "#.##";

    /**
     * Create a Legend from the top left with the given width and height.
     * @param x The number of pixels from the left
     * @param y The number of pixels from the top
     * @param width The width in pixels
     * @param height The height in pixels
     */
    LegendItem(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    /**
     * Set the legend title
     * @param title The title
     * @return The LegendItem
     */
    LegendItem setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * Set the background color
     * @param backgroundColor The background color
     * @return The LegendItem
     */
    LegendItem setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    /**
     * Add an Rule Entry
     * @param feature2DRule The rule
     * @return The LegendItem
     */
    LegendItem addRuleEntry(Feature2DRule feature2DRule) {
        for(IFeatureSymbolizer symbolizer: feature2DRule.getSymbolizers()) {
            if(symbolizer instanceof AreaSymbolizer) {
                //this.entries.add(new LegendEntry(LegendEntryType.AREA, symbolizer));
            }
        }
        return  this;
    }

    LegendItem addTextEntry(String title){
        if(title!=null){
            this.entries.add(new LegendText(title));
        }
        return this;
    }

    /**
     * Draw the legend
     * @param graphics
     */
    public void draw(Graphics2D graphics){
        // Draw background
        if(backgroundColor!=null) {
            graphics.setColor(backgroundColor);
            graphics.fillRect(x, y, width, height);
        }
        // Draw title
        graphics.setFont(titleFont);
        graphics.setColor(titleColor);
        FontMetrics fm = graphics.getFontMetrics();
        int titleHeight = fm.getHeight();
        LegendUtils.drawString( graphics, title, new Rectangle(x, y, width, titleHeight), HorizontalAlign.LEFT, VerticalAlign.MIDDLE );

        // Draw Entries
        graphics.setFont(textFont);
        graphics.setColor(textColor);
        fm = graphics.getFontMetrics();

        // Keep track of the entry x and y
        int entryX = x;
        int entryY = y + titleHeight + gapBetweenEntries;
        int maxTextWidth = -1;
        for(ILegendEntry legendEntry: entries) {
            legendEntry.draw(graphics, entryX, entryY, legendEntryWidth * 2, fm.getHeight());
        }

    }

      protected enum LegendEntryType {
        GROUP,
        POINT,
        LINE,
        AREA,
        COLORMAP,
        IMAGE,
        TEXT
    }

}