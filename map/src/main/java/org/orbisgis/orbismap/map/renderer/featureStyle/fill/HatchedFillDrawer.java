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
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import org.orbisgis.orbismap.map.layerModel.MapTransform;
import org.orbisgis.orbismap.map.renderer.featureStyle.AbstractDrawerFinder;
import org.orbisgis.orbismap.map.renderer.featureStyle.IFillDrawer;
import org.orbisgis.orbismap.map.renderer.featureStyle.IStrokeDrawer;
import org.orbisgis.orbismap.map.renderer.featureStyle.stroke.GraphicStrokeDrawer;
import org.orbisgis.orbismap.map.renderer.featureStyle.stroke.PenStrokeDrawer;
import org.orbisgis.orbismap.map.renderer.featureStyle.stroke.TextStrokeDrawer;
import org.orbisgis.orbismap.style.Uom;
import org.orbisgis.orbismap.style.fill.HatchedFill;
import static org.orbisgis.orbismap.style.fill.HatchedFill.DEFAULT_ALPHA;
import org.orbisgis.orbismap.style.parameter.ParameterException;
import org.orbisgis.orbismap.style.stroke.GraphicStroke;
import org.orbisgis.orbismap.style.stroke.PenStroke;
import org.orbisgis.orbismap.style.stroke.Stroke;
import org.orbisgis.orbismap.style.stroke.TextStroke;
import org.orbisgis.orbismap.style.utils.UomUtils;

/**
 * Drawer for the element <code>HatchedFill</code>
 *
 * @author Erwan Bocher, CNRS (2020)
 */
public class HatchedFillDrawer extends AbstractDrawerFinder<IStrokeDrawer, Stroke> implements IFillDrawer<HatchedFill> {
    //Useful constants.

    private static final double EPSILON = 0.01; // todo Eval, and use an external EPSILON value.
    private static final double TWO_PI_DEG = 360.0;
    private static final double PI_DEG = 180.0;

    public static final double DEFAULT_NATURAL_LENGTH = 100;

    private Shape shape;
    private AffineTransform affineTransform;

    @Override
    public Paint getPaint(HatchedFill styleNode, MapTransform mt) throws ParameterException {
        return null;
    }

    @Override
    public void draw(Graphics2D g2, MapTransform mapTransform, HatchedFill styleNode) throws ParameterException {
        Uom uom = styleNode.getUom();
        Stroke stroke = styleNode.getStroke();
        IStrokeDrawer drawer = getDrawer(stroke);
        if (drawer != null) {
                // Perpendicular distance between two lines

                float pDist = 0;
                Float distance = (Float) styleNode.getDistance().getValue();
                if (distance == null) {
                    throw new ParameterException("The distance parameter for the hatched fill cannot be null");

                }
                if (distance > 0) {
                    pDist = UomUtils.toPixel(distance, uom, mapTransform.getDpi(), mapTransform.getScaleDenominator());
                }

                float alpha = DEFAULT_ALPHA;
                Float angle = (Float) styleNode.getAngle().getValue();
                if (angle == null) {
                    throw new ParameterException("The angle parameter for the hatched fill cannot be null");
                }
                if (angle > 0) {
                    alpha = angle;
                }
                double hOffset = 0.0;
                Float offset = (Float) styleNode.getOffset().getValue();
                if (offset != null) {
                    hOffset = UomUtils.toPixel(offset, uom, mapTransform.getDpi(), mapTransform.getScaleDenominator());
                }

                drawHatch(g2, shape, mapTransform, alpha, pDist, (PenStroke) stroke, drawer, hOffset);

            
        }
    }

    /**
     * Static method that draw hatches within provided shp
     *
     * @param g2 the g2 to write on
     * @param shp the shape to hatch
     * @param mt the well known map transform
     * @param alph hatches orientation
     * @param pDist perpendicular distance between two hatch line (stroke width
     * not taken into account so a 10mm wide black PenStroke + pDist=10mm will
     * produce a full black behaviour...)
     * @param hOffset offset between the references point and the reference
     * hatch
     * @param penStrokeDrawer
     * @param penStroke
     * @throws ParameterException
     */
    public static void drawHatch(Graphics2D g2, Shape shp,
            MapTransform mt, double alph, double pDist, Stroke penStroke, IStrokeDrawer penStrokeDrawer,
            double hOffset) throws ParameterException {
        double alpha = alph;
        while (alpha < 0.0) {
            alpha += TWO_PI_DEG;
        }   // Make sure alpha is > 0
        while (alpha > TWO_PI_DEG) {
            alpha -= TWO_PI_DEG;
        } // and < 360.0
        alpha = alpha * Math.PI / PI_DEG; // and finally convert in radian
        double beta = Math.PI / 2.0 + alpha;
        double deltaOx = Math.cos(beta) * hOffset;
        double deltaOy = Math.sin(beta) * hOffset;
        Double naturalLength = penStrokeDrawer.getNaturalLength(penStroke, mt);
        if (naturalLength.isInfinite()) {
            naturalLength = DEFAULT_NATURAL_LENGTH;
        }

        // the first hatch to generate is the reference one : it crosses the reference point
        Point2D.Double geoRef = new Point2D.Double(0, 0);
        // Map geo ref point within g2 space
        Point2D ref = mt.getAffineTransform().transform(geoRef, null);
        // Apply hatch offset to ref point
        ref.setLocation(ref.getX() + deltaOx, ref.getY() + deltaOy);

        // Compute some var
        double cosAlpha = Math.cos(alpha);
        double sinAlpha = Math.sin(alpha);

        if (Math.abs(sinAlpha) < EPSILON) {
            sinAlpha = 0.0;
        }

        boolean vertical = false;

        if (Math.abs(cosAlpha) < EPSILON) {
            cosAlpha = 0.0;
            vertical = true;
        }

        double deltaHx = cosAlpha * naturalLength;
        double deltaHy = sinAlpha * naturalLength;

        double deltaDx = pDist / sinAlpha;
        double deltaDy = pDist / cosAlpha;

        Rectangle2D fbox = shp.getBounds2D();


        /* the following block compute the number of times the hatching pattern shall be drawn */
        int nb2start; // how many pattern to skip from the ref point to the begining of the shape ?
        int nb2end; // how many pattern to skip from the ref point to the end of the shape ?

        if (vertical) {
            if (deltaDx >= 0.0) {
                nb2start = (int) Math.ceil((fbox.getMinX() - ref.getX()) / deltaDx);
                nb2end = (int) Math.floor(((fbox.getMaxX() - ref.getX()) / deltaDx));
            } else {
                nb2start = (int) Math.floor((fbox.getMinX() - ref.getX()) / deltaDx);
                nb2end = (int) Math.ceil(((fbox.getMaxX() - ref.getX()) / deltaDx));
            }
        } else {
            if (cosAlpha < 0) {
                nb2start = (int) Math.ceil((fbox.getMinX() - ref.getX()) / deltaHx);
                nb2end = (int) Math.floor(((fbox.getMaxX() - ref.getX()) / deltaHx));
            } else {
                nb2start = (int) Math.floor((fbox.getMinX() - ref.getX()) / deltaHx);
                nb2end = (int) Math.ceil(((fbox.getMaxX() - ref.getX()) / deltaHx));
            }
        }

        int nb2draw = nb2end - nb2start;

        double ref_yXmin;
        double ref_yXmax;

        double cos_sin = cosAlpha * sinAlpha;

        ref_yXmin = ref.getY() + nb2start * deltaHy;
        ref_yXmax = ref.getY() + nb2end * deltaHy;

        double hxmin;
        double hxmax;
        if (vertical) {
            hxmin = nb2start * deltaDx + ref.getX();
            hxmax = nb2end * deltaDx + ref.getX();
        } else {
            hxmin = nb2start * deltaHx + ref.getX();
            hxmax = nb2end * deltaHx + ref.getX();
        }

        double hymin;
        double hymax;
        double nb2drawDeltaY = nb2draw * deltaHy;

        // Compute hatches sub-set to draw (avoid all pattern which not stands within the clip area...)
        if (vertical) {
            if (deltaHy < 0.0) {
                hymin = Math.ceil((fbox.getMinY() - ref.getY()) / deltaHy) * deltaHy + ref.getY();
                hymax = Math.floor((fbox.getMaxY() - ref.getY()) / deltaHy) * deltaHy + ref.getY();
            } else {
                hymin = Math.floor((fbox.getMinY() - ref.getY()) / deltaHy) * deltaHy + ref.getY();
                hymax = Math.ceil((fbox.getMaxY() - ref.getY()) / deltaHy) * deltaHy + ref.getY();
            }
        } else {
            if (cos_sin < 0) {
                hymin = Math.floor((fbox.getMinY() - ref_yXmin) / (deltaDy)) * deltaDy + ref_yXmin;
                hymax = Math.ceil((fbox.getMaxY() - ref_yXmax) / (deltaDy)) * deltaDy + ref_yXmax - nb2drawDeltaY;
            } else {
                hymin = Math.floor((fbox.getMinY() - nb2drawDeltaY - ref_yXmin) / (deltaDy)) * deltaDy + ref_yXmin;

                if (deltaDy < 0) {
                    hymax = Math.floor((fbox.getMaxY() + nb2drawDeltaY - ref_yXmax) / (deltaDy)) * deltaDy + ref_yXmax - nb2drawDeltaY;
                } else {
                    hymax = Math.ceil((fbox.getMaxY() + nb2drawDeltaY - ref_yXmax) / (deltaDy)) * deltaDy + ref_yXmax - nb2drawDeltaY;
                }
            }
        }

        double y;
        double x;

        Line2D.Double l = new Line2D.Double();

        // Inform graphic2g to only draw hatches within the shape !
        g2.clip(shp);

        if (vertical) {

            if (hxmin < hxmax) {
                if (deltaDx < 0) {
                    deltaDx *= -1;
                }
                for (x = hxmin; x < hxmax + deltaDx / 2.0; x += deltaDx) {
                    if (sinAlpha > 0) {
                        l.x1 = x;
                        l.y1 = hymin;
                        l.x2 = x;
                        l.y2 = hymax;
                    } else {
                        l.x1 = x;
                        l.y1 = hymax;
                        l.x2 = x;
                        l.y2 = hymin;
                    }

                    penStrokeDrawer.setShape(l);
                    penStrokeDrawer.draw(g2, mt, penStroke);
                    //g2.fillOval((int)(l.getX1() - 2),(int)(l.getY1() -2) , 4, 4);
                    //g2.fillOval((int)(l.getX2() - 2),(int)(l.getY2() -2) , 4, 4);
                }
            } else {

                // Seems to been unreachable !
                for (x = hxmin; x > hxmax - deltaDx / 2.0; x += deltaDx) {
                    l.x1 = x;
                    l.y1 = hymin;
                    l.x2 = x;
                    l.y2 = hymax;

                    penStrokeDrawer.setShape(l);
                    penStrokeDrawer.draw(g2, mt, penStroke);
                    //g2.fillOval((int)(l.getX1() - 2),(int)(l.getY1() -2) , 4, 4);
                    //g2.fillOval((int)(l.getX2() - 2),(int)(l.getY2() -2) , 4, 4);
                }
            }

        } else {
            if (hymin < hymax) {
                if (deltaDy < 0.0) {
                    deltaDy *= -1;
                }
                for (y = hymin; y < hymax + deltaDy / 2.0; y += deltaDy) {

                    if (cosAlpha > 0) {
                        // Line goes from the left to the right
                        l.x1 = hxmin;
                        l.y1 = y;
                        l.x2 = hxmax;
                        l.y2 = y + nb2draw * deltaHy;
                    } else {
                        // Line goes from the right to the left
                        l.x1 = hxmax;
                        l.y1 = y + nb2draw * deltaHy;
                        l.x2 = hxmin;
                        l.y2 = y;
                    }

                    penStrokeDrawer.setShape(l);
                    penStrokeDrawer.draw(g2, mt, penStroke);
                    //g2.fillOval((int)(l.getX1() - 2),(int)(l.getY1() -2) , 4, 4);
                    //g2.fillOval((int)(l.getX2() - 2),(int)(l.getY2() -2) , 4, 4);
                }
            } else {

                if (deltaDy > 0.0) {
                    deltaDy *= -1;
                }

                for (y = hymin; y > hymax - deltaDy / 2.0; y += deltaDy) {

                    if (cosAlpha > 0) {
                        // Line goes from the left to the right
                        l.x1 = hxmin;
                        l.y1 = y;
                        l.x2 = hxmax;
                        l.y2 = y + nb2draw * deltaHy;
                    } else {
                        // Line goes from the right to the left
                        l.x1 = hxmax;
                        l.y1 = y + nb2draw * deltaHy;
                        l.x2 = hxmin;
                        l.y2 = y;
                    }

                    penStrokeDrawer.setShape(l);
                    penStrokeDrawer.draw(g2, mt, penStroke);

                    //g2.fillOval((int)(l.getX1() - 2),(int)(l.getY1() -2) , 4, 4);
                    //g2.fillOval((int)(l.getX2() - 2),(int)(l.getY2() -2) , 4, 4);
                }
            }
        }
        g2.setClip(null);
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
    public IStrokeDrawer getDrawer(Stroke styleNode) {
        if (styleNode != null) {
            IStrokeDrawer drawer = drawerMap.get(styleNode);
            if (drawer == null) {
                if (styleNode instanceof PenStroke) {
                    drawer = new PenStrokeDrawer();
                    drawerMap.put(styleNode, drawer);
                }
                else if (styleNode instanceof TextStroke) {
                    drawer = new TextStrokeDrawer();
                    drawerMap.put(styleNode, drawer);
                }
                else if (styleNode instanceof GraphicStroke) {
                    drawer = new GraphicStrokeDrawer();
                    drawerMap.put(styleNode, drawer);
                }
            }
            return drawer;
        }
        return null;
    }

}
