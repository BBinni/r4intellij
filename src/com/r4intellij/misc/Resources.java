package com.r4intellij.misc;

import com.r4intellij.lang.RLanguage;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by moon on 7/23/14.
 */
public class Resources {
	private static List<Template> templates;

	/**
	 * Returns list of gitignore templates
	 *
	 * @return Gitignore templates list
	 */
	public static List<Template> getGitignoreTemplates() {
		if (templates == null) {
			templates = new ArrayList<Template>();

			try {
				String list = getResourceContent("/templates.list");
				BufferedReader br = new BufferedReader(new StringReader(list));

				for (String line; (line = br.readLine()) != null; ) {
					line = "/" + line;
					File file = getResource(line);
					String content = Resources.getResourceContent(line);
					templates.add(new Template(file, content));
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return templates;
	}

	public Resources() {
	}

	/**
	 * Returns gitignore templates directory
	 *
	 * @return Resources directory
	 */
	public static File getResource(String path) {
		URL resource = Resources.class.getResource(path);
		assert resource != null;
		return new File(resource.getPath());
	}

	/**
	 * Reads resource file and returns its content as a String
	 *
	 * @param path Resource path
	 * @return Content
	 */
	public static String getResourceContent(String path) {
		return convertStreamToString(Resources.class.getResourceAsStream(path));
	}

	/**
	 * Converts InputStream resource to String
	 *
	 * @param inputStream Input stream
	 * @return Content
	 */
	protected static String convertStreamToString(InputStream inputStream) {
		java.util.Scanner s = new java.util.Scanner(inputStream).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	public static class Template implements Comparable<Template> {
		private final File file;
		private final String name;
		private final String content;

		public Template(File file, String content) {
			this.file = file;
			this.name = file.getName().replace(RLanguage.FILENAME, "");
			this.content = content;
		}

		public File getFile() {
			return file;
		}

		public String getName() {
			return name;
		}

		public String getContent() {
			return content;
		}

		@Override
		public String toString() {
			return name;
		}

		public int compareTo(@NotNull final Template template) {
			return name.compareTo(template.name);
		}
	}
}
