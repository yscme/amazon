package cn.yscme.www.controller;

import org.springframework.stereotype.Controller;

@Controller
public class HomeController {
	public String index() {
		return "index";
	}
}
