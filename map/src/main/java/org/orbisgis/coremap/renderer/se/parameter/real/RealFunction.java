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
package org.orbisgis.coremap.renderer.se.parameter.real;

import java.sql.ResultSet;
import java.util.*;
import org.orbisgis.coremap.renderer.se.AbstractSymbolizerNode;
import org.orbisgis.coremap.renderer.se.SymbolizerNode;
import org.orbisgis.coremap.renderer.se.parameter.ParameterException;

/**
 * Defines a function on real numbers. A function is defined with a operation and
 * a set of operands. Available operations are :<br/>
 *   * addition - <code>ADD</code><br/>
 *   * Multiplication - <code>MUL</code><br/>
 *   * Division - <code>DIV</code><br/>
 *   * Substraction - <code>SUB</code><br/>
 *   * Square root - <code>SQRT</code><br/>
 *   * Decimal logarithm - <code>LOG</code><br/>
 *   * Neperian logarithm - <code>LN</code>
 * @author Maxence Laurent, Alexis Guéganno
 */
public class RealFunction extends AbstractSymbolizerNode implements  RealParameter {

    public enum Operators {
        ADD, MUL, DIV, SUB, SQRT, LOG, LN
    };

    private Operators op;
    private RealParameterContext ctx;
    private ArrayList<RealParameter> operands;

    /**
     * Builds an empty <code>RealFunction</code>, where only the operation
     * is defined.
     * @param operator
     */
    public RealFunction(Operators operator){
            op = operator;
            operands = new ArrayList<RealParameter>();
    }

    /**
     * Builds an empty <code>RealFunction</code>, where only the name of the operation
     * is defined.
     * @param name 
     */
    public RealFunction(String name) {
        ctx = RealParameterContext.REAL_CONTEXT;
        this.op = Operators.valueOf(name.toUpperCase());
        operands = new ArrayList<RealParameter>();
    }

    
    /**
     * Get the instance of {@code Operators} associated to this {@code RealFunction}.
     * @return 
     */
    public Operators getOperator() {
            return op;
    }

    /**
     * Gets the list of operands
     * @return
     */
    public List<RealParameter> getOperands() {
        return operands;
    }

    /**
     * Return i'th operand
     *
     * @param i
     * @return the real parameter
     * @throws IndexOutOfBoundsException if i is out of bounds
     */
    public RealParameter getOperand(int i){
        return operands.get(i);
    }

    /**
     * Add a new operand
     * @param operand the new operand to add
     * @throws ParameterException if this function doesn't support more
     */
    public void addOperand(RealParameter operand) throws ParameterException {
        switch (op) {
            case ADD:
            case MUL:
                this.operands.add(operand);
                operand.setParent(this);
                return;
            case DIV:
            case SUB:
                if (operands.size() < 2) {
                    this.operands.add(operand);
                    operand.setParent(this);
                } else {
                    throw new ParameterException(op + " requires exactly two operands");
                }
                return;
            case SQRT:
            case LN:
            case LOG:
                if (operands.size() < 1) {
                    this.operands.add(operand);
                    operand.setParent(this);
                } else {
                    throw new ParameterException(op + " requires exactly one operand");
                }
                return;
        }
    }

    @Override
    public Double getValue(ResultSet rs, long fid) throws ParameterException {
        List<Double>  vals = new LinkedList<Double>();
        for(RealParameter p : operands){
            vals.add(p.getValue(rs, fid));
        }
        return getValue(vals);
    }

    @Override
    public Double getValue(Map<String,Object> map)throws ParameterException {
        List<Double>  vals = new LinkedList<Double>();
        for(RealParameter p : operands){
            vals.add(p.getValue(map));
        }
        return getValue(vals);

    }

    private Double getValue(List<Double> vals) throws ParameterException {
        double result;
        switch (op) {
            case ADD:
                result = 0.0;
                for (Double p : vals) {
                    result += p;
                }
                return result;
            case MUL:
                result = 1.0;
                for (Double p : vals) {
                    result *= p;
                }
                return result;
            case DIV:
                if (vals.size() != 2) {
                    throw new ParameterException("A division requires two arguments !");
                }
                return vals.get(0) / vals.get(1);
            case SUB:
                if (vals.size() != 2) {
                    throw new ParameterException("A subtraction requires two arguments !");
                }
                return vals.get(0) / vals.get(1);
            case SQRT:
                if (vals.size() != 1) {
                    throw new ParameterException("A Square-root requires one argument !");
                }
                return Math.sqrt(vals.get(0));
            case LOG:
                if (vals.size() != 1) {
                    throw new ParameterException("A Log10 requires one argument !");
                }
                return Math.log10(vals.get(0));
            case LN:
                if (vals.size() != 1) {
                    throw new ParameterException("A natural logarithm requires one argument !");
                }
                return Math.log(vals.get(0));
        }
        throw new ParameterException("Unknown function name: " + op.toString());

    }

    @Override
    public String toString() {
        String result = op.toString() + "(";
        for (int i = 0; i < operands.size(); i++) {
            result += operands.get(i).toString();
            if (i < operands.size() - 1) {
                result += ",";
            }
        }
        result += ")";
        return result;
    }

    @Override
    public void setContext(RealParameterContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public RealParameterContext getContext() {
        return ctx;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

    

    @Override
    public List<SymbolizerNode> getChildren() {
        List<SymbolizerNode> ls =new ArrayList<SymbolizerNode>();
        ls.addAll(operands);
        return ls;
    }


}
