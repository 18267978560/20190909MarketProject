package com.legou.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.legou.cart.service.CartService;
import com.legou.common.utils.CookieUtils;
import com.legou.common.utils.JsonUtils;
import com.legou.common.utils.LegouResult;
import com.legou.pojo.TbItem;
import com.legou.pojo.TbUser;
import com.legou.service.ItemService;

@Controller
public class CartController {

	@Autowired
	private ItemService itemService;

	@Autowired
	private CartService cartService;



	//  '还未到购物车页面'，在这个页面完成对cookie中商品的信息添加，到cookie页面时可以直接使用
	@RequestMapping("/cart/add/{itemId}")
	public String addToCart(@PathVariable Long itemId, @RequestParam(defaultValue="1") Integer num, HttpServletRequest request,HttpServletResponse response) {

		//首先判断用户是否登陆,如果登陆则保存到redis中,如果没有登陆,则保存在cookie中
		TbUser tbUser = (TbUser) request.getAttribute("legouuser");
		if(tbUser != null) {

			cartService.addCartToRedis(tbUser.getId(),itemId ,num);

			return "cartSuccess";

		}
		//从客户端cookie中取出legoucart商品信息，如果存在则直接使用，如果不存在则从数据库中查找
		List<TbItem> tbItemList = getItemFromCookie(request);

		boolean flag = false;

		//遍历从cookie中取出的信息并判断是否存在，如果存在则不进入下面的if
		for (TbItem tbItem : tbItemList) {
			if(itemId == tbItem.getId()) {
				tbItem.setNum(tbItem.getNum() + num);
				//如果存在则没必要进入下个if，从数据库中查找，如果存在则直接设置
				flag = true;
			}
		}
		//判断
		if(!flag) {
			//从数据库中获取商品
			TbItem tbItem = itemService.getItem(itemId);
			tbItem.setNum(num);
			//获取图片需要显示在前端页面
			String imagePath = tbItem.getImage();
			//判断照片不为空
			if(StringUtils.isNotBlank(imagePath)) {
				String[] imagesPath = imagePath.split(",");
				tbItem.setImage(imagesPath[0]);
			}
			//
			tbItemList.add(tbItem);
		}
		//向浏览器客户端设置cookie，cookie的有效时间必须相同，不然会错
		CookieUtils.setCookie(request, response, "legoucart", JsonUtils.objectToJson(tbItemList), 30 * 60 * 60, true);
		return "cartSuccess";
	}



	//  '购物车页面'，返回购物车页面并从cookie中取出商品信息传入前端页面显示
	@RequestMapping("/cart/cart")
	public String goCart(HttpServletResponse response, HttpServletRequest request) {
		//从cookie中获取信息
		List<TbItem> list = getItemFromCookie(request);

		//判断如果用户登录的话,从redis中取出购物车的信息
		TbUser tbUser = (TbUser) request.getAttribute("legouuser");
		if(tbUser != null) {
			//将集合传入到redis中
			cartService.getRedisToCart(tbUser,list);
			//如果集合传到了redis中,那么浏览器页面的cookie就必须删除,不然redis中的商品会一直增加
			CookieUtils.deleteCookie(request, response, "legoucart");
			//将redis中的用户信息取出并返回给前端页面
			List<TbItem> lists = cartService.getItemList(tbUser);

			request.setAttribute("cartList", lists);
			return "cart";
		}

		//传入前端页面显示
		request.setAttribute("cartList", list);
		return "cart";
	}



	//  '更新购物车数量',更新用户中的商品数量
	@RequestMapping("/cart/update/num/{itemid}/{num}")
	@ResponseBody 
	public LegouResult modifyItemNum(@PathVariable long itemid, @PathVariable int num, HttpServletRequest request,
			HttpServletResponse response) {

		//判断用户是否登录
		TbUser tbUser = (TbUser) request.getAttribute("legouuser");
		if(tbUser != null) {
			//传一个id和一个商品id,还有一个需要修改的数量
			cartService.updateCartNum(tbUser.getId(),itemid,num);
			return LegouResult.ok();
		}
		
		//从cookie中获取到商品信息
		List<TbItem> itemList = getItemFromCookie(request);
		//遍历商品，判断选中商品的id和从cookie获取到的商品id相同的商品信息，并将其数量改变
		for (TbItem tbItem : itemList) {
			//判断
			if (tbItem.getId() == itemid) {
				tbItem.setNum(num);
			}
		}
		//将商品信息重新设置到浏览器客户端的cookie中，有效时间相同
		CookieUtils.setCookie(request, response, "legoucart", JsonUtils.objectToJson(itemList), 30 * 60 * 60, true);
		return LegouResult.ok();
	}



	//  '删除购物车',删除购物车中选中的商品信息
	@RequestMapping("/cart/delete/{itemid}")
	public String deleteCart(@PathVariable Long itemid, HttpServletRequest request, HttpServletResponse response) {

		//判断用户是否登录
		TbUser tbUser = (TbUser) request.getAttribute("legouuser");
		if(tbUser != null) {
			cartService.deleteCartFromRedis(tbUser.getId(),itemid);
			return "redirect:/cart/cart.html";
		}

		//通过cookie获取所有商品的信息
		List<TbItem> tbItems = getItemFromCookie(request);
		//遍历商品的信息，判断需要删除的id和商品中某个商品的id相等的话将他删除并跳出
		for (TbItem tbItem : tbItems) {
			if (tbItem.getId().longValue() == itemid) {
				tbItems.remove(tbItem);
				break;
			}
		}
		//重新将被删除后的商品信息添加到cookie中，cookie的有效时间相同
		CookieUtils.setCookie(request, response, "legoucart", JsonUtils.objectToJson(tbItems), 30 * 60 * 60, true);
		//重定向跳转到购物车页面
		return "redirect:/cart/cart.html";

	}



	//  '添加购物车'，从cookie中获取legoucart的信息，如果cookie中没有，再由数据库中查找并放入cookie中
	public List<TbItem> getItemFromCookie(HttpServletRequest request){
		//先通过cookie获得用户信息，并解码
		String cookieValue = CookieUtils.getCookieValue(request, "legoucart", true);

		//判断用户信息是否存在
		if(StringUtils.isBlank(cookieValue)) {
			return new ArrayList<TbItem>();
		}
		//将查找到的cookie中的信息放入集合中并返回，因为有可能是添加购物车添加了多个商品，所以需要用到集合
		List<TbItem> tbItemList = JsonUtils.jsonToList(cookieValue, TbItem.class);

		return tbItemList;
	}




}
