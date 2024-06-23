package com.mfc.batch.common.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaConfig {
	@Value("${spring.kafka.bootstrap-servers}")
	private String BOOTSTRAP_SERVER;

	@Value("${spring.kafka.consumer.group-id}")
	private String GROUP_ID;

	@Bean
	public ProducerFactory<String, Object> producerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return new DefaultKafkaProducerFactory<>(configProps);
	}

	@Bean
	public KafkaTemplate<String, Object> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

	// @Bean
	// public ConsumerFactory<String, PartnerListDto> partnerListConsumerFactory() {
	// 	Map<String, Object> configs = new HashMap<>();
	// 	configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
	// 	configs.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
	// 	configs.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
	//
	// 	return new DefaultKafkaConsumerFactory<>(
	// 			configs,
	// 			new StringDeserializer(),
	// 			new JsonDeserializer<>(PartnerListDto.class, false)
	// 	);
	// }
	//
	// @Bean
	// ConcurrentKafkaListenerContainerFactory<String, PartnerListDto> partnerListListener() {
	// 	ConcurrentKafkaListenerContainerFactory<String, PartnerListDto> factory
	// 			= new ConcurrentKafkaListenerContainerFactory<>();
	// 	factory.setConsumerFactory(partnerListConsumerFactory());
	// 	return factory;
	// }
}
