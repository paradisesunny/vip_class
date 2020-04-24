package com.kingyee.prad.controller.admin;


import com.kingyee.common.jackson.JacksonMapper;
import com.kingyee.common.util.EncryptUtil;
import com.kingyee.common.util.TimeUtil;
import com.kingyee.prad.entity.PradScoreRule;
import com.kingyee.prad.entity.PradUser;
import com.kingyee.prad.entity.SysUser;
import org.springframework.expression.spel.ast.Operator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class Test {

	public static void main(String[] args) {
		List<PradUser> list = new ArrayList<>();
		PradUser user1 = new PradUser();
		user1.setPuId(1l);
		PradUser user2 = new PradUser();
		user2.setPuId(2l);
		PradUser user3 = new PradUser();
		user3.setPuId(3l);
		list.add(user1);
		list.add(user2);
		list.add(user3);

		/*list.forEach((a) -> {
			System.out.println(a.getPuId());
			return;//跳出当前循环，继续下一轮
		});*/


		list.forEach((user) ->{
			if(user.getPuCreateTime()!= null){
				TimeUtil.longToString(user.getPuCreateTime(),TimeUtil.FORMAT_DATETIME_FULL);
			}
			if(user.getPuId() == 2){
				System.out.println(user.getPuId());
				return;//跳出当前循环，继续下一轮
			}
			System.out.println("执行了吗");
		});

		//Optional遍历List
		List<Long> ids = new ArrayList<>();
		Optional.ofNullable(list).orElse(new ArrayList<>()).forEach(user ->{
			user.getPuTotalScore();
			ids.add(user.getPuId());
		});

		//类型声明
		MathOperation addition = (int a, int b) -> a + b;

	}

	interface MathOperation {
		int operation(int a, int b);
	}

	interface GreetingService {
		void sayMessage(String message);
	}

	private int operate(int a, int b, MathOperation mathOperation) {
		return mathOperation.operation(a, b);
	}
}
