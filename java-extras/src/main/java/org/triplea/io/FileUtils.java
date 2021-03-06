package org.triplea.io;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/** A collection of useful methods related to files. */
public final class FileUtils {
  private FileUtils() {}

  /**
   * Returns a collection of abstract pathnames denoting the files and directories in the specified
   * directory.
   *
   * @param directory The directory whose files are to be listed.
   * @return An immutable collection of files. If {@code directory} does not denote a directory, the
   *     collection will be empty.
   * @see File#listFiles()
   */
  public static Collection<File> listFiles(final File directory) {
    checkNotNull(directory);
    return Optional.ofNullable(directory.listFiles()).map(List::of).orElseGet(List::of);
  }
}
