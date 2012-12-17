package org.fabrelab.pagekit.common.dao;

import org.fabrelab.pagekit.common.model.Entity;

public interface EntityDAO {
	
	public void harvest();

	public Long create(Entity rntity);

	public boolean enough();

}
