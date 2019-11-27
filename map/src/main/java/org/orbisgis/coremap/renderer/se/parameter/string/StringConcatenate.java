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
package org.orbisgis.coremap.renderer.se.parameter.string;

import java.sql.ResultSet;
import java.util.*;


import org.orbisgis.coremap.renderer.se.AbstractSymbolizerNode;
import org.orbisgis.coremap.renderer.se.SymbolizerNode;
import org.orbisgis.coremap.renderer.se.parameter.ParameterException;

/**
 * Implementation of the {@code Concatenate} SE function. This function takes at
 * least to String in input, and simply concatenates them. it is consequently
 * only dependant on a list of {@code StringParameter} instances.</p>
 * <p>This class embedded a set of {@code StringParameter} instances, and can
 * be seen as a simplified list. It implements {@code Iterable} to ease the
 * processing of its content.
 * @author Alexis Guéganno
 */
public class StringConcatenate extends AbstractSymbolizerNode implements StringParameter, Iterable<StringParameter> {

        private List<StringParameter> inputStrings;

       
        

        @Override
        public String getValue(ResultSet rs, long fid) throws ParameterException {
                List<String> inputs = new LinkedList<String>();
                int expectedSize = 0;
                for(StringParameter sp : inputStrings){
                        String tmp = sp.getValue(rs, fid);
                        inputs.add(tmp);
                        expectedSize+=tmp.length();
                }
                StringBuilder sb = new StringBuilder(expectedSize);
                for(String temps : inputs){
                        sb.append(temps);
                }
                return sb.toString();
        }

        @Override
        public String getValue(Map<String, Object> map) throws ParameterException {
                List<String> inputs = new LinkedList<String>();
                int expectedSize = 0;
                for(StringParameter sp : inputStrings){
                        String tmp = sp.getValue(map);
                        inputs.add(tmp);
                        expectedSize+=tmp.length();
                }
                StringBuilder sb = new StringBuilder(expectedSize);
                for(String temps : inputs){
                        sb.append(temps);
                }
                return sb.toString();
        }

        @Override
        public void setRestrictionTo(String[] list) {
        }

        
        /**
         * Gets the number of StringParameter that are concatenated using this
         * function.
         * @return
         */
        public int size() {
                return inputStrings.size();
        }

        /**
         * Add a {@code StringParameter} to the input of this function.
         * @param e
         * @return
         */
        public boolean add(StringParameter e) {
                e.setParent(this);
                return inputStrings.add(e);
        }

        /**
         * Remove the first found obejct equals to {@code o} from the list of
         * inputs of this function.
         * @param o
         * @return {@code true} if some element has been removed.
         * @throws
         *      {@code ClassCastException} - if the type of the specified
         *      element is incompatible with this list
         */
        public boolean remove(Object o) {
                StringParameter sp = (StringParameter) o;
                return inputStrings.remove(sp);
        }

        /**
         * Reset the list of this function's inputs.
         */
        public void clear() {
                inputStrings.clear();
        }

        /**
         * Get the ith {@code StringParameter} to be concatenated.
         * @param index
         * @return
         */
        public StringParameter get(int index) {
                return inputStrings.get(index);
        }

        /**
         * Set the ith element to be concatenated to {@code element}.
         * @param index
         * @param element
         * @return
         *      the element that was previously at position {@code index}.
         * @throws
         *      {@code IndexOutOfBoundsException} - if the index is out of range
         *      {@code (index < 0 || index >= size()}).
         */
        public StringParameter set(int index, StringParameter element) {
                element.setParent(this);
                return inputStrings.set(index, element);
        }

        /**
         * Add (insert) {@code element} at the specified position.
         * @param index
         * @param element
         * @throws
         *      {@code IndexOutOfBoundsException} - if the index is out of range
         *      {@code (index < 0 || index > size()}).
         */
        public void add(int index, StringParameter element) {
                element.setParent(this);
                inputStrings.add(index, element);
        }

        /**
         * Remove the {@code StringParameter} registered at position {@code
         * index}.
         * @param index
         * @return
         * the removed StringParameter.
         * @throws
         *      {@code IndexOutOfBoundsException} - if the index is out of range
         *      {@code (index < 0 || index >= size()}).
         */
        public StringParameter remove(int index) {
                return inputStrings.remove(index);
        }

        /**
         * Gets a {@code ListIterator} representation of the underlying set of
         * {@code StringParameter} instances.
         * @return
         */
        public ListIterator<StringParameter> listIterator() {
                return inputStrings.listIterator();
        }

        @Override
        public Iterator<StringParameter> iterator() {
                return inputStrings.listIterator();
        }

        @Override
        public List<SymbolizerNode> getChildren() {
            List<SymbolizerNode> ls =new ArrayList<SymbolizerNode>();
            ls.addAll(inputStrings);
            return ls;
        }

}
