package info.airsen.controller;

import info.airsen.common.Constant;
import info.airsen.common.ParseUtil;
import info.airsen.game.Coordinate;
import info.airsen.game.GameContext;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

	private static final Logger LOGGER = Logger.getLogger(HomeController.class);

	/**
	 * <p>测试连接是否能成功</p>
	 *
	 * @return 是否成功
	 */
	@RequestMapping(value = "/test")
	public
	@ResponseBody
	String test() {
		return "ok";
	}

	/**
	 * <p>执行算法</p>
	 *
	 * @return 是否成功
	 */
	@RequestMapping(value = "/next")
	public
	@ResponseBody
	int next() {
		// 获取到游戏环境
		String reqContext = ParseUtil.getParameter("gameContext");
		int serinum = ParseUtil.getParameter("serinum", 1);
		GameContext context = Constant.GSON.fromJson(reqContext, GameContext.class);

		// 直接走过去找食物的方法
		Coordinate food = context.getFood();
		Coordinate head = context.getSnakeHeads()[serinum];
		int currDir = head.getValue() % 10; // 对10求余得到个位数
		int[][] map = context.getMap();

		System.out.println(context);

		int x = food.getX() - head.getX();
		int y = food.getY() - head.getY();

//		if (x == 0) // 同个横坐标
//			if (map[head.getX() + 1][head.getY()] == 1 || map[head.getX() + 1][head.getY()] == 0)


		if (x > 0 && map[head.getX() + 1][head.getY()] == 0 || map[head.getX() + 1][head.getY()] == 1)
			return GameContext.DIR_RIGHT;
		else if (x < 0 && map[head.getX() - 1][head.getY()] == 0 || map[head.getX() - 1][head.getY()] == 1)
			return GameContext.DIR_LEFT;
		else if (y < 0 && map[head.getX()][head.getY() - 1] == 0 || map[head.getX()][head.getY() - 1] == 1)
			return GameContext.DIR_DOWN;
		else if (y > 0 && map[head.getX()][head.getY() + 1] == 0 || map[head.getX()][head.getY() + 1] == 1)
			return GameContext.DIR_UP;

		return GameContext.DIR_RIGHT;
	}

}
