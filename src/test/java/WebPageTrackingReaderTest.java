import com.google.inject.Inject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;


/**
 * Created by <a href="mailto:micahliu153@gmail.com">micah</a> on 2016/4/19.
 */
public class WebPageTrackingReaderTest {
    @Inject
    private WebPageTrackingReader webReader;
    @Test
    public void parseValidWebPageTrackingReaderTest(){
        try {
            Document doc = Jsoup.parse(new File("src\\main\\resources\\USPS_9200199999977453249942.html"),"UTF-8");
            PackageTracking packageTracking = WebPageTrackingReader.parse("usps","9200199999977453249942",doc);
            Assert.assertEquals(packageTracking.tracking_history.size(),6);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void parseValidWebPageTrackingReaderSecondSTest(){

        try {
            Document doc = Jsoup.parse(new File("src\\main\\resources\\USPS_9400110200881976430106.html"),"UTF-8");
            PackageTracking packageTracking = WebPageTrackingReader.parse("usps","9400110200881976430106",doc);
            Assert.assertEquals(packageTracking.tracking_history.size(),11);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void ParseInvalidPageTest(){
        try {
            Document doc = Jsoup.parse(new File("src\\main\\resources\\USPS_Invalid.html"),"UTF-8");
            Assert.assertEquals(webReader.validateDocument(doc),false);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void dateParseTest(){
        Assert.assertEquals("Mon Apr 04 08:20:00 PDT 2016", WebPageTrackingReader.dateParse("April 4, 2016 , 8:20 am").toString());
    }
    @Test
    public void dateParseSecondTest(){
        Assert.assertEquals("Mon Apr 04 00:00:00 PDT 2016", WebPageTrackingReader.dateParse("April 4, 2016").toString());

    }

}