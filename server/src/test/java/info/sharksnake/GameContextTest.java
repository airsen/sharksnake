package info.sharksnake;

import info.airsen.sharksnake.base.game.GameContext;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

/**
 * <p></p>
 *
 * @author airsen
 * @since 13-1-26 上午9:05
 */

//@RunWith(SpringJUnit4ClassRunner.class)
public class GameContextTest {

	private static final Logger LOGGER = Logger.getLogger(GameContextTest.class);
	private GameContext gameContext;

	@Before
	public void init() {
		this.gameContext = new GameContext();
	}

	/**
	 * <p>测试蛇的爬行</p>
	 * <p>单条蛇</p>
	 */
	@Test
	public void normalTest() {
		int[][] a = {{1, 2}, {3, 4}};
		int[][] b = new int[2][2];
		System.arraycopy(a, 0, b, 0, a.length);
		System.out.println(b);
		a[1][1] = 6;
		System.out.println(b);
		System.out.println(a);
	}

}
