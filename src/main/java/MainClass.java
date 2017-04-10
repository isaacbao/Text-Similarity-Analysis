import bean.Text;

/**
 * Created by rongyang_lu on 2017/3/15.
 */
public class MainClass {
    public static void main(String[] args) {
        String content1 = "写api文档工具";
        String content2 = "api生成工具";

        Text txt1 = new Text(content1);
        Text txt2 = new Text(content2);

        System.out.println("Hamming distance is : " + txt1.calculateHammingDistance(txt2));

    }

}
