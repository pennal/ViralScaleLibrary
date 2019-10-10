package org.viralscale.common.storage;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Singleton to store the data
public class DataStorage<T> {
    private static DataStorage instance;

    private Map<String, List<T>> data;

    private DataStorage() {
        this.data = new HashMap<>();
    }


    public static DataStorage getInstance() {
        if (instance == null) {
            instance = new DataStorage();
        }

        return instance;
    }


    public void add(String key, T data) {
        List<T> currentData = this.data.get(key);

        if (currentData == null) {
            currentData = new ArrayList<>();
            this.data.put(key, currentData);
        }

        currentData.add(data);
    }



    public List<T> get(String key) {
        return this.data.get(key);
    }

    public List<String> getAllKeys() {
        return new ArrayList<>(this.data.keySet());
    }



    public void remove(String key) {

    }


}
