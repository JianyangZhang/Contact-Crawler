package crawler.DAO;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimerTask;

public class RecnctThread extends TimerTask{
	@Override
	public void run() {
		try {
			CustomerDAO.reconnect();
			EmailDAO.reconnect();
			ResultDAO.reconnect();
			SearchQueryDAO.reconnect();
			ResultSgDAO.reconnect();
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			System.out.println("Reconnect database at: " + sdf.format(cal.getTime()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
