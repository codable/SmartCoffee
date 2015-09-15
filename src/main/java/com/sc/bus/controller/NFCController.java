package com.sc.bus.controller;

import java.awt.AWTException;
import java.awt.Robot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/nfc")
public class NFCController {

	/*
	 * Client will call it at startup, to get floors
	 */
	@RequestMapping(value = "/tag", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody String getFloors() {
		try {
			Robot robot = new Robot();
			type("1", robot);
			type("Hello world", robot);
		} catch (AWTException e) {
			e.printStackTrace();
		}

		return "{\"floors\": 3}";
	}

	private void type(String s, Robot robot) {
		byte[] bytes = s.getBytes();
		for (byte b : bytes) {
			int code = b;
			// keycode only handles [A-Z] (which is ASCII decimal [65-90])
			if (code > 96 && code < 123)
				code = code - 32;
			robot.delay(40);
			robot.keyPress(code);
			robot.keyRelease(code);
		}
	}
}
