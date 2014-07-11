package com.santrong.tcp.client;

import com.santrong.tcp.TcpDefine;
import com.santrong.tcp.client.base.AbstractTcpClient;

/**
 * @author weinianjie
 * @date 2014年7月11日
 * @time 下午5:37:06
 */
public class LocalTcpClient31004 extends AbstractTcpClient{
	
	public String getMajorXml() {
		return header
			+ "<ReqMsg>"
			+ "<MsgHead>"
				+ "<MsgCode>" + TcpDefine.Basic_Client_StartConfRecord + "</MsgCode>"
				+ "<ModId>" + TcpDefine.ModuleSign + "</ModId>"
				+ "<SessionId>1</SessionId>"
			+ "</MsgHead>"
			+ "<MsgBody>"
				+ "<StartConfRecordReq>"
					+ "<ConfID type=\"string\">#{ConfID}</ConfID>"
					+ "<IsLive type=\"int\">#{IsLive}</IsLive>"
					+ "<IsRecord type=\"int\">#{IsRecord}</IsRecord>"
					+ "<RecordType type=\"int\">#{RecordType}</RecordType>"
					+ "<Layout type=\"int\">#{Layout}</Layout>"
					+ "<bScale type=\"int\">#{bScale}</bScale>"
					+ "<CourseName type=\"string\">#{CourseName}</CourseName>"
					+ "<CourseAbs type=\"string\">#{CourseAbs}</CourseAbs>"
					+ "<Teacher type=\"string\">#{Teacher}</Teacher>"
					+ "<RcdName type=\"string\">#{RcdName}</RcdName>"
					+ "<RcdStreamInfoArray>"
						+ "<RcdStreamInfo>"
							+ "<StrmAddr type=\"string\">#{StrmAddr}</StrmAddr>"
							+ "<StrmPort type=\"int\">#{StrmPort}</StrmPort>"
							+ "<StrmUser type=\"string\">#{StrmUser}</StrmUser>"
							+ "<StrmPW type=\"string\">#{StrmPW}</StrmPW>"
							+ "<StrmType type=\"int\">#{StrmType}</StrmType>"
							+ "<StrmBandwidth type=\"int\">#{StrmBandwidth}</StrmBandwidth>"
							+ "<StrmFmt type=\"int\">#{StrmFmt}</StrmFmt>"
							+ "<StrmFRate type=\"int\">#{StrmFRate}</StrmFRate>"
							+ "<AudSmpRate type=\"int\">#{AudSmpRate}</AudSmpRate>"
							+ "<AudCh type=\"int\">#{AudCh}</AudCh>"
							+ "<AudBitrate type=\"int\">#{AudBitrate}</AudBitrate>"
						+ "</RcdStreamInfo>"	// JAVA端不做批量
					+ "</RcdStreamInfoArray>"
				+ "</StartConfRecordReq>"
			+ "</MsgBody>"
		+ "</ReqMsg>";	
	}

}
