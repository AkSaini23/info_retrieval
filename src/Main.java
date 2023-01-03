import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.queryparser.classic.ParseException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

public class Main extends JFrame implements ActionListener {
    static String query;
    JLabel label;
    JButton searchButton;
    JTextField field;
    JTextArea textArea;
    public Main() {
        field = new JTextField(50);
        Font font = new Font("Monospaced", Font.PLAIN, 20);
        field.setFont(font);
        searchButton= new JButton("Search");
        textArea=new JTextArea(25,60);
        JScrollPane scroll = new JScrollPane(textArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(field);
        add(searchButton);
        add(scroll);
        setVisible(true);
        textArea.setEditable(false);
        this.pack();
        textArea.setFont(font);
        textArea.setLineWrap(true);
        setLayout(new FlowLayout());
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        searchButton.addActionListener(this);

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            textArea.setText("");
            searchForQuery();

        } catch (IOException | ParseException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void searchForQuery() throws IOException, ParseException {

        Indexing indexing = new Indexing();
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
        query =field.getText();
        Analyzer ana = CustomAnalyzer.builder()
                .withTokenizer("standard")
                .addTokenFilter("lowercase")
                .addTokenFilter("stop", "ignoreCase", "false", "words", "stopwords.txt")
                .build();
        TokenStream token = ana.tokenStream(null, new StringReader(query));
        token.reset();
        ArrayList<String> ergebnis = new ArrayList<>();
        while (token.incrementToken()) {
            ergebnis.add(token.getAttribute(CharTermAttribute.class).toString());
        }

        StringBuilder queryInFormString= new StringBuilder();
        for (String str:ergebnis){
            queryInFormString.append(str).append(" ");
        }
        QueryExpansion queryExpansion = new QueryExpansion();
        ArrayList<String> queries = queryExpansion.newQueryWithSynonym(ergebnis);
        queries.add(0, String.valueOf(queryInFormString)); //adding our base query on the top of the list
        ArrayList<String> result = new ArrayList<>();
        for (String que : queries) {
            indexing.query(que);
            result = indexing.searcher();
        }


        for (String str : result) {
            textArea.append("-------------------------"+"\n");
            textArea.append(">>"+str+"\n"+"\n");
            textArea.append("-------------------------");
        }

    }

}


