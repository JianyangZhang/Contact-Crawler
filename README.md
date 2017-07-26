<h1 align="center">Contact Crawler</h1>
<h2>Crawl Over Ten Thousands Deliverable Emails & Phone Numbers Per Day :heavy_check_mark: </h2>
<p>Request-Handling Pipeline: <br/>
insert pending query -> query get scanned -> update query's status -> check results during or after the crawling</p>

<h4>Front-End:</h4>
<p>
  - login and schedule crawl queries<br/>
  - check query status<br/>
  - display results in tables<br/>
  - export results to .csv files and download<br/>
  - send bulk emails<br/>
</p>
<h4>Documentation: </h4>

| html          | Description   |
| ----------------         |---------------|
|index.html                | the homepage to login|
|search.html                 |    input the query into database    |
|result.html| show the result table fetch from database|


| php         | Description   |
| ----------------         |---------------|
|login.php                | verifyt the account from database |
|searchQ.php                 |    insert the query into database, fetch the exist query    |
|result.php| show the result table fetch from database which searched from linkedin|
|result_sg.php| show the result table fetch from database which searched from salegenie|
|refresh.php| asynchronous refresh the query|
|delete.php| asynchronous delete the query from database|


| javascript        | Description   |
| ----------------         |---------------|
|table2CSV.js                | convert the table in html to csv file that can be downloaded|



<h4>Back-End:</h4>
<p>
  - scan and process the pending queries multithreaded<br/>
  - crawl target emails & phone numbers from Linkedin & SalesGenie & Google<br/>
  - verify if the emails are valid and deliverable<br/>
  - store results into MySQL<br/>
</p>

<h4>Documentation: </h4>

| Package crawler          | Description   |
| ----------------         |---------------|
| EmailCrawlerAPI          | Entrance of the program, launch the Spring Boot |
| EmailCrawlerConfig       | Read configuration file |

| Package crawler.controller | Description   |
| ----------------         |---------------|
| CrawlEmailController     | Map RESTful API|

| Package crawler.DAO | Description   |
| ----------------         |---------------|
| MySQLConnector     | Encapsulate the methods of connecting to MySQL |
| RecnctThread     | Reconnect to MySQL to avoid timeout |
| CompanyDAO     | insert and update data to the Company table |
| CustomerDAO     | insert and update data to the Customer table |
| EmailDAO     | insert and update data to the Email table |
| ResultDAO     | insert and update data to the Result table |
| ResultSgDAO     | insert and update data to the ResultSg table |
| SalesgenieDAO     | insert and update data to the SalesgenieDAO table |
| SearchQueryDAO     | insert and update data to the SearchQueryDAO table |

| Package crawler.model | Description   |
| ----------------         |---------------|
| CrawlerQuery     | Data model of a query |
| Customer     | Data model of a customer |
| Email     | Data model of an Email |
| SalesGenieResult     |  Data model of a result from SalesGenie |

| Package crawler.service | Description   |
| ----------------         |---------------|
| Callback     | Interface of the callback when a query has been completed or failed |
| PollSearchQueryService     | Check if there is any pending query in database. If yes, send it to the line of production |
| CrawlEmailService     | The process of crawling email from Linkedin  |
| CrawlSalesGenieService     | The process of crawling SalesGenie |
| DriveBrowserService     | The implementation of the general browser operations |
| DriveLinkedinService     | The implementation of the browser operations for crawling Linkedin |
| DriveSalesgenieService     | The implementation of the browser operations for crawling SalesGenie |
| EmailVerifyService     | Verify if a given email address is deliverable |
| GeneratAccurateEmailsService     | Generate a person's email addresses based on his name and companies |
| SendEmailService     | Send email by Java code, should not be used to spam emails |
| LaunchWindowService     | UI by Java Swing (discarded) |

| Package crawler.thread | Description   |
| ----------------       |---------------|
| CrawlCompanyThread   | ??? |
| CrawlCustomerThread   | ??? |

## Copyright and license
Code and documentation copyright 2016-2017 the [Jianyang Zhang](https://github.com/JianyangZhang), [Wentao Wang](https://github.com/wentao-wang) and [Yihan Lu](https://github.com/xdoobx). Code released under the [MIT License](https://github.com/JianyangZhang/EmailCrawler/blob/master/licence).
