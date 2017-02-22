package Operations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;

public class DiskOps {
	public static HashMap<File, BasicFileAttributes> getFiles(File f) {
		HashMap<File, BasicFileAttributes> files = new HashMap<File, BasicFileAttributes>();
		if (f.listFiles() != null) {
			for (File e : f.listFiles()) {
				if (e.isDirectory()) {
					files.putAll(getFiles(e));
				} else if (e.isFile()) {
					try {
						files.put(e, Files.readAttributes(e.toPath(), BasicFileAttributes.class));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
		return files;
	}
	
	public static String getCreationTime(BasicFileAttributes b) {
		return getTime(b.creationTime().toString());
	}
	
	public static String getAccessTime(BasicFileAttributes b) {
		return getTime(b.lastAccessTime().toString());
	}
	
	public static String getModifiedTime(BasicFileAttributes b) {
		return getTime(b.lastAccessTime().toString());
	}
	
	private static String getTime(String b) {
		String[] x = b.split("T");
		return x[0] + " " + x[1].substring(0, 8);
	}
}
