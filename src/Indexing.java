
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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

    public void indexer() throws IOException {

        analyzer = CustomAnalyzer.builder()
                .withTokenizer("standard")
                .addTokenFilter("lowercase")
                .build();
        //"index" Folder for storing Indexes
        directory = FSDirectory.open(new File("index").toPath());
        config = new IndexWriterConfig(analyzer);
        indexWriter = new IndexWriter(directory, config);
        indexWriter.deleteAll();
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
        System.out.println(query);
    }

    public void searcher() throws IOException, ParseException {
        int hitsPerPage = 10;
        reader = DirectoryReader.open(directory);
        searchoo = new IndexSearcher(reader);
        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
        searchoo.search(query, collector);
        hits = collector.topDocs().scoreDocs;
        for (int x = 0; x < hits.length; x++) {
            System.out.println("Matched documents " + x + " : " + searchoo.doc(hits[x].doc).get("Text"));
        }
        if (hits.length == 0) {
            System.out.println("No Match Found....");
        }
    }


    public void postingList(ArrayList<String> tokens) throws IOException, ParseException {
        int count = 0;

        while (count < tokens.size()) {
            String token = tokens.get(count);
            System.out.println();
            Term term = new Term("Text", token);
            int freq = 0;
            int docFreq = reader.docFreq(term);
            long totalFreq = reader.totalTermFreq(term);
            int temp = 0;
            System.out.print("[" + token + ":" + totalFreq + ":" + docFreq + "]->[");
            for (int x = 0; x < documentLen; x++) {
                Terms terms = reader.getTermVector(x, "Text");
                TermsEnum termsEnum = terms.iterator();
                boolean found = termsEnum.seekExact(new BytesRef(token));
                if (found) {
                    PostingsEnum posting = termsEnum.postings(null, PostingsEnum.ALL);
                    int docID;
                    while ((docID = posting.nextDoc()) != DocIdSetIterator.NO_MORE_DOCS) {
                        freq = posting.freq();
                        for (int i = 0; i < freq; i++) {
                            System.out.print(x + ":" + freq + ":[" + posting.nextPosition() + "]]");
                            if(i!=freq-1){
                                System.out.print("->[");
                            }
                        }
                    }
                    if(freq==1 && docFreq>temp+1) {
                        System.out.print("->[");
                        temp = temp + 1;
                    }
                }
            }
            temp =0;
            System.out.println();
            count++;
        }
    }
}


