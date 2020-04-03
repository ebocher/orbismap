/**
 * SYMCORE is part of the OrbisGIS platform.
 * 
 * It's implement the OGC Symbology Conceptual Model: Core part.
 * 
 * Internal reference number of this OGC ® document: 18-067
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
 * SYMCORE is distributed under LGPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2020 CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * SYMCORE is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * SYMCORE is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * SYMCORE. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.style;

import java.util.List;

/**
 * This class is the root concept of the Symbology Conceptual Core Model.This
 class organizes the rules of symbolizing instructions to be applied by a
 rendering engine on a layer of geographic features (e.g., vector based
 spatial data or raster data). As an abstract class, it is designed to be
 extended (e.g., the FeatureTypeStyle extension for vector data).
 *
 * @author Erwan Bocher
 * @param <T>
 */
public interface IStyle<T extends IRule> extends IDescription, IName{
    
           

    /**
     * Gets the list of {@link IRule} contained in this Style.
     *
     * @return
     */
    List<T> getRules();  
    
    /**
     * Add a {@link IRule} to this {@code IStyle}.
     * @param rule to add
     */
    void addRule(T rule);
    
    /**
     * Add a {@link IRule} to this {@code IStyle} at position {@code index}.
     * @param index of the rule
     * @param rule to add
     */
     void addRule(int index, T rule);
     
     /**
     * Delete the {@link IRule} from this {@code IStyle}.
     * @param index of the rule 
     * @return true if the rule has been deleted
     */
     boolean deleteRule(int index);
     
      /**
     * Moves the ith {@link IRule} to position i+1 in the list of rules.
     * @param index of the rule
     * @return
     */
     public boolean moveRuleDown(int index);

}
