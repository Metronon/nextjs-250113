package com.ll.next_js_2025_01_10.global.initData;

import com.ll.next_js_2025_01_10.domain.member.member.service.MemberService;
import com.ll.next_js_2025_01_10.domain.post.post.service.PostService;
import com.ll.next_js_2025_01_10.standard.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

@Profile("dev")
@Configuration
@RequiredArgsConstructor
public class DevInitData {
    private final MemberService memberService;
    private final PostService postService;
    @Autowired
    @Lazy
    private DevInitData self;

    @Bean
    public ApplicationRunner devInitDataApplicationRunner() {
        return args -> {
            Ut.file.downloadByHttp("http://localhost:8080/v3/api-docs/apiV1", ".");

            String cmd = "yes | npx --package typescript --package openapi-typescript openapi-typescript apiV1.json -o ../nextjs-250113/frontend/src/lib/backend/apiV1/schema.d.ts\n";
            try {
                ProcessBuilder builder = new ProcessBuilder("C:\\Program Files\\Git\\bin\\bash.exe", "-c", cmd);
                builder.redirectErrorStream(true);
                Process process = builder.start();

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                }

                int exitCode = process.waitFor();
                System.out.println("Command exited with code: " + exitCode);

                File schemaFile = new File("../frontend/src/lib/backend/apiV1/schema.d.ts");
                if (schemaFile.exists()) {
                    System.out.println("Schema file was created successfully.");
                } else {
                    System.out.println("Schema file was not created.");
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        };
    }
}