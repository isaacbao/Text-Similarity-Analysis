package bean;

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

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Eigenvector(int weight, String word) {
        this.weight = weight;
        this.word = word;
    }

    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        return o == this || this.word.equals(((Eigenvector) o).getWord());
    }

    public static void addListWeightByFrequency(List<Eigenvector> list, Eigenvector eigenvector) {
        int index = list.indexOf(eigenvector);
        if (index > 0) {
            Eigenvector existEigenvector = list.get(index);
            existEigenvector.setWeight(existEigenvector.getWeight() + 1);
        }else{
            list.add(eigenvector);
        }
    }
}
