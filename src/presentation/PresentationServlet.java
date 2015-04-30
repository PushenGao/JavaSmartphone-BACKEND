package presentation;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dbutil.DaoImpl;

public class PresentationServlet extends HttpServlet{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//this servlet is used to get available car list from the server
	//first create a carmodeloptionio instance to communicate with the server
	//and get a car list, then forward to the info.jsp to show the available cars.
	public void doGet(HttpServletRequest req, HttpServletResponse resp){
//		System.out.println(System.getProperty("user.dir"));
		DaoImpl daoImpl = new DaoImpl();
		List<String> names =  daoImpl.getExistingUserid();
		String url = "info.jsp";
		for(int i = 0 ; i < names.size(); i++){
			String name = "name" + Integer.toString(i+1);
			req.setAttribute(name, names.get(i));
		}
		try {
			req.getRequestDispatcher(url).forward(req, resp);
		} catch (ServletException | IOException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp){
		doGet(req,resp);
	}
}
