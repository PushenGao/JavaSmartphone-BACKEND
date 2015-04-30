<%@page contentType="text/html; charset=GB2312"%>
<html>
    <head>
        <title>
           Show users
        </title>
    </head>

    <body style="background-color:lightblue">
    <h2 style="font-family:arial;color:red;font-size:25px"> users names:</h2>
        <% 
        for(int i = 1 ; ; i++){
        	String name = "name" + Integer.toString(i);
        	//System.out.println(name);
        	if(request.getAttribute(name) != null){
        		out.println(request.getAttribute(name));
        		out.println("</br>");
        	}
        	else{
        		break;
        	}
        }
        %>


    </body>
</html>