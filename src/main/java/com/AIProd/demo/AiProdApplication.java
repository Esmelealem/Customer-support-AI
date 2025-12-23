package com.AIProd.demo;

import com.AIProd.demo.ingestion.KnowledgeIngestionService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AiProdApplication {
	private final KnowledgeIngestionService knowledgeIngestionService;

	public AiProdApplication(KnowledgeIngestionService knowledgeIngestionService) {
		this.knowledgeIngestionService = knowledgeIngestionService;
	}

	public static void main(String[] args) {
		SpringApplication.run(AiProdApplication.class, args);

	}
	@Value("${openai.api-key}")
	private String key;
	@PostConstruct
	void check() {
		System.out.println("OpenAI key loaded: " + (key != null));
		// Test with a sample document
		int result = knowledgeIngestionService.ingestText(
				"Test Document",
				"This is sample content for testing."
		);
		System.out.println("Rows affected: " + result);
	}

}
