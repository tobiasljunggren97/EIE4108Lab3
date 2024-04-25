import java.io.IOException;
import java.util.StringTokenizer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class SalesStatistics {
    public static class Statistics {
        double[] data;
        double size;
        public Statistics(double data[]) {
            this.data = data;
            this.size = data.length;
        }

        double getMean() {
            // To get the mean of each customer, get every number from data
            // Find the sum of sales figures first
            // Then, return the mean result from January to December

            // Write your code here...
            double sum = 0.0;
            for (double value : data) {
                sum += value;
            }
            return sum / size;
        }

        double getVariance() {
            // To get the variance of each customer, get every number from data
            // Find the mean of each customer first
            // Calculate the difference between mean & sales figures of each month, then take the square of the difference
            // Store the result of the all 12 months variance calculated
            // Then, return the variance result from January to December

            // Write your code here...
            double mean = getMean();
            double temp = 0;
            for (double value : data) {
                temp += (value - mean) * (value - mean);
            }
            return temp / size;
        }
    }

    public static class Map extends Mapper<LongWritable, Text, Text, Text> {
        private Text custId = new Text();
        private Text custStats = new Text();

        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] token = line.split(",");

            // Prepare array for sales figures, ignoring the first token which is Customer ID
            double[] custSales = new double[token.length - 1];
            for (int i = 1; i < token.length; i++) {
                custSales[i - 1] = Double.parseDouble(token[i].trim());
            }

            Statistics stats = new Statistics(custSales);
            double mean = stats.getMean();
            double var = stats.getVariance();

            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%.1f", mean)).append(", ").append(String.format("%.1f", var));
            custId.set(token[0]);
            custStats.set(sb.toString());
            context.write(custId, custStats);
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "salesstats");
        job.setJarByClass(SalesStatistics.class);
        job.setMapperClass(Map.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        FileInputFormat.addInputPath(job, new Path(args[1]));
        FileOutputFormat.setOutputPath(job, new Path(args[2]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}