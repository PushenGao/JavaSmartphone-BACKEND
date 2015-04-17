package test;

import java.util.List;

import model.AccountDAO;
import model.AccountPO;
import dbutil.DaoImpl;

public class TestGetRecommend {
	public static void main(String args[]){
		DaoImpl daoImpl = new DaoImpl();
		
		List<AccountPO> recommends = daoImpl.getRecommendFriend("pusheng");
		System.out.print("");
	}
}
