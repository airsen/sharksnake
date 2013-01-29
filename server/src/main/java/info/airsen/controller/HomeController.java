package info.airsen.controller;

import info.airsen.common.ParseUtil;
import info.airsen.driver.DriverContext;
import info.airsen.game.Coordinate;
import info.airsen.game.GameContext;
import info.airsen.model.GameModel;
import info.airsen.model.Player;
import info.airsen.model.WebRequestModel;
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
		LOGGER.info(request.getRemoteAddr() + "登录首页");
		return new ModelAndView("home");
	}

	@RequestMapping(value = "/start")
	public
	@ResponseBody
	GameModel start() {
		List<Player> players = createPlayers();
		List<DriverContext> clientList = createPlayContexts(players);
		GameContext gameContext = createGameContext();

		GameModel gameModel = new GameModel(gameContext.getKey());

		int[] playerResults = new int[players.size() + 1];
		do {
			List<Coordinate> increMap = gameContext.popIncreMap();
			gameModel.add(increMap);
			for (int i = 0; i < clientList.size(); i++) {
				playerResults[i + 1] = clientList.get(i).next(gameContext);
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
		LOGGER.info(request.getRemoteAddr() + "提交测试");
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
			LOGGER.warn("内部错误", e);
			webRequestModel.set(false, "内部错误");
		}
		return webRequestModel;
	}


	/**
	 * <p>创建一个游戏局面</p>
	 *
	 * @return 游戏
	 */
	private GameContext createGameContext() {
		GameContext context = new GameContext();
		context.setCount(ParseUtil.getParameter("count", 1));
		context.setHeight(ParseUtil.getParameter("height", 20));
		context.setWidth(ParseUtil.getParameter("width", 20));
		context.setTotalRound(ParseUtil.getParameter("totalRound", 100));
		context.init();
		return context;
	}

	private List<Player> createPlayers() {
		String[] playerTypes = ParseUtil.getParameterValues("type");
		String[] playerAddresses = ParseUtil.getParameterValues("address");
		if (playerTypes.length != playerAddresses.length || playerTypes.length <= 0)
			return null;
		List<Player> players = new ArrayList<>();
		for (int i = 0; i < playerAddresses.length; i++) {
			if (StringUtils.isBlank(playerTypes[i]) || StringUtils.isBlank(playerAddresses[i]))
				continue;
			players.add(new Player(playerTypes[i], playerAddresses[i]));
		}
		return players;
	}

	private List<DriverContext> createPlayContexts(List<Player> players) {
		List<DriverContext> driverContexts = new ArrayList<DriverContext>();
		for (Player play : players)
			driverContexts.add(new DriverContext(play.getType(), play.getAddress()));
		return driverContexts;
	}

}
