package ui.effect;

import component.modifier.Modifier;
import component.modifier.changer.Arithmetic;
import component.modifier.changer.Changer;
import component.modifier.combinator.Combinator;
import component.modifier.pathway.Entrance;
import component.modifier.pathway.Exit;
import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import registry.render.FloatingLayerRegistry;
import registry.render.RenderLayer;
import util.Config;
import util.GridPos;

import java.util.Set;

public class EffectManager {

    /** 
     * @param c
     * @param pos
     */
    public static void createEffect(Color c, GridPos pos) {
        Pane target = FloatingLayerRegistry.INSTANCE.getPane(RenderLayer.EFFECTS);
        if (target == null) return;

        CircleEffect effect = new CircleEffect(c, pos);

        target.getChildren().add(effect);
        Transition transition = effect.getTransition();
        transition.setOnFinished(e -> target.getChildren().remove(effect));
        transition.play();
    }

    /** 
     * @param modifiers
     */
    public static void createEffectsWithModifierSet(Set<Modifier> modifiers) {
        for (Modifier modifier : modifiers) {
            Color selectedColor;

            if (modifier instanceof Entrance)
                selectedColor = Color.YELLOWGREEN;
            else if (modifier instanceof Exit)
                selectedColor = Color.RED;
            else if (modifier instanceof Changer)
                selectedColor = Color.CYAN;
            else if (modifier instanceof Combinator)
                selectedColor = Color.VIOLET;
            else
                selectedColor = Color.YELLOW;

            createEffect(selectedColor, modifier.getGridPos());
        }
    }

}
