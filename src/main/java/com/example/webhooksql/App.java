package com.example.webhooksql;

import com.example.webhooksql.client.HiringApiClient;
import com.example.webhooksql.dto.WebhookResponse;
import com.example.webhooksql.logic.SqlChooser;
import com.example.webhooksql.util.FileStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    ApplicationRunner runner(HiringApiClient client,
                             SqlChooser sqlChooser,
                             FileStore fileStore,
                             @Value("${app.storage.outDir}") String outDir,
                             @Value("${app.storage.outFile}") String outFile) {
        return args -> {
            // 1) Call generateWebhook
            WebhookResponse info = client.generateWebhook();

            // 2) Compute SQL
            String finalSql = sqlChooser.choose();

            // 3) Store the SQL result locally
            String path = fileStore.write(outDir, outFile, finalSql);
            System.out.println("Saved final SQL to: " + path);

            // 4) Submit the final SQL using returned accessToken (used as-is)
            ResponseEntity<String> resp = client.submitFinalQuery(info, finalSql);
            System.out.println("Submission status: " + resp.getStatusCode());
            System.out.println("Submission body: " + resp.getBody());
        };
    }
}
