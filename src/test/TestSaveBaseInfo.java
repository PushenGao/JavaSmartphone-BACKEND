package test;

import model.AccountDAO;
import dbutil.DaoImpl;

public class TestSaveBaseInfo {
	public static void main(String args[]){
		DaoImpl daoImpl = new DaoImpl();
		AccountDAO account = new AccountDAO();
		account.setAge("23");
		account.setGender("male");
		account.setId("haoyuc");
		account.setName("naochen");
		account.setPassword("12345");
		
		daoImpl.saveUserInfo(account);
	}
}
