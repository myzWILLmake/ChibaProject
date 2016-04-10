package moe.akagi.chibaproject.network.RetrieveData;

import java.util.Map;

/**
 * Created by yunze on 4/10/16.
 */
public class Manager {
    private DataListener listener;

    public void addDataListener(DataListener listener) {
        this.listener = listener;
    }

    public void removeDataListener() {
        this.listener = null;
    }

    public void dataReady(Object data) {
        if (listener == null)
            return;
        listener.onDataReady(data);
    }
}
