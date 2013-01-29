package info.airsen.face;

import lombok.Data;

/**
 * <p>请求的规范</p>
 *
 * @author airsen
 * @since 13-1-24 下午11:12
 */
@Data
public class RequestModel {

	public enum Status {
		SUCCESS, FAILD
	}

	private String key;
	private Function function; // 请求类型
	private int round; //回合
	private Status status; // 操作反馈

	private Context context; // current context;

	@Override
	public String toString() {
		return "RequestModel{" +
				"key='" + key + '\'' +
				", function=" + function +
				", round=" + round +
				", status=" + status +
				'}';
	}
}
