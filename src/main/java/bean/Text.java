package bean;

import util.WordSegmentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public boolean[] getSimHash() {
        return simHash;
    }

    public List<Eigenvector> getEigenvectors() {
        return eigenvectors;
    }

    public String getOriginText() {
        return originText;
    }

    public List<Eigenvector> calculateEigenvectors() {
        List<String> wordsInDoc = splitWords();
        List<Eigenvector> eigenvectorsTxt = calculateFrequency(wordsInDoc);
        return eigenvectorsTxt;
    }

    /**
     * 统计词频
     */
    private List<Eigenvector> calculateFrequency(List<String> wordsInDoc) {
        Map<String, Eigenvector> eigenvectorsTxt = new HashMap<>();
        wordsInDoc.forEach(word -> {
            Eigenvector eigenvector = eigenvectorsTxt.get(word);
            if (eigenvector == null) {
                eigenvectorsTxt.put(word,new Eigenvector(1,word));
            }else{
                eigenvector.frequercyUp();
            }
        });
        List<Eigenvector> result = new ArrayList<>();
        eigenvectorsTxt.forEach((word,eigenvectors)->{
            result.add(eigenvectors);
        });
        return result;
    }

    private List<String> splitWords() {
        return WordSegmentation.splitWords(this.originText);
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
     */
    public int calculateHammingDistance(Text text) {
        return calculateHammingDistance(this, text);
    }

    /**
     * 计算该文本的sim hash和另一个文本的sim hash的hamming distance
     */
    public static int calculateHammingDistance(Text text1, Text text2) {
        boolean[] text1SimHash = text1.getSimHash();
        boolean[] text2SimHash = text2.getSimHash();

        return calculateHammingDistance(text1SimHash, text2SimHash);
    }

    /**
     * 计算该文本的sim hash和另一个文本的sim hash的hamming distance
     */
    public static int calculateHammingDistance(boolean[] simHash1, boolean[] simHash2) {
        int hammingDistance = 0;
        for (int i = 0; i < BIT_COUNT; i++) {
            if (simHash1[i] != simHash2[i]) {
                hammingDistance++;
            }
        }
        return hammingDistance;
    }

    public static String simHashBooleanToString(boolean[] booleanArray) {
        if (booleanArray.length != BIT_COUNT) {
            throw new IllegalArgumentException("要转换的simHash位数不是" + BIT_COUNT + "位");
        }
        StringBuilder sb = new StringBuilder();
        for (boolean b : booleanArray) {
            if (b) {
                sb.append('1');
            } else {
                sb.append('0');
            }
        }
        return sb.toString();
    }

    public static boolean[] simHashStringToBoolean(String string) {
        if (!string.matches("[01]{" + BIT_COUNT + "}")) {
            throw new IllegalArgumentException("要转换的simHash位数不是" + BIT_COUNT + "位");
        }
        char[] chars = string.toCharArray();
        boolean[] result = new boolean[BIT_COUNT];
        for (int i = 0; i < BIT_COUNT; i++) {
            char c = chars[i];
            if (c == '1') {
                result[i] = true;
            }
        }
        return result;
    }
}
