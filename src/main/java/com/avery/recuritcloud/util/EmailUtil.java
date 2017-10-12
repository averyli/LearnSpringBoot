package com.avery.recuritcloud.util;

import com.avery.recuritcloud.entity.model.PublisherModel;
import org.joda.time.DateTime;

public class EmailUtil {
    public static PublisherModel getPublisherEmailModel()
    {
        return new PublisherModel("http://www.jianshu.com/p/f079f0b81fc5", DateTime.now());
    }
}
