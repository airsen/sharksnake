package info.airsen.sharksnake.server.controller;

import info.airsen.sharksnake.base.game.Coordinate;
import info.airsen.sharksnake.base.game.GameContext;
import info.airsen.sharksnake.server.common.ParseUtil;
import info.airsen.sharksnake.server.driver.DriverContext;
import info.airsen.sharksnake.server.model.GameModel;
import info.airsen.sharksnake.server.model.Player;
import info.airsen.sharksnake.server.model.WebRequestModel;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

	private static final Logger LOGGER = Logger.getLogger(HomeController.class);

	/**
	 * <p>首页</p>
	 */
	@RequestMapping(value = "/")
	public ModelAndView home(HttpServletRequest request) throws IOException {
		LOGGER.info(request.getRemoteAddr() + " - 登录首页");
		return new ModelAndView("home");
	}

	@RequestMapping(value = "/start")
	public
	@ResponseBody
	GameModel start() {
		List<Player> players = createPlayers();
		List<DriverContext> clientList = createPlayContexts(players);
		GameContext gameContext = createGameContext(players);

		GameModel gameModel = new GameModel(gameContext.getKey());
		gameModel.setWidth(gameContext.getWidth());
		gameModel.setHeight(gameContext.getHeight());

		int[] playerResults = new int[players.size() + 1];
		do {
			List<Coordinate> increMap = gameContext.popIncreMap();
			gameModel.add(increMap);
			for (int i = 0; i < gameContext.getCount(); i++) {
				if (gameContext.getSnakeHeads()[i + 1] != null) { // 已经逝去
					playerResults[i + 1] = clientList.get(i + 1).next(gameContext);
				}
//				playerResults[i + 1] = clientList.get(i).next(gameContext);
			}
		} while (gameContext.nextRound(playerResults));
		gameContext.getWinner(); // TODO
		return gameModel;
	}

	/**
	 * <p>测试连接是否能成功</p>
	 *
	 * @return 是否成功
	 */
	@RequestMapping(value = "/test")
	public
	@ResponseBody
	WebRequestModel test(HttpServletRequest request) {
		LOGGER.info(request.getRemoteAddr() + " - 提交测试");
		String address = ParseUtil.getParameter("address", "");
		String type = ParseUtil.getParameter("type", "");
		WebRequestModel webRequestModel = new WebRequestModel();

		DriverContext driverContext = new DriverContext(type, address);

		try {
			if (driverContext.test()) {
				webRequestModel.set(true, "连通成功");
			} else
				webRequestModel.set(false, "连通失败");
		} catch (Exception e) {
			LOGGER.warn("内部错误:" + e.getMessage());
			webRequestModel.set(false, "连接超时");
		}
		return webRequestModel;
	}


	/**
	 * <p>创建一个游戏局面</p>
	 *
	 * @return 游戏
	 */
	private GameContext createGameContext(List<Player> players) {
		GameContext context = new GameContext();
		context.setCount(ParseUtil.getParameter("count", players.size()));
		context.setHeight(ParseUtil.getParameter("height", 15));
		context.setWidth(ParseUtil.getParameter("width", 30));
		context.setTotalRound(ParseUtil.getParameter("totalRound", 100));
		context.init();
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i) == null)
				context.killSnake(i + 1);
		}
		if (context.getCount() > 1 && context.getAlive() == 1)
			context.setAlive(2);
		return context;
	}

	private List<Player> createPlayers() {
		String[] playerTypes = ParseUtil.getParameterValues("type");
		String[] playerAddresses = ParseUtil.getParameterValues("address");
		if (playerTypes.length != playerAddresses.length || playerTypes.length <= 0)
			return null;
		List<Player> players = new ArrayList<Player>();
		for (int i = 0; i < playerAddresses.length; i++) {
			if (StringUtils.isBlank(playerTypes[i]) || StringUtils.isBlank(playerAddresses[i]))
				players.add(null);
			else
				players.add(new Player(playerTypes[i], playerAddresses[i]));
		}
		return players;
	}

	private List<DriverContext> createPlayContexts(List<Player> players) {
		List<DriverContext> driverContexts = new ArrayList<DriverContext>();
		driverContexts.add(null); // 一楼给度娘
		for (Player player : players) {
			if (player == null)
				driverContexts.add(null);
			else
				driverContexts.add(new DriverContext(player.getType(), player.getAddress()));
		}
		return driverContexts;
	}

}
