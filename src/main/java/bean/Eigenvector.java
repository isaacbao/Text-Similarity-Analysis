package bean;

import enums.EigervectorAlgorithm;

import java.util.Arrays;

/**
 * 特征变量，描述一个词在句子中的权重
 * Created by rongyang_lu on 2017/4/6.
 */
public class Eigenvector {

    //词语
    private String word;
    //权重
    private int weight;
    //一个词语在一篇文章中的出现频率
    private int frequency;

//    private List<Integer> hashWithWeight;

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
//        hashWithWeight = calculateHashWithWeight();
    }

    public int[] calculateHashWithWeight(int size) {
        int[] hashWithWeight = new int[size];
        int negative = -weight;
        int positive = weight;
        Arrays.fill(hashWithWeight, negative);
        int wordHash = word.hashCode();
        String wordHashBitStr = Integer.toBinaryString(wordHash);
        int bitCount = wordHashBitStr.length();
        for (int index = 0; index < bitCount; index++) {
            int offset = index;
            boolean bitOfCurrentPosition = Integer.valueOf(wordHashBitStr.charAt(index)) % 2 != 0;
            if (bitOfCurrentPosition) {
                hashWithWeight[index] = positive;
            }
        }
        return hashWithWeight;
    }

    public String getWord() {
        return word;
    }

    public void frequercyUp() {
        this.frequency++;
    }

    public void calculateWeight(EigervectorAlgorithm algorithm){

    }

    public Eigenvector(int frequency, String word) {
        this.frequency = frequency;
        this.word = word;
        this.weight = 1;
    }

    @Override
    public int hashCode() {
        return word.hashCode();
    }

//    public List<Integer> getHashWithWeight() {
//        return hashWithWeight;
//    }


    @Override
    public String toString() {
        return "Eigenvector{" +
                "weight=" + weight +
                ", word='" + word + '\'' +
                '}';
    }
}
