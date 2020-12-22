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
package org.orbisgis.orbismap.map.renderer.featureStyle.fill;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import org.orbisgis.orbismap.map.layerModel.MapTransform;
import org.orbisgis.orbismap.map.renderer.featureStyle.AbstractDrawerFinder;
import org.orbisgis.orbismap.map.renderer.featureStyle.IFillDrawer;
import org.orbisgis.orbismap.map.renderer.featureStyle.IGraphicCollectionDrawer;
import org.orbisgis.orbismap.map.renderer.featureStyle.graphic.GraphicCollectionDrawer;
import org.orbisgis.orbismap.style.fill.DotMapFill;
import org.orbisgis.orbismap.style.graphic.GraphicCollection;
import org.orbisgis.orbismap.style.parameter.ParameterException;

/**
 * Drawer for the element <code>DotMapFill</code>
 *
 * @author ebocher
 */
public class DotMapFillDrawer extends AbstractDrawerFinder<IGraphicCollectionDrawer, GraphicCollection> implements IFillDrawer<DotMapFill> {

    private Random rand;
    static final int MAX_ATTEMPT = 100;

    private Shape shape;
    private AffineTransform affineTransform;

    @Override
    public Paint getPaint(DotMapFill styleNode, MapTransform mt) throws ParameterException {
        return null;
    }

    @Override
    public void draw(Graphics2D g2, MapTransform mapTransform, DotMapFill styleNode) throws ParameterException {
        if (shape != null) {
            GraphicCollection graphics = styleNode.getGraphics();
            IGraphicCollectionDrawer drawer = getDrawer(graphics);
            if (drawer!=null) {
                    Integer perMark = (Integer) styleNode.getQuantityPerMark().getValue();
                    Integer total = (Integer) styleNode.getTotalQuantity().getValue();

                    if (perMark == null || total == null) {
                        throw new ParameterException("Dot Map Fill: missing parameters !!!");
                    }
                    int nb = (int) Math.round(total / perMark);
                    Area area = new Area(shape);
                    if (rand == null) {
                        rand = new Random();
                    }
                    // setting the seed to the scale denominator will ensure that mark will not move when panning
                    rand.setSeed((long) mapTransform.getScaleDenominator());
                    for (int i = 0; i < nb; i++) {
                        Point2D.Double pos = findMarkPosition(area);
                        if (pos != null) {
                            drawer.setAffineTransform(AffineTransform.getTranslateInstance(pos.x, pos.y));
                            drawer.draw(g2, mapTransform, graphics);
                        }
                    }
                
            }
        }
    }

    /**
     * Ugly version to find a random point which stand within the area
     *
     * @param area
     * @return
     */
    private Point2D.Double findMarkPosition(Area area) {
        Rectangle2D bounds2D = area.getBounds2D();
        for (int i = 0; i < MAX_ATTEMPT; i++) {
            double x = rand.nextDouble() * bounds2D.getWidth() + bounds2D.getMinX();
            double y = rand.nextDouble() * bounds2D.getHeight() + bounds2D.getMinY();
            if (area.contains(x, y)) {
                return new Point2D.Double(x, y);
            }
        }
        return null;
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
    public AffineTransform getAffineTransform() {
        return affineTransform;
    }

    @Override
    public void setAffineTransform(AffineTransform affineTransform) {
        this.affineTransform = affineTransform;
    }

    @Override
    public IGraphicCollectionDrawer getDrawer(GraphicCollection styleNode) {
        if (styleNode != null) {
            IGraphicCollectionDrawer drawer = drawerMap.get(styleNode);
            if (drawer == null) {
                if (styleNode instanceof GraphicCollection) {
                    drawer = new GraphicCollectionDrawer();
                    drawerMap.put(styleNode, drawer);
                }
            }
            return drawer;
        }
        return null;
    }

}
