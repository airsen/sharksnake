package info.airsen.driver;

import info.airsen.game.GameContext;

/**
 * <p></p>
 *
 * @author airsen
 * @since 13-1-25 下午9:12
 */
public class UdpDriver extends Driver {
	@Override
	public boolean test() {
		return false;
	}

	@Override
	public int next(GameContext context) {
		return 0;
	}
}
