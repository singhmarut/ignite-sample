package com.marut.pagecount;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by marutsingh on 12/17/16.
 */
public class PageCountSimulator {
   // ExecutorService pageSimulator = Executors.newFixedThreadPool(1);
    ScheduledExecutorService pageSimulator = Executors.newScheduledThreadPool(1);
    //60 buckets for each second
    List<AtomicLong> countBuckets = new ArrayList<>(60);
    long start = 0;
    long totalCount = 0L;

    public void simulate(){
        start = Instant.ofEpochSecond(0L).until(Instant.now(),
                ChronoUnit.SECONDS);
        //Every 10 millisecond create an item
        pageSimulator.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Item item = new Item();
                addItem(item);
            }
        }, 0, 10, TimeUnit.MILLISECONDS);
    }

    public synchronized void addItem(Item item){
        //1 second
        long diff = (item.getTime() - start);
        System.out.println();
        System.out.println(" diff is: " + diff);
        //We need to find the right bucket where events have to go
        //Move all buckets if new bucket has to be created
        if (diff >= 1){
            for (int i = countBuckets.size(); i >=0; i--){
                moveBucket(i,(int)diff);
            }
            //Add a new bucket by making count to 0
            countBuckets.get(0).set(0L);
            start = getStartTime();
        }
        AtomicLong currentBucket = countBuckets.get(0);
        currentBucket.set(currentBucket.longValue() + 1);
        totalCount = 0L;
        for (int i = 0; i < countBuckets.size(); i++){
            String bucketCount = countBuckets.get(i).toString();
            totalCount += countBuckets.get(i).get();
            System.out.print(bucketCount + ",");
        }
        System.out.println("totalCount: " + totalCount);
    }

    long getStartTime(){
        return Instant.ofEpochSecond(0L).until(Instant.now(),
                ChronoUnit.SECONDS);
    }

    void moveBucket(int curIndex, int places){
        int newIndex = curIndex + places;
        if (newIndex < countBuckets.size()){
            long currentValue = countBuckets.get(curIndex).longValue();
            countBuckets.get(newIndex).set(currentValue);
        }
    }

    static class Item{
        //Time when the item was created
        long time = Instant.ofEpochSecond(0L).until(Instant.now(),
                ChronoUnit.SECONDS);

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }
    }
}
