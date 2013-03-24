package info.airsen.sharksnake.server.model;

import lombok.Data;

/**
 * <p></p>
 *
 * @author airsen
 * @since 13-1-25 下午4:44
 */
@Data
public class WebRequestModel {

	private boolean success;
	private String message;

	public void set(boolean success, String message) {
		this.success = success;
		this.message = message;
	}

}
