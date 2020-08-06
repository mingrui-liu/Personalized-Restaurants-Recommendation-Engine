package rpc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import db.MySQLConnection;
import entity.Item;
import external.TicketMasterClient;

/**
 * Servlet implementation class SearchItem
 */
@WebServlet("/search")
public class SearchItem extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchItem() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// allow access only if session exists
		HttpSession session = request.getSession(false);
		if (session == null) {
			response.setStatus(403);
			return;
		}

				// optional
		String userId = session.getAttribute("user_id").toString(); 

		//String userId = request.getParameter("user_id");

		double lat = Double.parseDouble(request.getParameter("lat"));
		double lon = Double.parseDouble(request.getParameter("lon"));

		TicketMasterClient client = new TicketMasterClient();
		List<Item> items = client.search(lat, lon, null);
		
		MySQLConnection connection = new MySQLConnection();
		Set<String> favoritedItemIds = connection.getFavoriteItemIds(userId);
		connection.close();

		
		JSONArray array = new JSONArray();
		for (Item item : items) {
			JSONObject obj = item.toJSONObject();
			try {
				obj.put("favorite", favoritedItemIds.contains(item.getItemId()));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			array.put(obj);

			array.put(item.toJSONObject());
		}
		RpcHelper.writeJsonArray(response, array);

				
//				JSONArray array = new JSONArray();
//				try {
//					array.put(new JSONObject().put("name", "abcd").put("address", "San Francisco").put("time", "01/01/2017"));
//					array.put(new JSONObject().put("name", "1234").put("address", "San Jose").put("time", "01/01/2017"));
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}

				//writer.print(array);
				//RpcHelper.writeJsonArray(response, array);	

				
				response.getWriter().append("Served at: ").append(request.getContextPath());
//		if(request.getParameter("username")!= null) {
//			String username = request.getParameter("username");
//			
//			try {
//				obj.put("username", username);
//			}catch (JSONException e) {
//				e.printStackTrace();
//			}
//		}
//		
		
		
		
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// allow access only if session exists
		HttpSession session = request.getSession(false);
		if (session == null) {
				response.setStatus(403);
					return;
				}

	

		doGet(request, response);
	}

}
