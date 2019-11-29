/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orbisgis.coremap.renderer.se.Utils;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.TopologyException;
import org.orbisgis.coremap.map.MapTransform;
import org.orbisgis.coremap.renderer.se.parameter.ParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ebocher
 */
public class FeatureUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeatureUtils.class);

    /**
     * Convert a spatial feature into a set of linear shape
     *
     * @param rs the data source
     * @param fid the feature id
     * @throws ParameterException
     * @throws IOException
     * @throws SQLException
     */
    public List<Shape> getLines(MapTransform mt, Geometry the_geom) throws ParameterException, IOException, SQLException {
        
        LinkedList<Shape> shapes = new LinkedList<Shape>();
        LinkedList<Geometry> geom2Process = new LinkedList<Geometry>();
        geom2Process.add(the_geom);
        AffineTransform at = null;
        while (!geom2Process.isEmpty()) {
            the_geom = geom2Process.remove(0);

            if (the_geom != null) {

                if (the_geom instanceof GeometryCollection) {
                    // Uncollectionize
                    int numGeom = the_geom.getNumGeometries();
                    for (int i = 0; i < numGeom; i++) {
                        geom2Process.add(the_geom.getGeometryN(i));
                    }
                } else if (the_geom instanceof Polygon) {
                    // Separate exterior and interior holes
                    Polygon p = (Polygon) the_geom;

                    shapes.add(mt.getShape(the_geom, true));

                    Shape shape = mt.getShape(p.getExteriorRing(), true);
                    if (shape != null) {
                        if (at != null) {
                            shape = at.createTransformedShape(shape);
                        }
                        shapes.add(shape);
                    }
                    int i;
                    // Be aware of polygon holes !
                    int numRing = p.getNumInteriorRing();
                    for (i = 0; i < numRing; i++) {
                        shape = mt.getShape(p.getInteriorRingN(i), true);
                        if (shape != null) {
                            if (at != null) {
                                shape = at.createTransformedShape(shape);
                            }
                            shapes.add(shape);
                        }
                    }
                } else {
                    Shape shape = mt.getShape(the_geom, false);

                    if (shape != null) {
                        if (at != null) {
                            shape = at.createTransformedShape(shape);
                        }
                        shapes.add(shape);
                    }
                }
            }
        }

        return shapes;
    }

    /**
     * Return one point for each geometry
     *
     * @param mt
     * @param the_geom
     * @return
     * @throws ParameterException
     * @throws IOException
     * @throws SQLException
     */
    public static Point2D getPointShape(MapTransform mt, Geometry the_geom)
            throws ParameterException, IOException, SQLException {
        AffineTransform at = mt.getAffineTransform();
        Point point;

        try {
            point = the_geom.getInteriorPoint();
        } catch (TopologyException ex) {
            LOGGER.error("getPointShape :: TopologyException: ", ex);
            point = the_geom.getCentroid();
        }
        return at.transform(new Point2D.Double(point.getX(), point.getY()), null);
    }

    /**
     * Return only the first point
     *
     * @param fid
     * @param mt
     * @return
     * @throws ParameterException
     * @throws IOException
     * @throws SQLException
     */
    public Point2D getFirstPointShape(MapTransform mt,
            Geometry theGeom) throws ParameterException, IOException, SQLException {
        AffineTransform at = mt.getAffineTransform();
        Coordinate[] coordinates = theGeom.getCoordinates();

        return at.transform(new Point2D.Double(coordinates[0].x, coordinates[0].y), null);
    }

    /**
     * Return all vertices of the geometry
     *
     * @param mt
     * @param theGeom
     * @return
     * @throws ParameterException
     * @throws IOException
     * @throws SQLException
     */
    public static List<Point2D> getPoints(MapTransform mt, Geometry theGeom) throws ParameterException, IOException, SQLException {
        //geom = ShapeHelper.clipToExtent(geom, mt.getAdjustedExtent());
        LinkedList<Point2D> points = new LinkedList<Point2D>();

        AffineTransform at = mt.getAffineTransform();
        Coordinate[] coordinates = theGeom.getCoordinates();
        for (Coordinate coord : coordinates) {
            points.add(at.transform(new Point2D.Double(coord.x, coord.y), null));
        }

        return points;
    }

}
