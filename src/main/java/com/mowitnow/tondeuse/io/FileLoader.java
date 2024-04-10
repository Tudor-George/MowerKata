package com.mowitnow.tondeuse.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileLoader {

    public static List<String> readFileToRowList(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line = null;
        List<String> result = new ArrayList<>();
        try {
            while((line = reader.readLine()) != null) {
                result.add(line);
            }

            return result;
        } finally {
            reader.close();
        }
    }
}
