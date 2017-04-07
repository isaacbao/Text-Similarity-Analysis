import bean.Eigenvector;
import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;
import util.ArrayUtil;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rongyang_lu on 2017/3/15.
 */
public class MainClass {
    public static void main(String[] args) {
        String text1 = "写api文档工具";
        String text2 = "api生成工具";

        List<Eigenvector> eigenvectorsTxt1 = getEigenvectorsInText(text1);
        List<Eigenvector> eigenvectorsTxt2 = getEigenvectorsInText(text2);

        boolean[] simHashTxt1 = getSimHash(eigenvectorsTxt1);
        boolean[] simHashTxt2 = getSimHash(eigenvectorsTxt2);

        int hammingDistance = calculateHammingDistance(simHashTxt1, simHashTxt2);

        System.out.println(simHashTxt1.toString());
    }

    /**
     * 计算两个simHash之间的hamming距离
     * @param simHashTxt1
     * @param simHashTxt2
     * @return
     */
    private static int calculateHammingDistance(boolean[] simHashTxt1, boolean[] simHashTxt2) {
        if (simHashTxt1.length > simHashTxt2.length) {
            simHashTxt2 = ArrayUtil.expandTo(simHashTxt2, simHashTxt1.length);
        } else {
            simHashTxt1 = ArrayUtil.expandTo(simHashTxt1, simHashTxt2.length);
        }
        int length=simHashTxt1.length;
        int hammingDistance = 0;
        for(int i=0;i<length;i++){
            if(simHashTxt1[i] != simHashTxt2[i]){
                hammingDistance++;
            }
        }
        return hammingDistance;
    }

    private static boolean[] getSimHash(List<Eigenvector> eigenvectors) {
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


    private static int getMaxBitCount(List<Eigenvector> eigenvectorsTxt1) {
        int maxBitCount = 0;
        for (Eigenvector eigenvector : eigenvectorsTxt1) {
            int bitCount = Integer.bitCount(eigenvector.getWord().hashCode());
            if (bitCount > maxBitCount) {
                maxBitCount = bitCount;
            }
        }
        return maxBitCount;
    }

    private static List<Eigenvector> getEigenvectorsInText(String text) {
        List<Eigenvector> eigenvectorsTxt = new ArrayList<>();
        IKSegmentation ikSeg = new IKSegmentation(new StringReader(text), true);
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

}
