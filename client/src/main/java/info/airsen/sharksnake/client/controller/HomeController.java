package info.airsen.sharksnake.client.controller;

import info.airsen.sharksnake.base.game.Coordinate;
import info.airsen.sharksnake.client.common.Constant;
import info.airsen.sharksnake.client.common.ParseUtil;
import info.airsen.sharksnake.base.game.GameContext;
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
		int currDir = head.getValue() % 10; // 对10求余得到个位数就是当前头的方向
		int[][] map = context.getMap();

		System.out.println(context);

		int $x = food.getX() - head.getX();// 头与食物的x差别量
		int $y = food.getY() - head.getY();// 头与食物的y差别量

		System.out.println(context);
		System.out.printf("( %d , %d )\n", $x, $y);

		//竞选出下一个方向

		// 1.先往x方向走回去一步，如果需要并且可能的话

		if ($x > 0 && (map[head.getX() + 1][head.getY()] == 0 || map[head.getX() + 1][head.getY()] == 1))
			return GameContext.DIR_RIGHT;
		else if ($x < 0 && (map[head.getX() - 1][head.getY()] == 0 || map[head.getX() - 1][head.getY()] == 1))
			return GameContext.DIR_LEFT;
		else if ($y < 0 && (map[head.getX()][head.getY() - 1] == 0 || map[head.getX()][head.getY() - 1] == 1))
			return GameContext.DIR_DOWN;
		else if ($y > 0 && (map[head.getX()][head.getY() + 1] == 0 || map[head.getX()][head.getY() + 1] == 1))
			return GameContext.DIR_UP;

		return GameContext.DIR_RIGHT;
	}

	private boolean isDest(int[][] map, Coordinate head, int $x, int $y) {
//		$x/Math.abs($x)
//		map[head.getX() + $x / Math.abs($x)]
		return Boolean.TRUE;
	}

}
