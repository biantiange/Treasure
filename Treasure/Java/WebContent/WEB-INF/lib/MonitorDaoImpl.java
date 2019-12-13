package monitor.dao;

import java.util.List;
import java.util.Map;

public class MonitorDaoImpl {
	/*// 查找购物车全部物品
	public List<Cart> findAll(int userId) {
		return DBUtil.find(Cart.class, "select * from cart where userId=?", new Object[] { userId });
	}

	// 修改蛋糕数量
	public int editCakeCount(Cart cart) {
		return DBUtil.executeUpdate("update cart set cakeCount=? where cartId=?",
				new Object[] { cart.getCakeCount(), cart.getCartId()});
	}
	public int editAllPrice(Cart cart) {
		return DBUtil.executeUpdate("update cart set allPrice=? where cartId=?",
				new Object[] { cart.getAllPrice(), cart.getCartId() });
	}

	// 删除购物车一项
	public int deleteCartItem(int cartId) {
		return DBUtil.executeUpdate("delete from cart where cartId=?", new Object[] { cartId });
	}

	// 商品添加到购物车
	public int addCart(Cart cart) {
		List<Cart> list = new MonitorServiceImpl().listCarts(cart.getUserId());
		for (Cart c : list) {
			if (c.getCakeId() == cart.getCakeId()) {
				cart.setCakeCount(c.getCakeCount() + 1);
				cart.setCartId(c.getCartId());
				cart.setAllPrice(c.getAllPrice()+c.getAllPrice());
				return (new MonitorServiceImpl().editCakeCount(cart)) *(new MonitorServiceImpl().editAllprice(cart)) ;
			}
		}
		return DBUtil.executeUpdate("insert into cart(userId,cakeId,cakeCount,allPrice,cakeImg) values (?,?,?,?,?)",
				new Object[] { cart.getUserId(), cart.getCakeId(), cart.getCakeCount(), cart.getAllPrice(),
						cart.getCakeImg() });
	}*/

}
