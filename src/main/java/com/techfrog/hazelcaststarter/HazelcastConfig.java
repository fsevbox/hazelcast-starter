package com.techfrog.hazelcaststarter;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.replicatedmap.ReplicatedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Map;

@Configuration
public class HazelcastConfig {

    @Autowired
    private ApplicationContext context;

    @Bean
    @Profile("k8s")
    public Config config() {
        Config config = new Config();
        JoinConfig joinConfig = config.getNetworkConfig().getJoin();
        joinConfig.getMulticastConfig().setEnabled(false);
        joinConfig.getKubernetesConfig().setEnabled(true)
                .setProperty("namespace", "default")
                .setProperty("service-name", "hazelcast-starter");
        return config;
    }

    @Bean
    @Profile("local")
    public HazelcastInstance hazelcastInstance() {
        return Hazelcast.newHazelcastInstance();
    }

    @Bean("map")
    public Map<String, String> getMap() {
        ReplicatedMap<String, String> map = getHazelcastInstance().getReplicatedMap("replicatedMap");
        map.addEntryListener(getEventListener());
        return map;
    }

    private HazelcastInstance getHazelcastInstance() {
        return context.getBean(HazelcastInstance.class);
    }

    @Bean
    public MapEventListener getEventListener() {
        return new MapEventListener();
    }

}
