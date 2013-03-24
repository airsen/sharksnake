package info.airsen.sharksnake.server.driver;

import info.airsen.sharksnake.base.game.GameContext;
import lombok.Data;
import lombok.Getter;
import org.apache.log4j.Logger;

/**
 * <p></p>
 *
 * @author airsen
 * @since 13-1-25 下午9:06
 */
@Data
public class DriverContext {

	private static final Logger LOGGER = Logger.getLogger(DriverContext.class);

	private Driver driver;

	public enum DriverType {
		UDP("udp", HttpDriver.class), HTTP("http", HttpDriver.class);

		@Getter
		private String alias;

		@Getter
		private Class driverClazz;

		DriverType(String alias, Class driverClazz) {
			this.alias = alias;
			this.driverClazz = driverClazz;
		}

		public static Driver getInstanceByAlias(String alias) {
			for (DriverType type : DriverType.values()) {
				if (type.getAlias().equals(alias)) {
					try {
						return (Driver) type.getDriverClazz().newInstance();
					} catch (Exception e) {
						LOGGER.error("内部出错", e);
					}
				}
			}
			return null;
		}

	}

	public DriverContext(String driverType, String address) {
		this.driver = DriverType.getInstanceByAlias(driverType);
		this.driver.setAddress(address);
	}

	public boolean test() {
		return driver.test();
	}

	public int next(GameContext context) {
		return driver.next(context);
	}

}
