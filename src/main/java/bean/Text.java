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

    //原始文本分词后提取的特征变量
    private List<Eigenvector> eigenvectors;

    //根据特征变量计算出的simHash值
    private boolean[] simHash;

    public Text(String originText) {
        this.originText = originText;
        this.eigenvectors = calculateEigenvectors();
        this.simHash = calculateSimHash(this.eigenvectors);
    }

    public void setOriginText(String originText) {
        this.originText = originText;
        this.eigenvectors = calculateEigenvectors();
        this.simHash = calculateSimHash(this.eigenvectors);
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

    private boolean[] calculateSimHash(List<Eigenvector> eigenvectors) {
        int bitCount = getMaxBitCount(eigenvectors);
        int[] hashSum = new int[bitCount];
        for (Eigenvector eigenvector : eigenvectors) {
            int[] hashWithWeight = eigenvector.calculateHashWithWeight(bitCount);
            for (int i = 0; i < bitCount; i++) {
                hashSum[i] += hashWithWeight[i];
            }
        }
        boolean[] simHash = new boolean[bitCount];
        for (int i = 0; i < bitCount; i++) {
            if (hashSum[i] > 0) {
                simHash[i] = true;
            }
        }
        return simHash;
    }

    private int getMaxBitCount(List<Eigenvector> eigenvectors) {
        int maxBitCount = 0;
        for (Eigenvector eigenvector : eigenvectors) {
            int bitCount = Integer.toBinaryString(eigenvector.getWord().hashCode()).length();
            if (bitCount > maxBitCount) {
                maxBitCount = bitCount;
            }
        }
        return maxBitCount;
    }

    /**
     * 计算该文本的sim hash和另一个文本的sim hash的hamming distance
     *
     * @return
     */
    public int calculateHammingDistance(Text text) {
        boolean[] thisSimHash = this.simHash;
        boolean[] thatSimHash = text.simHash;

        if (thisSimHash.length > thatSimHash.length) {
            thatSimHash = ArrayUtil.expandTo(thatSimHash, thisSimHash.length);
        } else {
            this.simHash = ArrayUtil.expandTo(thisSimHash, thatSimHash.length);
        }
        int length = thisSimHash.length;
        int hammingDistance = 0;
        for (int i = 0; i < length; i++) {
            if (thisSimHash[i] != thatSimHash[i]) {
                hammingDistance++;
            }
        }
        return hammingDistance;
    }
}
