package info.airsen.driver;

import info.airsen.common.Constant;
import info.airsen.game.GameContext;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * <p></p>
 *
 * @author airsen
 * @since 13-1-25 下午9:12
 */


public class HttpDriver extends Driver {

	private static final Logger LOGGER = Logger.getLogger(HttpDriver.class);

	private int lastDirection;

	public HttpDriver() {
		this.lastDirection = GameContext.DIR_UP;
	}

	@Override
	public void setAddress(String address) {
		if (!address.startsWith("http://")) {
			address = "http://" + address;
		}
		if (address.endsWith("/"))
			address = address.substring(0, address.length() - 1);
		this.address = address;
	}

	@Override
	public boolean test() {
		try {
			URL url = new URL(address + "/test");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(1000);
			connection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));//设置编码,否则中文乱码
			String result = reader.readLine();
			return StringUtils.isNotBlank(result) && result.equals("ok");
		} catch (Exception e) {
			LOGGER.warn("连接超时:" + e.getMessage());
		}
		return false;
	}

	@Override
	public int next(GameContext gameContext) {

		try {
			URL url = new URL(address + "/next?gameContext=" + Constant.GSON.toJson(gameContext));
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(1000);
			connection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));//设置编码,否则中文乱码
			int result = Integer.valueOf(reader.readLine());
			if (result > 0 && result < 5)
				lastDirection = result;
		} catch (Exception e) {
			LOGGER.warn("连接超时:" + e.getMessage());
		}
		return lastDirection;
	}
}
