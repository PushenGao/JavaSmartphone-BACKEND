package test;

import model.AccountDAO;
import dbutil.DaoImpl;

public class TestGetBaseInfo {
	public static void main(String args[]){
		DaoImpl daoImpl = new DaoImpl();
		AccountDAO account = new AccountDAO();
		
		account = daoImpl.getUserInfo("pusheng","12345");
		System.out.print("");
	}
}
