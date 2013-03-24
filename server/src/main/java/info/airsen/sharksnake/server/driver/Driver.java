package info.airsen.sharksnake.server.driver;

import info.airsen.sharksnake.base.game.GameContext;
import lombok.Data;

/**
 * <p></p>
 *
 * @author airsen
 * @since 13-1-25 下午9:07
 */
@Data
public abstract class Driver {

	protected String address;

	public abstract boolean test();

	public abstract int next(GameContext gameContext, int serinum);


}
