package hadoop;

import java.io.IOException;
import java.util.StringTokenizer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class HashSort {
  public static class HashSortMapper extends Mapper<LongWritable, Text, Text, Text> {

    public void map(LongWritable key, Text value, Context context)
        throws IOException, InterruptedException {
      // Split the input line into key and hash
      String[] parts = value.toString().split("\\s+");

      System.out.println(parts[1]);
      if (parts.length == 2) {
        context.write(new Text(parts[1]), new Text(parts[0]));
      }
    }
  }

  public static class HashSortReducer extends Reducer<Text, Text, Text, Text> {

    public void reduce(Text key, Iterable<Text> values, Context context)
        throws IOException, InterruptedException {
      // Output key-value pairs in sorted order
      for (Text value : values) {
        context.write(value, key);
      }
    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "Hash Sorting");

    job.setJarByClass(HashSort.class);
    job.setMapperClass(HashSortMapper.class);
    job.setReducerClass(HashSortReducer.class);

    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);

    job.setInputFormatClass(TextInputFormat.class);
    job.setOutputFormatClass(TextOutputFormat.class);

    System.out.println(args[1]);
    System.out.println(args[2]);
    TextInputFormat.addInputPath(job, new Path(args[1]));
    TextOutputFormat.setOutputPath(job, new Path(args[2]));

    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
