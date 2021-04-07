package cn.yscme.www.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.yscme.www.bean.Amazon;
import cn.yscme.www.pageProcessor.AmazonCOM;

@RestController
@RequestMapping("/amazon")
public class AmazonController {
	@PostMapping("/list/{search}/{page}/{thread}")
	public List<Map<String, Object>> list(@PathVariable String search,@PathVariable Integer page, @PathVariable Integer thread ) {
		Amazon.list.clear();
		new AmazonCOM().run(search,page,thread);
		return Amazon.list;
	}
	
}
