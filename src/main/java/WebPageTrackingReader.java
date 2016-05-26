import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
/**
 * Created by <a href="mailto:micahliu153@gmail.com">micah</a> on 2016/4/11.
 */
public class WebPageTrackingReader implements PackageTrackingReader{

    @Override
    public  PackageTracking read(String carrier,String trackingNumber){

        String usps_site_url = "https://tools.usps.com/go/TrackConfirmAction?qtc_tLabels1=" + trackingNumber;
        try{
            Document doc = getDocument(usps_site_url);
            return parse(carrier, trackingNumber, doc);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static PackageTracking parse(String carrier, String trackingNumber, Document doc) {
        PackageTracking packageTracking = new PackageTracking();
        packageTracking.carrier = carrier;
        packageTracking.tracking_number = trackingNumber;

        Elements events = doc.select("#tc-hits > tbody > tr.detail-wrapper");

        List<PackageTracking.TrackingHistory> list = new ArrayList<PackageTracking.TrackingHistory>(events.size());

        for (Element el : events){
            PackageTracking.TrackingHistory trackingHistory = new PackageTracking.TrackingHistory();
            String dateTxt = el.select("td.date-time").text().trim();
            trackingHistory.date = dateParse(dateTxt);
            String locationTxt = el.select("td.location").text();
            Location location = Location.parse(locationTxt);
            trackingHistory.location = location;
            trackingHistory.details = el.select("td.status").text().trim();

            trackingHistory.status = trackingHistory.details.toLowerCase().contains("delivered") ? "DELIVERED" : "TRANSIT";
            if (trackingHistory.details.toLowerCase().contains("delivered")) {
                trackingHistory.status = "delivered";
            } else {
                trackingHistory.status = "TRANSIT";
            }

            list.add(trackingHistory);
        }
        Collections.sort(list);
        packageTracking.tracking_history = list;
        packageTracking.tracking_status = list.get(list.size() - 1);

        return packageTracking;
    }

    public Document getDocument(String usps_site_url) throws IOException {
        return Jsoup.connect(usps_site_url)
                        .header("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.112 Safari/537.36")
                        .header("Connection","keep-alive")
                        .followRedirects(true)
                        .get();
    }


    public boolean validateDocument(Document doc) {
        Element err = doc.select("#results-multi > div.panel.tracking-result.result-error.result-open > div > div.tracking-summary > div.tracking-progress.status-error > div.progress-indicator > h2").first();
        if (doc.html().length() >0 && err !=null){
            return false;
        }else{
            return true;
        }
    }
    public static Date dateParse(String dateStr){
        Date d = null;
        try {
            Locale.setDefault(Locale.US);
            d = DateUtils.parseDateStrictly(dateStr, new String[] {"MMM d, yyyy , h:mm a", "MMM d, yyyy"});
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }
}
