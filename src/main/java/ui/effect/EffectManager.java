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

    public static void createEffect(Color c, GridPos pos) {
        Pane target = FloatingLayerRegistry.INSTANCE.getPane(RenderLayer.EFFECTS);
        if (target == null) return;

        CircleEffect effect = new CircleEffect(c, pos);

        target.getChildren().add(effect);
        Transition transition = effect.getTransition();
        transition.setOnFinished(e -> target.getChildren().remove(effect));
        transition.play();
    }

    public static void createEffectsWithModifierSet(Set<Modifier> modifiers) {
        for (Modifier modifier : modifiers) {
            Color selectedColor = switch (modifier) {
                case Entrance entrance -> Color.YELLOWGREEN;
                case Exit exit -> Color.RED;
                case Changer<?> changer -> Color.CYAN;
                case Combinator combinator -> Color.VIOLET;
                case null, default -> Color.YELLOW; // idk
            };

            createEffect(selectedColor, modifier.getGridPos());
        }
    }

}
