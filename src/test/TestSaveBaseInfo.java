package test;

import model.AccountPO;
import dbutil.DaoImpl;

public class TestSaveBaseInfo {
	public static void main(String args[]){
		DaoImpl daoImpl = new DaoImpl();
		AccountPO account = new AccountPO();
		account.getAccount().setAge("23");
		account.getAccount().setGender("male");
		account.getAccount().setId("jiatel1");
		account.getAccount().setName("xiaolangjun");
		account.setPassword("12345");
		
		daoImpl.saveUserInfo(account);
	}
}
