package interfaceApplication;

import esayhelper.JSONHelper;
import model.WebModel;

public class WebInfo {
	private WebModel web = new WebModel();
	public String WebInsert(String webInfo) {
		return web.resultmessage(web.AddWeb(JSONHelper.string2json(webInfo)), "新增网站信息成功");
	}
	public String WebDelete(String wbid) {
		return web.resultmessage(web.Delete(wbid), "删除网站信息成功");
	}
	public String WebUpdate(String wbid,String WebInfo){
		return web.resultmessage(web.Update(wbid,JSONHelper.string2json(WebInfo)), "网站信息更新成功");
	}
	public String WebShow(){
		return web.Select().toJSONString();
	}
	public String WebFind(String wbinfo) {
		return web.Select(wbinfo).toJSONString();
	}
	public String WebPage( int idx,int pageSize){
		return web.Page(idx, pageSize);
	}
	public String WebPageBy(int idx,int pageSize,String webinfo){
		return web.Page(webinfo,idx, pageSize);
	}
	public String WebSort(String wbid,long num) {
		return web.Sort(wbid, num).toJSONString();
	}
	public String WebSetWbg(String wbid,String wbgid) {
		return web.SetWbgId(wbid, wbgid);
	}
	public String SetTemp(String wbid,String tempid) {
		return web.SetTempId(wbid, tempid);
	}
	public String WebBatchDelete(String wbid) {
		return web.resultmessage(web.Delete(wbid.split(",")), "批量删除成功");
	}
}
