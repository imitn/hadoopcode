import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MepreduceJobSubmit2YarnUtil {

	public boolean submit() {

		Configuration conf = new Configuration();

		conf.set("yarn.resourcemanager.address", "192.168.1.1:8050");

		conf.set("mapreduce.framework.name", "yarn");

		conf.set("fs.defaultFS", "hdfs://192.168.1.1:8020");

		conf.set("fs.default.name", "hdfs://192.168.1.1:8020");

		String hdfsDst = File.separator + "data";

		conf.set("outputPath", hdfsDst);

		conf.set("tmpjars", "file:///tmp/MyappRootDir/lib/MyDependency.jar", "from -libjars command line option");

		URL[] libjars = null;
		try {
			libjars = getLibJars(conf);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		conf.setClassLoader(new URLClassLoader(libjars, conf.getClassLoader()));
		Thread.currentThread().setContextClassLoader(new URLClassLoader(libjars, Thread.currentThread().getContextClassLoader()));

		MyJob myJob = new MyJob();
		int result = 0;

		logger.info("----------run mapreduce job begin-----------");
		try {
			result = myJob.run(conf);
		} catch (Exception e) {
			logger.error("run job failure");
			e.printStackTrace();
		}
		logger.info("----------run  mapreduce job end-----------");

		if (result == 0) {
			return true;
		} else {
			logger.error("run job failure");
			return false;
		}
	}

	public URL[] getLibJars(Configuration conf) throws IOException {
		String jars = conf.get("tmpjars");
		if (jars == null) {
			return null;
		}
		String[] files = jars.split(",");
		List<URL> cp = new ArrayList<URL>();
		for (String file : files) {
			Path tmp = new Path(file);
			if (tmp.getFileSystem(conf).equals(FileSystem.getLocal(conf))) {
				cp.add(FileSystem.getLocal(conf).pathToFile(tmp).toURI().toURL());
			} else {

			}
		}
		return cp.toArray(new URL[0]);
	}

}
