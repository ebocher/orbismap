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
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2020 CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Feature2DStyle is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Feature2DStyle is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * Feature2DStyle. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.orbismap.style.label;

import java.util.ArrayList;
import java.util.List;
import org.orbisgis.orbismap.style.IStyleNode;
import org.orbisgis.orbismap.style.Uom;
import org.orbisgis.orbismap.style.parameter.Literal;
import org.orbisgis.orbismap.style.parameter.NullParameterValue;
import org.orbisgis.orbismap.style.parameter.ParameterValue;

/**
 * An {@code ExclusionZone} where the forbidden area is defined as a circle. It
 * is defined thanks to radius value. Its meaning is of course dependant of the
 * inner UOM instance. The resulting exclusion zone is a circle centered on the
 * point associated to the {@code LabelPoint},
 *
 *
 * @author Maxence Laurent, HEIG-VD (2010-2012)
 * @author Erwan Bocher, CNRS (2010-2020)
 */
public class ExclusionRadius extends ExclusionZone {
    
    private ParameterValue radius = new NullParameterValue();
    
    //In mm
    public static float DEFAULT_SIZE = 3;

    /**
     * Build a new {@code ExclusionRadius}.
     */
    public ExclusionRadius() {
    }

    /**
     * Get the radius defining this {@code ExclusionRadius}
     *
     * @return The radius as a {@code RealParameter}.
     */
    public ParameterValue getRadius() {
        return radius;
    }

    /**
     * Set the radius defining this {@code ExclusionRadius}
     *
     * @param radius
     */
    public void setRadius(float radius) {
        setRadius(new Literal(radius));
    }

    /**
     * Set the radius defining this {@code ExclusionRadius}
     *
     * @param radius
     */
    public void setRadius(ParameterValue radius) {
        if (radius == null) {
            this.radius = new NullParameterValue();
            this.radius.setParent(this);
        } else {
            this.radius = radius;
            this.radius.setParent(this);
            this.radius.format(Float.class, "value>=0");
        }
    }
    
    @Override
    public List<IStyleNode> getChildren() {
        List<IStyleNode> ls = new ArrayList<IStyleNode>();
        if (radius != null) {
            ls.add(radius);
        }
        return ls;
    }

    @Override
    public void initDefault() {
        setUom(Uom.MM);
        setRadius(DEFAULT_SIZE);
    }
}
