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
package com.aptana.ide.server.core.impl.modules;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;

import com.aptana.ide.server.core.IAbstractConfiguration;
import com.aptana.ide.server.core.IModule;
import com.aptana.ide.server.core.IModuleResource;
import com.aptana.ide.server.core.IModuleType;
import com.aptana.ide.server.core.IServer;

/**
 * default implementation of module
 * TODO FINISH IT
 * @author Pavel Petrochenko
 */
public class DefaultModule implements IModule
{
	private IProject project;
	private IPath path;
	private String name;
	private String[] pids;
	private final IModuleType type;

	/**
	 * @param type
	 */
	public DefaultModule(IModuleType type)
	{
		super();
		this.type = type;
	}

	/**
	 * @see com.aptana.ide.server.core.IModule#configurePublishOperation(java.lang.String,
	 *      com.aptana.ide.server.core.IAbstractConfiguration)
	 */
	public void configurePublishOperation(String operationId, IAbstractConfiguration configuration)
			throws CoreException
	{
	}

	
	/**
	 * @see com.aptana.ide.server.core.IModule#getDesiredPublishKind()
	 */
	public int getDesiredPublishKind()
	{
		return IServer.PUBLISH_INCREMENTAL;
	}

	/**
	 * @see com.aptana.ide.server.core.IModule#getName()
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @see com.aptana.ide.server.core.IModule#getPath()
	 */
	public IPath getPath()
	{
		return path;
	}

	/**
	 * @see com.aptana.ide.server.core.IModule#getProject()
	 */
	public IProject getProject()
	{
		return project;
	}

	/**
	 * @see com.aptana.ide.server.core.IModule#getPublishOperationConfiguration(java.lang.String)
	 */
	public IAbstractConfiguration getPublishOperationConfiguration(String operationId) throws CoreException
	{
		return null;
	}

	/**
	 * @see com.aptana.ide.server.core.IModule#getPublishOperationIds()
	 */
	public String[] getPublishOperationIds()
	{
		return (String[]) pids.clone();
	}

	/**
	 * @see com.aptana.ide.server.core.IModule#getRootResources()
	 */
	public IModuleResource[] getRootResources()
	{
		return null;
	}

	/**
	 * @see com.aptana.ide.server.core.IModule#getType()
	 */
	public IModuleType getType()
	{
		return type;
	}

	/**
	 * @see com.aptana.ide.server.core.IModule#isExternal()
	 */
	public boolean isExternal()
	{
		return project!=null;
	}

	/**
	 * @see com.aptana.ide.server.core.IModule#setConfiguration(com.aptana.ide.server.core.IAbstractConfiguration)
	 */
	public void setConfiguration(IAbstractConfiguration configuration)
	{

	}

	/**
	 * @see com.aptana.ide.server.core.IModule#setPublishOperationIds(java.lang.String[])
	 */
	public void setPublishOperationIds(String[] ids)
	{
		if (pids==null){
			throw new IllegalArgumentException();
		}
		this.pids=ids;
	}

	/**
	 * @see com.aptana.ide.server.core.IModule#unconfigurePublishOperation(java.lang.String)
	 */
	public void unconfigurePublishOperation(String operationId) throws CoreException
	{

	}

	/**
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter)
	{
		return Platform.getAdapterManager().getAdapter(this, adapter);
	}

	/**
	 * @see com.aptana.ide.server.core.IModule#storeConfig(com.aptana.ide.server.core.IAbstractConfiguration)
	 */
	public void storeConfig(IAbstractConfiguration subConfiguration)
	{
		
	}
}
