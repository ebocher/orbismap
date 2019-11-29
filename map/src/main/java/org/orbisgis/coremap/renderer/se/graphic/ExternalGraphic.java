/**
 * OrbisGIS is a java GIS application dedicated to research in GIScience.
 * OrbisGIS is developed by the GIS group of the DECIDE team of the 
 * Lab-STICC CNRS laboratory, see <http://www.lab-sticc.fr/>.
 *
 * The GIS group of the DECIDE team is located at :
 *
 * Laboratoire Lab-STICC – CNRS UMR 6285
 * Equipe DECIDE
 * UNIVERSITÉ DE BRETAGNE-SUD
 * Institut Universitaire de Technologie de Vannes
 * 8, Rue Montaigne - BP 561 56017 Vannes Cedex
 * 
 * OrbisGIS is distributed under GPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2017 CNRS (Lab-STICC UMR CNRS 6285)
 *
 * This file is part of OrbisGIS.
 *
 * OrbisGIS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * OrbisGIS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * OrbisGIS. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly:
 * info_at_ orbisgis.org
 */
package org.orbisgis.coremap.renderer.se.graphic;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.orbisgis.coremap.map.MapTransform;
import org.orbisgis.coremap.renderer.se.ViewBoxNode;
import org.orbisgis.coremap.renderer.se.common.Halo;
import org.orbisgis.coremap.renderer.se.parameter.ParameterException;
import org.orbisgis.coremap.renderer.se.parameter.real.RealParameter;
import org.orbisgis.coremap.renderer.se.parameter.real.RealParameterContext;
import org.orbisgis.coremap.renderer.se.transform.Transform;
import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.IUom;
import org.orbisgis.style.Uom;

/**
 * An external graphic is an image such as JPG, PNG, SVG.
 * Available action on such a graphic are affine transformations.
 * There is no way to re-style the graphic but setting opacity. It is dependant
 * upon the following values : <p>
 * <ul><li>OnlineResource : An URI where to retrieve the remote graphic.
 * Exclusive with InlineContent.</li>
 * <li>InlineContent : The content of a graphic is included inline. Exclusive
 * with OnlineResource.</li>
 * <li>Format : the MIME type of this graphic.</li>
 * <li>uom : A unit of measure</li>
 * <li>ViewBox : A box where the graphic will be rendered.</li>
 * <li>Transform : The affine transformation to apply on the graphic.</li>
 * <li>Opacity : Alpha filter to apply on the graphic. It must be a double value,
 * in the [0;1] range.</li>
 * <li>Halo : The halo to draw around the graphic.</li>
 * </ul>
 *
 * @todo Opacity not yet implemented !
 * 
 * @see MarkGraphic, Graphic, ViewBox
 * @author Maxence Laurent, Alexis Guéganno
 */
public final class ExternalGraphic extends Graphic implements IUom, TransformNode,
        ViewBoxNode {

    private ExternalGraphicSource source;
    private ViewBox viewBox;
    private RealParameter opacity;
    private Halo halo;
    private Transform transform;
    //private PlanarImage graphic;
    private Uom uom;
    private String mimeType;

    public ExternalGraphic() {
    }

    
    @Override
    public Uom getUom() {
        if (uom != null) {
            return uom;
        } else if(getParent() instanceof IUom){
            return ((IUom)getParent()).getUom();
        } else {
            return Uom.PX;
        }
    }

    @Override
    public Uom getOwnUom() {
        return uom;
    }

    @Override
    public void setUom(Uom uom) {
        this.uom = uom;
    }

    @Override
    public Transform getTransform() {
        return transform;
    }

    @Override
    public void setTransform(Transform transform) {
        this.transform = transform;
        if (transform != null) {
            transform.setParent(this);
        }
    }

    /**
     * Get the {@link Halo} that must be rendered around this {@code
     * ExternalGraphic}.
     * @return
     * A {@link Halo} instance, or null if not set.
     */
    public Halo getHalo() {
        return halo;
    }

    /**
     * Set the {@link Halo} that must be rendered around this {@code
     * ExternalGraphic}.
     * @param halo
     */
    public void setHalo(Halo halo) {
        this.halo = halo;
        if (halo != null) {
            halo.setParent(this);
        }
    }

    /**
     * Get the opacity applied to this {@code ExternalGraphic} at rendering time.
     * @return
     * The opacity as a {@link RealParameter} instance, in a {@link
     * RealParameterContext}.
     */
    public RealParameter getOpacity() {
        return opacity;
    }

    /**
     * Set the opacity applied to this {@code ExternalGraphic} at rendering time.
     * @param opacity
     */
    public void setOpacity(RealParameter opacity) {
        this.opacity = opacity;
        if (this.opacity != null) {
            this.opacity.setContext(RealParameterContext.PERCENTAGE_CONTEXT);
            this.opacity.setParent(this);
        }
    }

        @Override
    public ViewBox getViewBox() {
        return viewBox;
    }

        @Override
    public void setViewBox(ViewBox viewBox) {
        this.viewBox = viewBox;
        viewBox.setParent(this);
        updateGraphic();
    }

    @Override
    public void updateGraphic() {
        //graphic = null;
        /*
        try {
        if (source != null) {
        graphic = source.getPlanarImage(viewBox, null, null);
        }
        } catch (Exception ex) {
        ex.printStackTrace();
        }*/
    }

    public void setSource(ExternalGraphicSource src) throws IOException {
        this.source = src;
        if(source != null){
            source.setParent(this);
        }
        updateGraphic();
    }

    public ExternalGraphicSource getSource() {
        return source;
    }

    @Override
    public Rectangle2D getBounds(Map<String,Object> map, MapTransform mt) throws ParameterException, IOException {
        Rectangle2D.Double bounds = source.updateCacheAndGetBounds(viewBox, map, mt, mimeType);
        double width = bounds.getWidth();
        double height = bounds.getHeight();

        AffineTransform at = null;
        if (transform != null) {
            at = transform.getGraphicalAffineTransform(false, map, mt, width, height);
        }

        // reserve the place for halo
        if (halo != null) {
            double r = halo.getHaloRadius(map, mt);
            width += 2 * r;
            height += 2 * r;
            double px = bounds.getMinX()-r;
            double py = bounds.getMinY()-r;

            bounds = new Rectangle2D.Double(px, py, width, height);
        }

        /*
        if (at != null) {
            // take into account AT
            return at.createTransformedShape(bounds).getBounds2D();
        } else {
            return bounds;
        }*/
        return bounds;
    }

    @Override
    public void draw(Graphics2D g2, Map<String,Object> map,
            boolean selected, MapTransform mt, AffineTransform fat) throws ParameterException, IOException {


        Rectangle2D.Double bounds = source.updateCacheAndGetBounds(viewBox, map, mt, mimeType);

        AffineTransform at = new AffineTransform(fat);
        double width = bounds.getWidth();
        double height = bounds.getHeight();

        if (transform != null) {
            at.concatenate(transform.getGraphicalAffineTransform(false, map, mt, width, height));
        }

        // reserve the place for halo
        if (halo != null) {
            // Draw it
            halo.draw(g2, map, selected, at.createTransformedShape(bounds), mt, selected);
        }

        double op = 1.0;
        if (opacity != null){
            op = opacity.getValue(map);
        }

        source.draw(g2, map, at, mt, op, mimeType);
    }

    /*@Override
    public RenderableGraphics getRenderableGraphics(DataSet sds, long fid, boolean selected, MapTransform mt) throws ParameterException, IOException {
    // TODO Implements SELECTED!

    RenderedImage img;

    //if (graphic == null) {
    img = source.getPlanarImage(viewBox, map, mt, mimeType);
    //} else {
    //    img = graphic;
    //}

    if (img == null){
    return null;
    }

    double w = img.getWidth();
    double h = img.getHeight();

    AffineTransform at = new AffineTransform();
    if (transform != null){
    at = transform.getGraphicalAffineTransform(false, map, mt, w, h);
    }

    double px = 0;
    double py = 0;

    // reserve the place for halo
    if (halo != null) {
    double r = halo.getHaloRadius(map, mt);
    w += 2 * r;
    h += 2 * r;
    px = r;
    py = r;
    }

    Rectangle2D bounds = new Rectangle2D.Double(0.0, 0.0, w, h);

    at.concatenate(AffineTransform.getTranslateInstance(-w / 2.0, -h / 2.0));

    // Apply the AT to the bbox
    Shape atShp = at.createTransformedShape(bounds);
    Rectangle2D imageSize = atShp.getBounds2D();

    RenderableGraphics rg = Graphic.getNewRenderableGraphics(imageSize, 0, mt);

    if (halo != null) {
    halo.draw(rg, map, selected, atShp, mt, false);
    // and add a translation to center img on halo
    at.concatenate(AffineTransform.getTranslateInstance(px, py));
    }

    // TODO how to set opacity ?


    // apply the AT and draw the ext graphic
    rg.drawRenderedImage(img, at);

    return rg;
    }*/

    /*public double getMargin(DataSet sds, long fid, MapTransform mt) throws ParameterException, IOException {
    double delta = 0.0;

    if (this.halo != null) {
    delta += halo.getHaloRadius(map, mt);
    }

    return delta;
    }*/

    /*
    @Override
    public double getMaxWidth(DataSet sds, long fid, MapTransform mt) throws ParameterException, IOException {
    double delta = 0.0;
    if (viewBox != null && viewBox.usable()) {
    RenderedImage img;
    if (graphic == null) {
    img = source.getPlanarImage(viewBox, map, mt, mimeType);
    } else {
    img = graphic;
    }

    if (img != null){
    Point2D dim = viewBox.getDimensionInPixel(map, img.getHeight(), img.getWidth(), mt.getScaleDenominator(), mt.getDpi());

    delta = Math.max(dim.getY(), dim.getX());
    }
    else{
    return 0.0;
    }
    }

    delta += this.getMargin(map, mt);

    return delta;
    }*/

    @Override
    public List<IStyleNode> getChildren() {
        List<IStyleNode> ls = new ArrayList<IStyleNode>();
        if (halo != null) {
            ls.add(halo);
        }
        if (opacity != null) {
            ls.add(opacity);
        }
        if (transform != null) {
            ls.add(transform);
        }
        if (viewBox != null) {
            ls.add(viewBox);
        }
        if(source != null){
            ls.add(source);
        }
        return ls;
    }

}
