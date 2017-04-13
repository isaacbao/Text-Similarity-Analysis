package bean;

import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;
import util.ArrayUtil;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 要分析相似度的文本
 * Created by rongyang_lu on 2017/4/7.
 */
public class Text {
    //原始文本
    private String originText;

    //原始文本分词后提取的特征向量
    private List<Eigenvector> eigenvectors;

    //根据特征变量计算出的simHash值
    private boolean[] simHash;

    //simhash的位数，具体根据hash算法来定，现在用的是java string.hashcode，算出的hashcode是个int，就是32位
    private static int BIT_COUNT = 32;

    public Text(String originText) {
        this.originText = originText;
        this.eigenvectors = calculateEigenvectors();
        this.simHash = calculateSimHash();
    }

    public void setOriginText(String originText) {
        this.originText = originText;
        this.eigenvectors = calculateEigenvectors();
        this.simHash = calculateSimHash();
    }

    @Override
    public String toString() {
        return "Text{" +
                "eigenvectors=" + eigenvectors +
                ", originText='" + originText + '\'' +
                '}';
    }

    public List<Eigenvector> getEigenvectors() {
        return eigenvectors;
    }

    public String getOriginText() {
        return originText;
    }

    public List<Eigenvector> calculateEigenvectors() {
        List<Eigenvector> eigenvectorsTxt = new ArrayList<>();
        IKSegmentation ikSeg = new IKSegmentation(new StringReader(originText), true);
        Lexeme l = null;
        try {
            while ((l = ikSeg.next()) != null) {
                String word = l.getLexemeText();
                Eigenvector eigenvector = new Eigenvector(1, word);
                Eigenvector.addListWeightByFrequency(eigenvectorsTxt, eigenvector);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return eigenvectorsTxt;
    }

    private boolean[] calculateSimHash() {
        int[] hashSum = new int[BIT_COUNT];
        for (Eigenvector eigenvector : eigenvectors) {
            int[] hashWithWeight = eigenvector.calculateHashWithWeight(BIT_COUNT);
            for (int i = 0; i < BIT_COUNT; i++) {
                hashSum[i] += hashWithWeight[i];
            }
        }
        boolean[] simHash = new boolean[BIT_COUNT];
        for (int i = 0; i < BIT_COUNT; i++) {
            if (hashSum[i] > 0) {
                simHash[i] = true;
            }
        }
        return simHash;
    }

    /**
     * 计算该文本的sim hash和另一个文本的sim hash的hamming distance
     *
     */
    public int calculateHammingDistance(Text text) {
        boolean[] thisSimHash = this.simHash;
        boolean[] thatSimHash = text.simHash;

        int hammingDistance = 0;
        for (int i = 0; i < BIT_COUNT; i++) {
            if (thisSimHash[i] != thatSimHash[i]) {
                hammingDistance++;
            }
        }
        return hammingDistance;
    }
}
