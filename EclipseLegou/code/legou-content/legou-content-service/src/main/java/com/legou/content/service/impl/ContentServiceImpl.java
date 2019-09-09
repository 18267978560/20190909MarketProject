package com.legou.content.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.legou.common.pojo.EasyUIDataGridResult;
import com.legou.common.redis.JedisClientPool;
import com.legou.common.utils.JsonUtils;
import com.legou.common.utils.LegouResult;
import com.legou.content.service.ContentService;
import com.legou.mapper.TbContentMapper;
import com.legou.pojo.TbContent;
import com.legou.pojo.TbContentExample;
import com.legou.pojo.TbContentExample.Criteria;


@Service
public class ContentServiceImpl implements ContentService{
	
	@Autowired
	TbContentMapper tbContentMapper;

	@Autowired
	JedisClientPool jedisClientPool;
	
	//新增
	@Override
	public LegouResult saveContent(TbContent tbContent) {
		//数据库里需要两个时间，所以创建并添加
		tbContent.setCreated(new Date());
		tbContent.setUpdated(new Date());
		tbContentMapper.insert(tbContent);
		//每次新增的时候，删除缓存，避免页面不更新的问题
		jedisClientPool.hdel("CONTENT_LIST", tbContent.getCategoryId().toString());
		//返回值为状态，ok
		return LegouResult.ok();
	}
	
	//实现点击叶子节点显示出数据以及分页内容
	@Override
	public EasyUIDataGridResult getContentList(long categoryId, Integer page, Integer rows) {
		//启动分页
		PageHelper.startPage(page, rows);
		//创建一个example对象并查询出对应的数据类型
		TbContentExample example = new TbContentExample();
		//创建查找条件
		Criteria createCriteria = example.createCriteria();
		//将所需要查找的条件加入，影响接下来的查询语句
		createCriteria.andCategoryIdEqualTo(categoryId);
		//查找出符合条件的TbContent
		List<TbContent> selectByPrimaryKey = tbContentMapper.selectByExample(example);
		//转化成pageinfo类型
		PageInfo<TbContent> pageInfo = new PageInfo<TbContent>(selectByPrimaryKey);
		//创建返回值对象
		EasyUIDataGridResult easyUIDataGridResult = new EasyUIDataGridResult();
		//将页与行存入对象中并返回
		easyUIDataGridResult.setRows(selectByPrimaryKey);
		easyUIDataGridResult.setTotal(pageInfo.getTotal());
		return easyUIDataGridResult;
	}

	
	//轮播图，含有缓存
	@Override
	public List<TbContent> getContentByCategoryId(long categoryId) {
		//查询缓存中是否有数据
		String hget = jedisClientPool.hget("CONTENT_LIST",categoryId+"");
		//如果查询出的hget缓存中有结果，那么直接返回缓存中的内容
		if(!StringUtils.isEmpty(hget)) {
			return JsonUtils.jsonToList(hget, TbContent.class);
		}
		//创建TbContentExample查找
		TbContentExample example = new TbContentExample();
		//创建条件
		Criteria createCriteria = example.createCriteria();
		//增加条件，影响查询语句
		createCriteria.andCategoryIdEqualTo(categoryId);
		//返回值需要一个list类型
		List<TbContent> selectByExample = tbContentMapper.selectByExample(example);
		//将从数据库中查询找的内容设置进缓存中
		jedisClientPool.hset("CONTENT_LIST", categoryId+"", JsonUtils.objectToJson(selectByExample));
		//返回
		return selectByExample;
	}
	

}
