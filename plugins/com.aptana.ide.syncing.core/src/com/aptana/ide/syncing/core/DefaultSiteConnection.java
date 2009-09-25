/**
 * This file Copyright (c) 2005-2009 Aptana, Inc. This program is
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
package com.aptana.ide.syncing.core;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import com.aptana.ide.core.PlatformUtils;
import com.aptana.ide.core.epl.XMLMemento;
import com.aptana.ide.core.io.ConnectionPointUtils;

/**
 * A singleton for defining the default connection available to users.
 * 
 * @author Michael Xia (mxia@aptana.com)
 */
public class DefaultSiteConnection extends SiteConnection {

    public static final String NAME = "default"; //$NON-NLS-1$

    protected static final String STATE_FILENAME = "defaultConnection"; //$NON-NLS-1$

    private static final String ELEMENT_ROOT = "connection"; //$NON-NLS-1$
    private static final String ELEMENT_SITE = "connection"; //$NON-NLS-1$

    private static final String HOME_DIR = PlatformUtils
            .expandEnvironmentStrings(PlatformUtils.HOME_DIRECTORY);

    private static DefaultSiteConnection fInstance;

    private DefaultSiteConnection() {
    }

    public static DefaultSiteConnection getInstance() {
        if (fInstance == null) {
            fInstance = new DefaultSiteConnection();
            fInstance.setName(NAME);
            fInstance.setSource(ConnectionPointUtils.findOrCreateLocalConnectionPoint(Path.fromOSString(HOME_DIR)));
        }
        return fInstance;
    }

    /**
     * loadState
     * 
     * @param path
     */
    protected void loadState(IPath path) {
        File file = path.toFile();
        if (file.exists()) {
            try {
                FileReader reader = new FileReader(file);
                XMLMemento memento = XMLMemento.createReadRoot(reader);
                loadState(memento.getChild(ELEMENT_SITE));
            } catch (IOException e) {
            } catch (CoreException e) {
            }
        }
    }

    /**
     * saveState
     * 
     * @param path
     */
    protected void saveState(IPath path) {
        XMLMemento memento = XMLMemento.createWriteRoot(ELEMENT_ROOT);
        saveState(memento.createChild(ELEMENT_SITE));
        try {
            FileWriter writer = new FileWriter(path.toFile());
            memento.save(writer);
            isChanged();
        } catch (IOException e) {
        }
    }

}