package org.orbisgis.orbismap.se.converter;

import com.thoughtworks.xstream.XStream;
import org.orbisgis.orbismap.style.Feature2DStyle;
import org.orbisgis.orbismap.style.Feature2DStyleTerms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SymbologyEncodingIO {


    /**
     * Load Feature2Style converts
     *
     * @param xstream
     */
    private static void registerConverter( XStream xstream){
        xstream.registerConverter(new FeatureTypeStyleConverter());
        xstream.registerConverter(new Feature2DRuleConverter());
        xstream.registerConverter(new RuleFilterConverter());
        xstream.registerConverter(new AreaSymbolizerConverter());
        xstream.registerConverter(new LineSymbolizerConverter());
        xstream.registerConverter(new PointSymbolizerConverter());
        xstream.registerConverter(new TextSymbolizerConverter());
        xstream.registerConverter(new PenStrokeConverter());
        xstream.registerConverter(new WobbleStrokeConverter());
        xstream.registerConverter(new ViewBoxConverter());
        xstream.registerConverter(new GraphicSizeConverter());
        xstream.registerConverter(new SolidFillConverter());
        xstream.registerConverter(new MarkGraphicConverter());
        xstream.registerConverter(new HaloConverter());
        xstream.registerConverter(new DescriptionConverter());
        xstream.registerConverter(new GeometryParameterConverter());
        xstream.registerConverter(new RGBColorConverter());
        xstream.registerConverter(new HexaColorConverter());
        xstream.registerConverter(new WellknownNameColorConverter());
        xstream.registerConverter(new ParameterValueConverter());
        xstream.alias(Feature2DStyleTerms.FEATURE2DSTYLE, Feature2DStyle.class);
    }

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
            registerConverter(xstream);
            return (Feature2DStyle) xstream.fromXML(new FileInputStream(file));
        } else {
            throw new RuntimeException("Invalid input file path. Use a se extension file name.");
        }
    }
}
