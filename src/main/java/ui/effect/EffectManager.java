package ui.effect;

import component.modifier.Modifier;
import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import registry.render.FloatingLayerRegistry;
import registry.render.RenderLayer;
import util.Config;
import util.GridPos;

public class EffectManager {

    public static void createEffect(Color c, GridPos pos) {
        Pane target = FloatingLayerRegistry.INSTANCE.getPane(RenderLayer.EFFECTS);

        CircleEffect effect = new CircleEffect(c, pos);

        target.getChildren().add(effect);
        Transition transition = effect.getTransition();
        transition.setOnFinished(e -> target.getChildren().remove(effect));
        transition.play();
    }

    public static void createEffectsOnModifiers(Iterable<Modifier> modifiers) {
        for (Modifier modifier : modifiers) {
            if (modifier)



        }
    }

}
