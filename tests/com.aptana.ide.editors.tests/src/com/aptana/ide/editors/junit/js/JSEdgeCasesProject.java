/**
 * This file Copyright (c) 2005-2008 Aptana, Inc. This program is
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
package com.aptana.ide.editors.junit.js;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;

import com.aptana.ide.editors.junit.ProjectTestUtils;
import com.aptana.ide.scripting.ScriptingEngine;

/**
 * @author Robin
 */
public final class JSEdgeCasesProject
{
	private static JSEdgeCasesProject instance;

	/**
	 * project
	 */
	public IProject project;

	/**
	 * projectName
	 */
	public String projectName = "JSEdgeCasesProject"; //$NON-NLS-1$

	/**
	 * jsFolder
	 */
	public IFolder jsFolder;

	/**
	 * edgeCases_js_path
	 */
	public Path edgeCases_js_path;

	/**
	 * edgeCases_js_file
	 */
	public IFile edgeCases_js_file;

	/**
	 * getInstance
	 * 
	 * @return JSEdgeCasesProject
	 */
	public static JSEdgeCasesProject getInstance()
	{
		if (instance == null)
		{
			instance = new JSEdgeCasesProject();
		}
		return instance;
	}

	private JSEdgeCasesProject()
	{
		ProjectTestUtils.setAptanaPerspective();

		// create project
		this.project = ProjectTestUtils.createProject(this.projectName);

		// add sdoc folder
		ProjectTestUtils.createFolder("sdoc", this.project); //$NON-NLS-1$

		edgeCases_js_path = ProjectTestUtils.findFileInPlugin("com.aptana.ide.editors.junit", //$NON-NLS-1$
				"jsEdgeCases/EdgeCases.js"); //$NON-NLS-1$
		edgeCases_js_file = ProjectTestUtils.addFileToProject(edgeCases_js_path, this.project);

		// ensure scripting engine is started
		ScriptingEngine se = ScriptingEngine.getInstance();
		se.earlyStartup();

	}

	/**
	 * getProject
	 * 
	 * @return IProject
	 */
	public IProject getProject()
	{
		return project;
	}

	/**
	 * dispose
	 * 
	 * @throws CoreException
	 */
	public void dispose() throws CoreException
	{
		// waitForIndexer();
		project.delete(true, true, null);
	}
}
