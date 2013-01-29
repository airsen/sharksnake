package info.airsen;

import info.airsen.driver.Driver;
import info.airsen.game.GameContext;
import lombok.Data;
import org.apache.log4j.Logger;


/**
 * <p></p>
 *
 * @author airsen
 * @since 13-1-26 下午1:18
 */
@Data
public class DriverMock extends Driver {

	private static final Logger LOGGER = Logger.getLogger(DriverMock.class);

	private boolean testReturn;
	private int[] direction;

	public DriverMock() {
	}

	public DriverMock(boolean testReturn, int[] direction) {
		this.direction = direction;
		this.testReturn = testReturn;
	}


	@Override
	public boolean test() {
		return testReturn;
	}

	@Override
	public int next(GameContext gameContext) {
		return direction[gameContext.getRound()];
	}
}
