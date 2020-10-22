package org.orbisgis.orbismap.map.renderer.legend;

import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import org.orbisgis.orbismap.map.renderer.featureStyle.ISymbolizerDraw;
import org.orbisgis.orbismap.style.IStyleNode;
import org.orbisgis.orbismap.style.IStyleNodeVisitor;
import org.orbisgis.orbismap.style.parameter.Expression;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class LegendValueVisitor implements IStyleNodeVisitor {

    private final ISymbolizerDraw iSymbolizerDraw;
    private HashMap<String, Expression> expressionsProperties = new HashMap<String, org.orbisgis.orbismap.style.parameter.Expression>();

    private HashMap<String, String> expression_parameters = new HashMap<String, String>();

    private int count = 0;

    public LegendValueVisitor(ISymbolizerDraw iSymbolizerDraw){
        this.iSymbolizerDraw=iSymbolizerDraw;
    }
    /**
     * Recursively visits {@code sn} and all its children, searching for
     * feature-dependant nodes.
     *
     * @param sn
     */
    @Override
    public void visitSymbolizerNode(IStyleNode sn) {
        try {
            visitImpl(sn);
        } catch (Exception ex) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * The method that does the work...It is not callable directly by the
     * clients, as it does not clean the inner HashSet. If you want to use it
     * directly, inherit this class.
     *
     * @param sn
     * @throws java.lang.Exception
     */
    protected void visitImpl(IStyleNode sn) throws Exception {
        List<IStyleNode> children = sn.getChildren();
        if (sn instanceof org.orbisgis.orbismap.style.parameter.Expression) {
            org.orbisgis.orbismap.style.parameter.Expression exp =  (org.orbisgis.orbismap.style.parameter.Expression) sn;
            if (exp!=null) {
                net.sf.jsqlparser.expression.Expression expParsed = CCJSqlParserUtil.parseExpression(exp.getExpression(), false);
                if(expParsed instanceof CaseExpression){
                    //Create shape data and populate values
                }
                else {
                    //Default shape at a position
                }
            }
        }
        children.forEach((c) -> {
            try {
                visitImpl(c);
            } catch (Exception ex) {
                throw new RuntimeException("Cannot parse the expression for the node : "+ c.getClass().getSimpleName());
            }
        });
    }

    public HashMap<String, String> getExpressionParameters() {
        return expression_parameters;
    }

    public String getExpressionParametersAsString() {
        return expression_parameters.entrySet().stream().
                map(entrySet -> entrySet.getKey()+ " as " + entrySet.getValue()).
                collect(Collectors.joining(","));
    }

    public HashMap<String, org.orbisgis.orbismap.style.parameter.Expression> getExpressionsProperties() {
        return expressionsProperties;
    }
}
