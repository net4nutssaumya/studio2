/**
 * This file Copyright (c) 2005-2008 Aptana, Inc. This program is
 * dual-licensed under both the Aptana Public License and the GNU General
 * Public license. You may elect to use one or the other of these licenses.
 * 
 * This program is distributed in the hope that it will be useful, but
 * AS-IS and WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE, TITLE, or
 * NONINFRINGEMENT. Redistribution, except as permitted by whichever of
 * the GPL or APL youelect, is prohibited.
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
package com.aptana.ide.editor.yml;

import com.aptana.ide.editors.unified.BaseFileLanguageService;
import com.aptana.ide.editors.unified.FileService;
import com.aptana.ide.editors.unified.IFileServiceFactory;
import com.aptana.ide.editors.unified.IFileSourceProvider;
import com.aptana.ide.editors.unified.LanguageRegistry;
import com.aptana.ide.editors.unified.ParentOffsetMapper;
import com.aptana.ide.parsing.IParseState;
import com.aptana.ide.parsing.IParser;

/**
 * YMLFileServiceFactory
 */
public class YMLFileServiceFactory implements IFileServiceFactory
{

	private static YMLFileServiceFactory instance;
	private IParser _parser;

	/**
	 * YMLFileServiceFactory
	 */
	public YMLFileServiceFactory()
	{
		this._parser = LanguageRegistry.getParser(YMLMimeType.MimeType);
	}

	/**
	 * getInstance
	 * 
	 * @return the instance
	 */
	public static IFileServiceFactory getInstance()
	{
		if (instance == null)
		{
			instance = new YMLFileServiceFactory();
		}
		return instance;
	}

	/**
	 * @see com.aptana.ide.editors.unified.IFileServiceFactory#createFileService(com.aptana.ide.editors.unified.IFileSourceProvider)
	 */
	public FileService createFileService(IFileSourceProvider sourceProvider)
	{
		return createFileService(sourceProvider, true);
	}

	public FileService createFileService(IFileSourceProvider sourceProvider, boolean parse)
	{
		IParseState parseState = this._parser.createParseState(null);
		FileService fileService = new FileService(this._parser, parseState, sourceProvider, YMLMimeType.MimeType);

		fileService.setErrorManager(new YMLErrorManager(fileService));

		ParentOffsetMapper parentMapper = new ParentOffsetMapper(fileService);

		BaseFileLanguageService languageService = new BaseFileLanguageService(fileService, parseState, this._parser,
				parentMapper);
		fileService.addLanguageService(YMLMimeType.MimeType, languageService);

		if (parse)
		{
			fileService.doFullParse();
		}
		return fileService;
	}

}
