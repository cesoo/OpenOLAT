/**
* OLAT - Online Learning and Training<br>
* http://www.olat.org
* <p>
* Licensed under the Apache License, Version 2.0 (the "License"); <br>
* you may not use this file except in compliance with the License.<br>
* You may obtain a copy of the License at
* <p>
* http://www.apache.org/licenses/LICENSE-2.0
* <p>
* Unless required by applicable law or agreed to in writing,<br>
* software distributed under the License is distributed on an "AS IS" BASIS, <br>
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. <br>
* See the License for the specific language governing permissions and <br>
* limitations under the License.
* <p>
* Copyright (c) since 2004 at Multimedia- & E-Learning Services (MELS),<br>
* University of Zurich, Switzerland.
* <hr>
* <a href="http://www.openolat.org">
* OpenOLAT - Online Learning and Training</a><br>
* This file has been modified by the OpenOLAT community. Changes are licensed
* under the Apache 2.0 license as the original file.
*/

package org.olat.search.model;

import java.util.List;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.olat.core.util.StringHelper;



/**
 * Lucene document mapper.
 * @author Christian Guretzki
 */
public class OlatDocument extends AbstractOlatDocument {

	private static final long serialVersionUID = 2632864475115088251L;
	private String content = "";
	
	public OlatDocument() {
		super();
	}
	
	public OlatDocument(Document document) {
		super(document);
		content = document.get(CONTENT_FIELD_NAME);
	}


	/**
	 * @return Returns the content.
	 */
	public String getContent() {
		if (content == null) {
			return ""; // Do not return null
		}
		return content;
	}

	/**
	 * @param content The content to set.
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
	/**
	 * Generate a lucene document from the data stored in this document
	 * @return
	 */
	public Document getLuceneDocument() {
		Document document = new Document();
		document.add(createTextField(TITLE_FIELD_NAME,getTitle(), 4));
		document.add(createTextField(DESCRIPTION_FIELD_NAME,getDescription(), 2));
		document.add(createTextField(CONTENT_FIELD_NAME,getContent(), 0.5f ) );
		document.add(new StringField(RESOURCEURL_FIELD_NAME, getResourceUrl(), Field.Store.YES));
		document.add(new StringField(DOCUMENTTYPE_FIELD_NAME,getDocumentType(), Field.Store.YES));
		if(getCssIcon() != null) {
			document.add(new StringField(CSS_ICON,getCssIcon(), Field.Store.YES));
		}
		document.add(new StringField(FILETYPE_FIELD_NAME,getFileType(), Field.Store.YES));
		document.add(createTextField(AUTHOR_FIELD_NAME,getAuthor(), 2));
    
		try {
    	if(getCreatedDate() != null) {
    		document.add(new StringField(CREATED_FIELD_NAME, DateTools.dateToString(getCreatedDate(), DateTools.Resolution.DAY), Field.Store.YES) );
    	}
    }catch (Exception ex) {
    	// No createdDate set => does not add field
    }
    try {
    	if(getLastChange() != null) {
    		document.add(new StringField(CHANGED_FIELD_NAME, DateTools.dateToString(getLastChange(), DateTools.Resolution.DAY), Field.Store.YES) );
    	}
    }catch (Exception ex) {
    	// No changedDate set => does not add field
    }
    try {
    	if(getTimestamp() != null) {
    		document.add(new StringField(TIME_STAMP_NAME, DateTools.dateToString(getTimestamp(), DateTools.Resolution.MILLISECOND), Field.Store.YES) );
    	}
    }catch (Exception ex) {
    	// No changedDate set => does not add field
    }
    
		// Add various metadata
		if (metadata != null) {
			for (Entry<String, List<String>> metaDataEntry : metadata.entrySet()) {
				String key = metaDataEntry.getKey();
				for (String value : metaDataEntry.getValue()) {
					document.add(createTextField(key, value, 2) );
				}
			}
		}
		
		document.add(new TextField(PARENT_CONTEXT_TYPE_FIELD_NAME, getParentContextType(), Field.Store.YES));
		document.add(new TextField(PARENT_CONTEXT_NAME_FIELD_NAME, getParentContextName(), Field.Store.YES));
		if(StringHelper.containsNonWhitespace(getReservedTo())) {
			for(StringTokenizer tokenizer = new StringTokenizer(getReservedTo(), " "); tokenizer.hasMoreTokens(); ) {
				String reserved = tokenizer.nextToken();
				document.add(new StringField(RESERVED_TO, reserved, Field.Store.YES));
			}
		} else {
			document.add(new StringField(RESERVED_TO, "public", Field.Store.YES));
		}
	  return document;
	}


	private Field createTextField(String fieldName, String content, float boost) {
		TextField field = new TextField(fieldName,content, Field.Store.YES);
		field.setBoost(boost);
		return field;
	}
}
