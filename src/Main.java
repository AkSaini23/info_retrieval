import org.apache.lucene.queryparser.classic.ParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        Indexing indexing =new Indexing();
        indexing.indexer();
        String directory = "D:\\Sem5\\Information Retrieval\\P05\\P05_additional_resources\\Documents";
        File dir = new File(directory);
        File[] files = dir.listFiles();
        for (File f : files) {
            if (f.isFile()) {
                try (BufferedReader br = new BufferedReader(
                        new FileReader(f))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        indexing.writer(line);
                    }
                }
            }
        }
        indexing.query("it was a beautifull story story");
        indexing.searcher();
    }
}


