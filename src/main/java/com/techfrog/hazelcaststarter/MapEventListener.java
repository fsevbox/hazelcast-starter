package com.techfrog.hazelcaststarter;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;
import com.hazelcast.map.MapEvent;

public class MapEventListener implements EntryListener<String, String> {

    @Override
    public void entryAdded(EntryEvent<String, String> event) {
        System.out.println("Entry added: " + event);
    }

    @Override
    public void entryEvicted(EntryEvent<String, String> event) {
        System.out.println("Entry evicted: " + event);
    }

    @Override
    public void entryUpdated(EntryEvent<String, String> event) {
        System.out.println("Entry updated: " + event);
    }

    @Override
    public void mapCleared(MapEvent event) {
        System.out.println("Map cleared: " + event);
    }

    @Override
    public void mapEvicted(MapEvent event) {
        System.out.println("Map evicted: " + event);
    }

    @Override
    public void entryExpired(EntryEvent<String, String> event) {
        System.out.println("Entry expired: " + event);
    }

    @Override
    public void entryRemoved(EntryEvent<String, String> event) {
        System.out.println("Entry removed: " + event);
    }
}
