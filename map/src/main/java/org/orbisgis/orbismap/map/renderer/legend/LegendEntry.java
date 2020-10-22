package org.orbisgis.orbismap.map.renderer.legend;

import org.orbisgis.orbismap.style.Feature2DRule;
import org.orbisgis.orbismap.style.IFeatureSymbolizer;

import java.util.Locale;

public class LegendEntry {

    private final String[] text;
    private final IFeatureSymbolizer symbolizer;
    private final LegendItem.LegendEntryType legendEntryType;
    private Integer spacingAfter;

    public LegendEntry(String text) {
        this.text = new String[] {text};
        this.symbolizer = null;
        this.legendEntryType = null;
    }

    public LegendEntry(String text, LegendItem.LegendEntryType legendEntryType, IFeatureSymbolizer symbolizer) {
        this.symbolizer = symbolizer;
        this.legendEntryType = legendEntryType;
        this.text = text!=null || !text.isEmpty()?new String[] {text}:getText();
    }

    public LegendEntry(LegendItem.LegendEntryType legendEntryType, IFeatureSymbolizer symbolizer) {
        this.symbolizer = symbolizer;
        this.legendEntryType = legendEntryType;
        this.text = getText();
    }


    /**
     * Gets the legend entry text.
     * <p>
     * If the text has not been set then it looks for the text associated with
     * the rule. Otherwise it returns an empty string.
     * </p>
     *
     * @return
     */
    public String[] getText() {
        if (this.text != null) {
            return this.text;
        } else if (symbolizer != null) {
            return new String[]{getText(symbolizer)};
        }
        return new String[]{};
    }

    /**
     * Finds the text with the associated symbolizer
     * @param symbolizer
     * @return
     */
    private String getText(IFeatureSymbolizer symbolizer) {
        String text = "";

        String title = null;
        if (symbolizer != null) {
            if (symbolizer.getDescription().getTitle(Locale.FRANCE) != null) {
                title = symbolizer.getDescription().getTitle(Locale.FRANCE).toString();
            }
        }
        if (title != null && !"".equals(title)) {
            text = title;
        } else if (symbolizer.getName() != null && !"".equals(symbolizer.getName())) {
            text = symbolizer.getName();
        }
        if (text.length() > 19) {
            return text.substring(0, 18) + "...";
        } else {
            return text;
        }
    }

    /**
     * Finds the text with the associated rule
     * @param rule
     * @return
     */
    private String getText(Feature2DRule rule) {
        String text = "";
        String title = null;
        if (rule.getDescription() != null) {
            if (rule.getDescription().getTitle(Locale.FRANCE) != null) {
                title = rule.getDescription().getTitle(Locale.FRANCE).toString();
            }
        }
        if (title != null && !"".equals(title)) {
            text = title;
        } else if (rule.getName() != null && !"".equals(rule.getName())) {
            text = rule.getName();
        } else if (rule.getFilter() != null) {
            text = rule.getFilter().toString();        }

        if (text.length() > 19) {
            return text.substring(0, 18) + "...";
        } else {
            return text;
        }
    }

    /**
     * Spacing after image
     * @return null if default else number
     * of pixel to draw between current cell and next
     * cell
     */
    public Integer getSpacingAfter(){
        return this.spacingAfter;
    }

    /**
     * Sets the spacing after image.  Set
     * to null to use default
     * @param spacing
     */
    public void setSpacingAfter(Integer spacing){
        this.spacingAfter = spacing;
    }

}
