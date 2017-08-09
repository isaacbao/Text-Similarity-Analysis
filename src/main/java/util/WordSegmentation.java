package util;

import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rongyang_lu on 2017/7/31.
 */
public class WordSegmentation {

    public static List<String> splitWords(String text) {
        List<String> wordsInDoc = new ArrayList<>();
        IKSegmentation ikSeg = new IKSegmentation(new StringReader(text), true);
        Lexeme l = null;
        try {
            while ((l = ikSeg.next()) != null) {
                String word = l.getLexemeText();
                wordsInDoc.add(word);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wordsInDoc;
    }
}
