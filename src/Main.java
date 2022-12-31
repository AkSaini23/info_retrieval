import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        Indexing indexing =new Indexing();
        indexing.indexer();
        indexing.writ   er("Today is sunny.");
        indexing.writer("She is a sunny girl.");
        indexing.writer("To be or not to be.");
        indexing.writer("She is in Berlin today.");
        indexing.writer("Sunny Berlin !");
        indexing.writer("Berlin is always exciting!");
        indexing.query("sunny AND exciting");
        indexing.searcher();
        ArrayList<String> str = new ArrayList<>();
        str.add("sunny");
        str.add("to");
        indexing.postingList(str);

    }
}
