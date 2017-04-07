package interfaceApplication;

import org.json.simple.JSONObject;

import esayhelper.JSONHelper;
import model.WebGroupModel;
import model.WebModel;

public class WebGroup {
	private WebGroupModel webgroup = new WebGroupModel();
	private WebModel webModel = new WebModel();

	public String WebGroupInsert(String webgroupInfo) {
		JSONObject object = JSONHelper.string2json(webgroupInfo);
		return webgroup.resultmessage(webgroup.Add(object), "站群新增成功");
	}

	public String WebGroupDelete(String id) {
		int code = 0;
		String jsondtring = "{\"wbgid\":\"0\"}";
		if (webModel.SelectById(id).size()!=0) {
			code = webModel.UpdateByWbgId(id, JSONHelper.string2json(jsondtring));
		}
		if (code==0) {
			code = webgroup.Delete(id);
		}
		return webgroup.resultmessage(code, "站群删除成功");
	}

	public String WebGroupSelect(){
		return webgroup.Search().toJSONString();
	}

	public String WebGroupFind(String webinfo) {
		return webgroup.Select(webinfo);
	}

	public String WebGroupUpdate(String wbgid,String webgroupInfo) {
		return webgroup.resultmessage(webgroup.Update(wbgid,webgroupInfo), "站群修改成功");
	}

	public String WebGroupPage(int idx, int pageSize) {
		return webgroup.Page(idx, pageSize);
	}
	public String WebGroupPageBy(int idx,int pageSize,String webinfo) {
		return webgroup.Page(webinfo, idx, pageSize);
	}
	/**
	 * 修改排序值
	 * 
	 * @param num
	 *          上移时，num为负数，下移时，num为正数
	 * @return
	 */
	public String WebGroupSort(String wbid, long num) {
		return webgroup.Sort(wbid, num).toString();
	}

	/**
	 * 设置上级站群，默认fatherid为0时，是一级站群
	 * 
	 * @param wbgid
	 * @param fathrid
	 * @return
	 */
	public String WebGroupSetFatherId(String wbgid, String fathrid) {
		if (fathrid == null) {
			fathrid = "0";
		}
		return webgroup.resultmessage(webgroup.SetFatherId(wbgid, fathrid) != null ? 0 : 99,
				"设置上级站群成功");
	}

	public String WebGroupBatchDelete(String arrys) {
		return webgroup.resultmessage(webgroup.Delete(arrys.split(",")), "批量删除成功");
	}
}
