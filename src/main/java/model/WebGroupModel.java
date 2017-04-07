package model;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import esayhelper.DBHelper;
import esayhelper.JSONHelper;
//import esayhelper.JSONHelper;
import esayhelper.formHelper;
import esayhelper.jGrapeFW_Message;
//import session.session;

@SuppressWarnings("unchecked")
public class WebGroupModel {
	private static DBHelper dbwebgroup;
	private formHelper _form;
	// private static JSONObject _obj_session = new JSONObject();
	// private static session session;
	// private JSONArray arrays;

	public WebGroupModel() {
		_form = new formHelper();
		_form.addNotNull("name");
		HashMap<String, Object> defmap = new HashMap<>();
		defmap.put("ownid", 1);
		defmap.put("sort", 0);
		defmap.put("fatherid", 0); // 默认fatherid为0，为一级站群
		defmap.put("wbgid", getID());
		_form.adddef(defmap);

	}

	static {
		// session = new session();
		dbwebgroup = new DBHelper("mongodb", "webgroup");
		// _obj_session.put("webinfos", session.insertSession("webinfo",
		// dbwebgroup.select().toString()));
	}

	/**
	 * 新增站群
	 * 
	 * @param webgroupInfo
	 * @return 0：添加数据成功 1：存在非空字段 2：存在同名站群 其它异常
	 */
	public int Add(JSONObject webgroupInfo) {
		int ckcode = _form.check_forminfo(webgroupInfo);
		if (ckcode == 1) {
			return 1;
		}
		// 判断库中是否存在同名站群
		String name = webgroupInfo.get("name").toString();
		if (FindByName(name) != null) {
			return 2;
		}
		return dbwebgroup.insert(webgroupInfo) != null ? 0 : 99;
	}

	public int Delete(String id) {
		return dbwebgroup.eq("_id", new ObjectId(id)).delete() != null ? 0 : 99;
	}

	public JSONArray Search() {
		// if (session.get(_obj_session.get("webinfos").toString()) == null) {
		// _obj_session.put("webinfos", session.insertSession("webinfo",
		// dbwebgroup.select().toString()));
		// }
		// String webinfo = session.get(_obj_session.get("webinfos").toString());
		// String webinfo = dbwebgroup.selectbyCache().toJSONString();
		return dbwebgroup.select();
		// return (JSONArray) JSONValue.parse(webinfo);
	}

	public String Select(String webinfo) {
		JSONObject object = JSONHelper.string2json(webinfo);
		Set<Object> set = object.keySet();
		for (Object object2 : set) {
			dbwebgroup.eq(object2.toString(), object.get(object2.toString()));
		}
		return dbwebgroup.select().toJSONString();
	}

	public int Update(String wbgid, String webinfo) {
		// dbwebgroup.protectfield(field);
		JSONObject _webinfo = JSONHelper.string2json(webinfo);
		// 非空字段判断
		if (!_form.check_notnull_safe(_webinfo)) {
			return 1;
		}
		if (_webinfo.containsKey("name")) {
			String name = _webinfo.get("name").toString();
			if (FindByName(name) != null) {
				return 2;
			}
		}
		JSONObject object = dbwebgroup.eq("_id", new ObjectId(wbgid)).data(_webinfo).update();
		return object != null ? 0 : 99;
	}

	public JSONObject Sort(String wbid, long num) {
		return dbwebgroup.eq("_id", new ObjectId(wbid)).add("sort", num);
	}

	public String Page(int idx, int pageSize) {
		JSONArray array = dbwebgroup.page(idx, pageSize);
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
			dbwebgroup.eq(object2.toString(), JSONHelper.string2json(webinfo).get(object2
					.toString()));
		}
		JSONArray array = dbwebgroup.page(idx, pageSize);
		JSONObject object = new JSONObject() {
			private static final long serialVersionUID = 1L;
			{
				put("totlsize", (int) Math.ceil((double) array.size() / pageSize));
				put("currentPage", idx);
				put("PageSize", pageSize);
				put("data", array.toString());

			}
		};
		return object.toString();
	}

	public JSONObject FindByName(String name) {
		return dbwebgroup.eq("name", name).find();
	}

	public String FindByFatherId(String fatherid) {
		JSONArray array = dbwebgroup.eq("fatherid", fatherid).select();
		JSONObject _obj;
		String name = null;
		for (Object object : array) {
			_obj = (JSONObject) object;
			name = _obj.get("name").toString();
		}
		return name;
	}

	public String SetFatherId(String wbgid, String fathrid) {
		JSONObject _obj = new JSONObject();
		_obj.put("fatherid", fathrid);
		return dbwebgroup.eq("_id", new ObjectId(wbgid)).data(_obj).update().toString();
	}

	public int Delete(String[] arr) {
		dbwebgroup = (DBHelper) dbwebgroup.or();
		for (int i = 0; i < arr.length; i++) {
			// int code = delete(arr[i]);
			// if (code != 0) {
			// stringBuffer.append((i + 1) + ",");
			// }
			dbwebgroup.eq("_id", new ObjectId(arr[i]));
		}
		return dbwebgroup.delete() != null ? 0 : 99;
	}

	public JSONArray fixJson(JSONArray array) {
		JSONArray array2 = new JSONArray();
		
		return array2;
	}
	/**
	 * 生成32位随机编码
	 * 
	 * @return
	 */
	private static String getID() {
		String str = UUID.randomUUID().toString().trim();
		return str.replace("-", "");
	}

	public String resultmessage(int num, String message) {
		String msg = "";
		switch (num) {
		case 0:
			msg = message;
			break;
		case 1:
			msg = "必填字段没有填";
			break;
		case 2:
			msg = "已存在该站群";
			break;
		case 3:
			msg = "批量操作失败";
			break;
		default:
			msg = "其他操作异常";
			break;
		}
		return jGrapeFW_Message.netMSG(num, msg);
	}
}
