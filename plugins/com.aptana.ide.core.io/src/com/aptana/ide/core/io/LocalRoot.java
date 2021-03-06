/**
 * This file Copyright (c) 2005-2010 Aptana, Inc. This program is
 * dual-licensed under both the Aptana Public License and the GNU General
 * Public license. You may elect to use one or the other of these licenses.
 * 
 * This program is distributed in the hope that it will be useful, but
 * AS-IS and WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE, TITLE, or
 * NONINFRINGEMENT. Redistribution, except as permitted by whichever of
 * the GPL or APL you select, is prohibited.
 *
 * 1. For the GPL license (GPL), you can redistribute and/or modify this
 * program under the terms of the GNU General Public License,
 * Version 3, as published by the Free Software Foundation.  You should
 * have received a copy of the GNU General Public License, Version 3 along
 * with this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * Aptana provides a special exception to allow redistribution of this file
 * with certain other free and open source software ("FOSS") code and certain additional terms
 * pursuant to Section 7 of the GPL. You may view the exception and these
 * terms on the web at http://www.aptana.com/legal/gpl/.
 * 
 * 2. For the Aptana Public License (APL), this program and the
 * accompanying materials are made available under the terms of the APL
 * v1.0 which accompanies this distribution, and is available at
 * http://www.aptana.com/legal/apl/.
 * 
 * You may view the GPL, Aptana's exception and additional terms, and the
 * APL in the file titled license.html at the root of the corresponding
 * plugin containing this source file.
 * 
 * Any modifications to this file must keep this entire header intact.
 */

package com.aptana.ide.core.io;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.swing.filechooser.FileSystemView;

import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.PlatformObject;

import com.aptana.ide.core.PlatformUtils;
import com.aptana.ide.core.io.efs.LocalFile;

/**
 * @author Max Stepanov
 *
 */
public final class LocalRoot extends PlatformObject {

    private static final String HOME_DIR = PlatformUtils
            .expandEnvironmentStrings(PlatformUtils.HOME_DIRECTORY);
    private static final String DESKTOP = PlatformUtils
            .expandEnvironmentStrings(PlatformUtils.DESKTOP_DIRECTORY);
    private static final boolean ON_WINDOWS = Platform.OS_WIN32.equals(Platform.getOS());

	private final String name;
	private final File root;

	/**
	 *
	 */
	private LocalRoot(String name, File root) {
		super();
		this.name = name;
		this.root = root;
	}
	
	/* (non-Javadoc)
	 * @see com.aptana.ide.core.io.IConnectionPoint#getName()
	 */
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see com.aptana.ide.core.io.IConnectionPoint#getRootURI()
	 */
	public URI getRootURI() {
		return (new LocalFile(root)).toURI();
	}

	/* (non-Javadoc)
	 * @see com.aptana.ide.core.io.IConnectionPoint#getRoot()
	 */
	public IFileStore getRoot() {
		return new LocalFile(root);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.PlatformObject#getAdapter(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(Class adapter) {
		if (File.class == adapter) {
			return getFile();
		}
		if (IFileStore.class == adapter) {
			return getRoot();
		}
		return super.getAdapter(adapter);
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return root;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof LocalRoot)) {
			return false;
		}
		return root.equals(((LocalRoot) obj).root);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return root.hashCode();
	}

    @Override
    public String toString() {
        return getFile().toString();
    }

	public static LocalRoot[] createRoots() {
		List<LocalRoot> list = new ArrayList<LocalRoot>();
		if (Platform.OS_MACOSX.equals(Platform.getOS())) {
			for (File root : new File("/Volumes").listFiles()) { //$NON-NLS-1$
				try {
					if (root.listFiles() != null) {
						LocalRoot localRoot = new LocalRoot(root.getName(), root.getCanonicalFile());
						if ("/".equals(localRoot.getFile().getCanonicalPath())) { //$NON-NLS-1$
							list.add(0, localRoot);
						} else {
							list.add(localRoot);
						}
					}
				} catch (IOException e) {
				}
			}
		} else if (!ON_WINDOWS) {
			for (File root : File.listRoots()) {
				try {
					list.add(new LocalRoot(root.getName(), root.getCanonicalFile()));
				} catch (IOException e) {
				}
			}			
		}
		{	/* Home */
		    File homeFile;
            if (ON_WINDOWS) {
                homeFile = getWindowsHomeFile();
            } else {
                IPath homePath = new Path(HOME_DIR);
                homeFile = homePath.toFile();
            }
            if (homeFile != null && homeFile.exists() && homeFile.isDirectory()) {
                try {
                    list.add(new LocalRoot(homeFile.getName(), homeFile.getCanonicalFile()));
                } catch (IOException e) {
                }
            }
		}
		{	/* Desktop */
		    File desktopFile;
            if (ON_WINDOWS) {
                desktopFile = getWindowsDesktopFile();
            } else {
                IPath desktopPath = new Path(DESKTOP);
                desktopFile = desktopPath.toFile();
            }
            if (desktopFile != null && desktopFile.exists() && desktopFile.isDirectory()) {
                try {
                    list.add(new LocalRoot(desktopFile.getName(), desktopFile.getCanonicalFile()));
                } catch (IOException e) {
                }
            }
		}
		{	/* Documents */
			IPath docsPath = new Path(PlatformUtils.expandEnvironmentStrings(PlatformUtils.DOCUMENTS_DIRECTORY));
			File docsFile = docsPath.toFile();
			if (docsFile.exists() && docsFile.isDirectory()) {
				try {
					list.add(new LocalRoot(docsPath.lastSegment(), docsFile.getCanonicalFile()));
				} catch (IOException e) {
				}				
			}
		}

		return list.toArray(new LocalRoot[list.size()]);
	}

	public static LocalRoot[] createWindowsSubroots(File root) {
		File[] drives = FileSystemView.getFileSystemView().getFiles(root, false);
		List<LocalRoot> subroots = new ArrayList<LocalRoot>();
		for (File drive : drives) {
			try {
				subroots.add(new LocalRoot(FileSystemView.getFileSystemView()
						.getSystemDisplayName(drive), drive.getCanonicalFile()));
			} catch (IOException e) {
			}
		}
		return subroots.toArray(new LocalRoot[subroots.size()]);
	}

    private static File getWindowsHomeFile() {
        File desktop = getWindowsDesktopFile();
        if (desktop == null) {
            return null;
        }
        IPath homePath = new Path(HOME_DIR);
        String homeFilename = homePath.lastSegment();
        File[] files = FileSystemView.getFileSystemView().getFiles(desktop, false);
        for (File file : files) {
            if (file.getName().equals(homeFilename)) {
                return file;
            }
        }
        return null;
    }

    private static File getWindowsDesktopFile() {
        IPath desktopPath = new Path(DESKTOP);
        String desktopFilename = desktopPath.lastSegment();
        File[] files = FileSystemView.getFileSystemView().getRoots();
        for (File file : files) {
            if (file.getName().equals(desktopFilename)) {
                return file;
            }
        }
        return null;
    }
}
