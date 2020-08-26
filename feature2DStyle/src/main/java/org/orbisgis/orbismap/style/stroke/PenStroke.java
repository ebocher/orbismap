/**
 * Feature2DStyle is part of the OrbisGIS platform
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
 * Feature2DStyle is distributed under LGPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488) Copyright (C) 2015-2020
 * CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Feature2DStyle is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Feature2DStyle is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Feature2DStyle. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.orbismap.style.stroke;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.orbisgis.orbismap.style.IFillNode;
import org.orbisgis.orbismap.style.fill.SolidFill;
import org.orbisgis.orbismap.style.parameter.Literal;
import org.orbisgis.orbismap.style.parameter.NullParameterValue;
import org.orbisgis.orbismap.style.parameter.ParameterValue;
import org.orbisgis.orbismap.style.IFill;
import org.orbisgis.orbismap.style.IStyleNode;
import org.orbisgis.orbismap.style.Uom;

/**
 * Basic stroke for linear features. It is designed according to :
 * <ul><li>A {@link IFillNode} value</li>
 * <li>A width</li>
 * <li>A way to draw the extremities of the lines</li>
 * <li>A way to draw the joins between the segments of the lines</li>
 * <li>An array of dashes, that is used to draw the lines. The array is stored
 * as a StringParamater, that contains space separated double values. This
 * double values are used to determine the length of each opaque part (even
 * elements of the array) and the length of each transparent part (odd elements
 * of the array). If an odd number of values is given, the pattern is expanded
 * by repeating it twice to give an even number of values.</li>
 * <li>An offset used to know where to draw the line.</li>
 * </ul>
 *
 * @author Alexis Guéganno, CNRS (2012-2013)
 * @author Maxence Laurent, HEIG-VD (2010-2012)
 * @author Erwan Bocher, CNRS (2010-2020)
 */
public class PenStroke extends Stroke implements IFillNode {

    public static final float DEFAULT_WIDTH_PX = 1.0f;
    public static final float DEFAULT_WIDTH = .25f;
    /**
     * The cap used by default. Value is {@code LineCap.BUTT}.
     */
    public static final LineCap DEFAULT_CAP = LineCap.BUTT;
    /**
     * The join used by default. Value is {@code LineCap.MITRE}.
     */
    public static final LineJoin DEFAULT_JOIN = LineJoin.MITRE;
    private IFill fill;
    private ParameterValue width = new NullParameterValue();
    private LineJoin lineJoin;
    private LineCap lineCap;
    private ParameterValue dashArray = new NullParameterValue();
    private ParameterValue dashOffset = new NullParameterValue();


    public PenStroke() {
    }

    @Override
    public void initDefault() {
        SolidFill solidFill = new SolidFill();
        solidFill.setColor(Color.BLACK);
        solidFill.setOpacity(1.0f);
        setFill(solidFill);
        setWidth(DEFAULT_WIDTH);
        setUom(Uom.PX);
        setLineCap(DEFAULT_CAP);
        setLineJoin(DEFAULT_JOIN);
    }

    @Override
    public List<IStyleNode> getChildren() {
        List<IStyleNode> ls = new ArrayList<IStyleNode>();
        if (fill != null) {
            ls.add(fill);
        }
        if (dashOffset != null) {
            ls.add(dashOffset);
        }
        if (dashArray != null) {
            ls.add(dashArray);
        }
        if (width != null) {
            ls.add(width);
        }
        return ls;
    }

    @Override
    public IFill getFill() {
        return fill;
    }

    /**
     * Sets the fill used to draw the inside of this {@code PenStroke}.
     *
     * @param fill The new {@link IFill}. If null, will be set to a
     * {@link SolidFill} which color is black and opacity is 100%.
     */
    @Override
    public void setFill(IFill fill) {
        this.fill = fill;
        if (this.fill == null) {
            this.fill.setParent(this);
        }
    }

    /**
     * Sets the way to draw the extremities of a line.
     *
     * @param cap The new {@link LineCap}. Will be replaced by
     * DEFAULT_CAP if null.
     */
    public void setLineCap(LineCap cap) {
        lineCap = cap == null ? DEFAULT_CAP : cap;
    }

    /**
     * Gets the way used to draw the extremities of a line.
     *
     * @return
     */
    public LineCap getLineCap() {
        if (lineCap != null) {
            return lineCap;
        } else {
            return DEFAULT_CAP;
        }
    }

    /**
     * Sets the ways used to draw the join between line segments.
     *
     * @param join The new {@link LineJoin}. Will be replaced by {
     * @see DEFAULT_JOIN} if null.
     */
    public void setLineJoin(LineJoin join) {
        lineJoin = join == null ? DEFAULT_JOIN : join;
    }

    /**
     * Gets the ways used to draw the join between line segments.
     *
     * @return
     */
    public LineJoin getLineJoin() {
        if (lineJoin != null) {
            return lineJoin;
        } else {
            return DEFAULT_JOIN;
        }

    }

    /**
     * Set the width used to draw the lines with this {@code PenStroke}.
     *
     * @param width The new width.
     */
    public void setWidth(float width) {
        setWidth(new Literal(width));
    }

    /**
     * Set the width used to draw the lines with this {@code PenStroke}.
     *
     * @param width The new width.
     */
    public void setWidth(ParameterValue width) {
        if (width == null) {
            this.width = new NullParameterValue();
            this.width.setParent(this);
        } else {
            this.width = width;
            this.width.setParent(this);
            this.width.format(Float.class);
        }
    }

    /**
     * Gets the width used to draw the lines with this PenStroke.
     *
     * @return
     */
    public ParameterValue getWidth() {
        return width;
    }

    /**
     * Gets the offset let before drawing the first dash.
     *
     * @return The offset let before drawing the first dash.
     */
    public ParameterValue getDashOffset() {
        return dashOffset;
    }

    /**
     * Sets the offset let before drawing the first dash.
     *
     * @param dashOffset
     */
    public void setDashOffset(float dashOffset) {
        setDashOffset(new Literal(dashOffset));
    }

    /**
     * Sets the offset let before drawing the first dash.
     *
     * @param dashOffset.
     */
    public void setDashOffset(ParameterValue dashOffset) {
        if (dashOffset == null) {
            this.dashOffset = new NullParameterValue();
            this.dashOffset.setParent(this);
        } else {
            this.dashOffset = dashOffset;
            this.dashOffset.setParent(this);
            this.dashOffset.format(Float.class);
        }
    }

    /**
     * Gets the array of double values that will be used to draw a dashed line.
     * This "array" is in fact stored as a string parameter, filled with space
     * separated double values.</p>
     * <p>
     * These values represent the length (in the inner UOM) of the opaque (even
     * elements of the array) and transparent (odd elements of the array) parts
     * of the lines to draw.
     *
     * @return
     */
    public ParameterValue getDashArray() {
        return dashArray;
    }

    /**
     * Sets the array of double values that will be used to draw a dashed line.
     * This "array" is in fact stored as a string parameter, filled with space
     * separated double values.</p>
     * <p>
     * These values represent the length (in the inner UOM) of the opaque (even
     * elements of the array) and transparent (odd elements of the array) parts
     * of the lines to draw.
     *
     * @param dashArray The new dash array.
     */
    public void setDashArray(String dashArray) {
        setDashArray(new Literal(dashArray));
    }

    /**
     * Sets the array of double values that will be used to draw a dashed line.
     * This "array" is in fact stored as a string parameter, filled with space
     * separated double values.</p>
     * <p>
     * These values represent the length (in the inner UOM) of the opaque (even
     * elements of the array) and transparent (odd elements of the array) parts
     * of the lines to draw.
     *
     * @param dashArray The new dash array.
     */
    public void setDashArray(ParameterValue dashArray) {
        if (dashArray == null) {
            this.dashArray = new NullParameterValue();
            this.dashArray.setParent(this);
        } else {
            this.dashArray = dashArray;
            this.dashArray.setParent(this);
            this.dashArray.format(String.class);
        }
    }
}
