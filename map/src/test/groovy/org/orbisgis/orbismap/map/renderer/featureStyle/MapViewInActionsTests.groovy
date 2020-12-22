/**
 * Map is part of the OrbisGIS platform
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
 * Map is distributed under LGPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488) Copyright (C) 2015-2020
 * CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * Map is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * Map is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Map. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly: info_at_ orbisgis.org
 */
package org.orbisgis.orbismap.map.renderer.featureStyle

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.TestInfo
import org.orbisgis.orbisdata.datamanager.jdbc.postgis.POSTGIS
import org.orbisgis.orbismap.map.layerModel.StyledLayer
import org.orbisgis.orbismap.map.renderer.MapView
import org.junit.jupiter.api.Test
import org.orbisgis.orbisdata.datamanager.api.dataset.ISpatialTable
import org.orbisgis.orbisdata.datamanager.jdbc.h2gis.H2GIS
import org.orbisgis.orbismap.map.renderer.featureStyle.utils.ExpressionParser
import org.orbisgis.orbismap.style.Feature2DRule
import org.orbisgis.orbismap.style.Feature2DStyle
import org.orbisgis.orbismap.feature2dstyle.io.Feature2DStyleIO
import org.orbisgis.orbismap.style.Uom
import org.orbisgis.orbismap.style.fill.DotMapFill
import org.orbisgis.orbismap.style.fill.SolidFill
import org.orbisgis.orbismap.style.graphic.MarkGraphic
import org.orbisgis.orbismap.style.graphic.graphicSize.ViewBox
import org.orbisgis.orbismap.style.parameter.Literal
import org.orbisgis.orbismap.style.parameter.NullParameterValue
import org.orbisgis.orbismap.style.stroke.PenStroke
import org.orbisgis.orbismap.style.symbolizer.AreaSymbolizer;

import java.awt.Color

import static org.junit.jupiter.api.Assertions.*
import static org.orbisgis.orbismap.style.stroke.PenStroke.DEFAULT_CAP
import static org.orbisgis.orbismap.style.stroke.PenStroke.DEFAULT_JOIN


class MapViewInActionsTests {

    @Disabled
    @Test
    void createEmptyMapView() throws Exception {
        MapView mapView = new MapView()
        assertNull(mapView.envelope)
        //TODO : assertEquals(0, mapView.layers.size)
    }

    @Test
    void createMapView(TestInfo testInfo) throws Exception {
        H2GIS h2GIS = H2GIS.open("./target/mapview")
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        inputFile="/home/ebocher/Autres/data/IGN/data_cadastre/parc_dgi/Parc_dgi.shp"
        def inputFile2="/home/ebocher/Autres/data/DONNEES RENNES/Reseau_Rennes.shp"
        //inputFile ="/home/ebocher/Documents/NextCloud/Groupe_SIG_Vannes/Projets_de_recherche/En_cours/2017_PAENDORA/Reunions/2017_11_08_Construction_USR/IRIS-GE_2-0__SHP_LAMB93_D056_2016-01-01/IRIS-GE/1_DONNEES_LIVRAISON_2017-05-00243/IRIS-GE_2-0_SHP_LAMB93_D056-2016/IRIS_GE.SHP"

        h2GIS.link(new File(inputFile), "PARCELS", true)
        h2GIS.link(new File(inputFile2), "ROADS", true)

        ISpatialTable spatialTable =h2GIS.getSpatialTable("PARCELS")
        ISpatialTable spatialTable2 =h2GIS.getSpatialTable("ROADS")

        MapView mapView = new MapView()
        Feature2DStyle style = StylesForTest.createLineSymbolizer(Color.ORANGE, 1, 0, Uom.PX);
        //StyledLayer styledLayer = new StyledLayer(spatialTable, style)
        Feature2DStyle style2 = StylesForTest.createAreaSymbolizer(Color.yellow, 1, 0, Color.BLACK, 1);
        StyledLayer styledLayer2 = new StyledLayer(spatialTable2, style)
        //mapView << styledLayer
        mapView << styledLayer2
        mapView.draw();
        println(mapView.getEnvelope().toString())
        mapView.show();
        mapView.save("./target"+File.separator+ testInfo.getDisplayName()+".png")
    }

    @Disabled
    @Test
    void createMapViewLinkedTable(TestInfo testInfo) throws Exception {
        H2GIS h2GIS = H2GIS.open("./target/mapview")
        h2GIS.execute("drop table if exists remoteTable; CREATE LINKED TABLE remoteTable ('org.h2gis.postgis_jts.Driver', 'jdbc:postgresql_h2://localhost:5432/orbisgis_db', 'orbisgis', 'orbisgis', 'public', '(SELECT st_buffer(the_geom, 20) FROM landcover)');");
        ISpatialTable spatialTable = h2GIS.getSpatialTable("remoteTable")
        MapView mapView = new MapView()
        Feature2DStyle style = StylesForTest.createAreaSymbolizer(Color.yellow, 1, 0,Color.BLACK,1 );
        StyledLayer styledLayer = new StyledLayer(spatialTable, style)
        mapView << styledLayer
        mapView.draw();
        mapView.save("./target"+File.separator+ testInfo.getDisplayName()+".png")
        //mapView.show();
    }


    @Test
    void createMapViewPostGISFilter(TestInfo testInfo) throws Exception {
        String url = "jdbc:postgresql://localhost:5432/orbisgis_db";
        Properties props = new Properties();
        props.setProperty("user", "orbisgis");
        props.setProperty("password", "orbisgis");
        props.setProperty("url", url);
        POSTGIS postgis = POSTGIS.open(props);
        if(postgis!=null){
            postgis.load(new File(this.getClass().getResource("landcover2000.shp").toURI()), "landcover", true)
            ISpatialTable spatialTable = postgis.getSpatialTable("(SELECT * FROM landcover LIMIT 10)")
            MapView mapView = new MapView()
            Feature2DStyle style = createStyle("st_area(the_geom)>10000", Color.ORANGE, Color.BLACK,2 )
            StyledLayer styledLayer = new StyledLayer(spatialTable, style)
            mapView << styledLayer
            mapView.draw();
            //mapView.show();
            mapView.save("./target"+File.separator+ testInfo.getDisplayName()+".png")
        }
    }

    @Test
    void createMapViewPostGIS(TestInfo testInfo) throws Exception {
        String url = "jdbc:postgresql://localhost:5432/orbisgis_db";
        Properties props = new Properties();
        props.setProperty("user", "orbisgis");
        props.setProperty("password", "orbisgis");
        props.setProperty("url", url);
        POSTGIS postgis = POSTGIS.open(props);
        if(postgis!=null){
            postgis.load(new File(this.getClass().getResource("landcover2000.shp").toURI()), "landcover", true)
            ISpatialTable spatialTable = postgis.getSpatialTable("landcover")
            MapView mapView = new MapView()
            Feature2DStyle style = createStyle("st_area(the_geom)>10000", Color.ORANGE, Color.BLACK,2 )
            StyledLayer styledLayer = new StyledLayer(spatialTable, style)
            mapView << styledLayer
            mapView.draw();
            //mapView.show();
            mapView.save("./target"+File.separator+ testInfo.getDisplayName()+".png")
        }
    }

    /**
     * A basic style
     **/
    def createStyle(def  filter, def fillColor, def strokeColor, def strokeWidth){
        Feature2DStyle style = new Feature2DStyle()
        AreaSymbolizer areaSymbolizer = new AreaSymbolizer()
        areaSymbolizer.initDefault()
        areaSymbolizer.setGeometryParameter("the_geom")
        SolidFill solidFill = new SolidFill()
        solidFill.setColor(fillColor)
        solidFill.setOpacity(1.0)
        areaSymbolizer.setFill(solidFill)
        PenStroke penStroke = new PenStroke();
        SolidFill solidFillS = new SolidFill()
        solidFillS.setColor(strokeColor)
        solidFillS.setOpacity(1.0)
        penStroke.setFill(solidFillS);
        penStroke.setWidth(new Literal(strokeWidth));
        penStroke.setDashOffset(new NullParameterValue());
        penStroke.setDashArray(new NullParameterValue());
        penStroke.setLineCap(DEFAULT_CAP);
        penStroke.setLineJoin(DEFAULT_JOIN);
        areaSymbolizer.setStroke(penStroke)
        Feature2DRule rule = new Feature2DRule()
        rule.addSymbolizer(areaSymbolizer)
        rule.setFilter(filter)
        style.addRule(rule)
        return style
    }

    @Test
    void mapViewReadStyle(TestInfo testInfo) throws Exception {
        H2GIS h2GIS = H2GIS.open("./target/mapview")
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        String inputStyle = new File(this.getClass().getResource("styles/single_symbol/single_symbol_map.json").toURI()).getAbsolutePath();
        h2GIS.link(new File(inputFile), "LANDCOVER", true)
        ISpatialTable spatialTable =h2GIS.getSpatialTable("LANDCOVER")
        MapView mapView = new MapView()
        Feature2DStyle style = Feature2DStyleIO.fromJSON(new File(inputStyle));
        StyledLayer styledLayer =new StyledLayer(spatialTable,style )
        mapView << styledLayer
        mapView.draw();
        mapView.save("./target"+File.separator+ testInfo.getDisplayName()+".png")
        //mapView.show();
    }

    @Test
    void displayDotMap(TestInfo testInfo) throws Exception {
        H2GIS h2GIS = H2GIS.open("./target/mapview")
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        h2GIS.link(inputFile, "LANDCOVER", true)
        ISpatialTable spatialTable = h2GIS.getSpatialTable("LANDCOVER")
        MapView mapView = new MapView()
        def fillColorExperssion = "CASE WHEN runoff_sum<=0.05 THEN '#1a9641'" +
                " WHEN runoff_sum>0.05 and runoff_sum<0.2 THEN '#ffffc0'" +
                " WHEN runoff_sum>=0.2 and runoff_sum<0.4 THEN '#fdae61'" +
                "ELSE '#d7191c' END "
        def dotQuantityExpression =  "ST_AREA(THE_GEOM)"
        Feature2DStyle style = StylesForTest.createAreaSymbolizerDotFillStyle(dotQuantityExpression, fillColorExperssion)
        StyledLayer styledLayer = new StyledLayer(spatialTable, style)
        mapView << styledLayer
        mapView.draw();
        mapView.save("./target"+File.separator+ testInfo.getDisplayName()+".png")
        Feature2DStyleIO.toJSON(style, new File("./target"+File.separator+ testInfo.getDisplayName()+".json"));
    }

    @Test
    void displayRailStyle(TestInfo testInfo) throws Exception {
        H2GIS h2GIS = H2GIS.open("./target/mapview")
        String inputFile = new File(this.getClass().getResource("landcover2000.shp").toURI()).getAbsolutePath();
        h2GIS.link(inputFile, "LANDCOVER", true)
        ISpatialTable spatialTable = h2GIS.getSpatialTable("LANDCOVER")
        MapView mapView = new MapView()
        def fillColorExperssion = "CASE WHEN runoff_sum<=0.05 THEN '#1a9641'" +
                " WHEN runoff_sum>0.05 and runoff_sum<0.2 THEN '#ffffc0'" +
                " WHEN runoff_sum>=0.2 and runoff_sum<0.4 THEN '#fdae61'" +
                "ELSE '#d7191c' END "
        def dotQuantityExpression =  "ST_AREA(THE_GEOM)"
        Feature2DStyle style = StylesForTest.createAreaSymbolizerDotFillStyle(dotQuantityExpression, fillColorExperssion)
        StyledLayer styledLayer = new StyledLayer(spatialTable, style)
        mapView << styledLayer
        mapView.draw();
        mapView.save("./target"+File.separator+ testInfo.getDisplayName()+".png")
        Feature2DStyleIO.toJSON(style, new File("./target"+File.separator+ testInfo.getDisplayName()+".json"));
    }

    }
