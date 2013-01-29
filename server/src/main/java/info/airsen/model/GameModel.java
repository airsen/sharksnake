package info.airsen.model;

import info.airsen.game.Coordinate;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * <p></p>
 *
 * @author airsen
 * @since 13-1-26 下午9:59
 */
@Data
public class GameModel {

	private int winner;

	private boolean more; // 还需要继续请求时为true
	private String key; // 请求文件的key
	private int offset; // 偏移量

	private int round;
	private List<List<Coordinate>> frames;

	public GameModel(String key) {
		this.key = key;
		frames = new ArrayList<>();
		round = 0;
	}

	public void add(List<Coordinate> frame) {
		frames.add(frame);
		++round;
	}

}
