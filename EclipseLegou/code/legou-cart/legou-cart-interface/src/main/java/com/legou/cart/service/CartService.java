package com.legou.cart.service;

import java.util.List;

import com.legou.pojo.TbItem;
import com.legou.pojo.TbUser;

public interface CartService {

	void addCartToRedis(Long id, Long itemId, Integer num);

	void getRedisToCart(TbUser tbUser, List<TbItem> tbItemList);

	List<TbItem> getItemList(TbUser tbUser);

	void deleteCartFromRedis(Long id, Long itemid);

	void updateCartNum(Long id, long itemid, int num);

}
