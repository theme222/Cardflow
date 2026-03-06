package application;

import application.controller.PlacementController;
import component.GameTile;
import component.card.Card;
import component.modifier.changer.Arithmetic;
import component.modifier.changer.MaterialSetter;
import component.modifier.changer.SuitSetter;
import component.modifier.changer.ValueSetter;
import component.modifier.combinator.Combinator;
import component.modifier.pathway.Pathway;
import component.modifier.pathway.event.CardEnterEvent;
import component.modifier.pathway.event.CardExitEvent;
import component.mover.*;
import event.EventBus;
import logic.GameEndCondition;
import logic.event.AfterMovementEvent;
import logic.event.card.TileSelectChangeEvent;
import registry.render.FloatingLayerRegistry;
import registry.render.RenderLayer;
import registry.render.RendererRegistry;
import ui.base.EmptyTileRenderer;
import ui.card.CardRenderer;
import ui.modifier.changer.ArithmeticRenderer;
import ui.modifier.changer.SetterRenderer;
import ui.modifier.combinator.CombinatorRenderer;
import ui.modifier.pathway.PathwayRenderer;
import ui.mover.*;
import ui.overlay.SelectedTileOverlayRenderer;

/**
 * The {@code GameBootstrap} class handles the registration of all essential game components.
 * <p>
 * It serves as the setup phase for the game engine, ensuring that every logic component 
 * (like Movers or Cards) has a corresponding Renderer, and that global events are 
 * properly routed to their respective controllers.
 */
public class GameBootstrap {

    /**
     * Executes the full initialization sequence for the game.
     * <p>
     * This must be called during application startup before any views are instantiated.
     * It performs the following:
     * <ol>
     * <li>Registers Renderers (Logic to Visual mapping)</li>
     * <li>Registers Global Event Listeners</li>
     * <li>Defines Rendering Layers</li>
     * </ol>
     */
    public static void init() {
        registerRenderer();
        registerEvents();
        registerLayers();
    }

    /**
     * Maps logic classes to their specific visual Renderer instances.
     * <p>
     * This uses the {@link RendererRegistry} to ensure that when the game needs to 
     * draw a component (like a {@code Conveyor} or an {@code Arithmetic} modifier), 
     * it knows which UI class to use.
     */
    public static void registerRenderer() {
        RendererRegistry regInstance = RendererRegistry.INSTANCE;

        // Core Components
        regInstance.register(Card.class, CardRenderer.INSTANCE);
        regInstance.register(GameTile.class, EmptyTileRenderer.INSTANCE);

        // Movers (Conveyors and Filters)
        regInstance.register(Conveyor.class, ConveyorRenderer.INSTANCE);
        regInstance.register(FlipFlop.class, FlipFlopRenderer.INSTANCE);
        regInstance.register(ParityFilter.class, ParityFilterRenderer.INSTANCE);
        regInstance.register(RedBlackFilter.class, RedBlackFilterRenderer.INSTANCE);
        regInstance.register(Delay.class, DelayRenderer.INSTANCE);

        // Modifiers (Logic changers)
        regInstance.register(Arithmetic.class, ArithmeticRenderer.INSTANCE);
        regInstance.register(MaterialSetter.class, SetterRenderer.INSTANCE);
        regInstance.register(ValueSetter.class, SetterRenderer.INSTANCE);
        regInstance.register(SuitSetter.class, SetterRenderer.INSTANCE);
        regInstance.register(Combinator.class, CombinatorRenderer.INSTANCE);
        regInstance.register(Pathway.class, PathwayRenderer.INSTANCE);
    }

    /**
     * Hooks global game events into the system.
     * <p>
     * <ul>
     * <li><b>AfterMovementEvent:</b> Triggers the card's visual movement animation.</li>
     * <li><b>CardExitEvent:</b> Triggers the game's win-condition check.</li>
     * <li><b>TileSelectChangeEvent:</b> Updates the placement logic when a user picks a new tile type.</li>
     * </ul>
     */
    public static void registerEvents() {
        EventBus.register(AfterMovementEvent.class, CardRenderer.INSTANCE.movementListener);
        EventBus.register(CardExitEvent.class, GameEndCondition.INSTANCE::checkWinCondition);

        EventBus.register(TileSelectChangeEvent.class, PlacementController.INSTANCE::handleTileSelectChange);
    }

    /**
     * Configures the behavior of specific {@link RenderLayer}s.
     * <p>
     * Layers marked as "Floating" are typically rendered outside the standard 
     * grid constraints, such as active animations, particle effects, or UI overlays.
     */
    public static void registerLayers() {
        FloatingLayerRegistry.INSTANCE.markFloating(RenderLayer.CARDANIM);
        FloatingLayerRegistry.INSTANCE.markFloating(RenderLayer.EFFECTS);
        FloatingLayerRegistry.INSTANCE.markFloating(RenderLayer.OVERLAY);
    }
}