package com.example.webhooksql.util;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

@Component
public class FileStore {
    public String write(String dir, String file, String content) throws Exception {
        File d = new File(dir);
        if (!d.exists() && !d.mkdirs()) {
            throw new IllegalStateException("Could not create directory: " + dir);
        }
        File out = new File(d, file);
        try (FileOutputStream fos = new FileOutputStream(out)) {
            fos.write(content.getBytes(StandardCharsets.UTF_8));
        }
        return out.getAbsolutePath();
    }
}
