package info.airsen.driver;

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

	@Override
	public boolean test() {
		try {
			URL url = new URL(address + "/test");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(4000);
			connection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));//设置编码,否则中文乱码
			String result = reader.readLine();
			return StringUtils.isNotBlank(result) && result.equals("ok");
		} catch (Exception e) {
			LOGGER.warn("内部出错", e);
		}
		return false;

	}

	@Override
	public int next(GameContext gameContext) {
		return 1;
	}
}
