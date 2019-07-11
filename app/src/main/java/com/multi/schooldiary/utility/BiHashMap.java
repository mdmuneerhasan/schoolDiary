package com.multi.schooldiary.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BiHashMap<K1, K2, V> {

    private final Map<K1, Map<K2, V>> mMap;

    public BiHashMap() {
        mMap = new HashMap<K1, Map<K2, V>>();

    }

    public V put(K1 key1, K2 key2, V value) {
        Map<K2, V> map;
        if (mMap.containsKey(key1)) {
            map = mMap.get(key1);
        } else {
            map = new HashMap<K2, V>();
            mMap.put(key1, map);
        }

        return map.put(key2, value);
    }

    public V get(K1 key1, K2 key2) {
        if (mMap.containsKey(key1)) {
            return mMap.get(key1).get(key2);
        } else {
            return null;
        }
    }
    public boolean containsKeys(K1 key1, K2 key2) {
        return mMap.containsKey(key1) && mMap.get(key1).containsKey(key2);
    }

    public void clear() {
        mMap.clear();
    }

    public ArrayList<K2> get(K1 id) {
        ArrayList<K2> list=new ArrayList<>();
        Map<K2, V> map=mMap.get(id);
        if(map!=null){
            for(Map.Entry<K2, V> entry:map.entrySet()){
                list.add(entry.getKey());
            }
        }
        return  list;
    }
}