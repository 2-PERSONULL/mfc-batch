package com.mfc.batch.common.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.mfc.batch.batch.dto.kafka.PartnerSummaryDto;
import com.mfc.batch.batch.dto.kafka.PostSummaryDto;

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

	@Bean
	public ConsumerFactory<String, PostSummaryDto> postSummaryConsumerFactory() {
		Map<String, Object> configs = new HashMap<>();
		configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
		configs.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
		configs.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

		return new DefaultKafkaConsumerFactory<>(
				configs,
				new StringDeserializer(),
				new JsonDeserializer<>(PostSummaryDto.class, false)
		);
	}

	@Bean
	ConcurrentKafkaListenerContainerFactory<String, PostSummaryDto> postSummaryListener() {
		ConcurrentKafkaListenerContainerFactory<String, PostSummaryDto> factory
				= new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(postSummaryConsumerFactory());
		return factory;
	}

	@Bean
	public ConsumerFactory<String, PartnerSummaryDto> partnerSummaryConsumerFactory() {
		Map<String, Object> configs = new HashMap<>();
		configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
		configs.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
		configs.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

		return new DefaultKafkaConsumerFactory<>(
				configs,
				new StringDeserializer(),
				new JsonDeserializer<>(PartnerSummaryDto.class, false)
		);
	}

	@Bean
	ConcurrentKafkaListenerContainerFactory<String, PartnerSummaryDto> partnerSummaryListener() {
		ConcurrentKafkaListenerContainerFactory<String, PartnerSummaryDto> factory
				= new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(partnerSummaryConsumerFactory());
		return factory;
	}
}
