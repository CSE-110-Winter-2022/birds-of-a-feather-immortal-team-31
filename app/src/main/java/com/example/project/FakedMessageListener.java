package com.example.project;

import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FakedMessageListener extends MessageListener{
    private final MessageListener messageListener;
    private final ScheduledExecutorService executor;

    public FakedMessageListener(MessageListener realMessageListener, int frequency, String messagestr){
        this.messageListener = realMessageListener;
        this.executor = Executors.newSingleThreadScheduledExecutor();

        executor.scheduleAtFixedRate(() ->{
            Message message = new Message(messagestr.getBytes(StandardCharsets.UTF_8));
            this.messageListener.onFound(message);
            this.messageListener.onLost(message);
        }, 6, frequency, TimeUnit.SECONDS);
    }

    public void cleanup() {
    }
}
