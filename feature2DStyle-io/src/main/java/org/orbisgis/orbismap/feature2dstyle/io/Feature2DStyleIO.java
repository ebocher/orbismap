/**
 * Feature2DStyle-IO is part of the OrbisGIS platform
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
 * Feature2DStyle-IO  is distributed under LGPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2020 CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Feature2DStyle-IO  is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Feature2DStyle-IO  is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * Feature2DStyle-IO . If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.orbismap.feature2dstyle.io;

import org.orbisgis.orbismap.feature2dstyle.io.converter.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.orbisgis.style.Feature2DStyle;
import org.orbisgis.style.IFeatureSymbolizer;
import org.orbisgis.style.StyleNode;
import org.orbisgis.style.graphic.graphicSize.ViewBox;
import org.orbisgis.style.parameter.Expression;
import org.orbisgis.style.parameter.Literal;
import org.orbisgis.style.parameter.NullParameterValue;
import org.orbisgis.style.parameter.ParameterValue;

/**
 *
 * @author ebocher
 */
public class Feature2DStyleIO {

    /**
     * Save any style node to an xml file
     *
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static Feature2DStyle fromXML(File file) throws FileNotFoundException {
        if (file != null && isExtensionWellFormated(file, "se")) {
            XStream xstream = new XStream();
            xstream.registerConverter(new Feature2DStyleConverter());
            xstream.registerConverter(new Feature2DRuleConverter());
            xstream.registerConverter(new AreaSymbolizerConverter());
            xstream.registerConverter(new LineSymbolizerConverter());
            xstream.registerConverter(new PointSymbolizerConverter());
            xstream.registerConverter(new TextSymbolizerConverter());
            xstream.registerConverter(new PenStrokeConverter());
            xstream.registerConverter(new ViewBoxConverter());
            xstream.registerConverter(new GraphicSizeConverter());
            xstream.registerConverter(new SolidFillConverter());
            xstream.registerConverter(new MarkGraphicConverter());
            xstream.registerConverter(new HaloConverter());
            xstream.registerConverter(new DescriptionConverter());
            xstream.registerConverter(new UomConverter());
            xstream.registerConverter(new GeometryParameterConverter());
            xstream.registerConverter(new RGBColorConverter());
            xstream.registerConverter(new HexaColorConverter());
            xstream.registerConverter(new WellknownNameColorConverter());
            xstream.alias("Feature2DStyle", Feature2DStyle.class);
            return (Feature2DStyle) xstream.fromXML(new FileInputStream(file));
        } else {
            throw new RuntimeException("Invalid input file path. Use a se extension file name.");
        }
    }

    /**
     * Save any style node to an xml file
     *
     * @param styleNode
     * @param file
     * @throws FileNotFoundException
     */
    public static void toXML(StyleNode styleNode, File file) throws FileNotFoundException {
        if (file != null && isExtensionWellFormated(file, "se")) {
            XStream xstream = new XStream();
            xstream.registerConverter(new Feature2DStyleConverter());
            xstream.registerConverter(new Feature2DRuleConverter());
            xstream.registerConverter(new AreaSymbolizerConverter());
            xstream.registerConverter(new LineSymbolizerConverter());
            xstream.registerConverter(new PointSymbolizerConverter());
            xstream.registerConverter(new TextSymbolizerConverter());
            xstream.registerConverter(new PenStrokeConverter());
            xstream.registerConverter(new ViewBoxConverter());
            xstream.registerConverter(new GraphicSizeConverter());
            xstream.registerConverter(new SolidFillConverter());
            xstream.registerConverter(new MarkGraphicConverter());
            xstream.registerConverter(new HaloConverter());
            xstream.registerConverter(new DescriptionConverter());
            xstream.registerConverter(new UomConverter());
            xstream.registerConverter(new GeometryParameterConverter());
            xstream.registerConverter(new RGBColorConverter());
            xstream.registerConverter(new HexaColorConverter());
            xstream.registerConverter(new WellknownNameColorConverter());
            xstream.alias("Feature2DStyle", Feature2DStyle.class);
            xstream.toXML(styleNode, new FileOutputStream(file));
        } else {
            throw new RuntimeException("Invalid ouput file path. Use a se extension file name.");
        }
    }

    /**
     * Save any style node to a json file
     *
     * @param styleNode
     * @param file
     * @throws FileNotFoundException
     */
    public static void toJSON(StyleNode styleNode, File file) throws FileNotFoundException {
        if (file != null && isExtensionWellFormated(file, "se")) {
            XStream xstream = new XStream(new JettisonMappedXmlDriver());
            xstream.setMode(XStream.NO_REFERENCES);
            xstream.registerConverter(new Feature2DStyleConverter());
            xstream.registerConverter(new Feature2DRuleConverter());
            xstream.registerConverter(new AreaSymbolizerConverter());
            xstream.registerConverter(new LineSymbolizerConverter());
            xstream.registerConverter(new PointSymbolizerConverter());
            xstream.registerConverter(new TextSymbolizerConverter());
            xstream.registerConverter(new PenStrokeConverter());
            xstream.registerConverter(new SolidFillConverter());
            xstream.registerConverter(new ViewBoxConverter());
            xstream.registerConverter(new GraphicSizeConverter());
            xstream.registerConverter(new MarkGraphicConverter());
            xstream.registerConverter(new HaloConverter());
            xstream.registerConverter(new DescriptionConverter());
            xstream.registerConverter(new UomConverter());
            xstream.registerConverter(new GeometryParameterConverter());
            xstream.registerConverter(new RGBColorConverter());
            xstream.registerConverter(new HexaColorConverter());
            xstream.registerConverter(new WellknownNameColorConverter());
            xstream.alias("Feature2DStyle", Feature2DStyle.class);
            xstream.toXML(styleNode, new FileOutputStream(file));
        } else {
            throw new RuntimeException("Invalid ouput file path. Use a json extension file name.");
        }
    }

    /**
     * Method to marshall a parameter value
     *
     * @param fielName
     * @param parameterValue
     * @param writer
     */
    public static void marshalParameterValue(String fielName, ParameterValue parameterValue, HierarchicalStreamWriter writer) {
        if (parameterValue != null && !(parameterValue instanceof NullParameterValue)) {
            if (parameterValue instanceof Literal) {
                String valuetoWrite = String.valueOf(parameterValue.getValue());
                if (!valuetoWrite.isEmpty()) {
                    writer.startNode(fielName);
                    writer.setValue(valuetoWrite);
                    writer.endNode();
                }
            } else if (parameterValue instanceof Expression) {
                String valuetoWrite = String.valueOf(((Expression) parameterValue).getExpression());
                if (!valuetoWrite.isEmpty()) {
                    writer.startNode(fielName);
                    writer.startNode("Expression");
                    writer.setValue(valuetoWrite);
                    writer.endNode();
                    writer.endNode();
                }
            }
        }
    }

    /**
     * Marshal commun symbolizer properties
     *
     * @param symbolizer
     * @param writer
     * @param mc
     */
    public static void marshalSymbolizerMetadata(IFeatureSymbolizer symbolizer, HierarchicalStreamWriter writer, MarshallingContext mc) {
        String name = symbolizer.getName();
        if (name != null && !name.isEmpty()) {
            writer.startNode("Name");
            writer.setValue(name);
            writer.endNode();
        }
        Feature2DStyleIO.convertAnother(mc, symbolizer.getUom());
        Feature2DStyleIO.convertAnother(mc, symbolizer.getDescription());
        Feature2DStyleIO.convertAnother(mc, symbolizer.getGeometryParameter());
        Feature2DStyleIO.marshalParameterValue("PerpendicularOffset", symbolizer.getPerpendicularOffset(), writer);

        writer.startNode("Level");
        writer.setValue(String.valueOf(symbolizer.getLevel()));
        writer.endNode();

    }

    /**
     *
     * @param mc
     * @param styleNode
     */
    public static void convertAnother(MarshallingContext mc, Object styleNode) {
        if (styleNode != null) {
            mc.convertAnother(styleNode);
        }
    }

    /**
     * Check if the file has the good extension
     *
     * @param file
     * @param prefix
     * @return
     */
    public static boolean isExtensionWellFormated(File file, String prefix) {
        String path = file.getAbsolutePath();
        String extension = "";
        int i = path.lastIndexOf('.');
        if (i >= 0) {
            extension = path.substring(i + 1);
        }
        return extension.equalsIgnoreCase(prefix);
    }

    public static ParameterValue createParameterValue(HierarchicalStreamReader reader) {
        String value = reader.getValue();
        ParameterValue parameterValue = new Literal(value);
        if (reader.hasMoreChildren()) {
            reader.moveDown();
            if ("expression".equalsIgnoreCase(reader.getNodeName())) {
                parameterValue = new Expression(reader.getValue());
            }
            reader.moveUp();
        }
        return parameterValue;
    }

    public static ViewBox createViewBox(HierarchicalStreamReader reader) {
        ViewBox viewBox = new ViewBox();
        while (reader.hasMoreChildren()) {
            reader.moveDown();
            if ("height".equalsIgnoreCase(reader.getNodeName())) {
                viewBox.setHeight(new Literal(Float.parseFloat(reader.getValue())));
            } else if ("width".equalsIgnoreCase(reader.getNodeName())) {
                viewBox.setWidth(new Literal(reader.getValue()));
            }
            reader.moveUp();
        }
        return viewBox;
    }
}
