package info.airsen.sharksnake.server.model;

import lombok.Data;

/**
 * <p></p>
 *
 * @author airsen
 * @since 13-1-26 下午10:27
 */
@Data
public class Player {

	private String name;
	private String type;
	private String address;

	public Player(String type, String address) {
		this.type = type;
		setAddress(address);
	}
}
