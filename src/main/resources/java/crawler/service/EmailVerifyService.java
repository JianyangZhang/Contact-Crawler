package crawler.service;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

public class EmailVerifyService {
	public static void main(String[] args) {
		// System.out.println(new EmailVerifier().valid("jianyang212@gmail.com", "outlook.com"));
	}

	/**
	 * verify the email address whether is exist
	 * @param toMail: the email need to be verified
	 * @param domain: the domain send the request(can be anyone)
	 * @return whether the email is available
	 */
	public boolean valid(String toMail, String domain) {
		if (StringUtils.isBlank(toMail) || StringUtils.isBlank(domain))
			return false;
		if (!StringUtils.contains(toMail, '@'))
			return false;
		String host = toMail.substring(toMail.indexOf('@') + 1);
		if (host.equals(domain))
			return false;
		Socket socket = new Socket();
		try {
			// find the mx records
			Record[] mxRecords = new Lookup(host, Type.MX).run();
			if (ArrayUtils.isEmpty(mxRecords))
				return false;
			// the email server address
			String mxHost = ((MXRecord) mxRecords[0]).getTarget().toString();
			if (mxRecords.length > 1) { // sort by priority
				List<Record> arrRecords = new ArrayList<Record>();
				Collections.addAll(arrRecords, mxRecords);
				Collections.sort(arrRecords, new Comparator<Record>() {

					public int compare(Record o1, Record o2) {
						return new CompareToBuilder()
								.append(((MXRecord) o1).getPriority(), ((MXRecord) o2).getPriority()).toComparison();
					}

				});
				mxHost = ((MXRecord) arrRecords.get(0)).getTarget().toString();
			}
			// start smtp
			socket.connect(new InetSocketAddress(mxHost, 25));
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(new BufferedInputStream(socket.getInputStream())));
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			// the overtime(ms)
			long timeout = 6000;
			// the sleep time
			int sleepSect = 50;

			// connect
			if (getResponseCode(timeout, sleepSect, bufferedReader) != 220) {
				return false;
			}

			// handshake
			bufferedWriter.write("HELO " + domain + "\r\n");
			bufferedWriter.flush();
			if (getResponseCode(timeout, sleepSect, bufferedReader) != 250) {
				return false;
			}
			// id
			bufferedWriter.write("MAIL FROM: <check@" + domain + ">\r\n");
			bufferedWriter.flush();
			if (getResponseCode(timeout, sleepSect, bufferedReader) != 250) {
				return false;
			}
			// verify
			bufferedWriter.write("RCPT TO: <" + toMail + ">\r\n");
			bufferedWriter.flush();
			if (getResponseCode(timeout, sleepSect, bufferedReader) != 250) {
				return false;
			}
			// disconnect
			bufferedWriter.write("QUIT\r\n");
			bufferedWriter.flush();
			return true;
		} catch (NumberFormatException e) {
		} catch (TextParseException e) {
		} catch (IOException e) {
		} catch (InterruptedException e) {
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
			}
		}
		return false;
	}

	private int getResponseCode(long timeout, int sleepSect, BufferedReader bufferedReader)
			throws InterruptedException, NumberFormatException, IOException {
		int code = 0;
		for (long i = sleepSect; i < timeout; i += sleepSect) {
			Thread.sleep(sleepSect);
			if (bufferedReader.ready()) {
				String outline = bufferedReader.readLine();
				// FIXME read
				while (bufferedReader.ready())
					/* System.out.println( */bufferedReader.readLine()/* ) */;
				/* System.out.println(outline); */
				code = Integer.parseInt(outline.substring(0, 3));
				break;
			}
		}
		return code;
	}
}
