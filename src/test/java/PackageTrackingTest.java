import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by <a href="mailto:micahliu153@gmail.com">ibtadmin</a> on 2016/4/11.
 */
public class PackageTrackingTest {
    @Test
    public void parseFromJson() throws IOException {
        URL url = getClass().getResource("USPS_9200199999977453249942.json");
        File file =new File(url.getPath());
        PackageTracking tracking = JSON.parseObject(FileUtils.readFileToString(file),PackageTracking.class);
        Assert.assertEquals(tracking.carrier,"usps");
        Assert.assertEquals(tracking.tracking_number,"9200199999977453249942");

    }
    @Test
    public void parseFromJsonSecond() throws IOException {
        URL url = getClass().getResource("USPS_9400110200881976430106.json");
        File file =new File(url.getPath());
        PackageTracking tracking = JSON.parseObject(FileUtils.readFileToString(file),PackageTracking.class);
        Assert.assertEquals(tracking.carrier,"usps");
        Assert.assertEquals(tracking.tracking_number,"9400110200881976430106");
    }

}
