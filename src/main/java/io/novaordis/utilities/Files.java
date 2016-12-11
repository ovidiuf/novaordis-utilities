package io.novaordis.utilities;

import io.novaordis.utilities.crawler.Collector;
import io.novaordis.utilities.crawler.Crawler;
import io.novaordis.utilities.crawler.SingleThreadedCrawler;
import io.novaordis.utilities.file.FileFrame;
import io.novaordis.utilities.file.FileObliterator;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilenameFilter;
import java.io.StringWriter;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.List;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Collection;

/**
 * A collection of tools to work with files.
 *
 * @author <a href="mailto:ovidiu@feodorov.com">Ovidiu Feodorov</a>
 *
 * Copyright 2008 Ovidiu Feodorov
 *
 * @version <tt>$Revision$</tt>
 *
 * $Id$
 */
public class Files {

    // Constants -------------------------------------------------------------------------------------------------------

    private static final Logger log = Logger.getLogger(Files.class);

    public static final String SNAPSHOT_DIRECTORY_PROPERTY_NAME = "novaordis.util.snapshot.dir";

    // Static ----------------------------------------------------------------------------------------------------------

    /**
     * Appends the string content at the end of the file.
     *
     * If the enclosing directory does not exist, it will be created.
     *
     * @return true if the operation is successful, false otherwise. The error is logged with log4j.
     */
    public static boolean append(File f, String txt) {
        return write(f, txt, true);
    }

    /**
     * Writes the string content into the file, possibly overwritting previously existing content.
     *
     * If the enclosing directory does not exist, it will be created.
     *
     * @return true if the operation is successful, false otherwise. The error is logged with log4j.
     */
    public static boolean write(File f, String txt) {
        return write(f, txt, false);
    }

    /**
     * Writes the string content into the file.
     *
     * If the enclosing directory does not exist, it will be created.
     *
     * @return true if the operation is successful, false otherwise. The error is logged with log4j.
     */
    public static boolean write(File f, String content, boolean append) {

        File enclosingDir = f.getParentFile();

        if (!enclosingDir.isDirectory()) {
            if (!enclosingDir.mkdirs()) {
                log.error("failed to create enclosing directory " + enclosingDir);
                return false;
            }
        }

        FileWriter fw = null;

        try {

            fw = new FileWriter(f, append);
            PrintWriter pw = new PrintWriter(fw);
            pw.print(content);
            pw.flush();
            return true;
        }
        catch(Exception e) {
            log.error(e);
            return false;
        }
        finally {
            if (fw != null) {
                try {
                    fw.close();
                }
                catch(Exception e) {
                    log.error(e);
                }
            }
        }
    }

    /**
     * Read the content of a file as string. The file should be small enough as there is no
     * protection for OutOfMemoryError.
     */
    public static String read(File f) throws Exception
    {
        return read(f, 1024);
    }

    /**
     * Read the content of a file as byte[]. The file should be small enough as there is no protection for
     * OutOfMemoryError.
     */
    public static byte[] readBytes(File f) throws Exception {

        FileInputStream fis = null;
        BufferedInputStream bis = null;
        ByteArrayOutputStream baos = null;
        BufferedOutputStream bos = null;

        try  {

            fis = new FileInputStream(f);
            bis = new BufferedInputStream(fis, 10240);

            baos = new ByteArrayOutputStream(10240);
            bos = new BufferedOutputStream(baos, 10240);

            byte[] buffer = new byte[10240];

            int i;

            while((i = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, i);
            }

            bos.flush();

            return baos.toByteArray();
        }
        finally {

            if (bis != null) {

                try {
                    bis.close();
                }
                catch(Exception e) {
                    log.error("Failed to close the buffered input stream", e);
                }
            }

            if (fis != null) {

                try {
                    fis.close();
                }
                catch(Exception e) {
                    log.error("Failed to close the souce input stream", e);
                }
            }

            if (bos != null) {
                try  {
                    bos.close();
                }
                catch(Exception e) {
                    log.error("Failed to close the buffered output stream", e);
                }
            }

            if (baos != null) {
                try {
                    baos.close();
                }
                catch(Exception e) {
                    log.error("Failed to close the target input stream", e);
                }
            }
        }
    }

    /**
     * Read the content of a file as string. The file should be small enough as there is no
     * protection for OutOfMemoryError.
     */
    public static String read(File f, int bufferSize) throws Exception {

        StringBuilder sb = new StringBuilder();
        FileReader fr = null;
        BufferedReader br = null;

        try {

            fr = new FileReader(f);
            br = new BufferedReader(fr, bufferSize);

            int cnt;
            char[] buffer = new char[bufferSize];

            while((cnt = br.read(buffer, 0, buffer.length)) != -1) {

                boolean r = false;
                int start = 0;
                int end = cnt - 1;
                int i = 0;

                while(true) {

                    if (i > end) {
                        break;
                    }

                    if (buffer[i] == '\n') {
                        sb.append(buffer, start, r ? i - start - 1 : i - start).append('\n');
                        cnt -= i - start + 1;
                        start = i + 1;
                        r = false;
                    }
                    else if (buffer[i] == '\r') {
                        // swallow '\r'
                        r = true;
                    }

                    i++;
                }

                if (cnt > 0) {
                    sb.append(buffer, start, cnt);
                }
            }
        }
        finally {

            if (br != null) {
                br.close();
            }

            if (fr != null) {

                try {
                    fr.close();
                }
                catch(Exception e) {
                    log.error(e);
                }
            }
        }
        return sb.toString();
    }

    /**
     * Recursively deletes the contents of the given directory. If 'root' flag is true, the root
     * directory (meaning dir itself) is deleted as well.
     *
     * @return true if operation completed successfully, false otherwise.
     */
    public static boolean rmdir(File dir, boolean root)
    {
        Collector c = new FileObliterator(dir, root);
        Crawler crawler = new SingleThreadedCrawler(Crawler.POSTORDER);
        try
        {
            crawler.crawl(new FileFrame(dir), c);
            return true;
        }
        catch(Exception e)
        {
            log.info(e.getMessage());
            return false;
        }
    }

    /**
     * Copies the source file or directory to destination. If the source is a directory, the directory is copied
     * recursively. The behavior is identical with cp -r .../src .../dest (it will create .../dest/src/...)
     *
     * If 'dest' is a file that exists, its content will be overwritten.
     *
     * If 'dest' is a directory, an new file with the same name as the source will be created.
     *
     * If 'dest' is neither an existing file or directory, it will be interpreted as the
     * name of a new file to be created, and intermediate directories will be created, if needed.
     *
     * @return true if operation completed successfully, false otherwise. Errors are logged with
     *         log4j.
     */
    public static boolean cp(File src, File dest) {

        File destDir;
        String destFile;

        if (dest.isDirectory()) {

            destDir = dest;
            destFile = src.getName();
        }
        else {

            destDir = dest.getParentFile();
            destFile = dest.getName();
        }

        if (!destDir.isDirectory()) {

            // attempt to create the target dir
            if (!destDir.mkdirs()) {

                log.warn("Could not create directory " + destDir);
                return false;
            }
        }

        if (src.isDirectory()) {

            //
            // create root
            //

            File root = new File(dest, src.getName());

            if (!root.mkdir()) {

                return false;
            }

            //
            // recursive directory copy
            //

            File[] dirContent = src.listFiles();

            if (dirContent == null) {

                return true;
            }

            for(File c: dirContent) {

                if (!Files.cp(c, root)) {

                    return false;
                }
            }

            return true;
        }

        //
        // file copy
        //

        dest = new File(destDir, destFile);

        FileInputStream fis = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;

        try {

            fis = new FileInputStream(src);
            bis = new BufferedInputStream(fis, 10240);

            fos = new FileOutputStream(dest);
            bos = new BufferedOutputStream(fos, 10240);

            byte[] buffer = new byte[10240];

            int i;
            while((i = bis.read(buffer)) != -1) {

                bos.write(buffer, 0, i);
            }

            return true;
        }
        catch(Exception e) {

            return false;
        }
        finally {

            if (bis != null) {

                try {

                    bis.close();
                }
                catch(Exception e) {

                    log.error("Failed to close the buffered input stream", e);
                }
            }

            if (fis != null) {

                try {

                    fis.close();
                }
                catch(Exception e) {

                    log.error("Failed to close the souce input stream", e);
                }
            }

            if (bos != null) {

                try {

                    bos.close();
                }
                catch(Exception e) {

                    log.error("Failed to close the buffered output stream", e);
                }
            }

            if (fos != null) {

                try {
                    fos.close();
                }
                catch(Exception e) {
                    log.error("Failed to close the target input stream", e);
                }
            }
        }
    }

    /**
     * Returns true if the argument is an existent directory that contain no children.
     */
    public static boolean isEmpty(File dir) {

        if (dir.isDirectory()) {
            File[] content = dir.listFiles();
            return content == null || content.length == 0;
        }
        else {
            return false;
        }
    }

    /**
     * Extracts the optimal relative path (eliminating '.',  '..' or Windows drive letters) of the
     * descendant relative to its ancestor. If no relationship exists between ancestor and
     * descendant, the method must return null.
     *
     * If ancestor and descendant are the same directory or file, the relative path is "".
     *
     * 'ancestor' and 'descendent' could be either absolute and or relative relative. If a relative
     * value is used, it is considered relative to System.getProperty("user.dir");
     *
     * Othewise it returns a <b>relative</b> path (doesn't start with '/') with '/' as separator.
     */
    public static String relativePath(File ancestor, File descendant)
    {
        List<String> ancestorTokens = tokenize(ancestor);
        List<String> descendantTokens = tokenize(descendant);

        Iterator<String> di = descendantTokens.iterator();
        Iterator<String> ai = ancestorTokens.iterator();

        String relativePath = "";

        while(true)
        {
            if (!ai.hasNext())
            {
                // build the relative path and return

                for( ; di.hasNext(); )
                {
                    relativePath += di.next();

                    if (di.hasNext())
                    {
                        relativePath += "/";
                    }
                }

                return relativePath;
            }

            String ancestorToken = ai.next();

            if (!di.hasNext())
            {
                // descendent is "shorter", so it's not an actual descendant
                log.debug(descendant + " not a descendant of " + ancestor + ", " + descendant +
                        " is shorter");
                return null;
            }

            String descendantToken = di.next();

            if (!ancestorToken.equals(descendantToken))
            {
                // component differ, not a descendant
                log.debug(descendant + " not a descendant of " + ancestor + ", tokens '" +
                        ancestorToken + "' and '" + descendantToken + "' differ");
                return null;
            }

            // we're fine, on our way to reveal the relative path, loop ...
        }
    }

    /**
     * Another relative path extraction implemenation. Simpler implementation, but presumably
     * not that exahustively tested.
     *
     * Came up with it while working on http://jira.codehaus.org/browse/SCM-481
     *
     * @return null if there is no relationship between ancestor and descendant.
     */
    public static String relativePathExperimental(File ancestor, File descendent)
    {
        String path = descendent.getName();
        File parent = descendent.getParentFile();
        boolean relatives = false;

        while(!relatives && parent != null)
        {
            if (ancestor.equals(parent))
            {
                relatives = true;
            }
            else
            {
                path = parent.getName() + "/" + path;
                parent = parent.getParentFile();
            }
        }

        if (!relatives)
        {
            path = null;
        }

        return path;
    }

    /**
     * Tokenize an absolute or relative path, eliminating ".", ".." and Windows drive letters, if
     * present, and returning all path components staring from the filesystem root.
     *
     * If the file is relative, tokenization is done relative to System.getProperty("user.dir").
     * We rely on the fact that "user.dir" contains an absolute path, otherwise the invocation will
     * fail with IllegalStateException.
     *
     * The absolute root is represented as an empty list.
     *
     * @exception IllegalArgumentException if the file cannot be tokenized (contains too many ".."
     *            for example).
     * @exception IllegalStateException if some assumption we rely on (like "user.dir" being an
     *            absolute path) are not met.
     */
    public static LinkedList<String> tokenize(File f) {

        LinkedList<String> result = new LinkedList<>();

        boolean isRelative = !f.isAbsolute() && !f.getPath().startsWith("\\");

        if (isRelative) {
            // we look at System.getProperty("user.dir") and "pre-load" the result
            String userDirString = System.getProperty("user.dir");
            File userDir = new File(userDirString);
            if (!userDir.isAbsolute()) {
                throw new IllegalStateException("We expect an absolute \"user.dir\", but it is " +
                        "relative: " + userDirString);
            }

            result = tokenize(userDir);
        }

        for (StringTokenizer st = new StringTokenizer(f.getPath(), File.separator); st.hasMoreTokens(); ) {

            String tok = st.nextToken();

            if (tok.endsWith(":")) {
                // drive name on Windows, ignore
                continue;
            }

            if (".".equals(tok)) {
                // ignore
                continue;
            }

            if ("..".equals(tok)) {
                if (result.isEmpty()) {
                    throw new IllegalArgumentException(f + " contains an invalid number or '..'");
                }
                result.removeLast();
            }
            else {
                result.add(tok);
            }

        }

        return result;
    }

    /**
     * Creates the directory, or only the part that is needed, recursively if necessary (semantics
     * similar to mkdirs()).
     *
     * @return true if creation was successful, false otherwise. 
     */
    public static boolean mkdir(File f)
    {
        return f.mkdirs();
    }

    /**
     * Returns the file name's extension, defined as the string that follows after the last dot in
     * the file name. If there are no dots, returns null.
     */
    public static String getExtension(String path) {
        int i = path.lastIndexOf(".");

        if (i == -1) {
            return null;
        }

        path = path.substring(i + 1);

        if (path.contains("/")) {
            return null;
        }

        return path;
    }

    /**
     * Writes a snapshot of the object o in a file in the directory dir. The snapshot file name
     * starts with prefix 'prefix', and the implementation will make sure that it will only keep
     * the last 'keepLast' most recent files <b>with the same prefix</b> in the directory. If
     * o is a collection, will write the individual elements, one per line.
     *
     * Never throws exception, even unchecked, in case of failure, logs problem and returns null.
     *
     * @return the absolute path of the snapshot file on disk. May return null in case of failure.
     */
    public static String writeSnapshot(File dir, final String prefix, int keepLast, Object o) {

        if (dir == null) {
            log.warn("null snapshot directory");
            return null;
        }

        try {
            if (!dir.isDirectory() && !dir.mkdirs()) {
                // try to create the directory if it doesn't exist
                log.warn("failed to create snapshot directory " + dir);
                return null;
            }

            // generate a unique name
            File f = generateUniqueName(dir, prefix + "." + System.currentTimeMillis());

            if (!Files.write(f, convertToString(o))) {
                log.warn("failed to write snapshot file " + f);
                return null;
            }

            // count the files with the same prefix, and delete all, but the last 'keepLast' ones

            //noinspection Convert2Lambda
            File[] filesArray = dir.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name)
                {
                    return name.startsWith(prefix + ".");
                }
            });

            List<File> files = Arrays.asList(filesArray);
            //noinspection Convert2Lambda
            Collections.sort(files, new Comparator<File>() {
                public int compare(File f1, File f2)
                {
                    long diff = f1.lastModified() - f2.lastModified();
                    return diff == 0 ? 0 : diff < 0 ? -1 : 1;
                }
            });

            for(int i = 0; i < files.size(); i ++) {
                if (i + keepLast < files.size()) {
                    File ftd = files.get(i);
                    if (!ftd.delete()) {
                        log.warn("failed to delete " + ftd);
                    }
                }
            }

            // return the absolute path
            return f.getAbsolutePath();
        }
        catch(Throwable t) {
            log.error("failed to write collection snapshot on disk", t);
            return null;
        }
    }

    public static File generateUniqueName(File d, String name)
    {
        File f = new File(d, name);

        int extension = 0;

        while(f.exists())
        {
            f = new File(d, name + "." + extension ++);
        }

        return f;
    }

    private static String convertToString(Object o) {

        if (o == null) {
            return "null";
        }
        else if (o instanceof Collection) {

            Collection c = (Collection)o;
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);

            //noinspection Convert2streamapi
            for(Object e: c) {
                pw.println(e);
            }

            pw.close();

            return sw.toString();
        }
        else {
            return o.toString();
        }
    }

    /**
     * Looks up the system property SNAPSHOT_DIRECTORY_PROPERTY_NAME ('novaordis.util.snapshot.dir')
     * and uses the content of the property as snapshot directory name.
     *
     * For more details:
     *
     * @see Files#writeSnapshot(java.io.File, String, int, Object)
     */
    public static String writeSnapshot(String prefix, int keepLast, Object o) {

        String dirName = System.getProperty(SNAPSHOT_DIRECTORY_PROPERTY_NAME);

        if (dirName == null) {

            log.warn("No '" + SNAPSHOT_DIRECTORY_PROPERTY_NAME + "' system property set, don't know where to write the snapshot");
            return null;
        }

        File dir = new File(dirName);
        return writeSnapshot(dir, prefix, keepLast, o);
    }

    /**
     * @return true if the <b>content</b> of the files is identical.
     */
    public static boolean identical(File f, File f2) {

        try {

            byte[] content = read(f).getBytes();
            byte[] content2 = read(f2).getBytes();

            if (content.length != content2.length) {
                return false;
            }

            for(int i = 0; i < content.length; i ++) {
                if (content[i] != content2[i]) {
                    return false;
                }
            }

            return true;
        }
        catch(Exception e) {
            throw new IllegalStateException("file access generated I/O failure", e);
        }
    }

    /**
     * Applies permissions specified in chmod format. Multiple formats are allowed.
     *
     * @param posixMode Formats supported: "rwxr-xr-x"
     *
     * TODO: incomplete implementation
     */
    public static boolean chmod(File f, String posixMode) {

        Set<PosixFilePermission> permissions = PosixFilePermissions.fromString(posixMode);

        try {
            java.nio.file.Files.setPosixFilePermissions(f.toPath(), permissions);
        }
        catch(IOException e) {
            log.warn("failed to set permissions " + e.getMessage());
            return false;
        }
        return true;
    }

    public static String getPermissions(File f) throws IOException {

        Set<PosixFilePermission> permissions = java.nio.file.Files.getPosixFilePermissions(f.toPath());
        return PosixFilePermissions.toString(permissions);

    }

    /**
     * The same semantics as the basename Linux command. Equivalent with basename(f, null).
     */
    public static String basename(File f) {

        return basename(f, null);
    }

    /**
     * The same semantics as the basename Linux command.
     *
     * @param extension The string to be eliminate as extension when extracting the basename. If we want to eliminate
     *                  the dot, it must be specified in the extension string. null is acceptable, means ignore
     *                  extension and return the basename with it.
     */
    public static String basename(File f, String extension) {

        String name = f.getName();

        if (extension == null) {
            return name;
        }

        if (name.endsWith(extension)) {
            return name.substring(0, name.length() - extension.length());
        }

        return name;
    }

    /**
     * The given string is interpreted as a file path to be normalized. The normalization consists in resolving the
     * relative path elements ("." and "..") and coalescing superfluous "/".
     *
     * @exception IllegalArgumentException if the original path is null or the normalization process produce an invalid
     * path
     */
    public static String normalizePath(String s) throws IllegalArgumentException {

        if (s == null) {

            throw new IllegalArgumentException("null path");
        }

        boolean absolute = s.startsWith(File.separator);

        Stack<String> tokens = new Stack<>();

        int length = s.length();
        String currentToken = "";

        for(int i = 0; i < length; i ++) {

            char c = s.charAt(i);

            if (c == '.') {

                if (i == length - 1) {

                    return tokensToPath(absolute, tokens);
                }

                if (s.charAt(i + 1) == '.') {

                    i ++;

                    //
                    // go up one level
                    //

                    if (tokens.isEmpty()) {

                        throw new IllegalArgumentException("invalid path, it unwinds over root: " + s);
                    }

                    tokens.pop();
                }
            }
            else if (c == File.separatorChar) {

                if (!currentToken.isEmpty()) {

                    tokens.push(currentToken);
                    currentToken = "";
                }
            }
            else {

                currentToken += c;
            }
        }

        if (!currentToken.isEmpty()) {

            tokens.push(currentToken);
        }

        return tokensToPath(absolute, tokens);
    }

    static String tokensToPath(boolean absolute, Stack<String> tokens) {

        String path = "";

        while(!tokens.isEmpty()) {

            String s = tokens.pop();

            path = s + (path.isEmpty() ? "" : File.separator) + path;
        }

        return (absolute ? File.separator : "") + path;
    }

    // Attributes ------------------------------------------------------------------------------------------------------

    // Constructors ----------------------------------------------------------------------------------------------------

    // Public ----------------------------------------------------------------------------------------------------------

    // Package protected -----------------------------------------------------------------------------------------------

    // Protected -------------------------------------------------------------------------------------------------------

    // Private ---------------------------------------------------------------------------------------------------------

    // Inner classes ---------------------------------------------------------------------------------------------------
}
