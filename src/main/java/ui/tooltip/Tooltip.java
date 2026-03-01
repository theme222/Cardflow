package ui.tooltip;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import util.Config;

import java.util.ArrayList;
import java.util.List;

public class Tooltip {
    private String title; // also can be null
    private Color textColor;
    private Object[] description; // some values can and probably will be null

    // This contains the description it will display.
    // If this tooltip is referenced inside another tooltip then it will create its own tooltip on the side via getRefs()
    // Unless it doesn't have a description, in which it will simply just be part of that and not incur its own ref
    // Copying this directly from balatro btw its soo good

    public String getTitle() {
        return title;
    }

    public Color getTitleColor() {
        return textColor;
    }

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

    public List<Tooltip> getRefs() {
        List<Tooltip> refs = new ArrayList<>();

        for (Object o: description)
            if (o instanceof Tooltip tt && tt.description.length > 0) // Only ref the ones with a description
                refs.add(tt);

        return refs;
    }

    public Tooltip(String title, Color color, Object... description) {
        this.title = title;
        this.textColor = color;
        this.description = description;
    }

    public static Tooltip ref(Object o) {
        if (o == null) return null;
        else if (o instanceof Tooltip tt) return tt;
        else if (o instanceof Tippable tpp) return tpp.getTooltip();
        else return new Tooltip(o.toString(), Color.BLUE);
    }

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

}
