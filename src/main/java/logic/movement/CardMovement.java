package logic.movement;

import component.card.Card;
import util.GridPos;

/**
 * A record representing a single card's movement from one position to another.
 * 
 * @param card The {@link Card} that moved.
 * @param from The starting {@link GridPos}.
 * @param to The destination {@link GridPos}.
 */
public record CardMovement(
        Card card,
        GridPos from,
        GridPos to
) {}
