package stockmarketui.util;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.CopyOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import stockmarketui.simengine.Messages;

public class FileOperations {
	private static final String className = FileOperations.class.getName();

	public static Path getPath(String source) {
		if (source != null) {
			try {
				Path temp = Paths.get(source);
				return temp;
			} catch (InvalidPathException e) {
				LogHandler.getInstance().log(Level.INFO, className, "getFile", //$NON-NLS-1$
						Messages.getString("FileOperations.notValidPath") + source); //$NON-NLS-1$
				return null;
			}
		}
		LogHandler.getInstance().log(Level.INFO, className, "getFile", Messages.getString("FileOperations.emptyPath")); //$NON-NLS-1$ //$NON-NLS-2$
		return null;
	}

	public static File getFile(String source) {
		String absFileName = makePathAbsolute(source);
		if (absFileName != null) {
			Path tempPath = getPath(absFileName);
			if (tempPath != null) {
				File file = new File(absFileName);
				return file;
			}
		}
		return null;
	}

	public static boolean isDirectory(String source) {
		File src = getFile(source);
		if (src != null && src.isDirectory()) {
			return true;
		}
		return false;
	}

	public static boolean isValid(String source) {
		File src = getFile(source);
		if (src != null) {
			return true;
		}
		return false;
	}

	public static boolean exists(String source) {
		File src = getFile(source);
		if (src != null && src.exists()) {
			return true;
		}
		return false;
	}

	public static String getLastSegment(String source) {
		String seg = null;
		File src = getFile(source);
		if (src != null) {
			seg = src.getName();
		} else {
			try {
				URI uri = new URI(source);
				String path = uri.getPath();
				seg = path.substring(path.lastIndexOf('/') + 1);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return seg;
	}

	public static String getLastURISegment(String source) {
		String seg = null;
		try {
			URI uri = new URI(source);
			String path = uri.getPath();
			seg = path.substring(path.lastIndexOf('/') + 1);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return seg;
	}

	public static String getExtension(String path) {
		String seg = ""; //$NON-NLS-1$
		int i = path.lastIndexOf('.');
		int p = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
		if (i > p) {
			seg = path.substring(i + 1);

		}
		return seg;
	}

	public static boolean copyFileOrFolder(File source, File dest) {
		return copyFileOrFolder(source, dest, StandardCopyOption.REPLACE_EXISTING);
	}

	public static boolean copyFileOrFolder(String source, String dest) {
		return copyFileOrFolder(getFile(source), getFile(dest));
	}

	public static boolean copyFileOrFolder(File source, File dest, CopyOption... options) {
		if (source == null) {
			LogHandler.getInstance().log(Level.SEVERE, className, "copyFileOrFolder", //$NON-NLS-1$
					Messages.getString("FileOperations.noSource")); //$NON-NLS-1$
			return false;
		}
		if (dest == null) {
			LogHandler.getInstance().log(Level.SEVERE, className, "copyFileOrFolder", //$NON-NLS-1$
					Messages.getString("FileOperations.noDest")); //$NON-NLS-1$
			return false;
		}
		if (source.equals(dest)) {
			return true;
		}
		try {
			if (source.isDirectory())
				copyFolder(source, dest, options);
			else {
				ensureParentFolderExists(dest);
				copyFile(source, dest, options);
			}
		} catch (IOException e) {
			LogHandler.getInstance().log(Level.SEVERE, className, "copyFileOrFolder", //$NON-NLS-1$
					Messages.getString("FileOperations.errorCopy") + source + Messages.getString("FileOperations.and") //$NON-NLS-1$ //$NON-NLS-2$
							+ dest,
					e);
			return false;
		}
		return true;
	}

	private static void copyFolder(File source, File dest, CopyOption... options) throws IOException {
		if (!dest.exists())
			dest.mkdirs();
		File[] contents = source.listFiles();
		if (contents != null) {
			for (File f : contents) {
				File newFile = getFile(dest.getAbsolutePath() + File.separator + f.getName());
				if (newFile == null) {
					return;
				}
				if (f.isDirectory())
					copyFolder(f, newFile, options);
				else
					copyFile(f, newFile, options);
			}
		}
	}

	private static void copyFile(File source, File dest, CopyOption... options) throws IOException {
		Files.copy(source.toPath(), dest.toPath(), options);
	}

	public static void ensureParentFolderExists(File file) {
		File temp = file.getAbsoluteFile();
		File parent = temp.getParentFile();
		if (parent != null && !parent.exists())
			parent.mkdirs();
	}

	public static void ensureFolderExists(File file) {
		File temp = file.getAbsoluteFile();
		if (temp != null && !temp.exists())
			temp.mkdirs();
	}

	public static String makePathAbsolute(String fileName) {
		if (fileName == null) {
			return null;
		}
		File file = new File(fileName);
		String absFilename = null;
		try {
			absFilename = file.getCanonicalPath().toString();
		} catch (IOException e) {
			LogHandler.getInstance().log(Level.INFO, className, "makePathAbsolute", //$NON-NLS-1$
					Messages.getString("FileOperations.cantMakePathAbs") + fileName + ": " + e.getMessage()); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return absFilename;
	}

	public static boolean deleteFileOrFolder(String source) {
		if (!exists(source)) {
			return true;
		}
		Path directory = getPath(source);
		if (directory == null) {
			LogHandler.getInstance().log(Level.SEVERE, className, "deleteFileOrFolder", //$NON-NLS-1$
					Messages.getString("FileOperations.errorDel") + Messages.getString("FileOperations.wrongPathDel")); //$NON-NLS-1$ //$NON-NLS-2$
			return false;
		}
		try {
			LogHandler.getInstance().log(Level.INFO, className, "deleteFileOrFolder", //$NON-NLS-1$
					Messages.getString("FileOperations.del") + source); //$NON-NLS-1$
			Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					if (Files.getFileStore(file).supportsFileAttributeView(DosFileAttributeView.class)) {
						Files.setAttribute(file, "dos:readonly", false);
					} else if (Files.getFileStore(file).supportsFileAttributeView(PosixFileAttributeView.class)) {
						Files.setAttribute(file, "posix:permissions",
								Stream.of(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE,
										PosixFilePermission.OWNER_EXECUTE, PosixFilePermission.GROUP_READ,
										PosixFilePermission.GROUP_WRITE, PosixFilePermission.GROUP_EXECUTE,
										PosixFilePermission.OTHERS_READ, PosixFilePermission.OTHERS_WRITE,
										PosixFilePermission.OTHERS_EXECUTE)
										.collect(Collectors.toCollection(HashSet::new)));
					}
					Files.delete(file);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
					Files.delete(dir);
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			LogHandler.getInstance().log(Level.SEVERE, className, "deleteFileOrFolder", //$NON-NLS-1$
					Messages.getString("FileOperations.errorDel") + e); //$NON-NLS-1$
		}
		return true;
	}

	public static boolean moveFileOrFolder(String source, String dest) {
		if (source.equals(dest)) {
			return true;
		}
		copyFileOrFolder(getFile(source), getFile(dest));
		deleteFileOrFolder(source);
		return true;
	}

	public static void replaceAllInFile(String filename, String pattern, String value) {
		try {
			LogHandler.getInstance().log(Level.INFO, className, "replaceAllInFile", //$NON-NLS-1$
					String.format(Messages.getString("FileOperations.replacing"), filename,pattern,value)); //$NON-NLS-1$
			Path path = getPath(filename);
			Charset charset = StandardCharsets.UTF_8;

			String content = new String(Files.readAllBytes(path), charset);
			content = content.replace(pattern, value); //$NON-NLS-1$ //$NON-NLS-2$
			Files.write(path, content.getBytes(charset));
		} catch (IOException e) {
			LogHandler.getInstance().log(Level.SEVERE, className, "replaceAllInFile", //$NON-NLS-1$
					Messages.getString("FileOperations.errorReplace") + filename //$NON-NLS-1$
							+ Messages.getString("FileOperations.withError") + e); //$NON-NLS-1$
		}
	}

}

