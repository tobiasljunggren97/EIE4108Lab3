import java.io.IOException;
import java.util.StringTokenizer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;






public class WordCount {

    // LongWritable, Text, IntWritable: data types defined by Hadoop
    // LongWritable & IntWritable: Wrapper class in Hadoop
    public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {
        // As each token count as 1, we set "1" to IntWritable class directly
        private final static IntWritable one = new IntWritable(1);
        // A variable to store the token
        private Text word = new Text();

        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            // toString method: it converts the input text to string
            String line = value.toString();
            // StringTokenizer: it is similar to what split method usually do
            StringTokenizer tokenizer = new StringTokenizer(line);

            // Tokenization runs whenever there are still token left
            // 1. Set the next token to "word"
            // 2. Output the "word" and number of word count on each token
            // Hint 1: hasMoreTokens & nextToken methods in StringTokenizer class
            // Hint 2: set method in Text class
            // Hint 3: context.write() method helps to display output

            // Write your code here...
            while (tokenizer.hasMoreTokens()) {
                word.set(tokenizer.nextToken());
                context.write(word, one);
            }

        }
    }

    // 1st pair of Text, IntWritable refers to Input, eg. (Exam, 1)
    // 2nd pair of Text, IntWritable refers to Output, eg. (Exam, 3)
    public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {
        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {

            // Create a variable for storing the total number of occurrence of each token
            // Write your code here...
            int sum = 0;
            // Calculate the total number of occurrence of each token
            for (IntWritable value : values) {
                // Calculate the sum of all occurrence of each token
                // Write your code here...
                sum += value.get();
            }
            // Output the reduced result
            context.write(key, new IntWritable(sum));
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "wordcount");
        job.setJarByClass(WordCount.class);
        // setMapperClass & setReducerClass: tell the class/object type of Mapper & Reducer
        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);
        // setOutputKeyClass & setOutputValueClass: tell the class/object type of the output key-value pair
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        // addInputPath: tells where is the input file in HDFS
        FileInputFormat.addInputPath(job, new Path(args[1]));
        // setOutputPath: tells where should the result to be put in HDFS
        FileOutputFormat.setOutputPath(job, new Path(args[2]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}