package test;

import java.util.List;

import model.AccountPO;
import dbutil.DaoImpl;

public class TestSearchUser {
	public static void main(String args[]){
		DaoImpl daoImpl = new DaoImpl();
		
		AccountPO searched = daoImpl.getSearchUser("pusheng");
		System.out.print("");
	}
}
