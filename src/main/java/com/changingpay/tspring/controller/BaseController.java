package com.changingpay.tspring.controller;

import com.changingpay.tspring.business.BuyTicket;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

@Controller
public class BaseController {
	
	@Autowired
	private BuyTicket  buyTicket;
	
	@Value("#{file_resource.portal_log}")
	private String fileResource;
	
	@Value("#{file_resource.out_dir}")
	private String outDir;
	
	@RequestMapping("/login/testLogin.do")
	@ResponseBody
	public void print(){
		LogFactory.getLog(BaseController.class).info(buyTicket.insert666());
	}
	
	@RequestMapping("/login/login.do")
	public ModelAndView login(){
		ModelAndView mav = new ModelAndView();
		mav.setViewName("login");
		
		return mav;
	}
	
	@RequestMapping("/login/getPic.do")
	public void getPic(HttpServletResponse resp){
		File portalLog = new File(fileResource);
		try {
			InputStream ins = new FileInputStream(portalLog);
			byte[] tempArray = new byte[1024];

			OutputStream ops = resp.getOutputStream();
			while(ins.read(tempArray) != -1){
				ops.write(tempArray);
			}
			
			ops.flush();
			ops.close();
			ins.close();
		} catch (Exception e) {
			LogFactory.getLog(BaseController.class).info("文件读取失败。 "+e.getMessage());
		}
	}
}
