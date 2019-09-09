package com.legou.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.legou.common.utils.CookieUtils;
import com.legou.common.utils.LegouResult;
import com.legou.pojo.TbUser;
import com.legou.sso.service.TokenService;

public class LegouInterceptor implements HandlerInterceptor{

	@Autowired
	TokenService tokenService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//通过token从客户端中获取到对应的用户数据
		String tokenValue = CookieUtils.getCookieValue(request, "token");
		//判断该用户是否存在，如果存在的话，就可以允许将商品加入购物车中，反之不允许通过
		if(StringUtils.isBlank(tokenValue)) {
			return true;
		}
		//通过前台传的token从缓存中获取用户的信息
		LegouResult legouResult = tokenService.getTbUser(tokenValue);
		//如果信息不等于200，相当于用户登录的信息错误，返回false，登录过期
		if(legouResult.getStatus() != 200) {
			return true;
		}
		//获取到对应的用户信息
		TbUser tbUser = (TbUser) legouResult.getData();
		//将token中的用户信息传入controller中
		request.setAttribute("legouuser", tbUser);
		return true;
	}

	@Override	
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		System.out.println("postHandle");
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		System.out.println("afterCompletion");
	}
	
}
