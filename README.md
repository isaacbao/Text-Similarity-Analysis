# Text-Similarity-Analysis #
用于分析汉语文本相似度的demo

分词使用了[`ik-analyzer`](https://github.com/wks/ik-analyzer)

要先往本地maven库安装ik-analyzer之后才能运行，如果在将ik-analyzer添加进本地maven库的时候出现问题，可以参考[`这里`](http://stackoverflow.com/questions/19049478/how-to-add-third-party-jars-into-local-maven-repository)

相似度计算使用[`simhash算法`](http://leoncom.org/?p=650607)

最后在demo中输出的hamming distance越低，文本相似度越高
