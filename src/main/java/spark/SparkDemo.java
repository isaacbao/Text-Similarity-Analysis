package spark;

import org.apache.spark.ml.feature.HashingTF;
import org.apache.spark.ml.feature.IDF;
import org.apache.spark.ml.feature.IDFModel;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import util.WordSegmentation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rongyang_lu on 2017/7/25.
 */
public class SparkDemo {

    /**
     * 输入一堆文本（语料库），返回这堆文本的tfIDF特征向量
     * @param spark spark上下文
     * @param texts 语料库
     */
    public static List<List<Double>> tfIDF(SparkSession spark, List<String> texts) {
        StructType schema = new StructType(new StructField[]{
                new StructField("document", DataTypes.StringType, false, Metadata.empty()),
                new StructField("words", DataTypes.createArrayType(DataTypes.StringType, true), false, Metadata
                        .empty())
        });
        List<Row> data = new ArrayList<>();
        //分词
        texts.forEach(aritcle -> data.add(RowFactory.create(aritcle, WordSegmentation.splitWords(aritcle))));
        Dataset<Row> wordsData = spark.createDataFrame(data, schema);

        //计算TF
        HashingTF hashingTF = new HashingTF()
                .setInputCol("words")
                .setOutputCol("TF");
        Dataset<Row> featurizedData = hashingTF.transform(wordsData);

        //计算IDF
        IDF idf = new IDF().setInputCol("TF").setOutputCol("IDF");
        IDFModel idfModel = idf.fit(featurizedData);
        Dataset<Row> rescaledData = idfModel.transform(featurizedData);
        List<List<Double>> result = new ArrayList<>();
        rescaledData.select("words","TF", "IDF").takeAsList(texts.size()).forEach(row-> {
            Vector words = row.getAs(0);
            System.out.println("words:"+words.toString());
            Vector tfResult = row.getAs(1);
            Vector idfResult = row.getAs(2);
            double[] tfArray = tfResult.toDense().values();
            double[] idfArray = idfResult.toDense().values();
            List<Double> tfIdfList = new ArrayList<>();
            for (int i = 0; i < tfArray.length; i++) {
                tfIdfList.add(tfArray[i] * idfArray[i]);
            }
            tfIdfList.forEach(tfIdf -> System.out.print(tfIdf + ","));
            result.add(tfIdfList);
        });
        return result;
    }
}
