package org.fabrelab.pagekit.dianping.dao;

import java.util.HashMap;
import java.util.List;

import org.fabrelab.pagekit.common.dao.EntityDAO;
import org.fabrelab.pagekit.common.model.Entity;
import org.fabrelab.pagekit.dianping.model.Shop;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

public class ShopDAOImpl extends SqlMapClientDaoSupport implements EntityDAO {

	public List<Shop> listShop() {
		return (List<Shop>)getSqlMapClientTemplate().queryForList("listShop");
	}

	public Shop getShopById(Long id) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		return (Shop)getSqlMapClientTemplate().queryForObject("getShopById", map);
	}

	public void updateShop(Long id, String title, String address) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("title", title);
		map.put("address", address);
		getSqlMapClientTemplate().update("updateShop", map );
	}

	public Long create(Entity shop) {
		return (Long)getSqlMapClientTemplate().insert("createShop", (Shop)shop);
	}

	public void deleteShop(Long id) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		getSqlMapClientTemplate().delete("deleteShop", map );
	}

	@Override
	public void harvest() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean enough() {
		return false;
	}

}
