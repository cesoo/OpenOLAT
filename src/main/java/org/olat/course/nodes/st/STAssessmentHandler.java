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
package org.olat.course.nodes.st;

import org.olat.core.gui.UserRequest;
import org.olat.core.gui.components.stack.BreadcrumbPanel;
import org.olat.core.gui.components.stack.TooledStackedPanel;
import org.olat.core.gui.control.Controller;
import org.olat.core.gui.control.WindowControl;
import org.olat.course.assessment.handler.AssessmentConfig;
import org.olat.course.assessment.handler.AssessmentHandler;
import org.olat.course.assessment.handler.NonAssessmentConfig;
import org.olat.course.assessment.ui.tool.AssessmentCourseNodeController;
import org.olat.course.nodes.CourseNode;
import org.olat.course.nodes.STCourseNode;
import org.olat.course.run.scoring.ScoreCalculator;
import org.olat.course.run.userview.UserCourseEnvironment;
import org.olat.group.BusinessGroup;
import org.olat.modules.assessment.ui.AssessmentToolContainer;
import org.olat.modules.assessment.ui.AssessmentToolSecurityCallback;
import org.olat.repository.RepositoryEntry;
import org.springframework.stereotype.Service;

/**
 * 
 * Initial date: 20 Aug 2019<br>
 * @author uhensler, urs.hensler@frentix.com, http://www.frentix.com
 *
 */
@Service
public class STAssessmentHandler implements AssessmentHandler {

	@Override
	public String acceptCourseNodeType() {
		return STCourseNode.TYPE;
	}

	@Override
	public AssessmentConfig getAssessmentConfig(CourseNode courseNode) {
		if (courseNode instanceof STCourseNode) {
			STCourseNode stCourseNode = (STCourseNode) courseNode;
			return new STAssessmentConfig(stCourseNode.getScoreCalculator());
		}
		return NonAssessmentConfig.create();
	}

	@Override
	public ScoreCalculator getScoreCalculator(CourseNode courseNode) {
		if (courseNode instanceof STCourseNode) {
			STCourseNode stCourseNode = (STCourseNode) courseNode;
			return stCourseNode.getScoreCalculator();
		}
		return new ScoreCalculator();
	}

	@Override
	public Controller getDetailsEditController(UserRequest ureq, WindowControl wControl, BreadcrumbPanel stackPanel,
			CourseNode courseNode, UserCourseEnvironment coachCourseEnv, UserCourseEnvironment assessedUserCourseEnv) {
		return null;
	}

	@Override
	public boolean hasCustomIdentityList() {
		return true;
	}
	
	@Override
	public AssessmentCourseNodeController getIdentityListController(UserRequest ureq, WindowControl wControl,
			TooledStackedPanel stackPanel, CourseNode courseNode, RepositoryEntry courseEntry, BusinessGroup group,
			UserCourseEnvironment coachCourseEnv, AssessmentToolContainer toolContainer,
			AssessmentToolSecurityCallback assessmentCallback) {
		return new STIdentityListCourseNodeController(ureq, wControl, stackPanel, courseEntry, group, courseNode,
				coachCourseEnv, toolContainer, assessmentCallback);
	}

}