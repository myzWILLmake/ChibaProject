package moe.akagi.chibaproject.network.RetrieveData;

import java.util.EventListener;
import java.util.Map;

/**
 * Created by yunze on 4/10/16.
 */
public interface DataListener extends EventListener {
    void onDataReady(Object data);
}