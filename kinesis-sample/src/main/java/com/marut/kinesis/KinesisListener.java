package com.marut.kinesis;

import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessorFactory;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.KinesisClientLibConfiguration;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.Worker;

/**
 * Created by marutsingh on 12/19/16.
 */
public class KinesisListener {

    void processRecords(){
        final KinesisClientLibConfiguration config = new KinesisClientLibConfiguration(null,null,null,null);
        final IRecordProcessorFactory recordProcessorFactory = new SampleRecordProcessorFactory();
        final Worker worker = new Worker.Builder()
                .recordProcessorFactory(recordProcessorFactory)
                .config(config)
                .build();
    }
}
