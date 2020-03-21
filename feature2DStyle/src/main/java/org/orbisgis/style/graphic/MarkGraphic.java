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
package org.orbisgis.style.graphic;


import org.orbisgis.style.fill.Halo;
import org.orbisgis.style.fill.SolidFill;
import org.orbisgis.style.stroke.PenStroke;
import org.orbisgis.style.stroke.Stroke;
import org.orbisgis.style.transform.Transform;

import java.util.ArrayList;
import java.util.List;
import org.orbisgis.style.FillNode;
import org.orbisgis.style.IFill;
import org.orbisgis.style.IStyleNode;
import org.orbisgis.style.IUom;
import org.orbisgis.style.StrokeNode;
import org.orbisgis.style.Uom;
import org.orbisgis.style.ViewBoxNode;
import org.orbisgis.style.parameter.Literal;
import org.orbisgis.style.parameter.ParameterValue;
import org.orbisgis.style.parameter.TransformParameter;
import org.orbisgis.style.utils.ParameterValueHelper;

/**
 * A {@code MarkGraphic} is created by stroking and filling a geometry line or shape.
 * It is built using the following parameters :
 * <ul><li>A definition of the contained graphic, that can be exclusively of one of these types :
 *      <ul><li> WellKnownText : as defined in {@link WellKnownName}.</li>
 * <li>A unit of measure</li>
 * <li>A viewbox, as described in {@link ViewBox}</li>
 * <li>A {@link Transform}, that describes an affine transformation that must be applied on the mark.</li>
 * <li>A {@link Halo}</li>
 * <li>A {@link Fill}</li>
 * <li>A {@link Stroke}</li>
 * <li>A perpendicular offset</li>
 * </ul>
 * @author Maxence Laurent
 * @author Alexis Guéganno
 * @author Erwan Bocher, CNRS
 */
public class MarkGraphic extends Graphic implements FillNode, StrokeNode,
        ViewBoxNode, IUom, TransformNode {

        /**
         * The default size used to build {@code MarkGraphic} instances.
         */
    public static float DEFAULT_SIZE = 3;
    //private MarkGraphicSource source;
    private Uom uom;
    private TransformParameter transform;
    private ParameterValue wkn;
    private ViewBox viewBox;
    private ParameterValue perpendicularOffset;
    private Halo halo;
    private IFill fill;
    private Stroke stroke;

    /**
     * Build a default {@code MarkGraphic}. It is built using the {@link WellKnownName#CIRCLE}
     * value. The mark will be rendered using default solid fill and pen stroke. The 
     * associated unit of measure is {@link Uom#MM}, and it has the {@link #DEFAULT_SIZE}.
     */
    public MarkGraphic() {
        this.setTo3mmCircle();
    }

    /**
     * Transform this {@code MarkGraphic} in default one, as described in the default constructor.
     */
    public void setTo3mmCircle() {
        this.setUom(Uom.MM);
        this.setWkn(new Literal("'circle'"));
        this.setViewBox(new ViewBox(new Literal(DEFAULT_SIZE), new Literal(DEFAULT_SIZE)));
        this.setFill(new SolidFill());
        this.setStroke(new PenStroke());
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
    public TransformParameter getTransform() {
        return transform;
    }

    @Override
    public void setTransform(TransformParameter transform) {
        this.transform = transform;
        if (transform != null) {
            transform.setParent(this);
        }
    }

    @Override
    public IFill getFill() {
        return fill;
    }

    @Override
    public void setFill(IFill fill) {
        this.fill = fill;
        if (fill != null) {
            fill.setParent(this);
        }
    }

    /**
     * Get the {@link Halo} defined around this {@code MarkGraphic}.
     * @return The Halo drawn around the symbol
     */
    public Halo getHalo() {
        return halo;
    }

    /**
     * Set the {@link Halo} defined around this {@code MarkGraphic}.
     * @param halo The new halo to draw around the symbol.
     */
    public void setHalo(Halo halo) {
        this.halo = halo;
        if (halo != null) {
            halo.setParent(this);
        }
    }

    @Override
    public Stroke getStroke() {
        return stroke;
    }

    @Override
    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
        if (stroke != null) {
            stroke.setParent(this);
        }
    }

    @Override
    public ViewBox getViewBox() {
        return viewBox;
    }

    @Override
    public void setViewBox(ViewBox viewBox) {
        if (viewBox == null) {
            viewBox = new ViewBox();
        }
        else{
        this.viewBox = viewBox;
        viewBox.setParent(this);
        }
    }
    
    /**
     * Get the perpendicular offset applied to this {@code MarkGraphic} before rendering.
     * @return The perpendicular offset
     */
    public ParameterValue getPerpendicularOffset() {
        return perpendicularOffset;
    }

    /**
     * Set the perpendicular offset applied to this {@code MarkGraphic} before rendering.
     * @param perpendicularOffset The perpendicular offset
     */
    public void setPerpendicularOffset(ParameterValue perpendicularOffset) {
        ParameterValueHelper.validateAsDouble(perpendicularOffset);
        this.perpendicularOffset = perpendicularOffset == null ? ParameterValueHelper.createDoubleLiteral(1d) : perpendicularOffset;    
        this.perpendicularOffset.setParent(this);       
    }

    
    /**
     * Gets the WellKnownName defining this {@code MarkGraphic}.
     * @return the well-known name currently used, as a StringParameter.
     */
    public ParameterValue getWkn() {
        return wkn;
    }

    /**
     * Sets the WellKnownName defining this {@code MarkGraphic}.
     * @param wkn The new well-known name to use, as a StringParameter.
     */
    public void setWkn(ParameterValue wkn) {
        ParameterValueHelper.validateAsString(wkn);
        this.wkn = wkn;        
        if (this.wkn != null) {
            this.wkn.setParent(this);
        }
    }   

    @Override
    public List<IStyleNode> getChildren() {
        List<IStyleNode> ls = new ArrayList<IStyleNode>();
        if (wkn != null) {
            ls .add(wkn);
        }
        if (viewBox != null) {
            ls .add(viewBox);
        }
        if (perpendicularOffset != null) {
            ls .add(perpendicularOffset);
        }
        if (halo != null) {
            ls .add(halo);
        }
        if (fill != null) {
            ls .add(fill);
        }
        if (stroke != null) {
            ls .add(stroke);
        }
        if (transform != null) {
            ls .add(transform);
        }
        return ls;
    }

    
}
