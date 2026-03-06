package ui.tooltip;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import util.Config;

import java.util.*;

/**
 * The {@code Tooltip} class represents the data model for a UI tooltip.
 * <p>
 * It supports recursive references, where a tooltip can "contain" or 
 * "reference" other tooltips within its description. These references 
 * are resolved by the {@link TooltipLayer} for multi-column display.
 */
public class Tooltip {
    private String title; // also can be null
    private Color textColor;
    private Object[] description; // some values can and probably will be null

    /** @return The title text of this tooltip. */
    public String getTitle() {
        return title;
    }

    /** @return The color used for the tooltip title. */
    public Color getTitleColor() {
        return textColor;
    }

    /** 
     * Generates a JavaFX {@link TextFlow} representing the formatted description.
     * <p>
     * Tooltip references within the description are rendered with distinct 
     * colors and underlining.
     * @return A styled {@link TextFlow}.
     */
    public TextFlow getDescription() {
        TextFlow flow = new TextFlow();
        for (Object o: description) {
            if (o == null) continue;
            Text text = new Text();

            if (o instanceof Tooltip tt) {
                text.setText(tt.getTitle());
                text.setFill(tt.getTitleColor());
                text.setFont(Font.font(Config.REGULAR_FONT, FontWeight.BOLD, 12));
                text.setUnderline(true);
            }
            else {
                text.setText(o.toString());
                text.setFont(Font.font(Config.REGULAR_FONT, 12));
            }

            flow.getChildren().add(text);
        }
        return flow;
    }

    /** 
     * Extracts all other tooltips referenced in this tooltip's description.
     * @return A list of unique {@code Tooltip} references.
     */
    public List<Tooltip> getRefs() {
        Set<Tooltip> refs = new HashSet<>();

        for (Object o: description)
            if (o instanceof Tooltip tt && tt.description.length > 0) // Only ref the ones with a description
                refs.add(tt);

        return refs.stream().toList();
    }

    /**
     * Constructs a new {@code Tooltip}.
     * @param title The title text.
     * @param color The title color.
     * @param description An array of Objects (Strings or Tooltips) for the body.
     */
    public Tooltip(String title, Color color, Object... description) {
        this.title = title;
        this.textColor = color;
        this.description = description;
    }

    /** 
     * Helper to safely get a {@code Tooltip} reference from an object.
     * @param o The object (Tippable, Tooltip, or String).
     * @return A {@code Tooltip} instance.
     */
    public static Tooltip ref(Object o) {
        if (o == null) return null;
        else if (o instanceof Tooltip tt) return tt;
        else if (o instanceof Tippable tpp) return tpp.getTooltip();
        else return new Tooltip(o.toString(), Color.BLUE);
    }

    /** 
     * Creates a dummy {@link Tippable} that aggregates multiple sources.
     * @param tipTargets The sources to combine.
     * @return A combined {@link Tippable}.
     */
    public static Tippable getContainerFor(Tippable ...tipTargets) {
        // Since when displaying tooltips it affectively only cares about the refs of the first tip without caring about the title
        // So this can be used to create "Containers" for tooltips to display

        return () -> {
            Object[] tooltips = new Object[tipTargets.length];
            for (int i = 0; i < tipTargets.length; i++) {
                tooltips[i] = tipTargets[i] == null ? null: tipTargets[i].getTooltip();
            }
            return new Tooltip(null, Color.BLACK, tooltips);
        };
    }

    /** 
     * @param o
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        // 1. Check if they are the exact same instance in memory
        if (this == o) return true;

        // 2. Check for null or if they are from different classes
        if (o == null || getClass() != o.getClass()) return false;

        // 3. Cast to Tooltip
        Tooltip tooltip = (Tooltip) o;

        // 4. Compare fields (Objects.equals safely handles nulls)
        return Objects.equals(title, tooltip.title) &&
                Objects.equals(textColor, tooltip.textColor);

        // Not checking description for performance and object reference reasons
    }

    /** 
     * @return int
     */
    @Override
    public int hashCode() {
        // Hash the standard fields first
        return Objects.hash(title, textColor);
    }


}
