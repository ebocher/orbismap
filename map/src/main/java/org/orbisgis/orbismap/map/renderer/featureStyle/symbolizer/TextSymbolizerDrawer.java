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
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488) Copyright (C) 2015-2020
 * CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Map is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Map is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Map. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.orbismap.map.renderer.featureStyle.symbolizer;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import org.orbisgis.orbismap.map.layerModel.MapTransform;
import org.orbisgis.orbismap.map.renderer.featureStyle.AbstractDrawerFinder;
import org.orbisgis.orbismap.map.renderer.featureStyle.ILabelDrawer;
import org.orbisgis.orbismap.map.renderer.featureStyle.ISymbolizerDraw;
import org.orbisgis.orbismap.map.renderer.featureStyle.label.LineLabelDrawer;
import org.orbisgis.orbismap.map.renderer.featureStyle.label.PointLabelDrawer;
import org.orbisgis.orbismap.style.label.Label;
import org.orbisgis.orbismap.style.label.LineLabel;
import org.orbisgis.orbismap.style.label.PointLabel;
import org.orbisgis.orbismap.style.parameter.ParameterException;
import org.orbisgis.orbismap.style.symbolizer.TextSymbolizer;

/**
 * Drawer for the element <code>TextSymbolizer</code>
 *
 * @author Erwan Bocher, CNRS (2020)
 */
public class TextSymbolizerDrawer extends AbstractDrawerFinder<ILabelDrawer, Label> implements ISymbolizerDraw<TextSymbolizer> {
    
    private Shape shape;

    private BufferedImage bi;
    private Graphics2D g2_bi;

    @Override
    public void draw(Graphics2D g2, MapTransform mapTransform, TextSymbolizer symbolizer) throws ParameterException {
        Label label = symbolizer.getLabel();
        ILabelDrawer drawer = getDrawer(label);
        if (drawer!=null) {
            drawer.setShape(getShape());
            drawer.draw(g2, mapTransform, label);
        }
    }

    @Override
    public Shape getShape() {
        return shape;
    }

    @Override
    public void setShape(Shape shape) {
        this.shape = shape;
    }

    @Override
    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bi = bufferedImage;
    }

    @Override
    public BufferedImage getBufferedImage() {
        return bi;
    }

    @Override
    public void setGraphics2D(Graphics2D g2) {
        this.g2_bi = g2;
    }

    @Override
    public Graphics2D getGraphics2D() {
        return g2_bi;
    }

    @Override
    public void dispose(Graphics2D g2) {
        if (g2 != null) {
            g2_bi.dispose();
            g2_bi = null;
            g2.drawImage(bi, null, null);
            bi = null;
        }
    }

    @Override
    public ILabelDrawer getDrawer(Label styleNode) {
        if (styleNode != null) {
            ILabelDrawer drawer = drawerMap.get(styleNode);
            if (drawer == null) {
                if (styleNode instanceof PointLabel) {
                    drawer = new PointLabelDrawer();
                    drawerMap.put(styleNode, drawer);
                } else if (styleNode instanceof LineLabel) {
                    drawer = new LineLabelDrawer();
                    drawerMap.put(styleNode, drawer);
                }
            }
            return drawer;
        }
        return null;
    }

}
