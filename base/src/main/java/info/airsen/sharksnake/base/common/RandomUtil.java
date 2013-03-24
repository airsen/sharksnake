package info.airsen.sharksnake.base.common;

import info.airsen.sharksnake.base.game.Coordinate;

import java.util.Random;

/**
 * <p></p>
 *
 * @author airsen
 * @since 13-1-25 下午11:37
 */
public class RandomUtil {

	private static final Random random = new Random();

	public static Integer getRandomInt(int max) {
		return random.nextInt(max);
	}

	public static Coordinate getRandomCoordinate(int width, int height) {
		return new Coordinate(random.nextInt(width), random.nextInt(height));
	}

}
