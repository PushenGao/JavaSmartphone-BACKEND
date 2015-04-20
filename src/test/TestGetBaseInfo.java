package test;

import model.AccountPO;
import dbutil.DaoImpl;

public class TestGetBaseInfo {
	public static void main(String args[]){
		DaoImpl daoImpl = new DaoImpl();
		AccountPO account = new AccountPO();
		
		account = daoImpl.getUserInfo("pusheng","12345");
		System.out.print(account);
	}
}
