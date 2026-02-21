package application;

import component.GameTile;
import component.card.Card;
import component.mover.Conveyor;
import component.mover.FlipFlop;
import event.EventBus;
import logic.event.AfterMovementEvent;
import logic.movement.MovementTickResolver;
import registry.render.FloatingLayerRegistry;
import registry.render.RenderLayer;
import registry.render.RendererRegistry;
import ui.base.EmptyTileRenderer;
import ui.card.CardMovementAnimation;
import ui.card.CardRenderer;
import ui.mover.ConveyorRenderer;
import ui.mover.FlipFlopRenderer;

public class GameBootstrap {

    public static void init() {
        registerRenderer();
        registerEvents();
        
        registerLayers();
    }

    public static void registerRenderer() {
        // Initialize the renderer registry
        RendererRegistry.INSTANCE.register(Card.class, CardRenderer.INSTANCE);
        RendererRegistry.INSTANCE.register(GameTile.class, EmptyTileRenderer.INSTANCE);
        RendererRegistry.INSTANCE.register(Conveyor.class, ConveyorRenderer.INSTANCE);
        RendererRegistry.INSTANCE.register(FlipFlop.class, FlipFlopRenderer.INSTANCE);
    }

    public static void registerEvents() {
        EventBus.register(AfterMovementEvent.class, CardRenderer.INSTANCE.movementListener);
    }

    public static void registerLayers() {
        // register floating layers
        FloatingLayerRegistry.INSTANCE.markFloating(RenderLayer.CARDANIM);
    }
}