package info.airsen.driver;

import info.airsen.game.GameContext;
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

	public boolean test() {
		return false;
	}

	public int next(GameContext gameContext) {
		return 0;
	}


}
