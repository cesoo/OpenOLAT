/**
 * <a href="http://www.openolat.org">
 * OpenOLAT - Online Learning and Training</a><br>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); <br>
 * you may not use this file except in compliance with the License.<br>
 * You may obtain a copy of the License at the
 * <a href="http://www.apache.org/licenses/LICENSE-2.0">Apache homepage</a>
 * <p>
 * Unless required by applicable law or agreed to in writing,<br>
 * software distributed under the License is distributed on an "AS IS" BASIS, <br>
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. <br>
 * See the License for the specific language governing permissions and <br>
 * limitations under the License.
 * <p>
 * Initial code contributed and copyrighted by<br>
 * frentix GmbH, http://www.frentix.com
 * <p>
 */
package org.olat.modules.lecture.ui;

import java.util.List;
import java.util.Locale;

import org.olat.core.commons.persistence.SortKey;
import org.olat.core.gui.components.form.flexible.impl.elements.table.DefaultFlexiTableDataModel;
import org.olat.core.gui.components.form.flexible.impl.elements.table.FlexiSortableColumnDef;
import org.olat.core.gui.components.form.flexible.impl.elements.table.FlexiTableColumnModel;
import org.olat.core.gui.components.form.flexible.impl.elements.table.SortableFlexiTableDataModel;
import org.olat.modules.lecture.model.LectureBlockAndRollCall;

/**
 * 
 * Initial date: 29 mars 2017<br>
 * @author srosse, stephane.rosse@frentix.com, http://www.frentix.com
 *
 */
public class ParticipantLectureBlocksDataModel extends DefaultFlexiTableDataModel<LectureBlockAndRollCall>
implements SortableFlexiTableDataModel<LectureBlockAndRollCall> {
	
	private final Locale locale;
	
	public ParticipantLectureBlocksDataModel(FlexiTableColumnModel columnModel, Locale locale) {
		super(columnModel);
		this.locale = locale;
	}

	@Override
	public void sort(SortKey orderBy) {
		List<LectureBlockAndRollCall> rows = new ParticipantLectureBlocksSortDelegate(orderBy, this, locale).sort();
		super.setObjects(rows);
	}

	@Override
	public Object getValueAt(int row, int col) {
		LectureBlockAndRollCall block = getObject(row);
		return getValueAt(block, col);
	}

	@Override
	public Object getValueAt(LectureBlockAndRollCall row, int col) {
		switch(ParticipantCols.values()[col]) {
			case date: return row.getDate();
			case entry: return row.getEntryDisplayname();
			case lectureBlock: return row.getLectureBlockTitle();
			case coach: return row.getCoach();
			case present: return row;
			case appeal: {
				if(row.isRollCalled()) {
					int planned = row.getPlannedLecturesNumber();
					int attended = row.getLecturesAttendedNumber();
					if(attended < planned) {
						return Boolean.TRUE;
					}
				}
				return Boolean.FALSE;
			}
		}
		return null;
	}

	@Override
	public DefaultFlexiTableDataModel<LectureBlockAndRollCall> createCopyWithEmptyList() {
		return new ParticipantLectureBlocksDataModel(getTableColumnModel(), locale);
	}
	
	public enum ParticipantCols implements FlexiSortableColumnDef {
		date("table.header.date"),
		entry("table.header.entry"),
		lectureBlock("table.header.lecture.block"),
		coach("table.header.coach"),
		present("table.header.present"),
		appeal("table.header.appeal");
		
		private final String i18nKey;
		
		private ParticipantCols(String i18nKey) {
			this.i18nKey = i18nKey;
		}
		
		@Override
		public String i18nHeaderKey() {
			return i18nKey;
		}

		@Override
		public boolean sortable() {
			return true;
		}

		@Override
		public String sortKey() {
			return name();
		}
	}
}
