package model;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import esayhelper.DBHelper;
import esayhelper.JSONHelper;
import esayhelper.formHelper;
import esayhelper.jGrapeFW_Message;

@SuppressWarnings("unchecked")
public class WebModel {
	private static DBHelper dbweb;
	private formHelper _form;
	// private static JSONObject _obj_session = new JSONObject();
	// private static session session;
	static {
		// session = new session();
		dbweb = new DBHelper("mongodb", "webinfo", "_id");
		// _obj_session.put("webinfo",session.insertSession("webinfo",
		// dbweb.select().toString()));
	}

	public WebModel() {
		_form = new formHelper();
		String[] field = { "host", "logo", "icp", "title" };
		_form.addNotNull(field);
		HashMap<String, Object> map = new HashMap<>();
		map.put("wbid", getID());
		map.put("ownid", 0);
		map.put("engerid", 0);
		map.put("gov", "12");
		map.put("desp", "");
		map.put("policeid", "");
		map.put("wbgid", 0);
		map.put("isdelete", 0);
		map.put("isvisble", 0);
		map.put("tid", 0);
		map.put("sort", 0);
		map.put("authid", 0);
		map.put("taskid", 0);
		_form.adddef(map);
	}

	/**
	 * 
	 * @param webInfo
	 * @return 1:必填数据没有填 2：ICP备案号格式错误 3:ICP已存在 4: 公安网备案号格式错误 5：title已存在
	 *         6：网站描述字数超过限制
	 */
	public int AddWeb(JSONObject webInfo) {
		// 验证数据信息
		int cknode = _form.check_forminfo(webInfo);
		if (cknode == 1) {
			return 1;
		}
		String ICP = webInfo.get("icp").toString();
		if (!CheckIcp(ICP)) {
			return 2;
		}
		String policeid = webInfo.get("policeid").toString();
		if (!policeid.equals("")) {
			if (!Check.CheckIcpNum(policeid)) {
				return 4;
			}
		}
		if (FindWebByICP(ICP) != null) {
			return 3;
		}
		String webname = webInfo.get("title").toString();
		if (FindWebByTitle(webname) != null) {
			return 5;
		}
		if (!CheckDesp(webInfo.get("desp").toString())) {
			return 6;
		}
		return dbweb.data(webInfo).insertOnce() != null ? 0 : 99;
	}

	public int Delete(String webid) {
		return dbweb.findOne().eq("_id", new ObjectId(webid)).delete() != null ? 0 : 99;
	}

	public int Update(String wbid, JSONObject webinfo) {
		String ICP = webinfo.get("icp").toString();
		if (!_form.check_notnull_safe(webinfo)) {
			return 1;
		}
		if (webinfo.containsKey("icp")) {
			if (!CheckIcp(ICP)) {
				return 2;
			}
		}
		return dbweb.eq("_id", new ObjectId(wbid)).data(webinfo).update() != null ? 0 : 99;
	}

	public int UpdateByWbgId(String wbgid, JSONObject webinfo) {
		return dbweb.eq("wbgid", wbgid).data(webinfo).update() != null ? 0 : 99;
	}

	public JSONArray Select() {
		// if (session.get(_obj_session.get("webinfo").toString())==null) {
		// _obj_session.put("webinfo",session.insertSession("webinfo",
		// dbweb.select().toString()));
		// }
		// String webinfo = session.get(_obj_session.get("webinfo").toString());
		return dbweb.select();
	}

	public JSONArray Select(String webinfo) {
		JSONObject object = JSONHelper.string2json(webinfo);
		Set<Object> set = object.keySet();
		for (Object object2 : set) {
			dbweb.eq(object2.toString(), object.get(object2.toString()));
		}
		return dbweb.select();
	}

	public JSONArray SelectById(String wbgid) {
		return dbweb.eq("_id", new ObjectId(wbgid)).select();
	}

	public String Page(int idx, int pageSize) {
		JSONArray array = dbweb.page(idx, pageSize);
		JSONObject object = new JSONObject() {
			private static final long serialVersionUID = 1L;

			{
				put("totalSize", (int) Math.ceil((double) array.size() / pageSize));
				put("currentPage", idx);
				put("pageSize", pageSize);
				put("data", array);

			}
		};
		return object.toString();
	}

	public String Page(String webinfo, int idx, int pageSize) {
		Set<Object> set = JSONHelper.string2json(webinfo).keySet();
		for (Object object2 : set) {
			dbweb.eq(object2.toString(), JSONHelper.string2json(webinfo).get(object2
					.toString()));
		}
		JSONArray array = dbweb.page(idx, pageSize);
		JSONObject object = new JSONObject() {
			private static final long serialVersionUID = 1L;

			{
				put("totalSize", (int) Math.ceil((double) array.size() / pageSize));
				put("currentPage", idx);
				put("pageSize", pageSize);
				put("data", array);

			}
		};
		return object.toString();
	}

	public JSONObject Sort(String wbid, long num) {
		return dbweb.eq("_id", new ObjectId(wbid)).add("sort", num);
	}

	public String SetWbgId(String wbid, String wbgid) {
		JSONObject object = new JSONObject();
		object.put("wbgid", wbgid);
		return dbweb.eq("_id", new ObjectId(wbid)).data(object).update().toJSONString();
	}

	public String SetTempId(String wbid, String tempid) {
		JSONObject object = new JSONObject();
		object.put("tid", tempid);
		return dbweb.eq("_id", new ObjectId(wbid)).data(object).update().toJSONString();
	}

	public int Delete(String[] arr) {
//		StringBuffer stringBuffer = new StringBuffer();
		dbweb = (DBHelper) dbweb.or();
		for (int i = 0; i < arr.length; i++) {
			// int code = delete(arr[i]);
			// if (code != 0) {
			// stringBuffer.append((i + 1) + ",");
			// }
			dbweb.eq("_id", arr[i]);
		}
		return dbweb.delete() != null ? 0 : 99;
	}

	/**
	 * 匹配icp格式
	 * 
	 * @param icp
	 *          icp格式为类似于 皖icp备11016779号 或 京ICP备05087018号2
	 * @return
	 */
	public boolean CheckIcp(String icp) {
		return Check.check_icp(icp);
	}

	public boolean CheckDesp(String desp) {
		return desp.length() <= 1024;
	}

	public JSONObject FindWebByTitle(String title) {
		JSONObject rs = dbweb.eq("title", title).find();
		return rs;
	}

	public JSONObject FindWebByICP(String icp) {
		JSONObject rs = dbweb.eq("icp", icp).find();
		return rs;
	}

	/**
	 * 生成32位随机编码
	 * 
	 * @return
	 */
	public static String getID() {
		String str = UUID.randomUUID().toString().trim();
		return str.replace("-", "");
	}

	public String resultmessage(int num, String msg) {
		String message = "";
		switch (num) {
		case 0:
			message = msg;
			break;
		case 1:
			message = "必填数据没有填";
			break;
		case 2:
			message = "ICP备案号格式错误";
			break;
		case 3:
			message = "ICP备案号已存在";
			break;
		case 4:
			message = "公安网备案号格式错误";
			break;
		case 5:
			message = "网站名称不允许重复";
			break;
		case 6:
			message = "字数超过限制";
			break;
		default:
			message = "其他异常";
			break;
		}
		return jGrapeFW_Message.netMSG(num, message);
	}
}
