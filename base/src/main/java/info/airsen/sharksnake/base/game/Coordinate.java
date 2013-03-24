package info.airsen.sharksnake.base.game;

import lombok.Data;

/**
 * <p>坐标类</p>
 *
 * @author airsen
 * @since 13-1-26 下午2:16
 */

@Data
public class Coordinate {

	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Coordinate(int x, int y, int value) {
		this(x, y);
		this.value = value;
	}

	private int x;
	private int y;
	private int value;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Coordinate)) return false;
		Coordinate that = (Coordinate) o;
		if (x != that.x) return false;
		if (y != that.y) return false;
		return true;
	}

}
