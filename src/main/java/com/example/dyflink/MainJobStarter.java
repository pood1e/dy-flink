package com.example.dyflink;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.FromIteratorFunction;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Iterator;

/**
 * @author pood1e
 */
@Component
public class MainJobStarter implements ApplicationRunner {
	@Override
	public void run(ApplicationArguments args) throws Exception {
		Configuration configuration = new Configuration();
		configuration.setInteger("rest.port", 12012);
		StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(configuration);

		env.addSource(new FromIteratorFunction<>(new Iterator<ObjectNode>() {
			@Override
			public boolean hasNext() {
				return true;
			}

			@Override
			public ObjectNode next() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				ObjectNode node = JsonNodeFactory.instance.objectNode();
				node.put("tp", Math.random());
				return node;
			}
		}));
	}
}
