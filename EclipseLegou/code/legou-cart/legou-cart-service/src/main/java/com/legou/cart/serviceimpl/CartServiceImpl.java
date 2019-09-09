package com.legou.cart.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.legou.cart.service.CartService;
import com.legou.common.redis.JedisClient;
import com.legou.common.utils.CookieUtils;
import com.legou.common.utils.JsonUtils;
import com.legou.mapper.TbItemMapper;
import com.legou.pojo.TbItem;
import com.legou.pojo.TbUser;


@Service
public class CartServiceImpl implements CartService{

	
	@Autowired
	JedisClient jedisClient;

	@Autowired
	TbItemMapper tbItemMapper;
	
	//  '购物车' 添加商品到redis购物车中,如果用户是登陆状态,那么添加购物车信息到redis缓存中,而不是cookie中保存
	@Override
	public void addCartToRedis(Long id, Long itemId, Integer num) {
		//通过商品id从数据库中搜索出商品
		TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
		//设置商品数量
		tbItem.setNum(num);
		//将商品转化成json格式设置进入redis
		String tbItemJson = JsonUtils.objectToJson(tbItem);
		//设置到redis数据库中
		jedisClient.hset("legou" + id , itemId+"", tbItemJson);
		
	}

	
	
	//  '购物车'将用户商品从cookie中拿出并添加入redis中,如果用户是登陆状态,那么显示购物车中的商品将从数据库中获取到并显示,而不是从cookie中获取
	@Override
	public void getRedisToCart(TbUser tbUser, List<TbItem> tbItemList) {
		
		//通过遍历cookie中保存的商品信息,全部通过上面的方法添加到redis中保存
		for (TbItem tbItem : tbItemList) {
			addCartToRedis(tbUser.getId(), tbItem.getId(), tbItem.getNum());
		}
		
	}


	//  '购物车'获取用户商品信息并返回到前端页面显示
	@Override
	public List<TbItem> getItemList(TbUser tbUser) {
		//通过id获取用户redis购物车中的信息并返回
		List<String> JsonItems = jedisClient.hvals("legou" + tbUser.getId());
		//新建一个TbItem类型的数组用来放TbItem当做返回值
		List<TbItem> tbItemList = new ArrayList<TbItem>();
		//通过遍历JsonItems,获得其中的json类型的TbItem
		for (String tbItemJson : JsonItems) {
			//通过工具类JsonUtils将json类型的TbItem转为成为TbItem类型并放入集合中
			TbItem tbItem = JsonUtils.jsonToPojo(tbItemJson, TbItem.class);
			tbItemList.add(tbItem);
		}
		//返回
		return tbItemList;
	}


	//  '删除用户购物车信息',从redis中删除用户的购物车信息
	@Override
	public void deleteCartFromRedis(Long id, Long itemid) {
		
		jedisClient.hdel("legou" + id, itemid+"");
	}


	//  '更新用户购物车数量',从reids中获取用户的购物车商品信息,并更改需要更改的数量 
	@Override
	public void updateCartNum(Long id, long itemid, int num) {
		//通过用户id和商品的信息或得到对应的商品信息
		String tbItemJson = jedisClient.hget("legou" + id, itemid+"");
		//将json类型转成TbItem类型
		TbItem tbItem = JsonUtils.jsonToPojo(tbItemJson, TbItem.class);
		//设置需要更改的数量
		tbItem.setNum(num);
		//将数据传回redis数据库中
		jedisClient.hset("legou" + id, itemid+"", JsonUtils.objectToJson(tbItem));
	}
	
}
