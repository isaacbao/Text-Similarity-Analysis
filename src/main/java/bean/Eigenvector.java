package bean;

import java.util.Arrays;
import java.util.List;

/**
 * 特征变量，描述一个词在句子中的权重
 * Created by rongyang_lu on 2017/4/6.
 */
public class Eigenvector {
    //词语
    private String word;
    //权重
    private int weight;

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
        int bitCount = Integer.bitCount(wordHash);
        for (int index = 0; index < bitCount; index++) {
            int offset = index;
            boolean bitOfCurrentPosition = (wordHash >> offset) % 2 != 0;
            if (bitOfCurrentPosition) {
                hashWithWeight[index] = positive;
            }
        }
        return hashWithWeight;
    }

    public String getWord() {
        return word;
    }

//    public void setWord(String word) {
//        this.word = word;
//    }

    public Eigenvector(int weight, String word) {
        this.weight = weight;
        this.word = word;
//        this.hashWithWeight = word.hashCode() * weight;
    }

    /**
     * 往列表当中添加特征向量，特征向量权重按照词语的出现频率计算
     */
    public static void addListWeightByFrequency(List<Eigenvector> list, Eigenvector eigenvector) {
        int index = list.indexOf(eigenvector);
        if (index > 0) {
            Eigenvector existEigenvector = list.get(index);
            int weight = existEigenvector.getWeight();
            if (weight < 5) {
                existEigenvector.setWeight(weight + 1);
            } else {
                existEigenvector.setWeight(5);
            }
        } else {
            list.add(eigenvector);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Eigenvector that = (Eigenvector) o;

        return word.equals(that.word);

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
