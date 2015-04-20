package test;

import java.util.List;

import model.Account;
import dbutil.DaoImpl;

public class TestSearchUser {
	public static void main(String args[]){
		DaoImpl daoImpl = new DaoImpl();
		
		Account searched = daoImpl.getSearchUser("pusheng");
		System.out.print("");
	}
}
