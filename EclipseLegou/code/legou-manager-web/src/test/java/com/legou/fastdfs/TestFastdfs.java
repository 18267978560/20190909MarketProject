package com.legou.fastdfs;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

public class TestFastdfs {
	
	public static void main(String[] args) throws FileNotFoundException, IOException, MyException {
		//1、向工程中添加jar包
		//2、创建一个配置文件。配置tracker服务器地址
		//3、加载配置文件
		ClientGlobal.init("D:\\EclipseLegou\\code\\legou-manager-web\\src\\main\\resources\\conf\\fastdfs.properties");
		//4.创建一个TrackerClient对象
		TrackerClient trackerClient = new TrackerClient();
		//5.创建一个TrackerServer对象
		TrackerServer trackerServer = trackerClient.getConnection();
		//6、创建一个StorageServer的引用null就可以。
		StorageServer storageServer = null;
		//7、创建一个StorageClient对象。trackerserver,StorageServer两个参数。
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);
		//8、使用StorageClient对象上传文件。
		String[] strings = storageClient.upload_file("D:\\3.jpg", "jpg", null);
		for (String string : strings) {
			System.out.println(string);
		}
	}
	
}
