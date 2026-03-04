package application;

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

public class GameBootstrap {

    public static void init() {
        registerRenderer();
        registerEvents();
        
        registerLayers();
    }

    public static void registerRenderer() {
        // Initialize the renderer registry
        RendererRegistry regInstance = RendererRegistry.INSTANCE;

        regInstance.register(Card.class, CardRenderer.INSTANCE);

        regInstance.register(GameTile.class, EmptyTileRenderer.INSTANCE);
        regInstance.register(Conveyor.class, ConveyorRenderer.INSTANCE);
        regInstance.register(FlipFlop.class, FlipFlopRenderer.INSTANCE);
        regInstance.register(ParityFilter.class, ParityFilterRenderer.INSTANCE);
        regInstance.register(RedBlackFilter.class, RedBlackFilterRenderer.INSTANCE);
        regInstance.register(Delay.class, DelayRenderer.INSTANCE);
        regInstance.register(Arithmetic.class, ArithmeticRenderer.INSTANCE);
        regInstance.register(MaterialSetter.class, SetterRenderer.INSTANCE);
        regInstance.register(ValueSetter.class, SetterRenderer.INSTANCE);
        regInstance.register(SuitSetter.class, SetterRenderer.INSTANCE);
        regInstance.register(Combinator.class, CombinatorRenderer.INSTANCE);
        regInstance.register(Pathway.class, PathwayRenderer.INSTANCE);
    }

    public static void registerEvents() {
        EventBus.register(AfterMovementEvent.class, CardRenderer.INSTANCE.movementListener);
        EventBus.register(CardExitEvent.class, GameEndCondition.INSTANCE::checkWinCondition);
    }

    public static void registerLayers() {
        // register floating layers
        FloatingLayerRegistry.INSTANCE.markFloating(RenderLayer.CARDANIM);
        FloatingLayerRegistry.INSTANCE.markFloating(RenderLayer.EFFECTS);
        FloatingLayerRegistry.INSTANCE.markFloating(RenderLayer.OVERLAY);
    }
}