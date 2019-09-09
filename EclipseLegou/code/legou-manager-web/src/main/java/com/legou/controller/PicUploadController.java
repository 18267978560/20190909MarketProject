package com.legou.controller;

import java.util.HashMap;
import java.util.Map;

import javax.swing.plaf.multi.MultiFileChooserUI;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.legou.common.utils.FastDFSClient;
import com.legou.common.utils.JsonUtils;

@Controller
public class PicUploadController {
	
	@RequestMapping("/pic/upload")
	@ResponseBody
	public String uploadFile(MultipartFile uploadFile) {
		//需要捕获异常，调用上传文件的是客户，客户不能去处理异常 
		try {
			//创建工具类并传入配置文件使其初始化
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:conf/fastdfs.properties");
			/* 	需要两个参数
			 * 	1.fileName 文件全路径
			 * 	2.extName 文件扩展名，不包含（.） */
			//获取文件的名称以及路径
			int lastIndexOf = uploadFile.getOriginalFilename().lastIndexOf(".")+1;
			System.out.println(uploadFile.getOriginalFilename());
			System.out.println(lastIndexOf);
			String substring = uploadFile.getOriginalFilename().substring(lastIndexOf);
			System.out.println(substring);
			//获取地址
			String url = fastDFSClient.uploadFile(uploadFile.getBytes(),substring);
			System.out.println(url);
			String hostname="http://192.168.25.133/";
			//拼接路径
			url = hostname + url;
			System.out.println(url);
			
			//json返回的是键值对的类型
			Map map = new HashMap();
			map.put("error" , 0);
			map.put("url", url);
			return JsonUtils.objectToJson(map);
			
		} catch (Exception e) {
			Map map = new HashMap();
			map.put("error" , 1);
			map.put("url", "错误信息");
			return JsonUtils.objectToJson(map);
		}
	}
}
