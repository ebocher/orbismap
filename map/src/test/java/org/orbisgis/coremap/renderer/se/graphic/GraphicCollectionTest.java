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
package org.orbisgis.coremap.renderer.se.graphic;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.orbisgis.coremap.renderer.se.FeatureStyle;

/**
 *
 * @author Maxence Laurent
 */
public class GraphicCollectionTest {

    private FeatureStyle fts;

    /*
    public void variousGraphicDisplay() throws IOException, ParameterException, InvalidStyle {
        JFrame frame = new JFrame();
        frame.setTitle("Test GraphicCollection");

        // Get the JFrame’s ContentPane.
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());

        // Create an instance of DisplayJAI.
        DisplayJAI dj = new DisplayJAI();

        System.out.println(dj.getColorModel());

        fts = new Style(null, "src/test/resources/org/orbisgis/core/renderer/se/graphics.se");
        PointSymbolizer ps = (PointSymbolizer) fts.getRules().get(0).getCompositeSymbolizer().getSymbolizerList().get(0);
        GraphicCollection collec = ps.getGraphicCollection();


		MapTransform mt = new MapTransform();
        double width = Uom.toPixel(270, Uom.MM, mt.getDpi(), null, null);
        double height = Uom.toPixel(160, Uom.MM, mt.getDpi(), null, null);


        //Rectangle2D.Double dim = new Rectangle2D.Double(-width/2, -height/2, width, height);
        BufferedImage img = new BufferedImage((int)width, (int)height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D rg = img.createGraphics();
        rg.setRenderingHints(mt.getRenderingHints());

        collec.draw(rg, null, false, mt, AffineTransform.getTranslateInstance(width/2, height/2));

        rg.setStroke(new BasicStroke(1));
        rg.setPaint(Color.BLACK);

        rg.drawLine(0, (int)height/2, (int)width, (int)height/2);
        rg.drawLine((int)width/2, 0, (int)width/2, (int)height);

        dj.setBounds(0, 0, (int)width, (int)height);
        //dj.setBounds((int)rg.getMinX(), (int)rg.getMinY(), (int)rg.getWidth(), (int)rg.getHeight());

        //RenderedImage r = rg.createRendering(mt.getCurrentRenderContext());

        dj.set(img, 0,0);

        File file = new File("/tmp/graphics.png");
        ImageIO.write(img, "png", file);

        // Add to the JFrame’s ContentPane an instance of JScrollPane
        // containing the DisplayJAI instance.
        //contentPane.add(new JScrollPane(dj), BorderLayout.CENTER);
        contentPane.add(dj, BorderLayout.CENTER);

        // Set the closing operation so the application is finished.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize((int)width, (int)height+24); // adjust the frame size.
        frame.setVisible(true); // show the frame.

        
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(GraphicCollectionTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        assertTrue(true);
    }*/

    @Test
    public void testMoveUp() throws Exception{
            GraphicCollection gc = new GraphicCollection();
            PointTextGraphic ptg1 = new PointTextGraphic();
            PointTextGraphic ptg2 = new PointTextGraphic();
            PointTextGraphic ptg3 = new PointTextGraphic();
            PointTextGraphic ptg4 = new PointTextGraphic();
            gc.addGraphic(ptg1);
            gc.addGraphic(ptg2);
            gc.addGraphic(ptg3);
            gc.addGraphic(ptg4);
            assertEquals(gc.getNumGraphics(), 4);
            assertTrue(gc.getGraphic(0) == ptg1);
            assertTrue(gc.getGraphic(1) == ptg2);
            assertTrue(gc.getGraphic(2) == ptg3);
            assertTrue(gc.getGraphic(3) == ptg4);
            //We move ptg2 up
            gc.moveGraphicUp(1);
            assertTrue(gc.getGraphic(0) == ptg2);
            assertTrue(gc.getGraphic(1) == ptg1);
            assertTrue(gc.getGraphic(2) == ptg3);
            assertTrue(gc.getGraphic(3) == ptg4);
            //We move ptg2 up. Nothing is supposed to happen, as it is the uppest element.
            gc.moveGraphicUp(0);
            assertTrue(gc.getGraphic(0) == ptg2);
            assertTrue(gc.getGraphic(1) == ptg1);
            assertTrue(gc.getGraphic(2) == ptg3);
            assertTrue(gc.getGraphic(3) == ptg4);
            //We move ptg4 up. 
            gc.moveGraphicUp(3);
            assertTrue(gc.getGraphic(0) == ptg2);
            assertTrue(gc.getGraphic(1) == ptg1);
            assertTrue(gc.getGraphic(2) == ptg4);
            assertTrue(gc.getGraphic(3) == ptg3);
    }

    @Test
    public void testMoveDown() throws Exception{
            GraphicCollection gc = new GraphicCollection();
            PointTextGraphic ptg1 = new PointTextGraphic();
            PointTextGraphic ptg2 = new PointTextGraphic();
            PointTextGraphic ptg3 = new PointTextGraphic();
            PointTextGraphic ptg4 = new PointTextGraphic();
            gc.addGraphic(ptg1);
            gc.addGraphic(ptg2);
            gc.addGraphic(ptg3);
            gc.addGraphic(ptg4);
            assertEquals(gc.getNumGraphics(), 4);
            //We move ptg2 down
            gc.moveGraphicDown(1);
            assertTrue(gc.getGraphic(0) == ptg1);
            assertTrue(gc.getGraphic(1) == ptg3);
            assertTrue(gc.getGraphic(2) == ptg2);
            assertTrue(gc.getGraphic(3) == ptg4);
            //We move ptg1 down
            gc.moveGraphicDown(0);
            assertTrue(gc.getGraphic(0) == ptg3);
            assertTrue(gc.getGraphic(1) == ptg1);
            assertTrue(gc.getGraphic(2) == ptg2);
            assertTrue(gc.getGraphic(3) == ptg4);
            //We move ptg1 down. Nothing is supposed to happen, as it is the lowest element.
            gc.moveGraphicDown(3);
            assertTrue(gc.getGraphic(0) == ptg3);
            assertTrue(gc.getGraphic(1) == ptg1);
            assertTrue(gc.getGraphic(2) == ptg2);
            assertTrue(gc.getGraphic(3) == ptg4);
            
    }
}
