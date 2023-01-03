
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Indexing {
    Analyzer analyzer;
    Directory directory;
    IndexWriterConfig config;
    IndexWriter indexWriter;
    String querystr;
    IndexSearcher searchoo;
    Query query;
    ScoreDoc[] hits;

    static int documentLen = 0;
    IndexReader reader;
    ArrayList<String> matchedDocuments;

    public void indexer() throws IOException {

        analyzer = CustomAnalyzer.builder()
                .withTokenizer("standard")
                .addTokenFilter("stop", "ignoreCase", "false", "words", "stopwords.txt", "format", "wordset")
                .addTokenFilter("lowercase")
                .build();
        //"index" Folder for storing Indexes
        directory = FSDirectory.open(new File("index").toPath());
        config = new IndexWriterConfig(analyzer);
        indexWriter = new IndexWriter(directory, config);
        indexWriter.deleteAll();
        matchedDocuments = new ArrayList<>();
    }

    private static void addDoc(final IndexWriter w, String document) throws IOException {
        Document doc = new Document();
        FieldType ft = new FieldType();
        ft.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
        ft.setStored(true);
        ft.setStoreTermVectors(true);
        ft.setStoreTermVectorPositions(true);
        ft.setStoreTermVectorPayloads(true);
        ft.setStoreTermVectorOffsets(true);
        doc.add(new Field("Text", document, ft));
        w.addDocument(doc);
        documentLen++;
    }

    public void writer(String document) throws IOException {
        addDoc(indexWriter, document);
    }

    public void query(String str) throws IOException, ParseException {
        indexWriter.close();
        querystr = str;
        query = new QueryParser("Text", analyzer).parse(querystr);
    }

    public ArrayList<String> searcher() throws IOException, ParseException {
        int hitsPerPage = 10;
        reader = DirectoryReader.open(directory);
        searchoo = new IndexSearcher(reader);
        searchoo.setSimilarity(new BM25Similarity());
        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
        searchoo.search(query, collector);
        hits = collector.topDocs().scoreDocs;
        for (ScoreDoc hit : hits) {
            if (!matchedDocuments.contains(searchoo.doc(hit.doc).get("Text"))) {
                matchedDocuments.add(searchoo.doc(hit.doc).get("Text"));
            }
        }
        if (hits.length == 0) {
            System.out.println("No Match Found....");
        }
        return matchedDocuments;
    }
}


