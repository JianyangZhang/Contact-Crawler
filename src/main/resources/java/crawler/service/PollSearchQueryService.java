package crawler.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TimerTask;

import crawler.DAO.SearchQueryDAO;
import crawler.model.CrawlerQuery;

public class PollSearchQueryService extends TimerTask {
	private static boolean CRAWLER_IS_FREE = true;
	static final int NO_RESULT = 0;
	static final int COMPLETED = 1;
	public static final int FAILED = 2;
	
	
	// poll and crawl one pending query from database
	@Override
	public void run() {
		//System.out.println("polling search query from database...");
		if (CRAWLER_IS_FREE) {
			CRAWLER_IS_FREE = false;
			MyCallback myCallback = new MyCallback();
			ResultSet resultSet = SearchQueryDAO.getPendingQuery();
			// MySQLConnector.printResultSet(resultSet);
			try {
				if (resultSet.next()) {
					String search_id = resultSet.getString(1);
					SearchQueryDAO.updateToProcessing(search_id);
					CrawlerQuery query = new CrawlerQuery();
					/*System.out.print("search ID: " + resultSet.getString(1) + " | ");
					System.out.print("keyword: " + resultSet.getString(3) + " | ");
					System.out.print("client's linkedin ID: " + resultSet.getString(8) + " | ");
					System.out.print("internal company ID: " + resultSet.getString(9));
					System.out.println("target url Set size: " + resultSet.getInt(2));
					System.out.println("search from: " + resultSet.getString(6));*/
					query.setSearchID(resultSet.getString(1));
					query.setKeyword(resultSet.getString(3));
					query.setCount(resultSet.getInt(2));
					query.setLocation(resultSet.getString(4));
					query.setLinkedinID(resultSet.getString(8));
					query.setInternalCompanyID(resultSet.getString(9));
					query.setLkedin_email(resultSet.getString(11));
					query.setLkedin_passoword(resultSet.getString(12));
					if(resultSet.getString(6).equals("linkedin")) {
						CrawlEmailService.crawl(myCallback, query);
					}
					else if(resultSet.getString(6).equals("salegenie")) {
						CrawlSalesGenieService.crawl(myCallback, query);
					}
					if (myCallback.status == this.COMPLETED) {
						SearchQueryDAO.updateToComplete(search_id);
					} else if (myCallback.status == this.FAILED) {
						SearchQueryDAO.updateToFailed(search_id);
					}
					
					
				} else {
					//System.out.println("no pending search query");
					myCallback.process(this.NO_RESULT);
				}
			} catch (SQLException e) {
				myCallback.process(this.FAILED);
				e.printStackTrace();
				return;
			}
		}
	}

	// callback method
	private class MyCallback implements Callback {
		private int status;
		public void process(int status) {
			this.status = status;
			//System.out.println("attempted to poll search query, status code: " + status);
			//System.out.println("************************************************");
			CRAWLER_IS_FREE = true;
		}
	}
}
