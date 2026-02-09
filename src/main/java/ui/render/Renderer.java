package ui.render;

import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import registry.render.RenderLayer;
import util.GridPos;


public abstract class Renderer<T> {
    public abstract RenderLayer layer();
    public abstract void render(T tile, Pane node, GridPos pos);
}
