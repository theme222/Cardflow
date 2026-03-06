package component.modifier.changer;

/**
 * The {@code Setter} class is an abstract base for all modifiers that 
 * set a card property to a specific value.
 * <p>
 * This class serves as a grouping for setters that can be resolved 
 * together for rendering and logical classification.
 * 
 * @param <T> The type of property being set (e.g., Suit, Material, or Integer).
 */
public abstract class Setter<T> extends Changer<T> {
    // Used for resolving the image
}
