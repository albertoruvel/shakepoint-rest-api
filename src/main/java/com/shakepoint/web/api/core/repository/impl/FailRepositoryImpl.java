package com.shakepoint.web.api.core.repository.impl;
import com.shakepoint.web.api.core.repository.FailRepository;
import com.shakepoint.web.api.data.entity.VendingMachineFail;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


public class FailRepositoryImpl implements FailRepository {

	@PersistenceContext
	private EntityManager em;

	private final Logger log = Logger.getLogger(getClass());

	public FailRepositoryImpl() {

	}

	@Override
	public void addFail(VendingMachineFail fail) {
		try{
			em.persist(fail);
		}catch(Exception ex){
			log.error("Could not add fail", ex);
		}
	}

}
