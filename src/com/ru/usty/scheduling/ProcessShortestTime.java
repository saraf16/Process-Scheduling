package com.ru.usty.scheduling;
import com.ru.usty.scheduling.process.ProcessInfo;
import com.sun.xml.internal.ws.api.PropertySet;

import java.util.Comparator;


public class ProcessShortestTime  implements Comparator<ProcessInfo>{

    @Override
    public int compare(ProcessInfo x, ProcessInfo y)
    {
        // Assume neither string is null. Real code should
        // probably be more robust
        // You could also just return x.length() - y.length(),
        // which would be more efficient.
        if (x.totalServiceTime < y.totalServiceTime) {
            return -1;
        }
        if (x.totalServiceTime > y.totalServiceTime) {
            return 1;
        }
        return 0;
    }

}
