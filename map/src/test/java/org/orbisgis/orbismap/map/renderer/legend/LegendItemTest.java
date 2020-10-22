package org.orbisgis.orbismap.map.renderer.legend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LegendItemTest {
    @Test
    public void testCreateLegend(TestInfo testInfo) throws IOException {
        Map<RenderingHints.Key, Object> hints = new HashMap<>();
        hints.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        hints.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        RenderingHints screenHints = new RenderingHints(hints);
        BufferedImage image = new BufferedImage(200, 200,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.addRenderingHints(screenHints);
        LegendItem mapLegend = new LegendItem(20, 20, 200, 200 - 40).setTitle("OrbisGIS");


        mapLegend.draw(g2);
        File savePath = new File("./target" + File.separator+testInfo.getDisplayName()+".png");
        ImageIO.write(image, "png", savePath);
    }

    }
