import bean.Eigenvector;
import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

/**
 * Created by rongyang_lu on 2017/3/15.
 */
public class MainClass {
    public static void main(String[] args) {
        String text1 = "写api文档工具";
        String text2 = "api生成工具";


        IKSegmentation ikSeg = new IKSegmentation(new StringReader(text1), true);

        Lexeme l = null;
        try {
            while ((l = ikSeg.next()) != null) {
                String word = l.getLexemeText();
                int wordType = l.getLexemeType();
                System.out.println(wordType + "->" + word);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
