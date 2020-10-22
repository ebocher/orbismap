package org.orbisgis.orbismap.map.renderer.legend;

public abstract class MapItem {

    final int x;

    final int y;

    final int width;

    final int height;

    /**
     * Create an Item from the top left
     * @param x The number of pixels from the left
     * @param y The number of pixels from the top
     * @param width The width in pixels
     * @param height The height in pixels
     */
    MapItem(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
