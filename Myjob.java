import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Myjob{
	
	private Job job;

	public int run(Configuration conf) throws Exception {
		
		String path=conf.get("outputPath");
		Path out = new Path(path);
		out.getFileSystem(conf).delete(out, true);
		
    this.job = Job.getInstance(conf, "this is a mapreduce job");
        
    job.setJarByClass(Myjob.class);  
		job.setInputFormatClass(MyInputFormat.class);
		job.setMapperClass(Mapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setNumReduceTasks(0);
		FileOutputFormat.setOutputPath(job, out);
		
		return job.waitForCompletion(true) ? 0 : -1;

	}
	
}
